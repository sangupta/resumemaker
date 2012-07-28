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

import java.util.List;

import com.sangupta.resumemaker.util.PercentUtils;

public class TimeLine {
	
	private List<String> years;
	
	private float width;
	
	public TimeLine(List<String> years, float width) {
		this.years = years;
		this.width = width;
	}
	
	public String getWidthPercent() {
		return PercentUtils.twoDecimalString(this.width);
	}
	
	// Usual accessor's follow

	public List<String> getYears() {
		return years;
	}

	public float getWidth() {
		return PercentUtils.twoDecimals(width);
	}

}
