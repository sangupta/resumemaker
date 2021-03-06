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

public class Text {
	
	private float x;
	
	private float y;
	
	private String text;
	
	private String textAnchor;
	
	private String styleClass;
	
	public Text(float x, float y, String text) {
		this.x = x;
		this.y = y;
		this.text = text;
	}
	
	public Text(float x, float y, String text, String textAnchor, String styleClass) {
		this(x, y, text);
		this.textAnchor = textAnchor;
		this.styleClass = styleClass;
	}
	
	// Usual accessors follow

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getTextAnchor() {
		return textAnchor;
	}

	public void setTextAnchor(String textAnchor) {
		this.textAnchor = textAnchor;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
