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
import java.util.List;

import org.eclipse.egit.github.core.ExtendedRepositoryCommit;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryCommit;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.service.CollaboratorService;
import org.eclipse.egit.github.core.service.CommitService;
import org.eclipse.egit.github.core.service.ExtendedCommitService;
import org.eclipse.egit.github.core.service.OrganizationService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

import com.sangupta.resumemaker.Analyzer;
import com.sangupta.resumemaker.model.Config;
import com.sangupta.resumemaker.model.UserData;

public class GitHubAnalyzer implements Analyzer {

	final CommitService commitService = new CommitService();
	
	@Override
	public void analyze(Config config, UserData userData) throws Exception {
		final GitHubData data = userData.gitHubData;
		
		// get all repositories
		UserService userService = new UserService();
		final User currentUser = userService.getUser(config.gitHubConfig.userName);
		
		data.location = currentUser.getLocation();
		data.blogURL = currentUser.getBlog();
		data.followers = currentUser.getFollowers();
		data.joiningDate = currentUser.getCreatedAt();
		data.webURL = currentUser.getHtmlUrl();
		
		System.out.println("GitHub: user details fetched.");
		
		RepositoryService repositoryService = new RepositoryService();
		List<Repository> userRepositories = repositoryService.getRepositories(config.gitHubConfig.userName);
		
		System.out.println("GitHub: user repositories fetched.");
		
		data.publicRepositories = userRepositories.size();
		for(Repository repository : userRepositories) {
			GitHubRepositoryData git = getGitHubRepositoryData(repository, true);
			
			getCommitDataForRepositoryAndUser(repository, currentUser, data);
			
			data.addRepository(git);
		}
		
		// get all related organizations
		OrganizationService organizationService = new OrganizationService();
		List<User> orgUsers = organizationService.getOrganizations(config.gitHubConfig.userName);
		List<Repository> orgRepositories = new ArrayList<Repository>();
		for(User orgUser : orgUsers) {
			List<Repository> repositories = repositoryService.getOrgRepositories(orgUser.getLogin());
			if(repositories != null) {
				orgRepositories.addAll(repositories);
			}
		}
		
		System.out.println("GitHub: user organizations fetched.");
		
		// check for which all repositories is the user a collaborator
		CollaboratorService collaboratorService = new CollaboratorService();
		int size = 0;
		for(Repository repository : orgRepositories) {
			List<User> users = collaboratorService.getCollaborators(repository);
			
			for(User user : users) {
				if(user.getId() == currentUser.getId()) {
					// this repository has this user as a collaborator
					GitHubRepositoryData git = getGitHubRepositoryData(repository, false);
					
					getCommitDataForRepositoryAndUser(repository, currentUser, data);
					
					data.addRepository(git);
					
					size++;
				}
			}
		}
		data.contributingRepositories = size;
		
		System.out.println("GitHub: user contributing repositories fetched.");
		
		// get the number of lines impacted by this user for all above repositories
		// this is going to be a time consuming job
		// we will use this data to plot graphs in the final resume sheet
		
		System.out.println("Done with github.");
	}
	
	private GitHubRepositoryData getGitHubRepositoryData(Repository repository, boolean collaborated) {
		GitHubRepositoryData git = new GitHubRepositoryData(false);
		
		git.name = repository.getName();
		git.language = repository.getLanguage();
		git.description = repository.getDescription();
		git.watchers = repository.getWatchers();
		git.forks = repository.getForks();
		git.created = repository.getCreatedAt();
		git.lastPushed = repository.getPushedAt();
		
		return git;
	}
	
	private void getCommitDataForRepositoryAndUser(Repository repository, User currentUser, GitHubData data) throws Exception {
		final String name = repository.getName();
		
		System.out.print("GitHub: fetching details for repository " + name + "...");
		
		List<RepositoryCommit> commits = commitService.getCommits(repository);
		int commitSize = commits.size();
		
		System.out.println("Commits for " + name + " = " + commitSize);
		
		// fetch the data for each commit
		for(RepositoryCommit repositoryCommit : commits) {
			if(repositoryCommit.getCommitter() != null && repositoryCommit.getCommitter().getId() == currentUser.getId()) {
				// the user has committed this change
				final String sha = repositoryCommit.getSha();
				ExtendedCommitService ecs = new ExtendedCommitService();
				ExtendedRepositoryCommit commit = ecs.getCommit(repository, sha);

				GitHubCommitData commitData = getCommitData(name, sha, commit);
				
				data.addDetails(commitData);
			}
		}
		
		System.out.println("done!");
	}

	private GitHubCommitData getCommitData(String repositoryName, String sha, ExtendedRepositoryCommit commit) {
		GitHubCommitData commitData = new GitHubCommitData();

		commitData.repositoryID = repositoryName;
		commitData.sha = sha;
		commitData.createdAt = commit.getCommitter().getCreatedAt();
		commitData.additions = commit.getStats().getAdditions();
		commitData.deletions = commit.getStats().getDeletions();
		commitData.filesImpacted = commit.getFiles().size();
		
		return commitData;
	}

}
