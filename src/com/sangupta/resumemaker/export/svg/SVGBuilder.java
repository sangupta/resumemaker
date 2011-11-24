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

public class SVGBuilder {
	
	private static final String NEW_LINE = "\n";
	
	private StringBuilder builder;
	
	private boolean closed = false;
	
	public SVGBuilder(int width, int height) {
		builder = new StringBuilder();
	
		// create the base tag
		builder.append("<svg xmlns=\"http://www.w3.org/2000/svg\" version=\"1.1\" ");
		addParam(builder, "width", width);
		addParam(builder, "height", height);
		builder.append(">");
		builder.append(NEW_LINE);
	}
	
	public void addRectangle(Rectangle rectangle) {
		builder.append("<rect ");
		addParam(builder, "x", rectangle.getX());
		addParam(builder, "y", rectangle.getY());
		addParam(builder, "width", rectangle.getWidth());
		addParam(builder, "height", rectangle.getHeight());
		builder.append(" ></rect>");
		builder.append(NEW_LINE);
	}
	
	public void addText(Text text) {
		builder.append("<text ");
		addParam(builder, "x", text.getX());
		addParam(builder, "y", text.getY());
		addParam(builder, "text-anchor", "middle");
		addParam(builder, "class", "xAxisLabels");
		builder.append(" ><tspan>");
		builder.append(text.getText());
		builder.append("</tspan></text>");
		builder.append(NEW_LINE);
	}
	
	public void addLine(Line line) {
		builder.append("<line ");
		addParam(builder, "x1", line.getX1());
		addParam(builder, "y1", line.getY1());
		addParam(builder, "x2", line.getX2());
		addParam(builder, "y2", line.getY2());
		addParam(builder, "style", "stroke: rgb(255,0,0); stroke-width: 2");
		builder.append(" ></line>");
		builder.append(NEW_LINE);
	}
	
	public void addPath(Path path) {
		builder.append("<path ");
		addParam(builder, "d", path.getCommands());
		builder.append(" ></path>");
		builder.append(NEW_LINE);
	}

	public void setDescription(String description) {
		builder.append("<desc ");
		builder.append(">");
		builder.append(description);
		builder.append("</desc>");
		builder.append(NEW_LINE);
	}
	
	private void addParam(StringBuilder builder, String param, float value) {
		builder.append(param);
		builder.append("=\"");
		builder.append(value);
		builder.append("\" ");
	}

	private void addParam(StringBuilder builder, String param, int value) {
		builder.append(param);
		builder.append("=\"");
		builder.append(value);
		builder.append("\" ");
	}

	private void addParam(StringBuilder builder, String param, String value) {
		builder.append(param);
		builder.append("=\"");
		builder.append(value);
		builder.append("\" ");
	}

	public void close() {
		builder.append("</svg>");
		closed = true;
	}
	
	@Override
	public String toString() {
		if(!closed) {
			close();
		}
		
		return this.builder.toString();
	}

}
