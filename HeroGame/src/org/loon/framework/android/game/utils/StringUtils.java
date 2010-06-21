package org.loon.framework.android.game.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


import org.loon.framework.android.game.core.LSystem;

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
final public class StringUtils {
	private StringUtils() {
	}


	/**
	 * 将指定String转化为String[]
	 * 
	 * @param s
	 * @param tag
	 * @return
	 */
	public String[] stringtoArray(String s, String tag) {
		StringBuffer buf = new StringBuffer(s);
		int arraysize = 1;
		for (int i = 0; i < buf.length(); i++) {
			if (tag.indexOf(buf.charAt(i)) != -1)
				arraysize++;
		}
		String[] elements = new String[arraysize];
		int y, z = 0;
		if (buf.toString().indexOf(tag) != -1) {
			while (buf.length() > 0) {
				if (buf.toString().indexOf(tag) != -1) {
					y = buf.toString().indexOf(tag);
					if (y != buf.toString().lastIndexOf(tag)) {
						elements[z] = buf.toString().substring(0, y);
						z++;
						buf.delete(0, y + 1);
					} else if (buf.toString().lastIndexOf(tag) == y) {
						elements[z] = buf.toString().substring(0,
								buf.toString().indexOf(tag));
						z++;
						buf.delete(0, buf.toString().indexOf(tag) + 1);
						elements[z] = buf.toString();
						z++;
						buf.delete(0, buf.length());
					}
				}
			}
		} else {
			elements[0] = buf.toString();
		}
		buf = null;
		return elements;
	}

	/**
	 * 将指定数组转化为String
	 * 
	 * @param args
	 * @param separator
	 * @return
	 */
	public static String arrayToString(Object[] args, String separator) {
		if (args == null || separator == null) {
			return null;
		}
		StringBuffer result = new StringBuffer();
		if (args.length > 0) {
			result.append(args[0]);
			for (int i = 1; i < args.length; i++) {
				result.append(separator);
				result.append(args[i]);
			}
		}
		return result.toString();
	}

	/**
	 * 将指定数组转化为String
	 * 
	 * @param args
	 * @return
	 */
	public static String arrayToString(Object[] args) {
		return arrayToString(args, ",");
	}


	/**
	 * 将字符串中全角转为半角模式
	 * 
	 * @param keyTemp
	 * @return
	 * @throws Exception
	 */
	public static String toFulltoChange(String keyTemp) throws Exception {

		byte[] arrayTemp = keyTemp.getBytes();
		int len = arrayTemp.length;
		byte[] targetTemp = new byte[len];

		for (int i = 0; i < len; i++) {
			if (arrayTemp[i] <= 0) {
				if (len == i + 1)
					break;
				// 解析全角“
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -80) {
					arrayTemp[i] = 34;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角”
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -79) {
					arrayTemp[i] = 34;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角＂
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -94) {
					arrayTemp[i] = 34;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角‘
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -82) {
					arrayTemp[i] = 39;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角＇
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -89) {
					arrayTemp[i] = 39;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角＋
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -85) {
					arrayTemp[i] = 43;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角－
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -83) {
					arrayTemp[i] = 45;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角（
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -88) {
					arrayTemp[i] = 40;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角）
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -87) {
					arrayTemp[i] = 41;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角[
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -37) {
					arrayTemp[i] = 40;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角]
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -35) {
					arrayTemp[i] = 41;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角{
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -5) {
					arrayTemp[i] = 40;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角}
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -3) {
					arrayTemp[i] = 41;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角，
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -84) {
					arrayTemp[i] = 44;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角。
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -93) {
					arrayTemp[i] = 32;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角、
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -94) {
					arrayTemp[i] = 32;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角/
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -81) {
					arrayTemp[i] = 32;
					arrayTemp[i + 1] = 32;
				}

				// 解析全角;
				if (arrayTemp[i] == -93 && arrayTemp[i + 1] == -69) {
					arrayTemp[i] = 32;
					arrayTemp[i + 1] = 32;
				}
				// 解析全角空格
				if (arrayTemp[i] == -95 && arrayTemp[i + 1] == -95) {
					arrayTemp[i] = 32;
					arrayTemp[i + 1] = 32;
				}
				targetTemp[i] = arrayTemp[i];
				i++;
				// we do not make changes
				if (targetTemp[i] <= 0) {
					i++;
					targetTemp[i] = arrayTemp[i];
				}
			} else {
				// 解析半角。
				if (arrayTemp[i] == 46) {
					arrayTemp[i] = 32;
				}
				// 解析半角[
				if (arrayTemp[i] == 91) {
					arrayTemp[i] = 40;
				}
				// 解析半角]
				if (arrayTemp[i] == 93) {
					arrayTemp[i] = 41;
				}
				// 解析半角{
				if (arrayTemp[i] == 123) {
					arrayTemp[i] = 40;
				}
				// 解析半角}
				if (arrayTemp[i] == 125) {
					arrayTemp[i] = 41;
				}
				// 解析半角/
				if (arrayTemp[i] == 47) {
					arrayTemp[i] = 32;
				}
				// 解析半角\
				if (arrayTemp[i] == 92) {
					arrayTemp[i] = 32;
				}
				// 解析半角;
				if (arrayTemp[i] == 59) {
					arrayTemp[i] = 32;
				}
				targetTemp[i] = arrayTemp[i];
			}
		}

