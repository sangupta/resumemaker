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

import java.util.ArrayList;
import java.util.List;

import com.google.code.linkedinapi.schema.Certification;
import com.google.code.linkedinapi.schema.Education;
import com.google.code.linkedinapi.schema.MemberUrl;
import com.google.code.linkedinapi.schema.Patent;
import com.google.code.linkedinapi.schema.Position;
import com.google.code.linkedinapi.schema.Publication;
import com.google.code.linkedinapi.schema.Recommendation;
import com.google.code.linkedinapi.schema.Skill;
import com.google.code.linkedinapi.schema.TwitterAccount;

public class LinkedInUserData {
	
	private static final String COMMA = ",";

	private static final String SEMICOLON = ";";

	private String name;
	
	private String headline;
	
	private String summary;
	
	private String specialities;
	
	private String pictureUrl;
	
	private String publicProfileUrl;
	
	private final List<String> honors = new ArrayList<String>();
	
	private final List<String> interests = new ArrayList<String>();
	
	private final List<Recommendation> recommendations = new ArrayList<Recommendation>();
	
	private final List<Education> educations = new ArrayList<Education>();
	
	private final List<Certification> certifications = new ArrayList<Certification>();
	
	private final List<Patent> patents = new ArrayList<Patent>();
	
	private final List<Position> positions = new ArrayList<Position>();
	
	private final List<Publication> publications = new ArrayList<Publication>();
	
	private final List<Skill> skills = new ArrayList<Skill>();
	
	private final List<MemberUrl> memberUrlList = new ArrayList<MemberUrl>();
	
	private final List<TwitterAccount> twitterAccountList = new ArrayList<TwitterAccount>();

	public void addRecommendations(List<Recommendation> recommendations) {
		this.recommendations.addAll(recommendations);
	}
	
	public void addEducations(List<Education> educations) {
		this.educations.addAll(educations);
	}

	public void addCertifications(List<Certification> certifications) {
		this.certifications.addAll(certifications);
	}

	public void addPatents(List<Patent> patents) {
		this.patents.addAll(patents);
	}

	public void addPositions(List<Position> positionList) {
		this.positions.addAll(positionList);
	}

	public void addPublications(List<Publication> publicationList) {
		this.publications.addAll(publicationList);
	}

	public void addSkills(List<Skill> skillList) {
		this.skills.addAll(skillList);
	}
	
	public void setInterests(String interests) {
		if(interests != null && interests.trim().length() > 0) {
			String[] tokens = interests.split(COMMA);
			for(String token : tokens) {
				if(token != null && token.trim().length() > 0) {
					this.interests.add(token.trim());
				}
			}
		}
	}
	
	public void setHonors(String honors) {
		if(honors != null && honors.trim().length() > 0) {
			String[] tokens = honors.split(SEMICOLON);
			for(String token : tokens) {
				if(token != null && token.trim().length() > 0) {
					this.honors.add(token.trim());
				}
			}
		}
	}
	
	public List<String> getAwards() {
		return honors;
	}
	
	// Usual accessor's follow

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public List<Recommendation> getRecommendations() {
		return recommendations;
	}

	public List<Education> getEducations() {
		return educations;
	}

	public List<Certification> getCertifications() {
		return certifications;
	}

	public List<Patent> getPatents() {
		return patents;
	}

	public List<Position> getPositions() {
		return positions;
	}

	public List<Publication> getPublications() {
		return publications;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities;
	}

	public List<String> getInterests() {
		return interests;
	}

	public List<String> getHonors() {
		return honors;
	}

	public String getPictureUrl() {
		return pictureUrl;
	}

	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	public String getPublicProfileUrl() {
		return publicProfileUrl;
	}

	public void setPublicProfileUrl(String publicProfileUrl) {
		this.publicProfileUrl = publicProfileUrl;
	}

	public void addUrlResources(List<MemberUrl> memberUrlList) {
		this.memberUrlList.addAll(memberUrlList);
	}

	public void addTwitterAccounts(List<TwitterAccount> twitterAccountList) {
		this.twitterAccountList.addAll(twitterAccountList);
	}

	public List<MemberUrl> getMemberUrlList() {
		return memberUrlList;
	}

	public List<TwitterAccount> getTwitterAccountList() {
		return twitterAccountList;
	}
}
