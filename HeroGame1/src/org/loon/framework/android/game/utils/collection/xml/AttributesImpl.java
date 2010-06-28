package org.loon.framework.android.game.utils.collection.xml;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.android.game.utils.collection.ArrayIterator;
import org.loon.framework.android.game.utils.collection.ArrayMap;

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
class AttributesImpl implements Attributes {

	private ArrayMap arrayMap = null;

	public AttributesImpl(ArrayMap map) {
		arrayMap = map;
	}

	public Object[] toArray() {
		return arrayMap.toArray();
	}

	public Iterator iterator() {
		return new ArrayIterator(toArray());
	}

	public String toString() {
		return arrayMap.toString();
	}

	public boolean isEmpty() {
		return arrayMap.isEmpty();
	}

	public int size() {

		return arrayMap.size();
	}

	public Collection getCollection() {
		return arrayMap.values();
	}

	public boolean containsKey(Object value) {
		return arrayMap.containsKey(value);
	}

	public Object[] getAttributesKeys() {
		Set set = arrayMap.entrySet();
		Object[] result = new Object[set.size()];
		int i = 0;
		for (Iterator it = set.iterator(); it.hasNext();) {
			result[i++] = ((Entry) it.next()).getKey();
		}
		return result;
	}

	public Iterator iteratorKeys() {
		return new ArrayIterator(getAttributesKeys());
	}

	public boolean containsValue(Object value) {
		return arrayMap.containsValue(value);
	}

	public Object getAttributeValue(Object attributeName) {
		return arrayMap.get(attributeName);
	}

	public Object getAttributeValue(int index) {
		if (index >= size())
			return null;
		return toArray()[index];
	}

}
