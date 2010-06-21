package org.loon.framework.android.game.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.loon.framework.android.game.utils.collection.ArraySet;

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
public abstract class RegexpUtils {

	final static private Map regexpLazy = new HashMap();

	/**
	 * 以正则表达式查找指定条件是否存在
	 * 
	 * @param regexp
	 * @param string
	 * @return
	 */
	public static boolean find(String regexp, String string) {
		Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE
				| Pattern.DOTALL);
		Matcher matcher = pattern.matcher(string);
		return matcher.find();
	}

	/**
	 * 以正则表达式返回指定数据集合
	 * 
	 * @param regexp
	 * @param string
	 * @return
	 */
	public static Set group(String regexp, String string) {
		String key = PassWordUtils.toMD5(regexp);
		if (key != null) {
			key += string.hashCode();
			Set list = (Set) regexpLazy.get(key);
			if (list != null) {
				return list;
			}
			Pattern pattern = Pattern.compile(regexp, Pattern.CASE_INSENSITIVE
					| Pattern.DOTALL);
			Matcher matcher = pattern.matcher(string);
			list = new ArraySet(CollectionUtils.INITIAL_CAPACITY);
			while (matcher.find()) {
				list.add(matcher.group());
			}
			if (list.size() > 0) {
				regexpLazy.put(key, list);
			}
			return list;
		}
		return null;
	}

	/**
	 * 以正则表达式检索所有http地址数据的集合，并返回Set
	 * 
	 * @param string
	 * @return
	 */
	public static Set regexpHttp(String string) {
		return RegexpUtils.group(
				"http://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|]", string);
	}

	/**
	 * 以正则表达式检索所有中文数据的集合，并返回Set
	 * 
	 * @param string
	 * @return
	 */
	public static Set regexpChinese(String string) {
		return RegexpUtils.group("[\u4E00-\u9FA5\uFE30-\uFFA0]+", string);
	}

	/**
	 * 以正则表达式检索所有脚本数据的集合，并返回Set
	 * 
	 * @param string
	 * @return
	 */
	public static Set regexpJavaScript(String string) {
		return RegexpUtils.group("<script((.|\\n)*?)\\<\\/script\\>", string);
	}

	/**
	 * 以正则表达式检索所有img数据的集合，并返回Set
	 * 
	 * @param string
	 * @return
	 */
	public static Set regexpImg(String string) {
		return RegexpUtils.group(
				"<a\\s+[^>]+>\\s*(<img\\s+[^>]+>)\\s*</a>|(<img\\s+[^>]+>)",
				string);
	}

}
