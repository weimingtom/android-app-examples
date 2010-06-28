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
public class DownloadInfo {

	private String serverName;

	private String path;

	private int port;

	private File file;

	private long length;

	private int split;

	private File bkFile;

	private boolean isRunning;

	private States state;

	public DownloadInfo(String serverName, String path, int port, File file,
			long length, int split, File bkFile) {
		this.serverName = serverName;
		this.path = path;
		this.port = port;
		this.file = file;
		this.length = length;
		this.split = split;
		this.bkFile = bkFile;
		this.isRunning = false;
		this.state = new States();
	}

	public File getBkFile() {
		return bkFile;
	}

	public void setBkFile(File bkFile) {
		this.bkFile = bkFile;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getSplit() {
		return split;
	}

	public void setSplit(int split) {
		this.split = split;
	}

	public States getState() {
		return state;
	}

	public void setState(States state) {
		this.state = state;
	}

}
