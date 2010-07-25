package org.loon.framework.android.game.utils.collection.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
class DataObjectTableImpl implements DataObjectTable, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1982090111733950791L;

	private List objects;

	protected Map map;

	private String eigenName;

	private String eigenValue;

	private DataObject data;

	private List numbers;

	private boolean isReverse;

	public DataObjectTableImpl() {
		this.objects = new ArrayList();
		this.map = new HashMap();
		this.data = new DataObjectImpl();
		this.numbers = new ArrayList();
		this.isReverse = false;
	}

	public DataObjectTableImpl(String eigenName) {
		this.objects = new ArrayList();
		this.map = new HashMap();
		this.data = new DataObjectImpl();
		this.numbers = new ArrayList();
		this.isReverse = false;
		this.eigenName = eigenName;
	}

	public DataObjectTableImpl(String eigenName, String eigenValue) {
		objects = new ArrayList();
		map = new HashMap();
		data = new DataObjectImpl();
		numbers = new ArrayList();
		isReverse = false;
		this.eigenName = eigenName;
		this.eigenValue = eigenValue;
	}

	public DataObjectTableImpl(List list) {
		objects = new ArrayList();
		map = new HashMap();
		data = new DataObjectImpl();
		numbers = new ArrayList();
		isReverse = false;
		setDataObjectTable(list);
	}

	public String getEigenName() {
		return eigenName;
	}

	public void setEigenName(String eigenName) {
		this.eigenName = eigenName;
	}

	public String getEigenValue() {
		return eigenValue;
	}

	public void setEigenValue(String eigenValue) {
		this.eigenValue = eigenValue;
	}

	public boolean containsKey(String key) {
		return map.containsKey(key);
	}

	public String getValue(String key) {
		return data.get(key);
	}

	public void setValue(String key, String value) {
		data.set(key, value);
	}

	public DataObject getDataObject() {
		return data;
	}

	public List getObjects() {
		return objects;
	}

	public void setObjects(List objects) {
		this.objects = objects;
	}

	public void setDataObjectTable(DataObjectTable table) {
		for (int i = 0; i < table.size(); i++) {
			DataObject data = table.get(i);
			add(data);
		}

	}

	public void setDataObjectTable(List table) {
		for (int i = 0; i < table.size(); i++) {
			DataObject data = (DataObject) table.get(i);
			add(data);
		}

	}

	public void add(DataObject data) {
		add(getEigenName(), data);
	}

	public void add(String key, String value) {
		map.put(key, value);
	}

	public void add(String key, DataObject data) {
		objects.add(data);
		map.put(key, data);
	}

	public void addStore(String key, DataObject data) {
		List list = (List) map.get(key);
		if (list == null)
			list = new ArrayList();
		list.add(data);
		map.put(key, list);
	}

	public Map getMap() {
		return map;
	}

	public void set(DataObject data) {
		for (int i = 0; i < data.objectSize(); i++)
			add((DataObject) data.getObject(i));

	}

	public void add(int index, DataObject data) {
		objects.add(index, data);
	}

	public DataObject get(int index) {
		if (!isReverse) {
			return (DataObject) objects.get(index);
		} else {
			int lastIndex = objects.size();
			return (DataObject) objects.get(lastIndex - index - 1);
		}
	}

	public DataObject get(String key) {
		return (DataObject) map.get(key);
	}

	public int size() {
		return objects.size();
	}

	public void remove(int index) {
		objects.remove(index);
	}

	public void remove(DataObject obj) {
		objects.remove(obj);
	}

	public void removeAll() {
		objects = new ArrayList();
		map = new HashMap();
	}

	public void orderBy(String name) {
		sort("*" + name);
	}

	public void orderByDesc(String name) {
		sort("-" + name);
	}

	public void orderByAsc(String name) {
		sort("*" + name);
	}

	public void orderByNumber(String name) {
		sortNumber("*" + name);
	}

	public void orderByDescNumber(String name) {
		sortNumber("-" + name);
	}

	public void orderByAscNumber(String name) {
		sortNumber("*" + name);
	}

	public void addNumberKey(String key) {
		numbers.add(key);
	}

	public void sort(String sortKey) {
		DataObjectComparator comparator = new DataObjectComparator(sortKey);
		comparator.setNumberKeys(numbers);
		Collections.sort(getObjects(), comparator);
	}

	public void sortNumber(String sortKey) {
		List numbers = new ArrayList();
		numbers.add(sortKey);
		DataObjectComparator comparator = new DataObjectComparator(sortKey);
		comparator.setNumberKeys(numbers);
		Collections.sort(getObjects(), comparator);
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

}
