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
package com.sangupta.resumemaker.export.svg;

public class Path {
	
	private StringBuilder builder;
	
	public Path() {
		builder = new StringBuilder();
	}
	
	public Path moveTo(float x, float y) {
		return moveTo(x, y, false);
	}
	
	public Path moveTo(float x, float y, boolean relative) {
		if(!relative) {
			builder.append("M");
		} else {
			builder.append("m");
		}
		
		builder.append(String.valueOf(x));
		builder.append(",");
		builder.append(String.valueOf(y));
		
		return this;
	}
	
	public Path lineTo(float x, float y) {
		return lineTo(x, y, false);
	}
	
	public Path lineTo(float x, float y, boolean relative) {
		if(!relative) {
			builder.append("L");
		} else {
			builder.append("l");
		}
		
		builder.append(String.valueOf(x));
		builder.append(",");
		builder.append(String.valueOf(y));
		
		return this;
	}
	
	public Path arc(float x, float y, float radiusX, float radiusY, int xAxisRotation, boolean largeArcFlag, boolean sweepFlag) {
		return arc(x, y, radiusX, radiusY, xAxisRotation, largeArcFlag, sweepFlag, false);
	}
	
	public Path arc(float x, float y, float radiusX, float radiusY, int xAxisRotation, boolean largeArcFlag, boolean sweepFlag, boolean relative) {
		if(!relative) {
			builder.append("A");
		} else {
			builder.append("a");
		}
		builder.append(String.valueOf(radiusX));
		
		builder.append(",");
		builder.append(String.valueOf(radiusY));
		
		builder.append(",");
		builder.append(String.valueOf(xAxisRotation));
		
		builder.append(",");
		if(largeArcFlag) {
			builder.append("1");
		} else {
			builder.append("0");
		}
		
		builder.append(",");
		if(sweepFlag) {
			builder.append("1");
		} else {
			builder.append("0");
		}
		
		builder.append(",");
		builder.append(String.valueOf(x));
		
		builder.append(",");
		builder.append(String.valueOf(y));
		
		return this;
	}
	
	public void close() {
		builder.append("Z");
	}

	public String getCommands() {
		return builder.toString();
	}

}
