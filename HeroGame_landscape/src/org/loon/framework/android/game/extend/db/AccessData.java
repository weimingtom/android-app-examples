package org.loon.framework.android.game.extend.db;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.loon.framework.android.game.extend.db.index.IndexList;
import org.loon.framework.android.game.extend.db.type.TypeUtils;

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
public class AccessData implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4911857372850501982L;

	final static private int PAGE = 2;

	final static public int PAGESIZE = 1024;

	final static public int PAGECOUNTER = 4;

	final static public int OFFSET_MOUNTED = 20;

	public short span_size = 127;

	public Access access;

	private long versionBytes = serialVersionUID;

	private long fileLen = PAGESIZE * 2;

	private int freeListStart = 0;

	private short mounted = 0;

	private IndexList metaIndex = null;

	private Map openIndices = new HashMap(10);

	private byte[] passBytes;

	private boolean isPass;

	private void mount() throws IOException {
		access.seek(AccessData.OFFSET_MOUNTED);
		mounted = 1;
		access.writeShort(mounted);
	}

	private void writeSuperBlock() throws IOException {
		access.seek(0);
		access.writeLong(versionBytes);
		access.writeLong(fileLen);
		access.writeInt(freeListStart);
		access.writeShort(mounted);
		access.writeShort(span_size);
	}

	private void readSuperBlock() throws IOException {
		access.seek(0);
		versionBytes = access.readLong();
		fileLen = access.readLong();
		freeListStart = access.readInt();
		mounted = access.readShort();
		span_size = access.readShort();
	}

	public Access getAccess() {
		return access;
	}

	private void setVerify(final String pass) {
		try {
			int passlen = -1;
			if ((pass == null) || ((passlen = pass.trim().length()) <= 0)) {
				access.write(0x00);
				return;
			}
			access.write(0x01);
			this.passBytes = new byte[passlen];
			for (int i = 0; i < passlen; i++) {
				this.passBytes[i] = (byte) pass.charAt(i);
			}
			int crc32Value = TypeUtils.CRC32(this.passBytes, 0, passlen);
			TypeUtils.writeDWORD(access, crc32Value);
			isPass = true;

		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

	}

	private boolean isVerify(final String pass) {
		byte isPWD;
		int passcrc;
		try {
			isPWD = (byte) access.read();
			if ((isPWD & 0x01) != 0) { // 存在密码
				int passlen = -1;
				if ((pass == null) || ((passlen = pass.trim().length()) <= 0)) {
					return false;
				}
				this.passBytes = new byte[passlen];
				for (int i = 0; i < passlen; i++) {
					this.passBytes[i] = (byte) pass.charAt(i);
				}
				passcrc = TypeUtils.readDWORD(access);
				if (passcrc != TypeUtils.CRC32(this.passBytes, 0, passlen)) {
					this.passBytes = null;
					return false;
				}
			}
			return true;
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return false;
	}

	final public int writeMultiPageData(final boolean isString,
			final byte[] data, final int page, final int[] curPageOff,
			final int[] nextPage) throws IOException {
		int pageCounter = curPageOff[0];
		int curNextPage = nextPage[0];
		int curPage = page;
		int dct = 0;
		while (dct < data.length) {
			if (isPass && isString) {
				TypeUtils.xorData(data, passBytes);
			}
			int len = ((int) AccessData.PAGESIZE) - pageCounter;
			if (len <= 0) {
				if (curNextPage == 0) {
					curNextPage = this.allocPage();
					AccessData.pageSeek(this.access, curNextPage);
					this.access.writeInt(0);
					AccessData.pageSeek(this.access, curPage);
					this.access.writeInt(curNextPage);
				}
				AccessData.pageSeek(this.access, curNextPage);
				curPage = curNextPage;
				curNextPage = this.access.readInt();
				pageCounter = AccessData.PAGECOUNTER;
				len = ((int) AccessData.PAGESIZE) - pageCounter;
			}
			this.access.write(data, dct, Math.min(len, data.length - dct));
			pageCounter += Math.min(len, data.length - dct);
			dct += Math.min(len, data.length - dct);
		}
		nextPage[0] = curNextPage;
		curPageOff[0] = pageCounter;
		return curPage;
	}

	final public int readMultiPageData(final boolean isString,
			final byte[] data, final int page, final int[] curPageOff,
			final int[] nextPage) throws IOException {
		int pageCounter = curPageOff[0];
		int curNextPage = nextPage[0];
		int curPage = page;
		int dct = 0;
		int res;
		while (dct < data.length) {
			int len = ((int) AccessData.PAGESIZE) - pageCounter;
			if (len <= 0) {
				AccessData.pageSeek(this.access, curNextPage);
				curPage = curNextPage;
				curNextPage = this.access.readInt();
				pageCounter = AccessData.PAGECOUNTER;
				len = ((int) AccessData.PAGESIZE) - pageCounter;
			}
			res = this.access.read(data, dct, Math.min(len, data.length - dct));
			if (res == -1) {
				throw new IOException();
			} else {
				if (isPass && isString) {
					TypeUtils.xorData(data, passBytes);
				}
			}
			pageCounter += Math.min(len, data.length - dct);
			dct += res;
		}
		nextPage[0] = curNextPage;
		curPageOff[0] = pageCounter;
		return curPage;
	}

	public AccessData(final Access rai) throws IOException {
		this(rai, null);
	}

	public AccessData(final Access rai, final String pass) throws IOException {
		this(rai, pass, false);
	}

	public AccessData(final RandomAccessFile raf) throws IOException {
		this(new AccessImpl(raf), null);
	}

	public AccessData(final RandomAccessFile raf, final String pass)
			throws IOException {
		this(new AccessImpl(raf), pass, false);
	}

	public AccessData(final RandomAccessFile raf, final String pass,
			boolean init) throws IOException {
		this(new AccessImpl(raf), pass, init);
	}

	public AccessData(final File file, final String pass) throws IOException {
		this(file, pass, false);
	}

	public AccessData(final File file, final String pass, final boolean init)
			throws IOException {
		this(new AccessImpl(file, true, true), pass, init);
	}

	public AccessData(final Access rai, final String pass, final boolean init)
			throws IOException {
		if (rai == null) {
			throw new NullPointerException();
		}
		access = rai;
		if (init) {
			access.setLength(fileLen);
			writeSuperBlock();
			setVerify(pass);
			IndexList.init(this, PAGE, span_size);
		} else {
			readSuperBlock();
			if (versionBytes != serialVersionUID) {
				throw new RuntimeException("Version Exception!");
			}
			if (fileLen != access.length()) {
				throw new RuntimeException("File Exception!");
			}
			if (!(isPass = isVerify(pass))) {
				throw new RuntimeException("Password Exception!");
			}
		}
		mount();
		metaIndex = new IndexList(span_size, this, PAGE, TypeUtils.STRING,
				TypeUtils.INTEGER);
	}

	public boolean isPass() {
		return isPass;
	}

	public static void pageSeek(Access access, int page) throws IOException {
		access.seek((((long) page) - 1L) * AccessData.PAGESIZE);
	}

	public int allocPage() throws IOException {
		if (freeListStart != 0) {
			AccessAssistant flb = new AccessAssistant(access, freeListStart);
			if (flb.len > 0) {
				flb.len = flb.len - 1;
				int page = flb.branches[flb.len];
				flb.writeBlock();
				return page;
			} else {
				freeListStart = flb.nextPage;
				writeSuperBlock();
				return flb.page;
			}
		}
		long offset = access.length();
		fileLen = offset + AccessData.PAGESIZE;
		access.setLength(fileLen);
		writeSuperBlock();
		return ((int) ((long) (offset / AccessData.PAGESIZE))) + 1;
	}

	public void freePage(final int page) throws IOException {
		if (freeListStart == 0) {
			freeListStart = page;
			AccessAssistant.initPage(access, page);
			writeSuperBlock();
			return;
		}
		AccessAssistant flb = new AccessAssistant(access, freeListStart);
		if (flb.isFull()) {
			AccessAssistant.initPage(access, page);
			if (flb.nextPage == 0) {
				flb.nextPage = page;
				flb.writeBlock();
				return;
			} else {
				flb = new AccessAssistant(access, page);
				flb.nextPage = freeListStart;
				flb.writeBlock();
				freeListStart = page;
				writeSuperBlock();
				return;
			}
		}
		flb.addPage(page);
		flb.writeBlock();
	}

	public Set getKeys() {
		return metaIndex.getKeys();
	}

	public IndexList getIndex(final String name, final Serializer key,
			final Serializer val) throws IOException {
		Integer page = (Integer) metaIndex.get(name);
		if (page == null) {
			return null;
		}
		IndexList bsl = new IndexList(span_size, this, page.intValue(), key,
				val);
		openIndices.put(name, bsl);
		return bsl;
	}

	public IndexList makeIndex(String name, Serializer key, Serializer val)
			throws IOException {
		if (metaIndex.get(name) != null) {
			throw new IOException("Index already exists!");
		}
		int page = allocPage();
		metaIndex.put(name, new Integer(page));
		IndexList.init(this, page, span_size);
		IndexList bsl = new IndexList(span_size, this, page, key, val);
		openIndices.put(name, bsl);
		return bsl;
	}

	public void delIndex(String name) throws IOException {
		Integer page = (Integer) metaIndex.remove(name);
		if (page == null) {
			return;
		}
		IndexList bsl = new IndexList(span_size, this, page.intValue(),
				TypeUtils.NULL, TypeUtils.NULL);
		bsl.delete();
	}

	public void close() throws IOException {
		metaIndex.close();
		metaIndex = null;
		Object key;
		Set oi = openIndices.keySet();
		Iterator i = oi.iterator();
		while (i.hasNext()) {
			key = i.next();
			IndexList bsl = (IndexList) openIndices.get(key);
			bsl.close();
		}
		access.seek(AccessData.OFFSET_MOUNTED);
		access.write(0);
	}

}
