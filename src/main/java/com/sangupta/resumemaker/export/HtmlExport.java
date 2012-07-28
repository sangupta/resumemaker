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
import org.apache.velocity.tools.generic.AlternatorTool;

import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.Position;
import com.sangupta.jerry.util.AssertUtils;
import com.sangupta.resumemaker.Exporter;
import com.sangupta.resumemaker.export.svg.Circle;
import com.sangupta.resumemaker.export.svg.Line;
import com.sangupta.resumemaker.export.svg.Path;
import com.sangupta.resumemaker.export.svg.Rectangle;
import com.sangupta.resumemaker.export.svg.SVGBuilder;
import com.sangupta.resumemaker.export.svg.Text;
import com.sangupta.resumemaker.github.GitHubCommitData;
import com.sangupta.resumemaker.github.GitHubRepositoryData;
import com.sangupta.resumemaker.linkedin.LinkedInHelper;
import com.sangupta.resumemaker.model.Event;
import com.sangupta.resumemaker.model.UserData;
import com.sangupta.resumemaker.util.DateUtils;
import com.sangupta.resumemaker.util.HtmlUtils;
import com.sangupta.resumemaker.velocity.directives.LinkedInDatesDirective;
import com.sangupta.resumemaker.velocity.directives.MarkdownDirective;

public class HtmlExport implements Exporter {
	
	private static final int RADIUS_Y = 50;

	private static final String TEMPLATE_NAME = "resume.template.html";
	
	private static final String TEMPLATE_FOLDER = "./templates/" + TEMPLATE_NAME;
	
	private static final int GRAPHIC_WIDTH = 800;
	
	private static final VelocityEngine engine = new VelocityEngine();
	
	static {
		final String[] customDirectives = { LinkedInDatesDirective.class.getName(), 
				MarkdownDirective.class.getName() };
		
		StringBuilder builder = new StringBuilder();
		for(String directive : customDirectives) {
			builder.append(directive).append(",");
		}
		builder.deleteCharAt(builder.length() - 1);
		
		final String directives = builder.toString();
		
		File file = new File(TEMPLATE_FOLDER);
		if(!file.exists()) {
			throw new RuntimeException("Cannot find template at " + file.getAbsolutePath());
		}
		
		file = file.getAbsoluteFile();
		
		// initialize the velocity engine
		Properties properties = new Properties();
		properties.setProperty(VelocityEngine.RESOURCE_LOADER, "file");
		properties.setProperty("file" + VelocityEngine.RESOURCE_LOADER + ".class", FileResourceLoader.class.getName());
		properties.setProperty("userdirective", directives);
		
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
			String unformattedHTML = writer.toString();
			String formattedHTML = HtmlUtils.tidyHtml(unformattedHTML);
			FileUtils.write(exportFile, formattedHTML);
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
			Event event = new Event(position.getCompany().getName(), LinkedInHelper.fromStartDate(position.getStartDate()), LinkedInHelper.fromEndDate(position.getEndDate()));
			events.add(event);
			event.setDescription(position.getTitle());
		}
		
		context.put("positions", events);
		context.put("positionsTimeLine", createSVGTimeLineCode(events));
		
		// build education time line
		events = new ArrayList<Event>();
		for(Education education : userData.linkedInUserData.getEducations()) {
			Event event = new Event(education.getSchoolName(), LinkedInHelper.fromStartDate(education.getStartDate()), LinkedInHelper.fromEndDate(education.getEndDate()));
			event.setDescription(education.getDegree());
			events.add(event);
		}
		context.put("educations", events);
		context.put("educationTimeLine", createSVGTimeLineCode(events));
		
		// set the github data
		userData.gitHubData.sortRepositories();
		context.put("github", userData.gitHubData);
		context.put("githubGraph", createGithubGraph(userData.gitHubData.getCommitDatas()));
		
		// for each repository - prepare a separate SVG graph
		// of 52 week commit history
		final Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
		final Date oneYearPrevious = cal.getTime();
		
