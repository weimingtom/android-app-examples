package org.loon.framework.android.game.extend.db.index;

import java.io.IOException;

import org.loon.framework.android.game.extend.db.AccessData;
import org.loon.framework.android.game.extend.db.Serializer;
import org.loon.framework.android.game.extend.db.type.TypeBase;

public class IndexSpan extends BaseSpan {

	final static private int PAGECOUNTER = AccessData.PAGECOUNTER * 4;

	protected AccessData accessData;

	protected int page;

	protected int overflowPage;

	protected int prevPage;

	protected int nextPage;

	protected Serializer keySer;

	protected Serializer valSer;

	public static void init(final AccessData accessData, final int page,
			final int spanSize) throws IOException {
		AccessData.pageSeek(accessData.access, page);
		accessData.access.writeInt(0);
		accessData.access.writeInt(0);
		accessData.access.writeInt(0);
		accessData.access.writeShort((short) spanSize);
		accessData.access.writeShort(0);
	}

	public BaseSpan newInstance(BaseList sl) {
		try {
			int newPage = accessData.allocPage();
			init(accessData, newPage, accessData.span_size);
			return new IndexSpan(accessData, (IndexList) sl, newPage, keySer,
					valSer);
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	public void killInstance() {
		try {
			int curPage = overflowPage;
			int next;
			while (curPage != 0) {
				AccessData.pageSeek(accessData.access, curPage);
				next = accessData.access.readInt();
				accessData.freePage(curPage);
				curPage = next;
			}
			accessData.freePage(page);
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	public void flush() {
		try {
			AccessData.pageSeek(accessData.access, page);
			accessData.access.writeInt(overflowPage);
			accessData.access.writeInt((prev != null) ? ((IndexSpan) prev).page
					: 0);
			accessData.access.writeInt((next != null) ? ((IndexSpan) next).page
					: 0);
			accessData.access.writeShort((short) keys.length);
			accessData.access.writeShort((short) nKeys);

			int curPage = this.page;
			int[] curNextPage = new int[1];
			curNextPage[0] = this.overflowPage;
			int[] pageCounter = new int[1];
			pageCounter[0] = PAGECOUNTER;
			byte[] keyData;
			byte[] valData;
			for (int i = 0; i < nKeys; i++) {
				if ((pageCounter[0] + AccessData.PAGECOUNTER) > AccessData.PAGESIZE) {
					if (curNextPage[0] == 0) {
						curNextPage[0] = accessData.allocPage();
						AccessData.pageSeek(accessData.access, curNextPage[0]);
						accessData.access.writeInt(0);
						AccessData.pageSeek(accessData.access, curPage);
						accessData.access.writeInt(curNextPage[0]);
					}
					AccessData.pageSeek(accessData.access, curNextPage[0]);
					curPage = curNextPage[0];
					curNextPage[0] = accessData.access.readInt();
					pageCounter[0] = AccessData.PAGECOUNTER;
				}

				keyData = this.keySer.getBytes(keys[i]);
				valData = this.valSer.getBytes(vals[i]);
				pageCounter[0] += AccessData.PAGECOUNTER;
				accessData.access.writeShort(keyData.length);
				accessData.access.writeShort(valData.length);
				// 写入键
				curPage = accessData.writeMultiPageData(false, keyData,
						curPage, pageCounter, curNextPage);
				// 写入值
				curPage = accessData.writeMultiPageData(
						this.valSer.getType() == TypeBase.STRING, valData,
						curPage, pageCounter, curNextPage);
			}
			AccessData.pageSeek(accessData.access, this.page);
			this.overflowPage = accessData.access.readInt();
		} catch (IOException ioe) {
			throw new Error();
		}
	}

	/**
	 * 加载数据
	 * 
	 * @param bss
	 * @param accessData
	 * @param bsl
	 * @param spanPage
	 * @param key
	 * @param val
	 * @throws IOException
	 */
	private static void load(final IndexSpan bss, final AccessData accessData,
			final IndexList bsl, final int spanPage, final Serializer key,
			final Serializer val) throws IOException {
		bss.accessData = accessData;
		bss.page = spanPage;
		bss.keySer = key;
		bss.valSer = val;

		bsl.spanHash.put(new Integer(spanPage), bss);

		AccessData.pageSeek(accessData.access, spanPage);

		bss.overflowPage = accessData.access.readInt();
		bss.prevPage = accessData.access.readInt();
		bss.nextPage = accessData.access.readInt();
		int sz = accessData.access.readShort();
		bss.nKeys = accessData.access.readShort();

		bss.keys = new Comparable[sz];
		bss.vals = new Object[sz];

		int ksz, vsz;
		int curPage = spanPage;
		int[] curNextPage = new int[1];
		curNextPage[0] = bss.overflowPage;
		int[] pageCounter = new int[1];
		pageCounter[0] = PAGECOUNTER;

		for (int i = 0; i < bss.nKeys; i++) {
			if ((pageCounter[0] + AccessData.PAGECOUNTER) > AccessData.PAGESIZE) {
				AccessData.pageSeek(accessData.access, curNextPage[0]);
				curPage = curNextPage[0];
				curNextPage[0] = accessData.access.readInt();
				pageCounter[0] = AccessData.PAGECOUNTER;
			}
			ksz = accessData.access.readShort();
			vsz = accessData.access.readShort();
			pageCounter[0] += AccessData.PAGECOUNTER;
			byte[] k = new byte[ksz];
			byte[] v = new byte[vsz];
			// 读出数据
			curPage = accessData.readMultiPageData(false, k, curPage,
					pageCounter, curNextPage);

			curPage = accessData.readMultiPageData(
					bss.valSer.getType() == TypeBase.STRING, v, curPage,
					pageCounter, curNextPage);

			bss.keys[i] = (Comparable) bss.keySer.getObject(k);
			bss.vals[i] = bss.valSer.getObject(v);
		}

	}

	protected IndexSpan() {
	}

	public IndexSpan(AccessData accessData, IndexList bsl, int spanPage,
			Serializer key, Serializer val) throws IOException {

		IndexSpan.load(this, accessData, bsl, spanPage, key, val);

		this.next = null;
		this.prev = null;

		IndexSpan bss = this;
		IndexSpan temp;
		int np = nextPage;
		while (np != 0) {
			temp = (IndexSpan) bsl.spanHash.get(new Integer(np));
			if (temp != null) {
				bss.next = temp;
				break;
			}
			bss.next = new IndexSpan();
			bss.next.next = null;
			bss.next.prev = bss;
			bss = (IndexSpan) bss.next;

			IndexSpan.load(bss, accessData, bsl, np, key, val);
			np = bss.nextPage;
		}

		bss = this;
		np = prevPage;
		while (np != 0) {
			temp = (IndexSpan) bsl.spanHash.get(new Integer(np));
			if (temp != null) {
				bss.next = temp;
				break;
			}
			bss.prev = new IndexSpan();
			bss.prev.next = bss;
			bss.prev.prev = null;
			bss = (IndexSpan) bss.prev;

			IndexSpan.load(bss, accessData, bsl, np, key, val);
			np = bss.prevPage;
		}
	}
}
