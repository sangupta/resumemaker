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
package com.sangupta.resumemaker.github;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitHubData {
	
	private String location;
	
	private int publicRepositories;
	
	private int contributingRepositories;
	
	private int followers;
	
	private Date joiningDate;
	
	private String blogURL;
	
	private String webURL;
	
	private final Map<String, Integer> languages = new HashMap<String, Integer>();
	
	private final List<GitHubCommitData> commitDatas = new ArrayList<GitHubCommitData>();
	
	private final List<GitHubRepositoryData> repositories = new ArrayList<GitHubRepositoryData>();

	private int totalLanguages = 0;
	
	private void addLanguage(String language) {
		if(languages.containsKey(language)) {
			int oldValue = languages.get(language);
			languages.put(language, (oldValue + 1));
		} else {
			languages.put(language, 1);
		}
		
		totalLanguages++;
	}
	
	public void addRepository(GitHubRepositoryData repository) {
		this.repositories.add(repository);
		
		if(repository != null) {
			addLanguage(repository.getLanguage());
		}
	}
	
	public void sortRepositories() {
		Collections.sort(this.repositories);
	}
	
	public void addDetails(GitHubCommitData commitData) {
		this.commitDatas.add(commitData);
	}

	// Usual accessor's follow
	
	public int getTotalLanguages() {
		return this.totalLanguages;
	}

	public String getLocation() {
		return location;
	}

	public int getPublicRepositories() {
		return publicRepositories;
	}

	public int getContributingRepositories() {
		return contributingRepositories;
	}

	public int getFollowers() {
		return followers;
	}

	public Date getJoiningDate() {
		return joiningDate;
	}

	public String getBlogURL() {
		return blogURL;
	}

	public String getWebURL() {
		return webURL;
	}

	public Map<String, Integer> getLanguages() {
		return languages;
	}

	public List<GitHubCommitData> getCommitDatas() {
		return commitDatas;
	}

	public List<GitHubRepositoryData> getRepositories() {
		return repositories;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void setPublicRepositories(int publicRepositories) {
		this.publicRepositories = publicRepositories;
	}

	public void setContributingRepositories(int contributingRepositories) {
		this.contributingRepositories = contributingRepositories;
	}

	public void setFollowers(int followers) {
		this.followers = followers;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public void setBlogURL(String blogURL) {
		this.blogURL = blogURL;
	}

	public void setWebURL(String webURL) {
		this.webURL = webURL;
	}

	public void setTotalLanguages(int totalLanguages) {
		this.totalLanguages = totalLanguages;
	}

}
