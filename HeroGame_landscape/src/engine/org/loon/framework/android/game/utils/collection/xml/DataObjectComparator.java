package org.loon.framework.android.game.utils.collection.xml;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
class DataObjectComparator implements Comparator {
	public boolean isAsc;

	private String sortKey;

	private List numbers;

	public DataObjectComparator(String sortKey) {
		isAsc = true;
		numbers = new ArrayList();
		sortKey = sortKey.substring(1);
		String order = sortKey.substring(0, 1);
		if ("*".equals(order))
			isAsc = true;
		else if ("-".equals(order)) {
			isAsc = false;
		} else {
			isAsc = true;
		}
	}

	public void addNumberKey(String key) {
		numbers.add(key);
	}

	private boolean isNumber(String key) {
		return numbers.contains(key);
	}

	public void setNumberKeys(List numbers) {
		this.numbers = numbers;
	}

	public int compare(Object obj1, Object obj2) {
		DataObject data1 = (DataObject) obj1;
		DataObject data2 = (DataObject) obj2;
		String value1 = data1.get(sortKey);
		String value2 = data2.get(sortKey);
		if (value1 == null || value1.length() == 0) {
			return 0;
		}
		if (value2 == null || value2.length() == 0) {
			return 0;
		}
		if (isNumber(sortKey)) {
			Long long1 = new Long(value1);
			Long long2 = new Long(value2);
			if (isAsc) {
				return long1.compareTo(long2);
			} else {
				return long2.compareTo(long1);
			}
		}
		if (isAsc) {
			return value1.compareTo(value2);
		} else {
			return value2.compareTo(value1);
		}
	}

}
