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
package com.sangupta.resumemaker;

import java.io.IOException;

public class BrowserUtil {
	
	public static void openUrlInBrowser(String url) {
		// open the generated URL in system browser
		Runtime rt = Runtime.getRuntime();
		try {
			rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
