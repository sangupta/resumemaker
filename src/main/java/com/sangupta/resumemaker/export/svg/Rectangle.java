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

public class Rectangle {

	private float x;
	
	private float y;

	private float width;
	
	private float height;
	
	private float radiusX;
	
	private float radiusY;
	
	private Color color;
	
	private String styleClass;
	
	public Rectangle(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(float x, float y, float width, float height, String styleClass) {
		this(x, y, width, height);
		this.styleClass = styleClass;
	}
	
	public Rectangle(float x, float y, float width, float height, float radiusX, float radiusY) {
		this(x, y, width, height);
		this.radiusX = radiusX;
		this.radiusY = radiusY;
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

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public float getRadiusX() {
		return radiusX;
	}

	public void setRadiusX(float radiusX) {
		this.radiusX = radiusX;
	}

	public float getRadiusY() {
		return radiusY;
	}

	public void setRadiusY(float radiusY) {
		this.radiusY = radiusY;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public String getStyleClass() {
		return styleClass;
	}

	public void setStyleClass(String styleClass) {
		this.styleClass = styleClass;
	}

}
