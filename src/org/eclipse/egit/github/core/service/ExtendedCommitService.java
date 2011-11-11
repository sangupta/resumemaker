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
package org.eclipse.egit.github.core.service;

import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_COMMITS;
import static org.eclipse.egit.github.core.client.IGitHubConstants.SEGMENT_REPOS;

import java.io.IOException;

import org.eclipse.egit.github.core.ExtendedRepositoryCommit;
import org.eclipse.egit.github.core.IRepositoryIdProvider;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;

public class ExtendedCommitService extends CommitService {
	
	@Override
	public ExtendedRepositoryCommit getCommit(IRepositoryIdProvider repository, String sha) throws IOException {
		String id = getId(repository);
		
		if (sha == null) {
			throw new IllegalArgumentException("Sha cannot be null"); //$NON-NLS-1$
		}
		
		if (sha.length() == 0) {
			throw new IllegalArgumentException("Sha cannot be empty"); //$NON-NLS-1$
		}

		StringBuilder uri = new StringBuilder(SEGMENT_REPOS);
		uri.append('/').append(id);
		uri.append(SEGMENT_COMMITS);
		uri.append('/').append(sha);
		GitHubRequest request = createRequest();
		request.setUri(uri);
		request.setType(ExtendedRepositoryCommit.class);
		
		GitHubResponse response = client.get(request);
		
		return (ExtendedRepositoryCommit) response.getBody();
	}

}
