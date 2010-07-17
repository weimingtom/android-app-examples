package org.loon.framework.android.game.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import org.loon.framework.android.game.utils.collection.ArrayIterator;
import org.loon.framework.android.game.utils.collection.ArrayMap;
import org.loon.framework.android.game.utils.collection.ConverterMap;
import org.loon.framework.android.game.utils.collection.LRUMap;
import org.loon.framework.android.game.utils.ioc.injector.Dispose;

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

final public class CollectionUtils {

	final static public int INITIAL_CAPACITY = 10;

	final static private String ARGS_TAG = "=>";

	final static private int ARGS_TAG_LEN = ARGS_TAG.length();

	protected CollectionUtils() {
		super();
	}

	/**
	 * 判定指定对象是否存在于指定对象数组中
	 * 
	 * @param array
	 * @param obj
	 * @return
	 */
	public static int indexOf(Object[] array, Object obj) {
		for (int i = 0; i < array.length; ++i) {
			if (obj == array[i]) {
				return i;
			}
		}
		throw new NoSuchElementException("" + obj);
	}

	/**
	 * 将byte[]转化为char[]
	 * 
	 * @param bytes
	 * @return
	 */
	public static char[] byteToChar(byte[] bytes) {
		char[] tempArray = new char[bytes.length];
		for (int i = 0; i < bytes.length; i++) {
			tempArray[i] = (char) bytes[i];
		}
		return tempArray;
	}

