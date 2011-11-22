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
package com.sangupta.resumemaker.linkedin;

import java.util.EnumSet;

import com.google.code.linkedinapi.client.LinkedInApiClient;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.enumeration.ProfileField;
import com.google.code.linkedinapi.client.oauth.LinkedInAccessToken;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;
import com.google.code.linkedinapi.schema.Certifications;
import com.google.code.linkedinapi.schema.Educations;
import com.google.code.linkedinapi.schema.Patents;
import com.google.code.linkedinapi.schema.Person;
import com.google.code.linkedinapi.schema.Positions;
import com.google.code.linkedinapi.schema.Publications;
import com.google.code.linkedinapi.schema.RecommendationsReceived;
import com.google.code.linkedinapi.schema.Skills;
import com.sangupta.resumemaker.Analyzer;
import com.sangupta.resumemaker.BrowserUtil;
import com.sangupta.resumemaker.ResumeMaker;
import com.sangupta.resumemaker.model.Config;
import com.sangupta.resumemaker.model.UserData;

public class LinkedInAnalyzer implements Analyzer {

	@Override
	public void analyze(Config config, UserData userData) throws Exception {
		LinkedInUserData linkedInUserData = userData.linkedInUserData;
		
		// get data from linked in
		final LinkedInOAuthService oauthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(config.linkedInConfig.consumerKey, config.linkedInConfig.consumerSecret);
		LinkedInRequestToken requestToken = oauthService.getOAuthRequestToken();
		String authUrl = requestToken.getAuthorizationUrl();

		BrowserUtil.openUrlInBrowser(authUrl);
		
		// get the browser code
		System.out.print("Enter the authorization code as provided by LinkedIn: ");
		String code = ResumeMaker.bufferedReader.readLine();
		
		// try and get the access token
		LinkedInAccessToken accessToken = oauthService.getOAuthAccessToken(requestToken, code);
		
		// get the user profile now
		final LinkedInApiClientFactory factory = LinkedInApiClientFactory.newInstance(config.linkedInConfig.consumerKey, config.linkedInConfig.consumerSecret);
		final LinkedInApiClient client = factory.createLinkedInApiClient(accessToken);
		
		Person profile = client.getProfileForCurrentUser(EnumSet.of(ProfileField.FIRST_NAME, ProfileField.LAST_NAME, ProfileField.HEADLINE, ProfileField.SUMMARY, 
				ProfileField.RECOMMENDATIONS_RECEIVED, ProfileField.INTERESTS, ProfileField.ASSOCIATIONS, ProfileField.HONORS, ProfileField.SPECIALTIES, 
				ProfileField.POSITIONS, ProfileField.PUBLICATIONS, ProfileField.SKILLS, ProfileField.PATENTS, ProfileField.CERTIFICATIONS,
				ProfileField.EDUCATIONS));
		
		// get the details
		linkedInUserData.setName(profile.getFirstName() + " " + profile.getLastName());
		linkedInUserData.setHeadline(profile.getHeadline());
		linkedInUserData.setSummary(profile.getSummary());
		
		RecommendationsReceived recommendationsReceived = profile.getRecommendationsReceived();
		if(recommendationsReceived != null) {
			linkedInUserData.addRecommendations(recommendationsReceived.getRecommendationList());
		}
		
		linkedInUserData.setInterests(profile.getInterests());
		linkedInUserData.setSpecialities(profile.getSpecialties());
		linkedInUserData.setHonors(profile.getHonors());
		
		Positions positions = profile.getPositions();
		if(positions != null) {
			linkedInUserData.addPositions(positions.getPositionList());
		}
		
		Publications publications = profile.getPublications();
		if(publications != null) {
			linkedInUserData.addPublications(publications.getPublicationList());
		}
		
		Skills skills = profile.getSkills();
		if(skills != null) {
			linkedInUserData.addSkills(skills.getSkillList());
		}
		
		Patents patents = profile.getPatents();
		if(patents != null) {
			linkedInUserData.addPatents(patents.getPatentList());
		}

		Certifications certifications = profile.getCertifications();
		if(certifications != null) {
			linkedInUserData.addCertifications(certifications.getCertificationList());
		}
		
		Educations educations = profile.getEducations();
		if(educations != null) {
			linkedInUserData.addEducations(educations.getEducationList());
		}
		
		System.out.println("Done loading LinkedIn data!");
	}

}
