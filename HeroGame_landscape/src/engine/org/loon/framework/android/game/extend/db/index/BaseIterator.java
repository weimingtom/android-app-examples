package org.loon.framework.android.game.extend.db.index;

import java.util.ListIterator;
import java.util.NoSuchElementException;

public class BaseIterator implements ListIterator {
	BaseSpan baseSpan;
	int index;

	protected BaseIterator() {
	}

	public BaseIterator(BaseSpan ss, int index) {
		if (ss == null) {
			throw new NullPointerException();
		}
		this.baseSpan = ss;
		this.index = index;
	}

	public boolean hasNext() {
		if (index < baseSpan.nKeys) {
			return true;
		}
		return false;
	}

	public Object next() {
		Object o;
		if (index < baseSpan.nKeys) {
			o = baseSpan.vals[index];
		} else {
			throw new NoSuchElementException();
		}

		if (index < (baseSpan.nKeys - 1)) {
			index++;
		} else if (baseSpan.next != null) {
			baseSpan = baseSpan.next;
			index = 0;
		} else {
			index = baseSpan.nKeys;
		}
		return o;
	}

	public Comparable nextKey() {
		if (index < baseSpan.nKeys) {
			return baseSpan.keys[index];
		}
		throw new NoSuchElementException();
	}

	public boolean hasPrevious() {
		if (index > 0) {
			return true;
		}
		if ((baseSpan.prev != null) && (baseSpan.prev.nKeys > 0)) {
			return true;
		}
		return false;
	}

	public Object previous() {
		if (index > 0) {
			index--;
		} else if (baseSpan.prev != null) {
			baseSpan = baseSpan.prev;
			if (baseSpan.nKeys <= 0) {
				throw new NoSuchElementException();
			}
			index = (baseSpan.nKeys - 1);
		}
		return baseSpan.vals[index];
	}

	public void add(Object o) {
		throw new UnsupportedOperationException();
	}

	public void remove() {
		throw new UnsupportedOperationException();
	}

	public void set(Object o) {
		throw new UnsupportedOperationException();
	}

	public int nextIndex() {
		throw new UnsupportedOperationException();
	}

	public int previousIndex() {
		throw new UnsupportedOperationException();
	}

}