		return new String(targetTemp).trim();
	}


	/**
	 * 获得字符串长度
	 * 
	 * @param strData
	 * @return
	 */
	public static int toCharLength(String strData) {
		String strValue = toString(strData);
		int intRealLength = 0;
		for (int i = 0; i < strValue.length(); i++) {
			char chrValue = strValue.charAt(i);
			Character aa = new Character(chrValue);
			if (aa.compareTo(new Character((char) 0)) < 0
					|| aa.compareTo(new Character((char) 128)) > 0) {
				intRealLength = intRealLength + 2;
			} else {
				intRealLength = intRealLength + 1;
			}
		}
		return intRealLength;
	}

	/**
	 * 整型转化为char
	 * 
	 * @param var
	 * @return
	 */
	public static char toChar(int var) {
		return (char) var;
	}

	/**
	 * 转为String
	 * 
	 * @param str
	 * @return
	 */
	public static String toString(String str) {
		if (str == null || str.length() == 0)
			return "";
		else
			return str.trim();
	}

	/**
	 * 转为utf-8
	 * 
	 * @param src
	 * @return
	 */
	public static String getUTF8String(String src) {
		byte[] b = src.getBytes();
		char[] c = new char[b.length];
		for (int i = 0; i < b.length; i++) {
			c[i] = (char) (b[i] & 0x00FF);
		}
		return new String(c);
	}

	/**
	 * 转换字节单位
	 * 
	 * @param size
	 * @return
	 */
	public static String toByte(int size) {
		if (size > (1024 * 1024)) {
			return ((float) size / (1024 * 1024) + "").substring(0, 4) + "MB";
		} else if (size > 1024) {
			return ((float) size / 1024 + "").substring(0, 4) + "KB";
		} else {
			return size + "B";
		}
	}

	/**
	 * 转换String
	 * 
	 * @param String
	 * @return String
	 */
	public static String getString(String src, String encoding) {
		return getString(src, LSystem.encoding, encoding);
	}

	/**
	 * 字符转为int
	 * 
	 * @param c
	 * @return
	 */
	public static int toASCII(char c) {
		return c;
	}

	/**
	 * 以指定字符分割字符串
	 * 
	 * @param str
	 * @param c
	 * @return
	 */
	public static String[] split(String str, char c) {
		str += c;
		int n = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == c) {
				n++;
			}
		}
		String out[] = new String[n];
		for (int i = 0; i < n; i++) {
			int index = str.indexOf(c);
			out[i] = str.substring(0, index);
			str = str.substring(index + 1, str.length());
		}
		return out;
	}

	/**
	 * 过滤指定字符串
	 * 
	 * @param string
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replace(String string, String oldString,
			String newString) {
		if (string == null)
			return null;
		if (newString == null)
			return string;
		int i = 0;
		if ((i = string.indexOf(oldString, i)) >= 0) {
			char string2[] = string.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(string2.length);
			buf.append(string2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = string.indexOf(oldString, i)) > 0; j = i) {
				buf.append(string2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(string2, j, string2.length - j);
			return buf.toString();
		} else {
			return string;
		}
	}

	/**
	 * 不匹配大小写的过滤指定字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 不匹配大小写的过滤指定字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @param count
	 * @return
	 */
	public static final String replaceIgnoreCase(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		String lcLine = line.toLowerCase();
		String lcOldString = oldString.toLowerCase();
		int i = 0;
		if ((i = lcLine.indexOf(lcOldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = lcLine.indexOf(lcOldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 以指定条件过滤字符串
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @param count
	 * @return
	 */
	public static final String replace(String line, String oldString,
			String newString, int count[]) {
		if (line == null)
			return null;
		int i = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			int counter = 1;
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buf = new StringBuffer(line2.length);
			buf.append(line2, 0, i).append(newString2);
			i += oLength;
			int j;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				counter++;
				buf.append(line2, j, i - j).append(newString2);
				i += oLength;
			}

			buf.append(line2, j, line2.length - j);
			count[0] = counter;
			return buf.toString();
		} else {
			return line;
		}
	}

	/**
	 * 过滤\n标记
	 * 
	 * @param text
	 * @return
	 */
	public static String[] parseString(String text) {
		int token, index, index2;
		token = index = index2 = 0;
		while ((index = text.indexOf('\n', index)) != -1) {
			token++;
			index++;
		}
		token++;
		index = 0;

		String[] document = new String[token];
		for (int i = 0; i < token; i++) {
			index2 = text.indexOf('\n', index);
			if (index2 == -1) {
				index2 = text.length();
			}
			document[i] = text.substring(index, index2);
			index = index2 + 1;
		}

		return document;
	}

	/**
	 * 过滤xml中特殊字符
	 * 
	 * @param string
	 * @return
	 */
	public static final String toFromXML(String string) {
		string = replace(string, "&lt;", "<");
		string = replace(string, "&gt;", ">");
		string = replace(string, "&quot;", "\"");
		return replace(string, "&amp;", "&");
	}

	/**
	 * 检查一组字符串是否完全由中文组成
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinaLanguage(String str) {
		char[] chars = str.toCharArray();
		int[] ints = new int[2];
		boolean isChinese = false;
		int length = chars.length;
		byte[] bytes = null;
		for (int i = 0; i < length; i++) {
			bytes = ("" + chars[i]).getBytes();
			if (bytes.length == 2) {
				ints[0] = bytes[0] & 0xff;
				ints[1] = bytes[1] & 0xff;
				if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
						&& ints[1] <= 0xFE) {
					isChinese = true;
				}
			} else {
				return false;
			}
		}
		return isChinese;
	}



	/**
	 * 判断是否为null
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isEmpty(String param) {
		return param == null || param.length() == 0 || param.trim().equals("");
	}

	/**
	 * 判断是否可能为true
	 * 
	 * @param param
	 * @return
	 */
	public static boolean isCovertBoolean(String param) {
		if (isEmpty(param))
			return false;
		switch (param.charAt(0)) {
		case 49: // '1'
		case 84: // 'T'
		case 89: // 'Y'
		case 116: // 't'
		case 121: // 'y'
			return true;
		}
		return false;
	}

	/**
	 * 检查是否为utf8编码
	 * 
	 * @param text
	 * @return
	 */
	public static boolean isUTF8code(String text) {
		String sign = "";
		if (text.startsWith("%e")) {
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1) {
					p++;
				}
				sign += p;
			}
		}
		return sign.equals("147-1");
	}


	/**
	 * byte转为int
	 * 
	 * @param b
	 * @return
	 */
	public static int byteToint(byte b) {
		if (b < 0) {
			return (int) b + 0x100;
		}
		return b;
	}

	/**
	 * 显示指定编码下的字符长度
	 * 
	 * @param encoding
	 * @param str
	 * @return
	 */
	public static int getBytesLengthOfEncoding(String encoding, String str) {
		if (str == null || str.length() == 0)
			return 0;
		try {
			byte bytes[] = str.getBytes(encoding);
			int length = bytes.length;
			return length;
		} catch (UnsupportedEncodingException exception) {
			System.err.println(exception.getMessage());
		}
		return 0;
	}

	/**
	 * 转化指定字符串为指定编码格式
	 * 
	 * @param context
	 * @param encoding
	 * @return
	 */
	public static String getSpecialString(String context, String encoding) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(context
					.getBytes());
			InputStreamReader isr = new InputStreamReader(in, encoding);
			BufferedReader reader = new BufferedReader(isr);
			StringBuffer buffer = new StringBuffer();
			String result;
			while ((result = reader.readLine()) != null) {
				buffer.append(result);
			}
			return buffer.toString();
		} catch (Exception ex) {
			return context;
		}
	}

	/**
	 * 批量转换字符串数组编码
	 * 
	 * @param s
	 * @return
	 */
	public String[] getString(String[] strs, String sourceEncoding,
			String objectEncoding) {
		String[] ss = new String[strs.length];
		try {
			for (int i = 0; i < strs.length; i++) {
				byte[] aa = strs[i].getBytes(sourceEncoding);
				ss[i] = new String(aa, objectEncoding);
			}
		} catch (java.io.UnsupportedEncodingException e) {
			return null;
		}
		return ss;
	}

	/**
	 * 转换指定byte[]为md5
	 * 
	 * @param data
	 * @param start
	 * @param len
	 * @return
	 */
	private static byte[] compute(byte[] data, int start, int len) {
		int[] s = new int[4];
		s[0] = 0x67452301;
		s[1] = 0xefcdab89;
		s[2] = 0x98badcfe;
		s[3] = 0x10325476;

		byte[] padded = pad(data, start, len);

		for (int i = 0; i < padded.length; i += 64) {
			digest(padded, i, 64, s);
		}

		return encode(s);
	}

	private static int[] decode(byte[] input, int start, int len) {
		int[] output = new int[len / 4];

		for (int i = 0, j = start; j < (start + len); i++, j += 4)
			output[i] = (input[j] & 0xff) | ((input[j + 1] & 0xff) << 8)
					| ((input[j + 2] & 0xff) << 16)
					| ((input[j + 3] & 0xff) << 24);

		return output;
	}

	private static void digest(byte[] data, int start, int len, int[] s) {
		int[] x = decode(data, start, len);
		int a = s[0];
		int b = s[1];
		int c = s[2];
		int d = s[3];
		a = FF(a, b, c, d, x[0], 7, 0xd76aa478);
		d = FF(d, a, b, c, x[1], 12, 0xe8c7b756);
		c = FF(c, d, a, b, x[2], 17, 0x242070db);
		b = FF(b, c, d, a, x[3], 22, 0xc1bdceee);
		a = FF(a, b, c, d, x[4], 7, 0xf57c0faf);
		d = FF(d, a, b, c, x[5], 12, 0x4787c62a);
		c = FF(c, d, a, b, x[6], 17, 0xa8304613);
		b = FF(b, c, d, a, x[7], 22, 0xfd469501);
		a = FF(a, b, c, d, x[8], 7, 0x698098d8);
		d = FF(d, a, b, c, x[9], 12, 0x8b44f7af);
		c = FF(c, d, a, b, x[10], 17, 0xffff5bb1);
		b = FF(b, c, d, a, x[11], 22, 0x895cd7be);
		a = FF(a, b, c, d, x[12], 7, 0x6b901122);
		d = FF(d, a, b, c, x[13], 12, 0xfd987193);
		c = FF(c, d, a, b, x[14], 17, 0xa679438e);
		b = FF(b, c, d, a, x[15], 22, 0x49b40821);
		a = GG(a, b, c, d, x[1], 5, 0xf61e2562);
		d = GG(d, a, b, c, x[6], 9, 0xc040b340);
		c = GG(c, d, a, b, x[11], 14, 0x265e5a51);
		b = GG(b, c, d, a, x[0], 20, 0xe9b6c7aa);
		a = GG(a, b, c, d, x[5], 5, 0xd62f105d);
		d = GG(d, a, b, c, x[10], 9, 0x2441453);
		c = GG(c, d, a, b, x[15], 14, 0xd8a1e681);
		b = GG(b, c, d, a, x[4], 20, 0xe7d3fbc8);
		a = GG(a, b, c, d, x[9], 5, 0x21e1cde6);
		d = GG(d, a, b, c, x[14], 9, 0xc33707d6);
		c = GG(c, d, a, b, x[3], 14, 0xf4d50d87);
		b = GG(b, c, d, a, x[8], 20, 0x455a14ed);
		a = GG(a, b, c, d, x[13], 5, 0xa9e3e905);
		d = GG(d, a, b, c, x[2], 9, 0xfcefa3f8);
		c = GG(c, d, a, b, x[7], 14, 0x676f02d9);
		b = GG(b, c, d, a, x[12], 20, 0x8d2a4c8a);
		a = HH(a, b, c, d, x[5], 4, 0xfffa3942);
		d = HH(d, a, b, c, x[8], 11, 0x8771f681);
		c = HH(c, d, a, b, x[11], 16, 0x6d9d6122);
		b = HH(b, c, d, a, x[14], 23, 0xfde5380c);
		a = HH(a, b, c, d, x[1], 4, 0xa4beea44);
		d = HH(d, a, b, c, x[4], 11, 0x4bdecfa9);
		c = HH(c, d, a, b, x[7], 16, 0xf6bb4b60);
		b = HH(b, c, d, a, x[10], 23, 0xbebfbc70);
		a = HH(a, b, c, d, x[13], 4, 0x289b7ec6);
		d = HH(d, a, b, c, x[0], 11, 0xeaa127fa);
		c = HH(c, d, a, b, x[3], 16, 0xd4ef3085);
		b = HH(b, c, d, a, x[6], 23, 0x4881d05);
		a = HH(a, b, c, d, x[9], 4, 0xd9d4d039);
		d = HH(d, a, b, c, x[12], 11, 0xe6db99e5);
		c = HH(c, d, a, b, x[15], 16, 0x1fa27cf8);
		b = HH(b, c, d, a, x[2], 23, 0xc4ac5665);
		a = II(a, b, c, d, x[0], 6, 0xf4292244);
		d = II(d, a, b, c, x[7], 10, 0x432aff97);
		c = II(c, d, a, b, x[14], 15, 0xab9423a7);
		b = II(b, c, d, a, x[5], 21, 0xfc93a039);
		a = II(a, b, c, d, x[12], 6, 0x655b59c3);
		d = II(d, a, b, c, x[3], 10, 0x8f0ccc92);
		c = II(c, d, a, b, x[10], 15, 0xffeff47d);
		b = II(b, c, d, a, x[1], 21, 0x85845dd1);
		a = II(a, b, c, d, x[8], 6, 0x6fa87e4f);
		d = II(d, a, b, c, x[15], 10, 0xfe2ce6e0);
		c = II(c, d, a, b, x[6], 15, 0xa3014314);
		b = II(b, c, d, a, x[13], 21, 0x4e0811a1);
		a = II(a, b, c, d, x[4], 6, 0xf7537e82);
		d = II(d, a, b, c, x[11], 10, 0xbd3af235);
		c = II(c, d, a, b, x[2], 15, 0x2ad7d2bb);
		b = II(b, c, d, a, x[9], 21, 0xeb86d391);
		s[0] += a;
		s[1] += b;
		s[2] += c;
		s[3] += d;
	}

	private static byte[] encode(int[] input) {
		byte[] output = new byte[input.length * 4];

		for (int i = 0, j = 0; i < input.length; i++, j += 4) {
			output[j] = (byte) (input[i] & 0xff);
			output[j + 1] = (byte) ((input[i] >>> 8) & 0xff);
			output[j + 2] = (byte) ((input[i] >>> 16) & 0xff);
			output[j + 3] = (byte) ((input[i] >>> 24) & 0xff);
		}

		return output;
	}

	/**
	 * 将指定字符串加密
	 * 
	 * @param sourceString
	 * @return
	 */
	public static final String encrypt(String sourceString) {
		char[] HEX = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
				'B', 'C', 'D', 'E', 'F' };
		byte[] byteStr = sourceString.getBytes();
		byte[] resByte = compute(byteStr, 0, byteStr.length);
		StringBuffer stringbuffer = new StringBuffer();

		for (int i = 0; i < resByte.length; i++) {
			stringbuffer.append(HEX[byteToint(resByte[i]) / 16]);
			stringbuffer.append(HEX[byteToint(resByte[i]) % 16]);
		}

		return stringbuffer.toString();
	}

	private static int F(int x, int y, int z) {
		return ((x & y) | (~x & z));
	}

	private static int FF(int a, int b, int c, int d, int x, int s, int ac) {
		a += (F(b, c, d) + x + ac);
		a = ROTATE_LEFT(a, s);

		return a + b;
	}

	private static int G(int x, int y, int z) {
		return ((x & z) | (y & ~z));
	}

	private static int GG(int a, int b, int c, int d, int x, int s, int ac) {
		a += (G(b, c, d) + x + ac);
		a = ROTATE_LEFT(a, s);

		return a + b;
	}

	private static int H(int x, int y, int z) {
		return (x ^ y ^ z);
	}

	private static int HH(int a, int b, int c, int d, int x, int s, int ac) {
		a += (H(b, c, d) + x + ac);
		a = ROTATE_LEFT(a, s);

		return a + b;
	}

	private static int I(int x, int y, int z) {
		return (y ^ (x | ~z));
	}

	private static int II(int a, int b, int c, int d, int x, int s, int ac) {
		a += (I(b, c, d) + x + ac);
		a = ROTATE_LEFT(a, s);

		return a + b;
	}

	/**
	 * 检查指定字符串中是否存在中文字符。
	 * 
	 * @param checkStr
	 *            指定需要检查的字符串。
	 * @return 逻辑值（True Or False）。
	 */
	public static final boolean hasChinese(String checkStr) {
		boolean checkedStatus = false;
		boolean isError = false;
		String spStr = " _-";
		int checkStrLength = checkStr.length() - 1;
		for (int i = 0; i <= checkStrLength; i++) {
			char ch = checkStr.charAt(i);
			if (ch < '\176') {
				ch = Character.toUpperCase(ch);
				if (((ch < 'A') || (ch > 'Z')) && ((ch < '0') || (ch > '9'))
						&& (spStr.indexOf(ch) < 0)) {
					isError = true;
				}
			}
		}
		checkedStatus = !isError;
		return checkedStatus;
	}

	/**
	 * 以正则表达式部分截取
	 * 
	 * @param value
	 * @param pattern
	 * @param replacement
	 * @return
	 */
	public static String subStitute(String value, String pattern,
			String replacement) {
		if (value == null || value.length() == 0)
			return value;
		if (pattern == null || pattern.length() == 0)
			return value;
		StringBuffer sb = new StringBuffer();
		do {
			int patternIndex = value.indexOf(pattern);
			if (patternIndex == -1) {
				sb.append(value);
				break;
			}
			sb.append(value.substring(0, patternIndex) + replacement);
			value = value.substring(patternIndex + pattern.length(), value
					.length());
		} while (true);
		return sb.toString();
	}


	/**
	 * 检查是否为纯字母
	 * 
	 * @param value
	 * @return
	 */
	public final static boolean isAlphabet(String value) {
		if (value == null || value.length() == 0)
			return false;
		for (int i = 0; i < value.length(); i++) {
			char c = Character.toUpperCase(value.charAt(i));
			if ('A' <= c && c <= 'Z')
				return true;
		}
		return false;
	}

	/**
	 * 检查是否为字母与数字混合
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isAlphabetNumeric(String value) {
		if (value == null || value.trim().length() == 0)
			return true;
		for (int i = 0; i < value.length(); i++) {
			char letter = value.charAt(i);
			if (('a' > letter || letter > 'z')
					&& ('A' > letter || letter > 'Z')
					&& ('0' > letter || letter > '9'))
				return false;
		}
		return true;
	}

	private static byte[] pad(byte[] data, int start, int len) {
		int size = (len + 8 + 63) & (~63);
		byte[] newdata = new byte[size];
		System.arraycopy(data, start, newdata, 0, len);

		if ((size - 8) > len) {
			newdata[len] = (byte) 0x80;

			for (int i = len + 1; i < (size - 8); i++)
				newdata[i] = 0;
		}

		int databits = len * 8;

		for (int i = 0; i < 8; i++) {
			newdata[size - 8 + i] = (byte) (databits & 0xff);
			databits >>= 8;
		}

		return newdata;
	}

	private static int ROTATE_LEFT(int x, int n) {
		return ((x << n) | (x >>> (32 - n)));
	}

	/**
	 * 将加密后的内容输出.
	 * 
	 * @param abyte0
	 *            是经过MessageDigest处理后得到的Byte类型的值.
	 * @return 转换后的字符串。
	 */
	public static final String toHex(byte[] abyte0) {
		StringBuffer stringbuffer = new StringBuffer(abyte0.length * 2);

		for (int i = 0; i < abyte0.length; i++) {
			if ((abyte0[i] & 0xff) < 16) {
				stringbuffer.append("0");
			}

			stringbuffer.append(Long.toString(abyte0[i] & 0xff, 16));
		}

		return stringbuffer.toString();
	}

	/**
	 * 过滤特殊字符
	 * 
	 * @param str
	 * @return
	 */
	public static final String filterString(String str) {
		String message = str;
		message = message.replace('<', '_');
		message = message.replace('>', '_');
		message = message.replace('"', '_');
		message = replace(message, ",", "_");
		message = message.replace('%', '_');
		message = message.replace(';', '_');
		message = message.replace('(', '_');
		message = message.replace(')', '_');
		message = message.replace('&', '_');
		message = message.replace('+', '_');
		message = message.replace('-', '_');
		message = replace(message, "or", "_");
		message = replace(message, "and", "_");
		return message;
	}

	/**
	 * 过滤首字符
	 * 
	 * @param str
	 * @param pattern
	 * @param replace
	 * @return
	 */
	public static final String replaceFirst(String str, String pattern,
			String replace) {
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		if ((e = str.indexOf(pattern, s)) >= 0) {
			result.append(str.substring(s, e));
			result.append(replace);
			s = e + pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	}

	/**
	 * 以" "充满指定字符串
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillSpace(String str, int length) {
		int strLength = str.length();
		if (strLength >= length) {
			return str;
		}
		StringBuffer spaceBuffer = new StringBuffer();
		for (int i = 0; i < (length - strLength); i++) {
			spaceBuffer.append(" ");
		}
		return str + spaceBuffer.toString();
	}

	/**
	 * 得到定字节长的字符串，位数不足右补空格
	 * 
	 * @param str
	 * @param length
	 * @return
	 */
	public static String fillSpaceByByte(String str, int length) {
		byte[] strbyte = str.getBytes();
		int strLength = strbyte.length;
		if (strLength >= length) {
			return str;
		}
		StringBuffer spaceBuffer = new StringBuffer();
		for (int i = 0; i < (length - strLength); i++) {
			spaceBuffer.append(" ");
		}
		return str.concat(spaceBuffer.toString());
	}

	/**
	 * 得到定长的字符串，位数不足前补0
	 * 
	 * @param param
	 * @param length
	 * @return
	 */
	public static String getZero(String param, int length) {
		String temp = param.trim();
		for (int i = 0; temp.length() < length; i++) {
			temp = "0".concat(temp);
		}
		return temp.substring(0, length);
	}

	/**
	 * 返回String
	 * 
	 * @param Parame
	 * @return
	 */
	public static String getString(String param) {
		return (param == null) ? "" : param;
	}

	/**
	 * 转码为指定格式
	 * 
	 * @param Parame
	 * @param encoding
	 * @return
	 */
	public static String getString(String param, String initcoding,
			String encoding) {
		String result = getString(param);
		try {
			byte[] buffer = result.getBytes(initcoding);
			result = new String(buffer, encoding);
		} catch (Exception e) {
			return null;
		}
		return result;
	}

	public static Object getObject(Object param) {
		return (param == null) ? "" : param;
	}

	/**
	 * 返回指定字符串长度
	 * 
	 * @param s
	 * @return
	 */
	public static int length(String s) {
		if (s == null)
			return 0;
		else
			return s.getBytes().length;
	}

	/**
	 * 将字符串的数字取出到一个字符串中
	 * 
	 * @param s
	 *            String
	 * @return String
	 */
	public static String getDigitsOnly(String s) {
		StringBuffer digitsOnly = new StringBuffer();
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if (Character.isDigit(c)) {
				digitsOnly.append(c);
			}
		}
		return digitsOnly.toString();
	}

	/**
	 * 获得特定字符总数
	 * 
	 * @param str
	 * @param chr
	 * @return
	 */
	public static int charCount(String str, char chr) {
		int count = 0;
		if (str != null) {
			int length = str.length();
			for (int i = 0; i < length; i++) {
				if (str.charAt(i) == chr) {
					count++;
				}
			}
			return count;
		}
		return count;
	}

	/**
	 * 返回指定字符位置前数据
	 * 
	 * @param str
	 * @param chr
	 * @param max
	 * @return
	 */
	public static String charSubstring(String str, char chr, int max) {
		int count = 0;
		StringBuffer sbr = new StringBuffer();
		if (str != null) {
			int length = str.length();
			for (int i = 0; i < length; i++) {
				char result = str.charAt(i);
				sbr.append(result);
				if (result == chr) {
					count++;
				}
				if (count == max) {
					return sbr.toString();
				}
			}
		}
		return sbr.toString();
	}

	/**
	 * 清除字符串数组中空格
	 * 
	 * @param strings
	 * @return
	 */
	public static String[] trim(String[] s) {
		if (s == null) {
			return null;
		}
		for (int i = 0, len = s.length; i < len; i++) {
			s[i] = s[i].trim();
		}
		return s;
	}

	/**
	 * 整理字符串中指定字符，清空指定符号
	 * 
	 * @param s
	 * @param delimit
	 * @return
	 */
	public static String trim(String s, char delimit[]) {
		if (s == null) {
			return null;
		}
		int length = s.length();
		int beginIndex = 0;
		int endIndex = length;
		for (; beginIndex < length; beginIndex++) {
			char c = s.charAt(beginIndex);
			boolean found = false;
			for (int i = 0; i < delimit.length; i++) {
				if (delimit[i] != c) {
					continue;
				}
				found = true;
				break;
			}
			if (!found)
				break;
		}

		for (; endIndex > beginIndex; endIndex--) {
			char c = s.charAt(endIndex - 1);
			boolean found = false;
			for (int i = 0; i < delimit.length; i++) {
				if (delimit[i] != c)
					continue;
				found = true;
				break;
			}

			if (!found)
				break;
		}

		if (beginIndex == endIndex)
			return "";
		if (beginIndex > 0 || endIndex < length)
			return s.substring(beginIndex, endIndex);
		else
			return s;
	}

	/**
	 * hash等值判定
	 * 
	 * @param source
	 * @param obj
	 * @return
	 */
	public static final boolean hashEqules(Object source, Object obj) {
		return (hash(source) == hash(obj));
	}


	/**
	 * hash化
	 * 
	 * @param x
	 * @return
	 */
	private static int hash(Object x) {
		int h = x.hashCode();
		h += ~(h << 9);
		h ^= (h >>> 14);
		h += (h << 4);
		h ^= (h >>> 10);
		return h;
	}


}
