package org.loon.framework.android.game.utils.http;

import java.io.File;

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

public class ExternalDownload extends Thread {

	private DownloadInfo info;

	private DownloadJob[] downloadjob;

	private boolean[] needHelp;

	private HttpHeader hheader;

	private SocketManager sm;

	private long[] Range;

	private int split;

	private long[] Offset;

	private DownloadControl sc;

	private boolean stop;

	private int code;

	public ExternalDownload(HttpHeader hheader, SocketManager sm) {
		this.info = sm.getInfo();
		this.hheader = hheader;
		this.sm = sm;
		this.split = info.getSplit();
		this.Range = new long[split];
		this.Offset = new long[split];
		this.needHelp = new boolean[split];
		this.stop = false;
		for (int i = 0; i < split; i++) {
			needHelp[i] = true;
		}
	}

	public void run() {
		stop = false;
		downloadjob = new DownloadJob[split];
		hheader.setGet(info.getPath() + " HTTP/1.1");
		hheader.setHost(info.getServerName());
		String header;
		header = hheader.getHeaderString();
		downloadjob[0] = new DownloadJob(this, null, header, info, sm, 0, 0);
		code = downloadjob[0].downloadTesting();
		if (code != 4) {
			sc = new DownloadControl();
			sc.setInfo(info, this);
			sc.start();
			downloadjob[0] = null;
			Range[0] = 0;
			if (code == 1) {
				if (info.isRunning()) {
					getDNRange();
				} else {
					RangeCal();
				}
				for (int i = 0; i < split; i++) {
					hheader.setRange(Range[i] + "-");
					header = hheader.getHeaderString();
					downloadjob[i] = new DownloadJob(this, sc, header, info,
							sm, Range[i], i);
					downloadjob[i].setOffset(Offset[i]);
					downloadjob[i].start();
				}
			} else if (code == 0) {
				hheader.setRange(Range[0] + "-");
				header = hheader.getHeaderString();
				downloadjob[0] = new DownloadJob(this, sc, header, info, sm,
						Range[0], 0);
				Offset[0] = info.getLength();
				downloadjob[0].setOffset(Offset[0]);
				downloadjob[0].start();
			}
		}
	}

	public void setStop() {
		stop = true;
		sc.setStop();
		TMPFileManager dn = new TMPFileManager();
		for (int i = 0; i < split; i++) {
			Range[i] = Range[i] + downloadjob[i].getTotalRead();
			downloadjob[i].setStop();
		}
		dn.setDNfileContent(info, Range, Offset);
		dn = null;
	}

	public boolean getStop() {
		return stop;
	}

	public boolean remove() {
		sc.remove();
		for (int i = 0; i < split; i++) {
			downloadjob[i].setStop();
		}
		return true;
	}

	public void getDNRange() {
		TMPFileManager dn = new TMPFileManager();
		File bkfile = info.getBkFile();

		Range = dn.getRange(bkfile, split);
		Offset = dn.getOffset(bkfile, split);

		dn = null;
	}

	public void RangeCal() {
		long length = info.getLength();
		long mok, nam;
		mok = length / split;
		nam = length % split;
		Range[split - 1] = (length - mok - nam);
		Offset[split - 1] = length;
		length = length - mok - nam;
		for (int i = (split - 2); i >= 0; i--) {
			Range[i] = (length - mok);
			Offset[i] = length - 1;
			length = length - mok;
		}
	}

	public long GetOffset(int thnum) {
		return Offset[thnum];
	}

	public void setOffset(int thnum, long offset) {
		Offset[thnum] = offset;
	}

	public long GetRange(int thnum) {
		return Range[thnum];
	}

	public void setRange(int thnum, long range) {
		Range[thnum] = range;
	}

	public void DownComplete(int thnum) {
		needHelp[thnum] = false;
		downloadjob[thnum] = null;
		Download dh = new Download(this, downloadjob, split, thnum);
		dh.start();
	}

	public void DownAllComplete() {
		try {
			sc.setStop();
			info.getBkFile().delete();
			stop = true;
		} catch (Exception e) {
		}
	}

	public void DownHelp(int thnum, int helpthnum, long range) {
		downloadjob[helpthnum].setOffset(range - 1);
		setRange(thnum, range);
		setOffset(thnum, Offset[helpthnum]);
		hheader.setRange(range + "-");
		String header = hheader.getHeaderString();
		downloadjob[thnum] = new DownloadJob(this, sc, header, info, sm, range,
				thnum);
		downloadjob[thnum].setOffset(Offset[helpthnum]);
		downloadjob[thnum].start();
		setNeedHelp(thnum, true);
	}

	public void setNeedHelp(int thnum, boolean b) {
		needHelp[thnum] = b;
	}

	public boolean getNeedHelp(int thnum) {
		return needHelp[thnum];
	}

	public DownloadInfo getInfo() {
		return info;
	}

	public int getCode() {
		return code;
	}

}
