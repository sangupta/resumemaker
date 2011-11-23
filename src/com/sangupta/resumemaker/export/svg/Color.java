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

public class Color {
	
private int red;
	
	private int blue;
	
	private int green;
	
	private float alpha;
	
	public Color(int red, int green, int blue, float alpha) {
		this.red = red;
		this.green = green;
		this.blue = blue;
		this.alpha = alpha;
	}
	
	public String toRGBAString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append("rgba(");
		builder.append(this.red);
		builder.append(", ");
		builder.append(this.green);
		builder.append(", ");
		builder.append(this.blue);
		builder.append(", ");
		builder.append(this.alpha);
		builder.append(")");
		
		return builder.toString();
	}
	
	public String toHexString() {
		StringBuilder builder = new StringBuilder();
		
		builder.append('#').append(Integer.toHexString(this.red)).append(Integer.toHexString(this.green)).append(Integer.toHexString(this.blue));
		
		return builder.toString();
	}

}
