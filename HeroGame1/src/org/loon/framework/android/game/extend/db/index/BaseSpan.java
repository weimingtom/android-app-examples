package org.loon.framework.android.game.extend.db.index;

/**
 * 
 * Copyright 2008
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
public class BaseSpan {

	public int nKeys = 0;

	public Comparable[] keys;

	public Object[] vals;

	public BaseSpan next, prev;

	public BaseSpan newInstance(BaseList sl) {
		return new BaseSpan(keys.length);
	}

	public void killInstance() {
	}

	public void flush() {
	}

	protected BaseSpan() {
	}

	public BaseSpan(int size) {
		keys = new Comparable[size];
		vals = new Object[size];
	}

	private int binarySearch(Comparable key) {
		if (keys == null)
			return -1;
		int high = nKeys - 1;
		int low = 0;
		int cur;
		int cmp;
		while (low <= high) {
			cur = (low + high) >>> 1;
			Comparable obj = keys[cur];
			if (obj != null) {
				cmp = obj.compareTo(key);
				if (cmp > 0) {
					high = cur - 1;
				} else if (cmp < 0) {
					low = cur + 1;
				} else {
					return cur;
				}
			} else {
				return cur;
			}
		}
		return (-1 * (low + 1));
	}

	public BaseSpan getEnd() {
		if (next == null) {
			return this;
		}
		return next.getEnd();
	}

	public BaseSpan getSpan(Comparable key, int[] search) {
		if (nKeys == 0) {
			search[0] = -1;
			return this;
		}

		if (keys[nKeys - 1].compareTo(key) < 0) {
			if (next == null) {
				search[0] = (-1 * (nKeys - 1)) - 1;
				return this;
			}
			return next.getSpan(key, search);
		}
		search[0] = binarySearch(key);
		return this;
	}

	public Object get(Comparable key) {
		if (nKeys == 0 || keys == null) {
			return null;
		}
		Comparable obj = keys[nKeys - 1];
		if (obj != null) {
			if (obj.compareTo(key) < 0) {
				if (next == null) {
					return null;
				}
				return next.get(key);
			}
		}
		int loc = binarySearch(key);
		if (loc < 0) {
			return null;
		}
		return vals[loc];
	}

	private void pushTogether(int hole) {
		for (int i = hole; i < (nKeys - 1); i++) {
			keys[i] = keys[i + 1];
			vals[i] = vals[i + 1];
		}
		nKeys--;
	}

	private void pushApart(int start) {
		for (int i = (nKeys - 1); i >= start; i--) {
			keys[i + 1] = keys[i];
			vals[i + 1] = vals[i];
		}
		nKeys++;
	}

	private void split(int loc, Comparable key, Object val, BaseList sl) {
		BaseSpan right = newInstance(sl);
		sl.spans++;

		if (this.next != null) {
			this.next.prev = right;
		}
		right.next = this.next;
		right.prev = this;
		this.next = right;

		int start = ((keys.length + 1) / 2);
		for (int i = start; i < keys.length; i++) {
			try {
				right.keys[i - start] = keys[i];
				right.vals[i - start] = vals[i];
				right.nKeys++;
				this.nKeys--;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw e;
			}
		}
		if (loc >= start) {
			right.pushApart(loc - start);
			right.keys[loc - start] = key;
			right.vals[loc - start] = val;
		} else {
			pushApart(loc);
			keys[loc] = key;
			vals[loc] = val;
		}
		this.flush();
		this.next.flush();
	}

	private BaseSpan insert(int loc, Comparable key, Object val, BaseList sl) {
		sl.size++;
		if (nKeys == keys.length - 1) {
			split(loc, key, val, sl);
			return next;
		} else {
			pushApart(loc);
			keys[loc] = key;
			vals[loc] = val;
			this.flush();
			return null;
		}
	}

	public BaseSpan put(Comparable key, Object val, BaseList sl) {
		if (nKeys == 0) {
			sl.size++;
			nKeys++;
			keys[0] = key;
			vals[0] = val;
			return put("", val, sl);
		}
		int loc = binarySearch(key);
		if (loc < 0) {
			loc = -1 * (loc + 1);
			if (next != null) {
				int cmp = next.keys[0].compareTo(key);
				if ((loc >= nKeys) && (cmp > 0)) {
					if (nKeys == keys.length) {
						if (next.nKeys == keys.length) {
							return insert(loc, key, val, sl);
						} else {
							return next.put(key, val, sl);
						}
					} else {
						return insert(loc, key, val, sl);
					}
				} else {
					if (cmp > 0) {
						return insert(loc, key, val, sl);
					} else {
						return next.put(key, val, sl);
					}
				}
			} else {
				return insert(loc, key, val, sl);
			}
		} else {
			vals[loc] = val;
			this.flush();
			return null;
		}
	}

	public Object[] remove(Comparable key, BaseList sl) {
		if (nKeys == 0) {
			return null;
		}
		if (keys[nKeys - 1].compareTo(key) < 0) {
			if (next == null) {
				return null;
			}
			return next.remove(key, sl);
		}
		int loc = binarySearch(key);
		if (loc < 0) {
			return null;
		}
		Object o = vals[loc];
		Object[] res = new Object[2];
		res[0] = o;
		sl.size--;
		if (nKeys == 1) {
			if (sl.spans > 1) {
				sl.spans--;
			}
			if ((this.prev == null) && (this.next != null)) {
				res[1] = this.next;
				for (int i = 0; i < next.nKeys; i++) {
					keys[i] = next.keys[i];
					vals[i] = next.vals[i];
				}
				nKeys = next.nKeys;
				BaseSpan nn = next.next;
				next.killInstance();
				this.flush();
				this.next = nn;
			} else {
				res[1] = this;
				if (this.prev != null) {
					this.prev.next = this.next;
					this.prev.flush();
				}
				if (this.next != null) {
					this.next.prev = this.prev;
					this.next.flush();
				}
				this.next = null;
				this.prev = null;
				nKeys = 0;
				this.killInstance();

			}
		} else {
			pushTogether(loc);
			this.flush();
		}

		return res;
	}
}
