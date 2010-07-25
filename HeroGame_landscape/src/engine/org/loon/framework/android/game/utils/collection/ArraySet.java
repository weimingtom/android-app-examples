package org.loon.framework.android.game.utils.collection;

import java.io.Serializable;
import java.util.AbstractSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.loon.framework.android.game.utils.CollectionUtils;

/**
 * Copyright 2008
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain compar
 * copy of the License at
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
public class ArraySet extends AbstractSet implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3050726681789055447L;

	private final static int GROWTH = 2;

	private Object[] elements = null;

	private int size = 0;

	public ArraySet() {
		this(10);
	}

	public ArraySet(final int initialCapacity) {
		elements = new Object[CollectionUtils.INITIAL_CAPACITY];
	}

	public final int size() {
		return size;
	}

	public final boolean isEmpty() {
		return elements == null;
	}

	public final Iterator iterator() {
		return new LocalIterator();
	}

	public final void clear() {
		for (int i = 0; i < elements.length; i++) {
			elements[i] = null;
		}
		size = 0;
	}

	public final boolean contains(Object obj) {
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

	public final boolean add(Object obj) throws NullPointerException {
		if (obj == null) {
			throw new NullPointerException("cannot add null to a ArraySet");
		}

		for (int i = 0; i < size; i++) {
			if (obj.equals(elements[i])) {
				return false;
			}
		}
		compressCapacity(1 + GROWTH);

		if (size == elements.length) {
			Object[] newArray = (Object[]) new Object[elements.length + GROWTH];
			System.arraycopy(elements, 0, newArray, 0, size);
			elements = newArray;
		}

		elements[size] = obj;
		size++;
		return true;
	}

	public final boolean remove(Object obj) {
		if (obj == null) {
			return false;
		}
		if (elements == null) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (obj.equals(elements[i])) {
				size--;
				elements[i] = elements[size];
				elements[size] = null;
				if (size == 0) {
					elements = null;
				} else {
					compressCapacity(GROWTH);
				}
				return true;
			}
		}

		return false;
	}

	private void compressCapacity(int capacity) {
		if (capacity + this.size < elements.length) {
			Object[] newArray = (Object[]) new Object[this.size + 2];
			System.arraycopy(elements, 0, newArray, 0, this.size);
			elements = newArray;
		}
	}

	final public Object clone() {
		ArraySet result = null;
		try {
			ArraySet arraySet = (ArraySet) super.clone();
			result = arraySet;
		} catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
		Object[] es = (Object[]) new Object[size];
		System.arraycopy(elements, 0, es, 0, size);
		result.elements = es;
		return result;
	}

	private class LocalIterator implements Iterator {

		private int position = 0;

		public final boolean hasNext() {
			return position < size;
		}

		public final Object next() throws NoSuchElementException {
			if (position >= size) {
				throw new NoSuchElementException();
			}
			Object result = elements[position];
			position++;
			return result;
		}

		public final void remove() {
			if (position <= 0) {
				throw new IllegalStateException();
			}
			position--;
			size--;
			elements[position] = elements[size];
			elements[size] = null;
			if (size == 0) {
				elements = null;
			} else {
				compressCapacity(GROWTH);
			}
		}

	}

}
