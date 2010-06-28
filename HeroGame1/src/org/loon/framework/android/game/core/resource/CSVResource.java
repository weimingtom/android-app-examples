package org.loon.framework.android.game.core.resource;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.android.game.utils.ioc.reflect.Reflector;

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
public class CSVResource {

	/**
	 * 载入CSV数据后转化为指定类的Object[]
	 * 
	 * @param fileName
	 * @param clazz
	 * @return
	 */
	final static public Object[] loadPropertys(final Class clazz,
			final String fileName) {
		Object[] results = null;
		try {
			List properts = CSVResource.loadPropertys(fileName);
			int size = properts.size();
			results = (Object[]) Array.newInstance(clazz, size);
			for (int i = 0; i < size; i++) {
				Map property = (Map) properts.get(i);
				Set set = property.entrySet();
				results[i] = Reflector.getReflector(clazz).newInstance();
				for (Iterator it = set.iterator(); it.hasNext();) {
					Entry entry = (Entry) it.next();
					Reflector.doStaticInvokeRegister(results[i], (String) entry
							.getKey(), (String) entry.getValue());
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return results;
	}

	/**
	 * 载入CSV文件数据到List
	 * 
	 * @param fileName
	 * @return
	 */
	final static public List loadPropertys(final String fileName) {
		List result = new ArrayList();
		try {
			CSVReader csv = new CSVReader(fileName);
			List names = csv.readLineAsList();
			int length = names.size();
			for (; csv.ready();) {
				Map propertys = new HashMap(length);
				String[] csvItem = csv.readLineAsArray();
				for (int i = 0; i < length; i++) {
					propertys.put((String) names.get(i), csvItem[i]);
				}
				result.add(propertys);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return result;
	}

	/**
	 * 载入CSV文件数据到Object[]
	 * 
	 * @param fileName
	 * @return
	 */
	final static public Object[] loadArrays(final String fileName) {
		return CSVResource.loadPropertys(fileName).toArray();
	}

}
