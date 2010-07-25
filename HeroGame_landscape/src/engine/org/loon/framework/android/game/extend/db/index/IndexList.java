package org.loon.framework.android.game.extend.db.index;

import java.io.IOException;
import java.util.HashMap;
import java.util.Random;

import org.loon.framework.android.game.extend.db.AccessData;
import org.loon.framework.android.game.extend.db.Serializer;

public class IndexList extends BaseList {
	public int firstSpanPage = 0;
	public int firstLevelPage = 0;
	public int skipPage = 0;
	private AccessData accessData;

	public HashMap spanHash = new HashMap();
	public HashMap levelHash = new HashMap();

	protected IndexList() {
	}

	public IndexList(int spanSize, AccessData bf, int skipPage, Serializer key,
			Serializer val) throws IOException {
		if (spanSize < 1) {
			throw new Error("Span size too small");
		}
		this.skipPage = skipPage;
		this.accessData = bf;
		AccessData.pageSeek(accessData.access, skipPage);
		firstSpanPage = accessData.access.readInt();
		firstLevelPage = accessData.access.readInt();
		size = bf.access.readInt();
		spans = bf.access.readInt();
		first = new IndexSpan(bf, this, firstSpanPage, key, val);
		stack = new IndexLevels(bf, firstLevelPage, this);
		random = new Random(System.currentTimeMillis());
	}

	public void close() {
		flush();
		first = null;
		stack = null;
	}

	public void flush() {
		try {
			AccessData.pageSeek(accessData.access, skipPage);
			accessData.access.writeInt(firstSpanPage);
			accessData.access.writeInt(firstLevelPage);
			accessData.access.writeInt(size);
			accessData.access.writeInt(spans);

		} catch (IOException ioe) {
			throw new Error();
		}
	}

	public void delete() throws IOException {
		BaseLevels curLevel = stack, nextLevel;
		while (curLevel != null) {
			nextLevel = curLevel.levels[0];
			curLevel.killInstance();
			curLevel = nextLevel;
		}

		BaseSpan curSpan = first, nextSpan;
		while (curSpan != null) {
			nextSpan = curSpan.next;
			curSpan.killInstance();
			curSpan = nextSpan;
		}

		accessData.freePage(skipPage);
	}

	public static void init(AccessData bf, int page, int spanSize)
			throws IOException {
		int firstSpan = bf.allocPage();
		int firstLevel = bf.allocPage();
		AccessData.pageSeek(bf.access, page);
		bf.access.writeInt(firstSpan);
		bf.access.writeInt(firstLevel);
		bf.access.writeInt(0);
		bf.access.writeInt(1);
		IndexSpan.init(bf, firstSpan, spanSize);
		IndexLevels.init(bf, firstLevel, firstSpan, 4);
	}

	public int maxLevels() {
		int max = super.maxLevels();
		int cells = (int) ((AccessData.PAGESIZE - 8) / 4);
		return (max > cells) ? cells : max;
	}

}
