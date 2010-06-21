package org.loon.framework.android.game.extend.db.index;

import java.io.IOException;

import org.loon.framework.android.game.extend.db.AccessData;

public class IndexLevels extends BaseLevels {
	public int levelPage;
	public int spanPage;
	public AccessData lock;

	protected IndexLevels() {
	}

	public IndexLevels(AccessData lock, int levelPage, IndexList bsl)
			throws IOException {
		this.levelPage = levelPage;
		this.lock = lock;

		AccessData.pageSeek(lock.access, levelPage);

		bsl.levelHash.put(new Integer(this.levelPage), this);

		int maxLen = lock.access.readShort();
		int nonNull = lock.access.readShort();
		spanPage = lock.access.readInt();
		bottom = (IndexSpan) bsl.spanHash.get(new Integer(spanPage));

		this.levels = new IndexLevels[maxLen];
		int lp;
		for (int i = 0; i < nonNull; i++) {
			lp = lock.access.readInt();
			if (lp != 0) {
				levels[i] = (IndexLevels) bsl.levelHash.get(new Integer(lp));
				if (levels[i] == null) {
					levels[i] = new IndexLevels(lock, lp, bsl);
					bsl.levelHash.put(new Integer(lp), levels[i]);
				}
			} else {
				levels[i] = null;
			}
		}

	}

	public static void init(AccessData lock, int page, int spanPage,
			int maxHeight) throws IOException {
		AccessData.pageSeek(lock.access, page);
		lock.access.writeShort((short) maxHeight);
		lock.access.writeShort(0);
		lock.access.writeInt(spanPage);
	}

	public void flush() {
		try {
			AccessData.pageSeek(lock.access, levelPage);
			lock.access.writeShort((short) levels.length);
			int i = 0;
			for (i = 0; i < levels.length; i++) {
				if (levels[i] == null) {
					break;
				}
			}
			lock.access.writeShort(i);
			lock.access.writeInt(((IndexSpan) bottom).page);
			for (i = 0; i < levels.length; i++) {
				if (levels[i] == null) {
					break;
				}
				lock.access.writeInt(((IndexLevels) levels[i]).levelPage);
			}
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	public void killInstance() {
		try {
			lock.freePage(levelPage);
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	public BaseLevels newInstance(int levels, BaseSpan ss, BaseList sl) {
		try {
			IndexSpan bss = (IndexSpan) ss;
			IndexList bsl = (IndexList) sl;
			int page = lock.allocPage();
			IndexLevels.init(lock, page, bss.page, levels);
			return new IndexLevels(lock, page, bsl);
		} catch (IOException ioe) {
			throw new Error();
		}
	}
}
