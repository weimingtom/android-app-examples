package org.loon.framework.android.game.utils.http;

import java.io.IOException;
import java.net.Socket;

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
public class SocketManager {

	private DownloadInfo info;

	public SocketManager(DownloadInfo info) {
		this.info = info;
	}

	public Socket openSocket(String SrvName, int port) {
		Socket soc = null;
		try {
			soc = new Socket(SrvName, port);
		} catch (Exception e) {
			System.exit(-1);
		}
		return soc;
	}

	public Socket openSocket() {
		Socket soc = null;
		if (info == null) {
			return soc;
		}
		try {
			soc = new Socket(info.getServerName(), info.getPort());
		} catch (Exception e) {
			System.exit(-1);
		}
		return soc;
	}

	public void closeSocket(Socket s) {
		try {
			s.close();
		} catch (IOException e) {
		}
	}

	public DownloadInfo getInfo() {
		return info;
	}

}
