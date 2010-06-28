package org.loon.framework.android.game.utils.collection.xml;

import org.loon.framework.android.game.utils.StringUtils;

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
 * @version 0.1
 */
class DataObjectUtil {

	public static final int TEXT_STATE = 0;

	public static final int DOLLAR_STATE = 1;

	public static final int FIELD_NAME_STATE = 2;

	public DataObjectUtil() {
	}

	public static String getSubstituteValue(String source, String key,
			String value) {
		DataObject data = new DataObjectImpl();
		data.set(key, value);
		return getSubstituteValue(source, data);
	}

	public static String getSubstituteValue(String value, DataObject data) {
		return getSubstituteValue(value, data, false);
	}

	public static String getSubstituteValue(String value, DataObject data,
			boolean isEscape) {
		if (value == null)
			return null;
		StringBuffer output = new StringBuffer();
		int stateType = 0;
		String key = "";
		for (int i = 0; i < value.length(); i++) {
			char charRead = value.charAt(i);
			if (stateType == 2)
				key = key + charRead;
			switch (charRead) {
			case 36: // '$'
				stateType = 1;
				char next2Read = value.charAt(i + 2);
				if ('$' == next2Read)
					output.append(charRead);
				break;

			case 123: // '{'
				if (stateType == 1) {
					char nextRead = value.charAt(i + 1);
					if ('$' != nextRead) {
						stateType = 2;
					} else {
						output.append(charRead);
					}
				} else {
					output.append(charRead);
				}
				break;

			case 125: // '}'
				if (stateType == 2) {
					String name = key.substring(0, key.length() - 1);
					String obj = data.get(name.toLowerCase());
					if (obj == null) {
						obj = "";
					}
					if (isEscape) {
						obj = StringUtils.toFromXML(obj);
					}
					output.append(obj);
					stateType = 0;
					key = "";
					break;
				}
			default:
				if (stateType != 2)
					output.append(charRead);
				break;
			}
		}

		return output.toString();
	}

}
