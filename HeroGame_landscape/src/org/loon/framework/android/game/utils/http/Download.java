package org.loon.framework.android.game.utils.http;

/**
 * 
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
public class Download extends Thread {

	private ExternalDownload downloadmanager;

	private DownloadJob[] downloadjob;

	private int split;

	private int thnum;

	public Download(ExternalDownload dm, DownloadJob[] dj, int split,
			int mythnum) {
		this.downloadmanager = dm;
		this.downloadjob = dj;
		this.split = split;
		this.thnum = mythnum;
	}

	public void run() {
		int helpthnum = 0;
		int count = 0;
		long range = -1;
		for (; count < split; count++) {
			if ((helpthnum = search(count)) != -1) {
				if ((range = remainCheck(helpthnum)) != -1) {
					downloadmanager.DownHelp(thnum, helpthnum, range);
					break;
				}
			}
		}
	}

	public long remainCheck(int helpthnum) {
		long retval = -1;
		if (!downloadjob[helpthnum].isAlive()) {
			long curPointer = downloadmanager.GetRange(helpthnum);
			retval = curPointer;
		} else {
			long curPointer = downloadjob[helpthnum].getTotalRead()
					+ downloadmanager.GetRange(helpthnum);
			long remain = downloadmanager.GetOffset(helpthnum) - curPointer;
			long checkMB = 512 * 1000;
			if (remain > checkMB) {
				retval = curPointer + checkMB;
			}
		}
		return retval;
	}

	public int search(int offset) {
		int helpthnum = -1;
		for (int i = offset; i < split; i++) {
			if (downloadmanager.getNeedHelp(i)) {
				helpthnum = i;
				downloadmanager.setNeedHelp(i, false);
				break;
			}
		}
		return helpthnum;
	}

}