	/**
	 * 将char[]转化为byte[]
	 * 
	 * @param chars
	 * @return
	 */
	public static byte[] charToByte(char[] chars) {
		byte[] tempArray = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			tempArray[i] = (byte) chars[i];
		}
		return tempArray;
	}

	/**
	 * 将String转化为byte[]
	 * 
	 * @param string
	 * @return
	 */
	public static byte[] stringToByte(String string) {
		char[] chars = string.toCharArray();
		byte[] tempArray = new byte[chars.length];
		for (int i = 0; i < chars.length; i++) {
			tempArray[i] = (byte) chars[i];
		}
		return tempArray;
	}

	/**
	 * 将double[]转换为int[]
	 * 
	 * @param doubles
	 * @return
	 */
	public static int[] doubleToInt(double[] doubles) {
		int size = doubles.length;
		int[] valorInt = new int[size];
		for (int i = 0; i < size; i++) {
			valorInt[i] = (int) doubles[i];
		}
		return valorInt;
	}

	/**
	 * 将float转换为int[]
	 * 
	 * @param ints
	 * @return
	 */
	public static int[] floatToInt(float[] ints) {
		int size = ints.length;
		int[] valorInt = new int[size];
		for (int i = 0; i < size; i++) {
			valorInt[i] = (int) ints[i];
		}
		return valorInt;
	}

	/**
	 * 汇聚多个String[]到一个中
	 * 
	 * @param as
	 * @return
	 */
	public static String[] compactStrings(String[] as) {
		String as1[] = new String[as.length];
		int i = 0;
		for (int j = 0; j < as.length; j++) {
			i += as[j].length();
		}
		char ac[] = new char[i];
		i = 0;
		for (int k = 0; k < as.length; k++) {
			as[k].getChars(0, as[k].length(), ac, i);
			i += as[k].length();
		}
		String s = new String(ac);
		i = 0;
		for (int l = 0; l < as.length; l++) {
			as1[l] = s.substring(i, i += as[l].length());
		}
		return as1;
	}

	/**
	 * 扩充指定数组
	 * 
	 * @param obj
	 * @param i
	 * @param flag
	 * @return
	 */
	public static Object expand(Object obj, int i, boolean flag) {
		int j = Array.getLength(obj);
		Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j
				+ i);
		System.arraycopy(obj, 0, obj1, flag ? 0 : i, j);
		return obj1;
	}

	/**
	 * 扩充指定数组
	 * 
	 * @param obj
	 * @param size
	 * @return
	 */
	public static Object expand(Object obj, int size) {
		return expand(obj, size, true);
	}

	/**
	 * 扩充指定数组
	 * 
	 * @param obj
	 * @param size
	 * @param flag
	 * @param class1
	 * @return
	 */
	public static Object expand(Object obj, int size, boolean flag, Class class1) {
		if (obj == null) {
			return Array.newInstance(class1, 1);
		} else {
			return expand(obj, size, flag);
		}
	}

	/**
	 * 剪切出指定长度的数组
	 * 
	 * @param obj
	 * @param size
	 * @return
	 */
	public static Object cut(Object obj, int size) {
		int j;
		if ((j = Array.getLength(obj)) == 1) {
			return Array.newInstance(obj.getClass().getComponentType(), 0);
		}
		int k;
		if ((k = j - size - 1) > 0) {
			System.arraycopy(obj, size + 1, obj, size, k);
		}
		j--;
		Object obj1 = Array.newInstance(obj.getClass().getComponentType(), j);
		System.arraycopy(obj, 0, obj1, 0, j);
		return obj1;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static Object[] copyOf(Object[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static Object[] copyOf(Object[] obj, int newSize) {
		return (Object[]) copyOf(obj, newSize, ((Object) (obj)).getClass());
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @param newType
	 * @return
	 */
	private static Object[] copyOf(Object[] obj, int newSize, Class newType) {
		Object[] copy = ((Object) newType == (Object) Object[].class) ? (Object[]) new Object[newSize]
				: (Object[]) Array.newInstance(newType.getComponentType(),
						newSize);
		return copy;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static int[] copyOf(int[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static int[] copyOf(int[] obj, int newSize) {
		int tempArr[] = new int[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static double[] copyOf(double[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static double[] copyOf(double[] obj, int newSize) {
		double tempArr[] = new double[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static float[] copyOf(float[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static float[] copyOf(float[] obj, int newSize) {
		float tempArr[] = new float[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] copyOf(byte[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static byte[] copyOf(byte[] obj, int newSize) {
		byte tempArr[] = new byte[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static char[] copyOf(char[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static char[] copyOf(char[] obj, int newSize) {
		char tempArr[] = new char[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static long[] copyOf(long[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static long[] copyOf(long[] obj, int newSize) {
		long tempArr[] = new long[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @return
	 */
	public static boolean[] copyOf(boolean[] obj) {
		return copyOf(obj, obj.length);
	}

	/**
	 * copy指定长度的数组数据
	 * 
	 * @param obj
	 * @param newSize
	 * @return
	 */
	public static boolean[] copyOf(boolean[] obj, int newSize) {
		boolean tempArr[] = new boolean[newSize];
		System.arraycopy(obj, 0, tempArr, 0, Math.min(obj.length, newSize));
		return tempArr;
	}

	/**
	 * 将参数转化为map保存
	 * 
	 * @param args
	 * @return
	 */
	final static public Map getArgsToMap(String[] args) {
		Map maps = CollectionUtils.createArrayMap(args.length);
		for (int i = 0; i < args.length; i++) {
			String part = args[i];
			int index = part.indexOf(ARGS_TAG);
			if (index != -1) {
				String key = part.substring(0, index).replaceAll(" ", "");
				String value = part.substring(index + ARGS_TAG_LEN,
						part.length()).replaceAll(" ", "");
				maps.put(key, value);
			}
		}
		return maps;
	}

	/**
	 * 将指定字符串转化以","分割为Map
	 * 
	 * @param args
	 * @return
	 */
	final static public Map getArgsToMap(String args) {
		final String tag = ",";
		if (args.indexOf(tag) == -1 && args.indexOf(ARGS_TAG) == -1)
			return null;
		return getArgsToMap(args.split(tag));
	}

	/**
	 * 将数组变成字符串。
	 * 
	 * @param tag
	 * @param array
	 * @return
	 */
	final static public String implode(final String tag, final Object[] array) {
		if (array == null || array.length == 0)
			return null;
		StringBuffer result = new StringBuffer();
		for (Iterator it = new ArrayIterator(array); it.hasNext();) {
			result.append(it.next());
			result.append(tag);
		}
		int len = result.length();
		result = result.delete(len - tag.length(), len);

		return result.toString();
	}

	/**
	 * 检查指定Collection是否为空
	 * 
	 * @param collection
	 * @return
	 */
	final static public boolean isEmpty(Collection collection) {
		return collection == null || collection.size() == 0;
	}

	/**
	 * 检查指定Map是否为空
	 * 
	 * @param map
	 * @return
	 */
	final static public boolean isEmpty(Map map) {
		return map == null || map.size() == 0;
	}

	/**
	 * 检查指定Collection中是否包含指定对象
	 * 
	 * @param collection
	 * @param item
	 * @return
	 */
	final static public boolean contains(Collection collection, Object item) {
		return collection != null && collection.contains(item);
	}

	/**
	 * 检查指定Map中是否包含指定键
	 * 
	 * @param collection
	 * @param item
	 * @return
	 */
	final static public boolean containsKey(Map collection, Object item) {
		return collection != null && collection.containsKey(item);
	}

	/**
	 * 检查指定Map中是否包含指定值
	 * 
	 * @param collection
	 * @param item
	 * @return
	 */
	final static public boolean containsValue(Map collection, Object item) {
		return collection != null && collection.containsValue(item);
	}

	/**
	 * 返回指定Collection的首元素
	 * 
	 * @param collection
	 * @return
	 */
	final static public Object first(Collection collection) {
		Object[] obj = collection.toArray();
		if (obj.length > 0) {
			return obj[0];
		} else {
			return null;
		}
	}

	final static public Map synchronizedLRUMap(final int size) {
		return Collections.synchronizedMap(new LRUMap(size));
	}

	final static public Map synchronizedLRUMap() {
		return synchronizedLRUMap(10);
	}

	final static public Set synchronizedSet() {
		return Collections.synchronizedSet(new HashSet(INITIAL_CAPACITY));
	}

	final static public List synchronizedList(final int size) {
		return Collections.synchronizedList(createList(size));
	}

	final static public List synchronizedList() {
		return synchronizedList(INITIAL_CAPACITY);
	}

	final static public List createList(Collection collection) {
		return new ArrayList(collection);
	}

	final static public Collection createCollection(Object[] objects) {
		Collection result = createCollection();
		for (int i = 0; i < objects.length; i++) {
			result.add(objects[i]);
		}
		return result;
	}

	/**
	 * 生成集合
	 * 
	 * @return
	 */
	final static public Collection createCollection() {
		return new ArrayList();
	}

	final static public Collection createCollection(int size) {
		return size > 0 ? new ArrayList(size) : createCollection();
	}

	final static public Map createMap() {
		return createMap(INITIAL_CAPACITY);
	}

	final static public Map createMap(final int size) {
		return size > 0 ? new HashMap(size) : new HashMap();
	}

	final static public ArrayMap createArrayMap() {
		return new ArrayMap();
	}

	final static public ArrayMap createArrayMap(final int size) {
		return size > 0 ? new ArrayMap(size) : createArrayMap();
	}

	final static public Set createSet() {
		return createSet(INITIAL_CAPACITY);
	}

	final static public Set createSet(final int size) {
		return new HashSet(size);
	}

	final static public Set createSet(final Set set) {
		return new HashSet(set);
	}

	final static public List createList() {
		return createList(INITIAL_CAPACITY);
	}

	final static public List createList(int size) {
		return size > 0 ? new ArrayList(size) : createList();
	}

	final static public Collection createCollection(Object object) {
		Collection collection = createCollection();
		collection.add(object);
		return collection;
	}

	final static public ConverterMap createConverterMap() {
		return new ConverterMap();
	}

	final static public void visitor(final Collection collection,
			final Dispose dispose) {
		if (collection != null && dispose != null) {
			for (Iterator it = collection.iterator(); it.hasNext();) {
				dispose.accept(it.next());
			}
		}
	}
}
