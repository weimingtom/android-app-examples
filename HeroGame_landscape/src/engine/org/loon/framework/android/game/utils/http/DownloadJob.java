package org.loon.framework.android.game.utils.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;

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
public class DownloadJob extends Thread {

	private final int N206 = 206;

	private final int N200 = 200;

	private final int N403 = 403;

	private final int N404 = 404;

	private ExternalDownload observer;

	private DownloadInfo info;

	private String header;

	private BufferedInputStream bis;

	private SocketManager sm;

	private long range;

	private int mynum;

	private boolean stop;

	private DownloadControl sc;

	private FileManager fm;

	private boolean help;

	public static String readline(BufferedInputStream bis) {
		String line = "";
		char c;
		try {
			while ((c = (char) bis.read()) != '\r') {
				line += c;
			}
			bis.skip(1);
		} catch (IOException e) {
		}
		return line;
	}

	public static String secToString(long sec) {
		int min = (int) (sec / 60);
		int hour = (min / 60);
		min = (min % 60);
		sec = (sec % 60);
		return hour + ":" + min + ":" + sec;
	}

	public static boolean redirectCheck(URL url) {
		boolean retVal = false;
		try {
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setInstanceFollowRedirects(false);
			if (connection.getResponseCode() == 302) {
				retVal = true;
			} else {
				retVal = false;
			}
			connection.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public static String getRedirectLocation(URL url) {
		String retVal = "";
		try {
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setInstanceFollowRedirects(false);
			retVal = con.getHeaderField("Location");
			con.disconnect();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return retVal;
	}

	public DownloadJob(ExternalDownload observer, DownloadControl sc,
			String header, DownloadInfo info, SocketManager sm, long range,
			int mynum) {
		this.observer = observer;
		this.header = header;
		this.info = info;
		this.sm = sm;
		this.range = range;
		this.mynum = mynum;
		this.fm = new FileManager();
		this.sc = sc;
		this.help = true;
		this.stop = false;
	}

	public void run() {
		int code = downloadTesting();
		if (code == 0 || code == 1) {
			getData();
			saveData();
			if (!stop) {
				observer.DownComplete(mynum);
			}
		}
	}

	public void downStatr(long ReadLen) {
		sc.addLen(ReadLen);
	}

	private void getData() {
		String Data;

		while ((Data = DownloadJob.readline(bis)) != null) {
			if (Data.equals("")) {
				break;
			}
		}
	}

	private void saveData() {
		fm.inputToFile(this, bis, range, info.getFile(), info.getBkFile());
	}

	public void setStop() {
		stop = true;
		fm.setStop();
	}

	public long getTotalRead() {
		return fm.getTotalRead();
	}

	public long getCurPointer() {
		return fm.getCurPointer();
	}

	public void setOffset(long offset) {
		fm.setOffset(offset);
	}

	public void setHelp(boolean h) {
		this.help = h;
	}

	public boolean getHelp() {
		return this.help;
	}

	private int getHeadCode() {
		int rsHcode = 0;
		try {
			String rsHeader = null;
			rsHeader = DownloadJob.readline(bis);
			int t = rsHeader.indexOf(" ") + 1;
			rsHcode = Integer.parseInt(rsHeader.substring(t, (t + 3)));
		} catch (Exception e) {
			return 0;
		}
		return rsHcode;
	}

	private void request() {
		Socket soc = sm.openSocket();
		Request rqnrs = new Request();
		rqnrs.getRequest(soc, header);
		bis = rqnrs.getBufferedInputStream(soc);
	}

	public int downloadTesting() {
		int retCode = 1;
		while (retCode < 4 && retCode != 0) {
			request();
			int code = getHeadCode();
			if (code == N403 | code == N404) {
				retCode = 4;
			} else if (code == N206) {
				retCode = 1;
				break;
			} else if (code == N200) {
				retCode = 0;
			} else {
				retCode += 1;
			}
		}
		return retCode;
	}

}
