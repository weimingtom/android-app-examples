package org.loon.framework.android.game.utils;

import java.io.InputStream;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.collection.xml.Ldom;
import org.loon.framework.android.game.utils.collection.xml.XmlFile;

/**
 * 
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
 * @version 0.1.1
 */
final public class XmlUtils {

	private XmlUtils() {
	}

	/**
	 * 获得指定XML文档的全部DOM实例
	 * 
	 * @param fileName
	 * @return
	 */
	public static Ldom getInstance(String fileName) {
		return getInstance(fileName, null);
	}

	/**
	 * 获得指定XML文档的指定DOM实例
	 * 
	 * @param fileName
	 * @param root
	 * @return
	 */
	public static Ldom getInstance(String fileName, String root) {
		try {
			XmlFile xmlFile = new XmlFile(fileName);
			Ldom config = xmlFile.parse(root);
			if (config != null) {
				config.setEncode(LSystem.encoding);
			}
			return config;
		} catch (Exception e) {
			throw new RuntimeException(fileName + " file not found !");
		}
	}

	/**
	 * 获得指定流文件的DOM实例
	 * 
	 * @param in
	 * @return
	 */
	public static Ldom getInstance(InputStream in) {
		XmlFile xmlFile = new XmlFile();
		Ldom config;
		try {
			config = xmlFile.parse(null, in);
			config.setEncode(LSystem.encoding);
			return config;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
