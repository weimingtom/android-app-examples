package org.loon.framework.android.game.utils.collection.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.loon.framework.android.game.utils.NumberUtils;
import org.loon.framework.android.game.utils.StringUtils;

class DataObjectImpl implements DataObject, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -198209016247118610L;

	private Map valueMap;

	private Map typeMap;

	private List valueList;

	private String eigenName;

	private boolean isEigenBoolean;

	private Map eigenMap;

	private Map objectMap;

	private List objectList;

	private DataObject data;

	private boolean isReverse;

	private int objectSize;

	private byte bytes[];

	private String language;

	public DataObjectImpl() {
		valueMap = new LinkedHashMap();
		typeMap = new HashMap();
		valueList = new ArrayList();
		isEigenBoolean = false;
		eigenMap = new HashMap();
		objectMap = new LinkedHashMap();
		objectList = new ArrayList();
		isReverse = false;
		objectSize = 0;
	}

	public DataObjectImpl(Map map) {
		valueMap = new LinkedHashMap();
		typeMap = new HashMap();
		valueList = new ArrayList();
		isEigenBoolean = false;
		eigenMap = new HashMap();
		objectMap = new LinkedHashMap();
		objectList = new ArrayList();
		isReverse = false;
		objectSize = 0;
		String keys[] = getKeys(map);
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Object value = map.get(key);
			if (value instanceof String) {
				set(key, (String) value);
			} else if (value instanceof String[]) {
				setObject(key, value);
			}
		}

	}

	public DataObjectImpl(DataObject data) {
		valueMap = new LinkedHashMap();
		typeMap = new HashMap();
		valueList = new ArrayList();
		isEigenBoolean = false;
		eigenMap = new HashMap();
		objectMap = new LinkedHashMap();
		objectList = new ArrayList();
		isReverse = false;
		objectSize = 0;
		if (data == null) {
			return;
		} else {
			this.setDataObject(data);
			this.data = data;
			return;
		}
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setDataObject(DataObject data) {
		set(data);
		setObject(data);
	}

	public final void copy(DataObject data) {
		if (data == null)
			return;
		set(data);
		for (int i = 0; i < data.objectSize(); i++)
			addObject(data.getObject(i));

	}

	public boolean isReverse() {
		return isReverse;
	}

	public void setReverse(boolean isReverse) {
		this.isReverse = isReverse;
	}

	public DataObjectTable getDataObjectTable() {
		DataObjectTable table = new DataObjectTableImpl();
		for (int i = 0; i < objectSize(); i++)
			table.add((DataObject) getObject(i));

		return table;
	}

	public DataObject getDataObject() {
		return data;
	}

	public boolean containsValueKey(String key) {
		return valueMap.containsKey(key);
	}

	public boolean containsValue(String value) {
		return valueList.contains(value);
	}

	public boolean containsObjectKey(String key) {
		return objectMap.containsKey(key);
	}

	public boolean containsObjectValue(String value) {
		return objectList.contains(value);
	}

	public final void setMap(Map map) {
		String keys[] = getKeys(map);
		if (keys == null)
			return;
		for (int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Object obj = map.get(key);
			if (obj instanceof String)
				set(key, (String) obj);
		}

	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte bytes[]) {
		this.bytes = bytes;
	}

	public final String get(String key) {
		return (String) valueMap.get(key);
	}

	public final int getInt(String key) {
		String number = (String) valueMap.get(key);
		return NumberUtils.parseInt(number);
	}

	public final String getType(String key) {
		return (String) typeMap.get(key);
	}

	public final void set(String key, int value) {
		set(key, String.valueOf(value), "int");
	}

	public final void set(String key, long value) {
		set(key, String.valueOf(value), "long");
	}

	public final void set(String key, double value) {
		set(key, String.valueOf(value), "double");
	}

	public final void set(String key, boolean value) {
		set(key, String.valueOf(value), "boolean");
	}

	public final void set(String key, String value) {
		set(key, value, "String");
	}

	public final void set(String key, String value, String type) {
		typeMap.put(key, type);
		valueMap.put(key, value);
		valueList.add(key + value);
	}

	public final void remove(String key) {
		typeMap.remove(key);
		valueMap.remove(key);
	}

	public final void set(String key, DataObjectTable table) {
		set(key, table.getValue(key));
	}

	public final void set(DataObject data) {
		if (data == null)
			return;
		String valuesKeys[] = data.getValueKeys();
		for (int i = 0; i < valuesKeys.length; i++)
			set(valuesKeys[i], data);

	}

	public final void set(String key, DataObject data) {
		if (key == null || data == null) {
			return;
		} else {
			set(key, data.get(key));
			return;
		}
	}

	public String[] getValueKeys() {
		return getKeys(valueMap);
	}

	public int valueSize() {
		return valueMap.size();
	}

	public Object[] getValues() {
		Iterator it = valueMap.values().iterator();
		return getIteratorValues(it);
	}

	public Object[] getValues(String key) {
		List result = new ArrayList();
		for (int i = 0; i < valueList.size(); i++) {
			String value = (String) valueList.get(i);
			if (value.indexOf(key) != -1) {
				result.add(StringUtils.replace(value, key, ""));
			}
		}
		return result.toArray();
	}

	public void change(String key1, String key2) {
		valueMap.put(key1, get(key2));
	}

	public String getEigenName() {
		return eigenName;
	}

	public void setEigenName(String eigenName) {
		this.eigenName = eigenName;
	}

	public boolean isEigenBoolean() {
		return isEigenBoolean;
	}

	public void setEigenBoolean(boolean isEigenBoolean) {
		this.isEigenBoolean = isEigenBoolean;
	}

	public String getEigenValue(String key) {
		return (String) eigenMap.get(key);
	}

	public void setEigenValue(String key, String value) {
		eigenMap.put(key, value);
	}

	public Object getObject(String key) {
		return objectMap.get(key);
	}

	public Object getObject(int index) {
		if (!isReverse())
			return objectList.get(index);
		else
			return objectList.get(objectSize - index - 1);
	}

	public Object getFirstObject() {
		int max = objectSize();
		if (max == 0)
			return null;
		else
			return objectList.get(0);
	}

	public Object getLastObject() {
		int max = objectSize();
		if (max == 0)
			return null;
		else
			return objectList.get(max - 1);
	}

	public void setObject(int index, Object data) {
		objectList.set(index, data);
	}

	public void addObject(int index, Object data) {
		objectList.add(index, data);
	}

	public final void removeAttributeAll() {
		valueMap = new HashMap();
	}

	public void removeObject(int index) {
		objectList.remove(index);
	}

	public boolean removeObject(Object value) {
		return objectList.remove(value);
	}

	public void removeObjectAll() {
		objectList = new ArrayList();
	}

	public void setObject(String key, Object value) {
		objectMap.put(key, value);
		objectList.add(value);
		objectSize++;
	}

	public void setObject(DataObject data) {
		if (data == null)
			return;
		String objectKeys[] = data.getObjectKeys();
		for (int i = 0; i < objectKeys.length; i++) {
			setObject(objectKeys[i], data);
		}

	}

	public void addObject(Object data) {
		objectList.add(data);
		objectSize++;
	}

	public String[] getObjectKeys() {
		return getKeys(objectMap);
	}

	public int objectSize() {
		return objectList.size();
	}

	public List getObjects() {
		return objectList;
	}

	public Map getMap() {
		return valueMap;
	}

	public static String[] getKeys(Map map) {
		Iterator it = map.keySet().iterator();
		return getIteratorValues(it);
	}

	public static String[] getIteratorValues(Iterator it) {
		List result = getIteratorList(it);
		return (String[]) result.toArray(new String[result.size()]);
	}

	public static List getIteratorList(Iterator it) {
		List result = new ArrayList();
		for (; it.hasNext(); result.add(it.next()))
			;
		return result;
	}

}
