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

import java.util.Calendar;
import java.util.Date;

import com.google.code.linkedinapi.schema.EndDate;
import com.google.code.linkedinapi.schema.StartDate;

public class LinkedInHelper {
	
	public static Date fromStartDate(StartDate date) {
		if(date == null) {
			return null;
		}
		
		int day = date.getDay() != null ? date.getDay().intValue() : 0;
		int month = date.getMonth() != null ? date.getMonth().intValue() : 0;
		int year = date.getYear().intValue();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
		return calendar.getTime();
	}
	
	public static Date fromEndDate(EndDate date) {
		if(date == null) {
			return null;
		}
		
		int day = date.getDay() != null ? date.getDay().intValue() : 0;
		int month = date.getMonth() != null ? date.getMonth().intValue() : 0;
		int year = date.getYear().intValue();
		
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, day);
		calendar.set(Calendar.MONTH, month);
		calendar.set(Calendar.YEAR, year);
		
		return calendar.getTime();
	}

}
