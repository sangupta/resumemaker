/**
 *
 * Resume Maker
 * Copyright (c) 2011, Sandeep Gupta
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package com.sangupta.resumemaker.export;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Position;
import com.sangupta.resumemaker.Exporter;
import com.sangupta.resumemaker.linkedin.LinkedInHelper;
import com.sangupta.resumemaker.model.Event;
import com.sangupta.resumemaker.model.TimeLine;
import com.sangupta.resumemaker.model.UserData;
import com.sangupta.resumemaker.util.DateUtils;

public class HtmlExport implements Exporter {
	
	private static final String TEMPLATE_NAME = "resume.template.html";
	
	private static final String TEMPLATE_FOLDER = "./templates/" + TEMPLATE_NAME;
	
	private static final VelocityEngine engine = new VelocityEngine();
	
	static {
		File file = new File(TEMPLATE_FOLDER);
		if(!file.exists()) {
			throw new RuntimeException("Cannot find template at " + file.getAbsolutePath());
		}
		
		file = file.getAbsoluteFile();
		
		// initialize the velocity engine
		Properties properties = new Properties();
		properties.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
		properties.setProperty("file" + VelocityEngine.RESOURCE_LOADER + ".class", FileResourceLoader.class.getName());
		
		properties.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, file.getParentFile().getAbsolutePath());
		
		engine.init(properties);		
	}
	
	@Override
	public void export(UserData userData, File exportFile) {
		Template template = engine.getTemplate(TEMPLATE_NAME);
		
		VelocityContext pageModel = getModel(userData);
		
		StringWriter writer = new StringWriter();
		try {
			template.merge(pageModel, writer);
		} finally {
			try {
				writer.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			FileUtils.write(exportFile, writer.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private VelocityContext getModel(UserData userData) {
		VelocityContext context = new VelocityContext();
		
		context.put("name", userData.linkedInUserData.getName());
		context.put("createdOn", new Date());
		context.put("linkedin", userData.linkedInUserData);
		context.put("gravatarURL", userData.gravatarImageURL);

		// build linkedin jobs timeline
		List<Event> events = new ArrayList<Event>();
		for(Position position : userData.linkedInUserData.getPositions()) {
			events.add(new Event(position.getCompany().getName(), LinkedInHelper.fromStartDate(position.getStartDate()), LinkedInHelper.fromEndDate(position.getEndDate())));
		}
		
		TimeLine timeLine = createTimeLineCode(events);
		context.put("positions", events);
		context.put("positionsTimeLine", timeLine);
		
		// build education time line
		events = new ArrayList<Event>();
		for(Education education : userData.linkedInUserData.getEducations()) {
			events.add(new Event(education.getSchoolName(), LinkedInHelper.fromStartDate(education.getStartDate()), LinkedInHelper.fromEndDate(education.getEndDate())));
		}
		timeLine = createTimeLineCode(events);
		context.put("educations", events);
		context.put("educationTimeLine", timeLine);
		
		// set the github data
		context.put("github", userData.gitHubData);
		
		return context;
	}
	
	private TimeLine createTimeLineCode(List<Event> events) {
		if(events == null) {
			return null;
		}
		
		// sort the collection based on the dates
		Collections.sort(events);
		
		// start finding the segment widths
		// and build up the array
		final int startYear = DateUtils.getYear(events.get(0).getStartDate());
		final int endYear = DateUtils.getYear(events.get(events.size() - 1).getEndDate()) + 2; // add TWO to make sure that we if we are nearing the end of the current year, then we have enough space at the end of the graph
		
		float totalYearSegments = endYear - startYear;  
		final float yearSegmentWidth = 100 / totalYearSegments;
		final float monthSegmentWidth = yearSegmentWidth / 12;
		
		// modify the original events to include the start and end coordinates
		for(Event event : events) {
			int myStartYear = DateUtils.getYear(event.getStartDate());
			int myStartMonth = DateUtils.getMonth(event.getStartDate());
			
			float startCoord = ((myStartYear - startYear) * yearSegmentWidth) + (myStartMonth * monthSegmentWidth);

			float endCoord;
			Date endDate;
			if(event.getEndDate() != null) {
				endDate = event.getEndDate();
			} else {
				endDate = Calendar.getInstance().getTime();
			}

			int myEndYear = DateUtils.getYear(endDate);
			int myEndMonth = DateUtils.getMonth(endDate);
				
			endCoord = ((myEndYear - startYear) * yearSegmentWidth) + (myEndMonth * monthSegmentWidth);
			
			event.setCoords(startCoord, endCoord);
		}
		
		// build up the year list to be thrown back
		List<String> years = new ArrayList<String>();
		for(int year = startYear; year < endYear; year++) {
			years.add(String.valueOf(year));
		}
		
		return new TimeLine(years, yearSegmentWidth);
	}
	
}
