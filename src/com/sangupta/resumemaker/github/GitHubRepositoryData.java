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

import java.util.Date;

public class GitHubRepositoryData implements Comparable<GitHubRepositoryData> {
	
	private boolean collaborated = false;
	
	private String name;
	
	private String language;
	
	private String description;
	
	private int watchers;
	
	private int forks;
	
	private Date created;
	
	private Date lastPushed;
	
	private String githubCommitGraph = null;
	
	public GitHubRepositoryData(boolean collaborated) {
		this.collaborated = collaborated;
	}
	
	public boolean isCollaborated() {
		return this.collaborated;
	}

	@Override
	public int compareTo(GitHubRepositoryData other) {
		if(other == null) {
			return -1;
		}
		
		if(this.watchersForkers() != other.watchersForkers()) {
			return 0 - (this.watchersForkers() - other.watchersForkers());
		}
		
		return this.name.toLowerCase().compareTo(other.name.toLowerCase());
	}
	
	public int watchersForkers() {
		return this.watchers + this.forks;
	}
	
	// Usual accessor's follow

	public String getName() {
		return name;
	}

	public String getLanguage() {
		return language;
	}

	public String getDescription() {
		return description;
	}

	public int getWatchers() {
		return watchers;
	}

	public int getForks() {
		return forks;
	}

	public Date getCreated() {
		return created;
	}

	public Date getLastPushed() {
		return lastPushed;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setWatchers(int watchers) {
		this.watchers = watchers;
	}

	public void setForks(int forks) {
		this.forks = forks;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public void setLastPushed(Date lastPushed) {
		this.lastPushed = lastPushed;
	}

	public String getGithubCommitGraph() {
		return githubCommitGraph;
	}

	public void setGithubCommitGraph(String githubCommitGraph) {
		this.githubCommitGraph = githubCommitGraph;
	}

}
