package org.loon.framework.android.game.extend.db.index;

import java.util.Random;
import java.util.Set;

public class BaseList {

	protected BaseSpan first;

	protected BaseLevels stack;

	public Random random;

	public int size = 0;

	public int spans = 0;

	public void flush() {
	}

	protected BaseList() {
	}

	public BaseList(int span) {
		if (span < 1) {
			throw new Error("Span < 1 !");
		}
		first = new BaseSpan(span);
		stack = new BaseLevels(1, first);
		spans = 1;
		random = new Random(System.currentTimeMillis());
	}

	public int size() {
		return size;
	}

	public int maxLevels() {
		int hob = 0;
		while (spans > 0) {
			hob++;
			spans = spans / 2;
		}
		return (hob > 4) ? hob : 4;
	}

	public int generateColHeight() {
		int bits = random.nextInt();
		boolean cont = true;
		int res = 0;
		for (res = 0; cont; res++) {
			cont = ((bits % 2) == 0) ? true : false;
			bits = bits / 2;
		}
		return Math.max(0, Math.min(res, maxLevels()));
	}

	public void commit() {
		stack.flush();
		flush();
	}

	public void put(final Comparable key, final Object val) {
		if (key == null) {
			throw new NullPointerException();
		}
		if (val == null) {
			throw new NullPointerException();
		}
		BaseLevels slvls = stack.put(stack.levels.length - 1, key, val, this);
		if (slvls != null) {
			BaseLevels[] levels = new BaseLevels[slvls.levels.length];
			for (int i = 0; i < slvls.levels.length; i++) {
				if (i < stack.levels.length) {
					levels[i] = stack.levels[i];
				} else {
					levels[i] = slvls;
				}
			}
			stack.levels = levels;
			stack.flush();
			flush();
		}
	}

	public Object remove(Comparable key) {
		if (key == null) {
			throw new NullPointerException();
		}
		Object[] res = stack.remove(stack.levels.length - 1, key, this);
		if (res != null) {
			if (res[1] != null) {
				BaseLevels slvls = (BaseLevels) res[1];
				for (int i = 0; i < slvls.levels.length; i++) {
					if (stack.levels[i] == slvls) {
						stack.levels[i] = slvls.levels[i];
					}
				}
				stack.flush();
			}
			flush();
			return res[0];
		}
		return null;
	}

	public Object get(Comparable key) {
		if (key == null) {
			throw new NullPointerException();
		}
		return stack.get(stack.levels.length - 1, key);
	}

	public BaseIterator iterator() {
		return new BaseIterator(first, 0);
	}

	public BaseIterator min() {
		return new BaseIterator(first, 0);
	}

	public BaseIterator max() {
		BaseSpan ss = stack.getEnd();
		return new BaseIterator(ss, ss.nKeys - 1);
	}

	public Set getKeys() {
		return stack.keys();
	}

	public BaseIterator find(Comparable key) {
		int[] search = new int[1];
		BaseSpan ss = stack.getSpan(stack.levels.length - 1, key, search);
		if (search[0] < 0) {
			search[0] = -1 * (search[0] + 1);
		}
		return new BaseIterator(ss, search[0]);
	}

}
