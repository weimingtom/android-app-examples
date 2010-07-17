package org.loon.framework.android.game.core.resource;

import java.io.InputStream;

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
public class FileResource {

	private InputStream input;

	/**
	 * 构造函数，用于载入一个配置文件
	 * 
	 * @param fileName
	 */
	public FileResource(String fileName) {
		open(fileName);
	}

	public void open(String fileName) {
		input = Resources.getResourceAsStream(fileName);
	}

	public void close() {
		if (input != null) {
			try {
				input.close();
			} catch (Exception e) {
			}
			input = null;
		}
	}

	public boolean isClosed() {
		if (input == null) {
			return true;
		}
		return false;
	}

	public String readLine() {
		if (input == null) {
			return null;
		}

		int temp;
		StringBuffer buffer = new StringBuffer();

		try {
			while ((temp = input.read()) != 13) {
				if ((int) temp == -1) {
					close();
					break;
				} else if ((int) temp != 10) {
					buffer.append((char) temp);

				}
			}
		} catch (Exception e) {
			close();
		}

		return buffer.toString();
	}

	public int readIntLine() {
		return (Integer.valueOf(readLine())).intValue();
	}

	public String[] readArrayLine() {
		return readArrayLine('\n');
	}

	/**
	 * 读取以指定符号分割的字符串数组
	 * 
	 * @param delimit
	 * @return
	 */
	public String[] readArrayLine(char delimit) {
		String line;
		String[] array;
		StringBuffer buffer;
		int index, count;
		char c;
		line = readLine();
		if (line.length() == 0) {
			array = new String[1];
			array[0] = "";
		}
		index = 0;
		count = 0;
		while (index < line.length()) {
			if (line.charAt(index++) == delimit) {
				count++;
			}
		}
		array = new String[count + 1];
		buffer = new StringBuffer();
		index = 0;
		count = 0;
		while (index < line.length()) {
			c = line.charAt(index++);
			if (c == delimit) {
				array[count++] = buffer.toString();
				buffer = new StringBuffer();
			} else {
				buffer = buffer.append(c);
			}
		}
		array[count++] = buffer.toString();

		return array;
	}

	public int[] readIntArrayLine() {
		String[] buffer = readArrayLine('|');
		int[] array = new int[buffer.length];
		for (int n = 0; n < buffer.length; n++) {
			array[n] = (Integer.valueOf(buffer[n])).intValue();
		}
		return array;
	}

}
