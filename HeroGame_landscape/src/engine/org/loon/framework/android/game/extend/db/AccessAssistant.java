package org.loon.framework.android.game.extend.db;

import java.io.IOException;

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

public class AccessAssistant {

	protected int page;

	protected int nextPage;

	protected int len;

	protected int[] branches = null;

	protected Access file;

	public AccessAssistant(final Access file, final int startPage)
			throws IOException {
		this.file = file;
		this.page = startPage;
		AccessData.pageSeek(file, startPage);
		nextPage = file.readInt();
		len = file.readInt();
		if (len > 0) {
			branches = new int[len];
			for (int i = 0; i < len; i++) {
				branches[i] = file.readInt();
			}
		}
	}

	public void writeBlock() throws IOException {
		AccessData.pageSeek(file, page);
		file.writeInt(nextPage);
		if (len > 0) {
			file.writeInt(len);
			for (int i = 0; i < len; i++) {
				file.writeInt(branches[i]);
			}
		} else {
			file.writeInt(0);
		}
	}

	public boolean isFull() {
		int cells = (int) ((AccessData.PAGESIZE) / 4);
		if (cells - len > 0) {
			return false;
		}
		return true;
	}

	public void addPage(final int page) {
		int[] t = new int[len + 1];
		if (len > 0) {
			for (int i = 0; i < len; i++) {
				t[i] = branches[i];
			}
		}
		t[len] = page;
		len++;
		branches = t;
	}

	public static void initPage(final Access file, final int page)
			throws IOException {
		AccessData.pageSeek(file, page);
		file.writeInt(0);
		file.writeInt(0);
	}
}
