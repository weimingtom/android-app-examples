package org.loon.framework.android.game.utils.collection;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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

public class ArraySortBag {

	private final static int INITIAL_CAPACITY = 20;

	private Object[] elements;

	private int size;

	public ArraySortBag() {
		size = 0;
		elements = new Object[INITIAL_CAPACITY];
	}

	private void swap(Object[] data, int x, int y) {
		Object temp = data[x];
		data[x] = data[y];
		data[y] = temp;
	}

	private void sort(Object[] src, Object[] dest, int low, int high, int off) {
		int length = high - low;
		if (length < 10) {
			for (int i = low; i < high; i++)
				for (int j = i; j > low
						&& ((Comparable) dest[j - 1]).compareTo(dest[j]) > 0; j--) {
					swap(dest, j, j - 1);
				}
			return;
		}
		int destLow = low;
		int destHigh = high;
		low += off;
		high += off;
		int mid = (low + high) >> 1;
		sort(dest, src, low, mid, -off);
		sort(dest, src, mid, high, -off);
		if (((Comparable) src[mid - 1]).compareTo(src[mid]) <= 0) {
			System.arraycopy(src, low, dest, destLow, length);
			return;
		}
		for (int i = destLow, p = low, q = mid; i < destHigh; i++) {
			if (q >= high || p < mid
					&& ((Comparable) src[p]).compareTo(src[q]) <= 0) {
				dest[i] = src[p++];
			} else {
				dest[i] = src[q++];
			}
		}
	}

	private Object[] sort(Object[] obj) {
		if (size == 0) {
			return null;
		}
		Object[] dest = new Object[size];
		System.arraycopy(obj, 0, dest, 0, size);
		sort(obj, dest, 0, size, 0);
		int i = 0, j = 0;
		for (i = 0; i < size - 1; i++) {
			if (dest[i] != dest[i + 1]) {
				dest[i] = dest[i + 1];
				j++;
			}
		}
		return dest;
	}

	private void expandCapacity(int capacity) {
		if (elements.length < capacity) {
			Object[] bagArray = (Object[]) new Object[capacity];
			System.arraycopy(elements, 0, bagArray, 0, size);
			elements = bagArray;
		}
	}

	private void compressCapacity(int capacity) {
		if (capacity + this.size < elements.length) {
			Object[] newArray = (Object[]) new Object[this.size + 2];
			System.arraycopy(elements, 0, newArray, 0, this.size);
			elements = newArray;
		}
	}

	private boolean remove(Object obj, boolean isRemoves) {
		if (obj == null) {
			return false;
		}
		if (elements == null) {
			return false;
		}
		boolean flag = false;
		for (int i = size; i > 0; i--) {
			if (obj.equals(elements[i])) {
				flag = true;
				size--;
				elements[i] = elements[size];
				elements[size] = null;
				if (size == 0) {
					elements = null;
				} else {
					compressCapacity(2);
				}
				if (!isRemoves) {
					return true;
				}
			}
		}
		return flag;
	}

	public boolean add(Object obj) {
		if (size == elements.length) {
			expandCapacity((size + 1) * 2);
		}
		return (elements[size++] = obj) != null;
	}

	public void clear() {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	public boolean contains(Object obj) {
		if (obj == null) {
			return false;
		}
		if (elements == null) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (elements[i] != null && obj.equals(elements[i])) {
				return true;
			}
		}
		return false;
	}

	public Object first() {
		Object[] first = sort(elements);
		return first == null ? null : first[0];
	}

	public int getCount(Object obj) {
		int index = 0;
		for (int i = 0; i < size; i++) {
			if (obj == elements[i]) {
				index++;
			}
		}
		return index;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Object kth(int k) {
		if (k < 1 || k >= size) {
			return null;
		}
		Object[] first = sort(elements);
		return first == null ? null : first[k - 1];
	}

	public boolean removeAll(Object obj) {
		return remove(obj, true);
	}

	public boolean remove(Object obj) {
		return remove(obj, false);
	}

	public Object second() {
		if (size < 2) {
			return null;
		}
		Object[] first = sort(elements);
		return first == null ? null : first[1];
	}

	public int size() {
		return size;
	}

	public Set uniqueSet() {
		Object[] bagArray = new Object[size];
		int i = 0, j = 0;
		for (i = 0; i < size - 1; i++) {
			while (bagArray[i] == elements[i + 1]) {
				i++;
			}
			bagArray[j++] = elements[i];
		}
		Object[] tempArray = new Object[i];
		System.arraycopy(bagArray, 0, tempArray, 0, i);
		return new HashSet(Arrays.asList(tempArray));
	}

}
