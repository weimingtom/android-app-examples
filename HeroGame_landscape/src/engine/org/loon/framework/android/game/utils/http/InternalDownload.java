package org.loon.framework.android.game.utils.http;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Observable;

import org.loon.framework.android.game.utils.collection.ArrayByte;

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
public class InternalDownload extends Observable implements Runnable {

	private static final int MAX_BUFFER_SIZE = 2048;

	public static final String STATUSES[] = { "Downloading", "Paused",
			"Complete", "Cancelled", "Error" };

	public static final int DOWNLOADING = 0;

	public static final int PAUSED = 1;

	public static final int COMPLETE = 2;

	public static final int CANCELLED = 3;

	public static final int ERROR = 4;

	public ArrayByte bytes;

	private AbstractClient client;

	private int size;

	private int downloaded;

	private int status;

	InternalDownload(AbstractClient client) {
		this.bytes = new ArrayByte(8192);
		this.client = client;
		this.size = -1;
		this.downloaded = 0;
		this.status = DOWNLOADING;
	}

	public String getURL() {
		return client.getURLString();
	}

	public int getSize() {
		return size;
	}

	public float getProgressValue() {
		return ((float) downloaded / size);
	}

	public float getProgress() {
		return ((float) downloaded / (float) size) * 100;
	}

	public int getStatus() {
		return status;
	}

	public void pause() {
		status = PAUSED;
		stateChanged();
	}

	public void resume() {
		status = DOWNLOADING;
		stateChanged();
		download();
	}

	public void cancel() {
		status = CANCELLED;
		stateChanged();
	}

	private void error() {
		status = ERROR;
		stateChanged();
	}

	public void reset() {
		size = 0;
		downloaded = 0;
		status = CANCELLED;
		stateChanged();
		bytes.reset();
	}

	/**
	 * 开始下载目标文件
	 * 
	 */
	public void download() {
		Thread thread = new Thread(this);
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}

	/**
	 * 返回byte[]数据
	 * 
	 * @return
	 */
	public byte[] getBytes() {
		return bytes.getData();
	}

	public void run() {
		InputStream in = null;
		try {
			HttpURLConnection connection = client.getConnection();
			connection.connect();
			if (connection.getResponseCode() / 100 != 2) {
				error();
			}
			int contentLength = connection.getContentLength();
			if (contentLength < 1) {
				error();
			}
			if (size == -1) {
				size = contentLength;
				stateChanged();
			}
			bytes.setPosition(downloaded);
			in = connection.getInputStream();
			while (status == DOWNLOADING) {
				byte buffer[];
				if (size - downloaded > MAX_BUFFER_SIZE) {
					buffer = new byte[MAX_BUFFER_SIZE];
				} else {
					buffer = new byte[size - downloaded];
				}
				int read = in.read(buffer);
				if (read == -1) {
					break;
				}
				bytes.write(buffer, 0, read);
				downloaded += read;
				stateChanged();
			}
			if (status == DOWNLOADING) {
				status = COMPLETE;
				stateChanged();
			}
			connection.disconnect();
		} catch (Exception e) {
			error();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
				}
			}
		}
	}

	private void stateChanged() {
		setChanged();
		notifyObservers();
	}
}
