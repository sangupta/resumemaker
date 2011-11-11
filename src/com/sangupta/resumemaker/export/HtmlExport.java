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
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.FileResourceLoader;

import com.sangupta.resumemaker.Exporter;
import com.sangupta.resumemaker.model.UserData;

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
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			FileUtils.write(exportFile, writer.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private VelocityContext getModel(UserData userData) {
		VelocityContext context = new VelocityContext();
		
		context.put("name", userData.linkedInUserData.getName());
		context.put("createdOn", new Date());
		context.put("linkedin", userData.linkedInUserData);
		context.put("gravatarURL", userData.gravatarImageURL);
		context.put("github", userData.gitHubData);
		
		return context;
	}

}