		for(GitHubRepositoryData repo : userData.gitHubData.getRepositories()) {
			final String repoName = repo.getName();
			
			List<GitHubCommitData> commits = new ArrayList<GitHubCommitData>();
			// build a list of commits
			for(GitHubCommitData commit : userData.gitHubData.getCommitDatas()) {
				if(commit.repositoryID.equals(repoName) && commit.createdAt.after(oneYearPrevious)) {
					commits.add(commit);
				}
			}
			
			// build the SVG graph
			String graph = createGithubWeeklyGraph(commits);
			repo.setGithubCommitGraph(graph);
		}

		// for alternating rows
		context.put("alternator", new AlternatorTool());
		
		// return the final context back
		return context;
	}

	private String createGithubWeeklyGraph(List<GitHubCommitData> commits) {
		if(commits == null) {
			return null;
		}
		
		Collections.sort(commits);
		
		int[] weeklyValues = new int[52];
		
		final int GRAPH_WIDTH = 412;
		final int GRAPH_HEIGHT = 70;
		
		final int BAR_WIDTH = 8;
		
		final int ORIGIN_X = GRAPH_WIDTH - (52 * BAR_WIDTH);
		final int ORIGIN_Y = GRAPH_HEIGHT - 50;
		
		// find max Y
		for(GitHubCommitData commit : commits) {
			int linesCommitted = commit.additions + commit.deletions;
			int week = DateUtils.getWeekOfYear(commit.createdAt);
			weeklyValues[week] = weeklyValues[week] + linesCommitted;
		}
		
		int maxCommits = 0;
		for(int i = 0; i < weeklyValues.length; i++) {
			int linesCommitted = weeklyValues[i];
			maxCommits += linesCommitted;
		}
		
		float yScalingFactor = 0f;
		if(maxCommits > 0) {
			yScalingFactor = ((float) ORIGIN_Y) / maxCommits;
		}
		
		// start building the graph
		SVGBuilder svgBuilder = new SVGBuilder(GRAPH_WIDTH, GRAPH_HEIGHT);
		
		// build the timeline
		for(int i = 0; i < 52; i++) {
			float startX = ORIGIN_X + i * 8;
			float y = ORIGIN_Y + 1;
			Line line = new Line(startX, y, startX + 7, y, "graphWeekMark");
			svgBuilder.addLine(line);
		}
		
		// the data bars
		for(int i = 0; i < weeklyValues.length; i++) {
			int week = i;
			int sum = weeklyValues[i];
			
			if(sum > 0) {
				float startX = ORIGIN_X + (week - 1) * 8;
				float topY = ORIGIN_Y - (yScalingFactor * sum); 
				
				Rectangle rectangle = new Rectangle(startX + 1, topY, 6, ORIGIN_Y - topY, "weeklyGraphBar");
				svgBuilder.addRectangle(rectangle);
			}
		}
		
		// create the legend
		Rectangle rectangle = new Rectangle(10 + ORIGIN_X, ORIGIN_Y + 15, 10, 10);
		svgBuilder.addRectangle(rectangle);
		
		Text text = new Text(25 + ORIGIN_X, ORIGIN_Y + 25, "commits by this user", "start", "graphLegendText");
		svgBuilder.addText(text);
		
		text = new Text(GRAPH_WIDTH - 150, ORIGIN_Y + 25, "52 week participation", "start", "graphLegendText");
		svgBuilder.addText(text);
		
		return svgBuilder.toString();
	}

	/**
	 * Create an area graph of commit data
	 * 
	 * @param commitDatas
	 * @return
	 */
	private String createGithubGraph(List<GitHubCommitData> commits) {
		if(AssertUtils.isEmpty(commits)) {
			return null;
		}
		
		Collections.sort(commits);

		final int startYear = DateUtils.getYear(commits.get(0).createdAt);
		final int endYear = DateUtils.getYear(commits.get(commits.size() - 1).createdAt) + 2; // add TWO to make sure that we if we are nearing the end of the current year, then we have enough space at the end of the graph
		float totalYearSegments = endYear - startYear;  

		final float yearSegmentWidth = GRAPHIC_WIDTH / totalYearSegments;
		final float weekSegmentWidth = yearSegmentWidth / 52;

		final int X_AXIS_MOVED = 100;
		final int HEIGHT_OF_GRAPH = 250;
		final int THICKNESS = 3;
		
		SVGBuilder svgBuilder = new SVGBuilder(GRAPHIC_WIDTH + X_AXIS_MOVED, HEIGHT_OF_GRAPH + 50);
		
		// create the basic timeline
		Rectangle rectangle = new Rectangle(0 + X_AXIS_MOVED, HEIGHT_OF_GRAPH, GRAPHIC_WIDTH, THICKNESS, "graphGridLines");
		svgBuilder.addRectangle(rectangle);
		
		// add the caption
		Text text = new Text(GRAPHIC_WIDTH / 2, HEIGHT_OF_GRAPH + 50, "Commit Timeline");
		svgBuilder.addText(text);
		
		// add X-axis labels
		final float textAdditive = yearSegmentWidth * 0.5f;
		
		for(int year = startYear; year < endYear; year++) {
			int index = year - startYear;
			float x = yearSegmentWidth * index;
			
			// add the year vertical bar distinguisher
			rectangle = new Rectangle(x + X_AXIS_MOVED, HEIGHT_OF_GRAPH, THICKNESS, 10, "graphGridLines");
			svgBuilder.addRectangle(rectangle);
			
			// add the year number
			text = new Text(x + textAdditive + X_AXIS_MOVED, HEIGHT_OF_GRAPH + 20f, String.valueOf(year), "middle", "xAxisLabels");
			svgBuilder.addText(text);
		}

		// determine max value for y-axis
		float maxLines = 0;
		for(GitHubCommitData commit : commits) {
			maxLines += commit.additions - commit.deletions;
		}

		// normalize the maximum value
		final float lineFactor = HEIGHT_OF_GRAPH / maxLines;
		
		// create the Y-AXIS
		rectangle = new Rectangle(X_AXIS_MOVED, 0, THICKNESS, HEIGHT_OF_GRAPH, "graphGridLines");
		svgBuilder.addRectangle(rectangle);
		
		final int Y_AXIS_DIVISIONS = 5;
		float yInterval = maxLines / Y_AXIS_DIVISIONS;
		for(int count = 0; count < Y_AXIS_DIVISIONS; count++) {
			float lines = count * yInterval;
			float y = lines * lineFactor;
			rectangle = new Rectangle(X_AXIS_MOVED - 10, y, 10, THICKNESS, "graphGridLines");
			svgBuilder.addRectangle(rectangle);
			
			text = new Text(X_AXIS_MOVED - 15, HEIGHT_OF_GRAPH - y + 5, String.valueOf(((int) lines)), "end", "yAxisLabels");
			svgBuilder.addText(text);
		}
		
		// System.out.println("Line factor: " + lineFactor);
		
		float totalLines = 0;
		int lastMyYear = 0;
		int lastMyWeek = 0;
		
		float lastX = 0f;
		float lastY = 0f;
		
		for(GitHubCommitData commit : commits) {
			final int myYear = DateUtils.getYear(commit.createdAt); 
			final int myWeek = DateUtils.getWeekOfYear(commit.createdAt);
			
			if(myYear == lastMyYear && myWeek == lastMyWeek) {
				totalLines += commit.additions + commit.deletions;
			} else {
				float x = ((myYear - startYear) * yearSegmentWidth) + ((myWeek - 1) * weekSegmentWidth);
				float y = totalLines * lineFactor;

				if(!(y == lastY && y == 0.0)) {
					Line line = new Line(lastX + X_AXIS_MOVED, HEIGHT_OF_GRAPH - lastY, x + X_AXIS_MOVED, HEIGHT_OF_GRAPH - y, "trendLine");
					svgBuilder.addLine(line);
				}
				
				lastMyYear = myYear;
				lastMyWeek = myWeek;
				
				// System.out.println("Week " + myWeek + " of year " + myYear + " had totalLines of " + totalLines + " as (" + x + ", " + y + ")");
				
				lastX = x;
				lastY = y;
			}
		}
		
		return svgBuilder.toString();
	}

	private String createSVGTimeLineCode(List<Event> events) {
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
		final float yearSegmentWidth = GRAPHIC_WIDTH / totalYearSegments;
		final float monthSegmentWidth = yearSegmentWidth / 12;
		
		final int BASE_LINE = 200;
		final int THICKNESS = 3;
		
		SVGBuilder svgBuilder = new SVGBuilder(GRAPHIC_WIDTH, 250);
		
		// create the basic timeline
		Rectangle rectangle = new Rectangle(0, BASE_LINE, GRAPHIC_WIDTH, THICKNESS, "graphGridLines");
		svgBuilder.addRectangle(rectangle);
		
		final float textAdditive = yearSegmentWidth * 0.5f;
		
		for(int year = startYear; year < endYear; year++) {
			int index = year - startYear;
			float x = yearSegmentWidth * index;
			
			// add the year vertical bar distinguisher
			rectangle = new Rectangle(x, 200, THICKNESS, 10, "graphGridLines");
			svgBuilder.addRectangle(rectangle);
			
			// add the year number
			Text text = new Text(x + textAdditive, 220f, String.valueOf(year), "middle", "xAxisLabels");
			svgBuilder.addText(text);
		}
		
		// start building the shape for each year
		for(int index = 0; index < events.size(); index++) {
			Event event = events.get(index);
			
			int myStartYear = DateUtils.getYear(event.getStartDate());
			int myStartMonth = DateUtils.getMonth(event.getStartDate());
			
			// compute the arc dimensions
			float startX = ((myStartYear - startYear) * yearSegmentWidth) + (myStartMonth * monthSegmentWidth);

			float endX;
			Date endDate;
			if(event.getEndDate() != null) {
				endDate = event.getEndDate();
			} else {
				endDate = Calendar.getInstance().getTime();
			}

			int myEndYear = DateUtils.getYear(endDate);
			int myEndMonth = DateUtils.getMonth(endDate);
				
			endX = ((myEndYear - startYear) * yearSegmentWidth) + (myEndMonth * monthSegmentWidth);
			
			final float mid = (endX + startX) / 2;
			
			String styleClassName = "fillColor" + String.valueOf((index % 8) + 1);
			
			Path path = new Path();
			path.setStyleClassName(styleClassName);
			path.moveTo(startX, 199).arc(endX, 199, ((endX - startX) / 2), RADIUS_Y, 0, false, true).close();
			svgBuilder.addPath(path);
			
			// compute the label path
			Line line = new Line(mid, 112, mid, 160, "graphGridLines");
			svgBuilder.addLine(line);
			
			// add the star or circle around the end
			if(event.getEndDate() != null) {
				Circle circle = new Circle(mid, 160, 3);
				svgBuilder.addCircle(circle);
			} else {
				// for the last event on the timeline
				// add a star
				svgBuilder.addPath(createStarPath(mid, 160));
			}
			
			// add the position name over this line
			Text text = new Text(mid, 100, event.getName(), "middle", "xAxisLabels");
			svgBuilder.addText(text);
		}
		
		return svgBuilder.toString();
	}
	
	private Path createStarPath(float x, float y) {
		final float x1 = 2.938926261f;
		final float x2 = 1.816517946f;
		final float x3 = 4.755282581f;
		final float x4 = 1.122669832f;
		final float x5 = 0f;
		
		final float y1 = 2.135084972f;
		final float y2 = -1.319777541f;
		final float y3 = -3.455084972f;
		final float y4 = -3.455222459f;
		final float y5 = -6.91f;
		
		Path path = new Path();
		
		path.moveTo(x, y)
		.lineTo(x + x1, y + y1)
		.lineTo(x + x2, y + y2)
		.lineTo(x + x3, y + y3)
		.lineTo(x + x4, y + y4)
		.lineTo(x + x5, y + y5)
		.lineTo(x - x4, y + y4)
		.lineTo(x - x3, y + y3)
		.lineTo(x - x2, y + y2)
		.lineTo(x - x1, y + y1)
		.close();
		
		return path;
	}
	
}

