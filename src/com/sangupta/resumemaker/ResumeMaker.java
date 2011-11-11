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
package com.sangupta.resumemaker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.FileUtils;

import com.sangupta.resumemaker.export.HtmlExport;
import com.sangupta.resumemaker.github.GitHubAnalyzer;
import com.sangupta.resumemaker.gravatar.GravatarAnalyzer;
import com.sangupta.resumemaker.linkedin.LinkedInAnalyzer;
import com.sangupta.resumemaker.model.Config;
import com.sangupta.resumemaker.model.UserData;
import com.thoughtworks.xstream.XStream;

/**
 * @author sangupta
 *
 */
public class ResumeMaker {
	
	public static BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
	
	private static final String USER_DATA_BUFFER = "user.data";

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		Config config = new Config();
		
		// linkedin
		config.linkedInConfig.consumerKey = "";
		config.linkedInConfig.consumerSecret = "";
		config.linkedInConfig.userName = "sangupta";
		
		// github
		config.gitHubConfig.userName = "sangupta";
		
		// gravatar
		config.gravatarID = "sandy.pec@gmail.com";
		
		UserData userData = null;
		
		final long startTime = System.currentTimeMillis();
		System.out.println("Start fetching user data at " + startTime);
		userData = gatherUserData(config);
		final long endTime = System.currentTimeMillis();
		System.out.println("Start fetching user data at " + endTime + ", time taken " + (endTime - startTime) + " ms.");
		
		writeUserData(userData);
		
		userData = readUserData();
		
		File exportFile = new File("resume.html");
		
		System.out.print("Exporting the resume... ");
		Exporter exporter = new HtmlExport();
		exporter.export(userData, exportFile);
		System.out.println("done!");
		
		BrowserUtil.openUrlInBrowser("file:///" + exportFile.getAbsolutePath());
	}

	private static UserData readUserData() throws FileNotFoundException, IOException, ClassNotFoundException {
		File file = new File(USER_DATA_BUFFER);
		XStream stream = new XStream();
		return (UserData) stream.fromXML(file);
	}
	
	private static void writeUserData(UserData userData) throws FileNotFoundException, IOException {
		XStream stream = new XStream();
		String xml = stream.toXML(userData);
		FileUtils.write(new File(USER_DATA_BUFFER), xml);
	}

	private static UserData gatherUserData(Config config) throws Exception {
		final UserData data = new UserData();
		
		new LinkedInAnalyzer().analyze(config, data);
		
		new GravatarAnalyzer().analyze(config, data);
		
		new GitHubAnalyzer().analyze(config, data);

		return data;
	}

}
