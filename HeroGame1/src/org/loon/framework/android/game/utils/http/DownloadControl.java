package org.loon.framework.android.game.utils.http;

import org.loon.framework.android.game.core.LSystem;

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
public class DownloadControl extends Thread {

	private long[] len;

	private boolean stop;

	private States states;

	private long timer;

	private long totalLen;

	private long sec;

	private long curlen;

	private int percent;

	private long remainLen;

	private long seclen;

	private int sw;

	private ExternalDownload downloadmanager;

	public DownloadControl() {
		this.timer = LSystem.SECOND;
		this.len = new long[2];
		this.len[0] = 0;
		this.len[1] = 0;
		this.seclen = 0;
		this.stop = false;
		this.sw = 0;
	}

	public void addLen(long len) {
		this.len[sw] += len;
	}

	public void setStop() {
		stop = true;
	}

	public void remove() {
		stop = true;
		this.interrupt();
	}

	public void run() {
		try {
			do {
				sleep(timer);
				sec += 1;
				seclen = len[sw];
				len[sw] = 0;
				sw = (sw == 0) ? 1 : 0;
				curlen = states.getLenght();
				states.setLenght(curlen + seclen);
				percent = (int) (((double) (curlen + seclen) / totalLen) * 100);
				states.setPercent(percent);
				states.setKB((float) (seclen / timer));
				remainLen -= seclen;
				if (percent == 100) {
					downloadmanager.DownAllComplete();
				}
			} while (!stop);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设定下载信息与下载管理器
	 * 
	 * @param info
	 * @param dm
	 */
	public void setInfo(DownloadInfo info, ExternalDownload dm) {
		this.states = info.getState();
		this.totalLen = info.getLength();
		this.remainLen = this.totalLen;
		this.downloadmanager = dm;
		this.curlen = states.getLenght();
		this.remainLen -= this.curlen;
	}

}
