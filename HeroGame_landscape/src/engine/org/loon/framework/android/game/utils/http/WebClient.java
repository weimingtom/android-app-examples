package org.loon.framework.android.game.utils.http;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class WebClient extends AbstractClient {

	public WebClient(String url) {
		super(url);
	}

	public WebClient(URL url) {
		super(url);
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public void postData(String data) {
		this.postData = data;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public int getResponseLength() {
		return responseLength;
	}

	public HashMap getCookies() {
		return cookies;
	}

	public void setCookies(HashMap cookies) {
		this.cookies = cookies;
	}

	public void freeCookies() {
		cookies.clear();
		cookie = "";
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getMethod() {
		return this.method;
	}

	public String getCookie() {
		return cookie;
	}

}
