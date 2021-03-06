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
package com.sangupta.resumemaker.gravatar;

import com.sangupta.resumemaker.Analyzer;
import com.sangupta.resumemaker.MD5Util;
import com.sangupta.resumemaker.model.Config;
import com.sangupta.resumemaker.model.UserData;

public class GravatarAnalyzer implements Analyzer {

	@Override
	public void analyze(Config config, UserData userData) {
		if(config.gravatarID == null) {
			userData.gravatarImageURL = null;
			return;
		}
		
		String hash = MD5Util.md5Hex(config.gravatarID);
		String imageURL = "http://www.gravatar.com/avatar/" + hash;
		
		userData.gravatarImageURL = imageURL;
	}

}
