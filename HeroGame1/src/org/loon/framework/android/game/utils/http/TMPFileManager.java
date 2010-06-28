package org.loon.framework.android.game.utils.http;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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

public class TMPFileManager {

	public TMPFileManager() {
	}

	public DownloadInfo getDNfileContent(File dnfile) {
		DownloadInfo info = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(dnfile));
			String path = br.readLine();
			int port = Integer.parseInt(br.readLine());
			int split = Integer.parseInt(br.readLine());
			String server = br.readLine();
			File bkFile = new File(br.readLine());
			File downFile = new File(br.readLine());
			long length = Long.parseLong(br.readLine());
			info = new DownloadInfo(server, path, port, downFile, length,
					split, bkFile);
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return info;
	}

	public void setDNfileContent(DownloadInfo info, long[] Range, long[] Offset) {
		try {
			FileWriter fw = new FileWriter(info.getBkFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(info.getPath());
			bw.newLine();
			bw.write(info.getPort() + "");
			bw.newLine();
			bw.write(info.getSplit() + "");
			bw.newLine();
			bw.write(info.getServerName());
			bw.newLine();
			bw.write(info.getBkFile().getPath());
			bw.newLine();
			bw.write(info.getFile().getPath());
			bw.newLine();
			bw.write(info.getLength() + "");
			bw.newLine();
			int split = info.getSplit();
			bw.write("Range");
			bw.newLine();
			for (int i = 0; i < split; i++) {
				bw.write(Range[i] + "");
				bw.newLine();
			}
			bw.write("Offset");
			bw.newLine();
			for (int i = 0; i < split; i++) {
				bw.write(Offset[i] + "");
				bw.newLine();
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long[] getRange(File bkfile, int split) {
		long[] range = new long[split];
		try {
			BufferedReader br = new BufferedReader(new FileReader(bkfile));
			while (!br.readLine().equals("Range")) {
			}
			for (int i = 0; i < split; i++) {
				range[i] = Long.parseLong(br.readLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return range;
	}

	public long[] getOffset(File bkfile, int split) {
		long[] Offset = new long[split];

		try {
			BufferedReader br = new BufferedReader(new FileReader(bkfile));
			while (!br.readLine().equals("Offset")) {
			}
			for (int i = 0; i < split; i++) {
				Offset[i] = Long.parseLong(br.readLine());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return Offset;
	}
}
