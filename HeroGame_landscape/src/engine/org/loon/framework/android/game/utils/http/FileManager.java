package org.loon.framework.android.game.utils.http;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * Copyright 2008 - 2009
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
public class FileManager {

	private long offset;

	private RandomAccessFile raf;

	private long totalRead;

	private boolean stop = false;

	private static int size = 4096;

	public FileManager() {
	}

	public void inputToFile(DownloadJob dj, BufferedInputStream bis,
			long range, File file, File bkFile) {
		byte[] buffer = new byte[size];
		int ReadCount = 0;
		long curPointer = 0;
		totalRead = 0;
		try {
			raf = new RandomAccessFile(file, "rw");
			raf.seek(range);
			while ((ReadCount = bis.read(buffer, 0, size)) != -1 && !stop) {
				curPointer = raf.getFilePointer();
				totalRead += ReadCount;
				if ((curPointer + ReadCount) > (offset + 1)) {
					ReadCount -= (curPointer + ReadCount) - (offset + 1);
					raf.write(buffer, 0, ReadCount);
					dj.downStatr(ReadCount);
					break;
				} else {
					raf.write(buffer, 0, ReadCount);
					dj.downStatr(ReadCount);
				}
			}
			bis.close();
			raf.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setStop() {
		this.stop = true;
	}

	public void setOffset(long offset) {
		this.offset = offset;
	}

	public long getTotalRead() {
		return totalRead;
	}

	public long getCurPointer() {
		long retval = 0;
		try {
			retval = raf.getFilePointer();
		} catch (IOException e) {
		}
		return retval;
	}

}
