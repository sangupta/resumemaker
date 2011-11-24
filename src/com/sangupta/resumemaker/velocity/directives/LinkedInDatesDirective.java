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
package com.sangupta.resumemaker.velocity.directives;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.directive.Directive;
import org.apache.velocity.runtime.parser.node.Node;

import com.google.code.linkedinapi.schema.EndDate;
import com.google.code.linkedinapi.schema.StartDate;

public class LinkedInDatesDirective extends Directive {

	@Override
	public String getName() {
		return "linkedInDates";
	}

	@Override
	public int getType() {
		return LINE;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node) throws IOException, ResourceNotFoundException, ParseErrorException, MethodInvocationException {
		StartDate startDate = null;
		EndDate endDate = null;

		String startReplacement = null;
		String endReplacement = null;
		
		// read the params
		startDate = (StartDate) getArgument(node, context, 0);
		endDate = (EndDate) getArgument(node, context, 1);
		startReplacement = (String) getArgument(node, context, 2);
		endReplacement = (String) getArgument(node, context, 3);
		
		StringBuilder builder = new StringBuilder();
		boolean found = false;
		
		if(startDate != null) {
			if(startDate.getMonth() != null && startDate.getMonth() != 0) {
				builder.append(startDate.getMonth()).append("/");
				found = true;
			}
			if(startDate.getYear() != null) {
				builder.append(startDate.getYear());
				found = true;
			}
		}
		
		if(!found && startReplacement != null) {
			builder.append(startReplacement);
			found = true;
		}
		
		if(found) {
			builder.append(" - ");
		}
		
		found = false;
		
		if(endDate != null) {
			if(endDate.getMonth() != null && endDate.getMonth() != 0) {
				builder.append(endDate.getMonth()).append("/");
				found = true;
			}
			if(endDate.getYear() != null) {
				builder.append(endDate.getYear());
				found = true;
			}
		}
		
		if(!found && endReplacement != null) {
			builder.append(endReplacement);
			found = true;
		}
		
		if(!found) {
			// remove the additive of ' - '
			builder.delete(builder.length() - 3, builder.length());
		}
		
		writer.write(builder.toString());
		
		return true;
	}
	
	private Object getArgument(Node node, InternalContextAdapter context, int index) {
		if(node.jjtGetNumChildren() > index && node.jjtGetChild(index) != null) {
			return node.jjtGetChild(index).value(context);
		}
		
		return null;
	}

}
