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
package com.sangupta.resumemaker.model;

import java.util.Date;

import com.sangupta.resumemaker.util.PercentUtils;

public class Event implements Comparable<Event> {
	
	private String name;
	
	private String toolTip;
	
	private float start;
	
	private float end;
	
	private Date startDate;
	
	private Date endDate;
	
	public Event(String name, Date start, Date end) {
		this.name = name;
		this.startDate = start;
		this.endDate = end;
	}
	
	public void setCoords(float start, float end) {
		this.start = start;
		this.end = end;
	}

	@Override
	public int compareTo(Event other) {
		return this.startDate.compareTo(other.startDate);
	}
	
	public float getWidth() {
		return PercentUtils.twoDecimals(this.end - this.start);
	}

	public String getStartPercent() {
		return PercentUtils.twoDecimalString(this.start);
	}
	
	public String getEndPercent() {
		return PercentUtils.twoDecimalString(this.end);
	}
	
	public String getWidthPercent() {
		return PercentUtils.twoDecimalString(this.getWidth());
	}
	
	// Usual accessor's follow

	public String getName() {
		return name;
	}

	public Date getStartDate() {
		return startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public String getToolTip() {
		return toolTip;
	}

	public float getStart() {
		return PercentUtils.twoDecimals(start);
	}

	public float getEnd() {
		return PercentUtils.twoDecimals(end);
	}

}
