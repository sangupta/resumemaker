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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GitHubData {
	
	public String location;
	
	public int publicRepositories;
	
	public int contributingRepositories;
	
	public int followers;
	
	public Date joiningDate;
	
	public String blogURL;
	
	public String webURL;
	
	private final Map<String, Integer> languages = new HashMap<String, Integer>();
	
	public final List<GitHubCommitData> commitDatas = new ArrayList<GitHubCommitData>();
	
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
	
	public final List<GitHubRepositoryData> repositories = new ArrayList<GitHubRepositoryData>();

	public void addRepository(GitHubRepositoryData repository) {
		this.repositories.add(repository);
		
		if(repository != null) {
			addLanguage(repository.language);
		}
	}
	
	public int getTotalLanguages() {
		return this.totalLanguages;
	}

	public void addDetails(GitHubCommitData commitData) {
		this.commitDatas.add(commitData);
	}

}
