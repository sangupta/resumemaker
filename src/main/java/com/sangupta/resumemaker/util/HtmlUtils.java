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
package com.sangupta.resumemaker.util;

import java.io.StringWriter;

import net.htmlparser.jericho.MicrosoftConditionalCommentTagTypes;
import net.htmlparser.jericho.Source;
import net.htmlparser.jericho.SourceFormatter;

public class HtmlUtils {
	
	static {
		MicrosoftConditionalCommentTagTypes.register();
	}
	
	public static String tidyHtml(String htmlSource) {
		try {
			Source source = new Source(htmlSource) ;
			StringWriter writer = new StringWriter();
			new SourceFormatter(source).setIndentString("  ").setTidyTags(true).writeTo(writer);
			writer.close();
			
			return writer.toString();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return htmlSource;
	}

}
