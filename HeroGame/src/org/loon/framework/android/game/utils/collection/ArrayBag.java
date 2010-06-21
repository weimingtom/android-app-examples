package org.loon.framework.android.game.utils.collection;

import java.io.Serializable;

import org.loon.framework.android.game.utils.CollectionUtils;

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
public class ArrayBag implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1982727378680791345L;

	private Object[] elements;

	private int size;

	public ArrayBag() {
		size = 0;
		elements = new Object[CollectionUtils.INITIAL_CAPACITY];
	}

	public ArrayBag(int initialCapacity) {
		if (initialCapacity < 0)
			throw new IllegalArgumentException(
					"The initialCapacity is negative: " + initialCapacity);
		elements = new Object[initialCapacity];
		size = 0;
	}

	public void add(Object element) {
		if (size == elements.length) {
			ensureCapacity((size + 1) * 2);
		}
		elements[size] = element;
		size++;
	}

	public void addMany(Object[] elements) {
		if (size + elements.length > elements.length) {
			ensureCapacity((size + elements.length) * 2);
		}
		System.arraycopy(elements, 0, elements, size, elements.length);
		size += elements.length;
	}

	public void addAll(ArrayBag addend) {
		ensureCapacity(size + addend.size);
		System.arraycopy(addend.elements, 0, elements, size, addend.size);
		size += addend.size;
	}

	public Object clone() {
		ArrayBag answer;
		try {
			answer = (ArrayBag) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(
					"This class does not implement Cloneable");
		}
		answer.elements = (Object[]) elements.clone();
		return answer;
	}

	public int countOccurrences(Object target) {
		int answer;
		int size;
		answer = 0;
		for (size = 0; size < size; size++) {
			if (target == elements[size]) {
				answer++;
			}
		}
		return answer;
	}

	public void ensureCapacity(int minimumCapacity) {
		Object[] biggerArray;

		if (elements.length < minimumCapacity) {
			biggerArray = (Object[]) new Object[minimumCapacity];
			System.arraycopy(elements, 0, biggerArray, 0, size);
			elements = biggerArray;
		}
	}

	public int getCapacity() {
		return elements.length;
	}

	public Object grab() {
		int i;

		if (size == 0)
			throw new IllegalStateException("Bag size is zero");

		i = (int) (Math.random() * size) + 1;
		return elements[i];
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

	public boolean remove(Object target) {
		return remove(target, false);
	}

	public boolean removeAll(Object target) {
		return remove(target, true);
	}

	public int size() {
		return size;
	}

	public void trim() {
		Object trimmedArray[];
		if (elements.length != size) {
			trimmedArray = new Object[size];
			System.arraycopy(elements, 0, trimmedArray, 0, size);
			elements = trimmedArray;
		}
	}

	public static ArrayBag union(ArrayBag b1, ArrayBag b2) {
		ArrayBag answer = new ArrayBag(b1.getCapacity() + b2.getCapacity());
		System.arraycopy(b1.elements, 0, answer.elements, 0, b1.size);
		System.arraycopy(b2.elements, 0, answer.elements, b1.size, b2.size);
		answer.size = b1.size + b2.size;
		return answer;
	}

}
