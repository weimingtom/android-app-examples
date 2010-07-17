package org.loon.framework.android.game.extend.db.index;

import java.util.HashSet;
import java.util.Set;

public class BaseLevels {

	public BaseLevels[] levels;

	public BaseSpan bottom;

	public BaseLevels newInstance(int levels, BaseSpan ss, BaseList sl) {
		return new BaseLevels(levels, ss);
	}

	public void killInstance() {
	}

	public void flush() {
	}

	protected BaseLevels() {
	}

	public BaseLevels(int size, BaseSpan span) {
		if (size < 1) {
			throw new Error("size < 1 !");
		}
		levels = new BaseLevels[size];
		bottom = span;
	}

	public BaseSpan getEnd() {
		for (int i = (levels.length - 1); i >= 0; i--) {
			if (levels[i] != null) {
				return levels[i].getEnd();
			}
		}
		return bottom.getEnd();
	}

	public BaseSpan getSpan(int start, Comparable key, int[] search) {
		for (int i = Math.min(start, levels.length - 1); i >= 0; i--) {
			if ((levels[i] != null) && (levels[i].key().compareTo(key) <= 0)) {
				return levels[i].getSpan(i, key, search);
			}
		}
		return bottom.getSpan(key, search);
	}

	public Comparable key() {
		return bottom.keys[0];
	}

	public Set keys() {
		Set set = new HashSet(10);
		for (int i = 1; i < bottom.keys.length; i++) {
			Object key = bottom.keys[i];
			if (key != null) {
				set.add(key);
			}
		}
		return set;
	}

	public Object get(int start, Comparable key) {
		for (int i = Math.min(start, levels.length - 1); i >= 0; i--) {
			if ((levels[i] != null) && (levels[i].key().compareTo(key) <= 0)) {
				return levels[i].get(i, key);
			}
		}
		return bottom.get(key);
	}

	public Object[] remove(int start, Comparable key, BaseList sl) {

		Object[] res = null;
		BaseLevels slvls = null;
		for (int i = Math.min(start, levels.length - 1); i >= 0; i--) {
			if (levels[i] != null) {
				int cmp = levels[i].key().compareTo(key);
				if ((cmp < 0) || ((i == 0) && (cmp <= 0))) {
					res = levels[i].remove(i, key, sl);
					if ((res != null) && (res[1] != null)) {
						slvls = (BaseLevels) res[1];
						if (levels.length >= slvls.levels.length) {
							res[1] = null;
						}
						for (int j = 0; j < (Math.min(slvls.levels.length,
								levels.length)); j++) {
							if (levels[j] == slvls) {
								levels[j] = slvls.levels[j];
							}
						}
						this.flush();
					}
					return res;
				}
			}
		}
		res = bottom.remove(key, sl);
		if ((res != null) && (res[1] != null)) {
			if (res[1] == bottom) {
				res[1] = this;
			} else {
				res[1] = null;
			}
		}
		if ((bottom.nKeys == 0) && (sl.first != bottom)) {
			this.killInstance();
		}
		return res;
	}

	public BaseLevels put(final int start, final Comparable key,
			final Object val, final BaseList sl) {
		BaseSpan ss = null;
		BaseLevels slvls = null;
		for (int i = Math.min(start, levels.length - 1); i >= 0; i--) {
			if ((levels[i] != null) && (levels[i].key().compareTo(key) <= 0)) {
				slvls = levels[i].put(i, key, val, sl);
				if (slvls != null) {
					for (int j = i + 1; j < (Math.min(slvls.levels.length,
							levels.length)); j++) {
						slvls.levels[j] = levels[j];
						levels[j] = slvls;
					}
					if (levels.length < slvls.levels.length) {
						this.flush();
						return slvls;
					}
				}
				this.flush();
				return null;
			}
		}
		ss = bottom.put(key, val, sl);
		if (ss != null) {
			int height = sl.generateColHeight();
			if (height != 0) {
				slvls = this.newInstance(height, ss, sl);
				for (int i = 0; i < (Math.min(height, levels.length)); i++) {
					slvls.levels[i] = levels[i];
					levels[i] = slvls;
				}
			}
			this.flush();
			if (levels.length >= height) {
				return null;
			}
			return slvls;
		} else {
			return null;
		}
	}
}
