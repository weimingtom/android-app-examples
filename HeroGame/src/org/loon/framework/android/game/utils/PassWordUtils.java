package org.loon.framework.android.game.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.core.LGUID;
import org.loon.framework.android.game.core.LUUID;

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
final public class PassWordUtils {

	// 16进制context array
	private static final String[] hexDigits = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	static private MessageDigest digest = null;

	final static private String pswBase1 = "0123456789.abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ_";

	final static private String pswBase2 = "iMEtuv45zA3OPmnCjcdFGHI9.aJKLpqrsQDTUB2V78bXwYZ_01oNRS6efghxyWkl";

	final static private HashMap pswMapEmc = new HashMap();

	final static private HashMap pswMapDec = new HashMap();

	final static private int ENCODE_XORMASK = 0x5A;

	final static private char ENCODE_DELIMETER = '\002';

	final static private char ENCODE_CHAR_OFFSET1 = 'A';

	final static private char ENCODE_CHAR_OFFSET2 = 'h';

	static {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
		}
		for (int index = 0; index < pswBase1.length(); index++) {
			char c = pswBase1.charAt(index);
			pswMapEmc.put(String.valueOf(c), String.valueOf(pswBase2
					.charAt(index)));
			pswMapDec.put(String.valueOf(pswBase2.charAt(index)), String
					.valueOf(c));
		}
	}

	/**
	 * 将byte[]转为16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String toHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	/**
	 * 将byte转为16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte b) {
		int n = b;
		if (n < 0)
			n = 256 + n;
		int d1 = n / 16;
		int d2 = n % 16;
		return hexDigits[d1] + hexDigits[d2];
	}

	/**
	 * 将int转为16进制字符串
	 * 
	 * @param i
	 * @return
	 */
	public static String toHexString(final int i) {
		StringBuffer buf = new StringBuffer();
		appendHex(buf, i);
		return buf.toString();
	}

	/**
	 * 将int插入StringBuffer
	 * 
	 * @param buf
	 * @param i
	 */
	public static void appendHex(final StringBuffer buf, final int i) {
		buf.append(Integer.toHexString((i >> 24) & 0xff));
		buf.append(Integer.toHexString((i >> 16) & 0xff));
		buf.append(Integer.toHexString((i >> 8) & 0xff));
		buf.append(Integer.toHexString(i & 0xff));
	}

	/**
	 * 调用md5算法
	 * 
	 * @param origin
	 * @return
	 */
	public static String toMD5(String origin) {
		String result = null;
		try {
			result = new String(origin);
			result = PassWordUtils
					.toHexString(digest.digest(result.getBytes()));
		} catch (Exception ex) {
			return null;
		}
		return result;
	}

	/**
	 * 返回一组字母英文的混合密码
	 * 
	 * @param length
	 * @return
	 */
	public static String toBlendPassword(int length) {
		if (length < 1)
			return null;
		String strChars[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "a",
				"b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m", "n",
				"p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "a" };
		StringBuffer strPassword = new StringBuffer();
		int nRand = (int) Math.round(Math.random() * 100D);
		for (int i = 0; i < length; i++) {
			nRand = (int) Math.round(Math.random() * 100D);
			strPassword.append(strChars[nRand % (strChars.length - 1)]);
		}

		return strPassword.toString();
	}

	/**
	 * 返回一组数字密码
	 * 
	 * @param length
	 * @return
	 */
	public static String toNumberPassword(int length) {
		if (length < 1)
			return null;
		String strChars[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9" };
		StringBuffer strPassword = new StringBuffer();
		int nRand = (int) Math.round(Math.random() * 100D);
		for (int i = 0; i < length; i++) {
			nRand = (int) Math.round(Math.random() * 100D);
			strPassword.append(strChars[nRand % (strChars.length - 1)]);
		}

		return strPassword.toString();
	}

	/**
	 * 以Cookie格式将指定字符串解码
	 * 
	 * @param cookieVal
	 * @return
	 */
	public static String[] decodePasswordCookie(String cookieVal) {

		if ((cookieVal == null) || (cookieVal.length() <= 0)) {
			return null;
		}

		char[] chars = cookieVal.toCharArray();
		byte[] bytes = new byte[chars.length / 2];
		int b;

		for (int n = 0, m = 0; n < bytes.length; n++) {
			b = chars[m++] - ENCODE_CHAR_OFFSET1;
			b |= ((chars[m++] - ENCODE_CHAR_OFFSET2) << 4);
			bytes[n] = (byte) (b ^ (ENCODE_XORMASK + n));
		}

		cookieVal = new String(bytes);

		int pos = cookieVal.indexOf(ENCODE_DELIMETER);
		int pos1 = cookieVal.lastIndexOf(ENCODE_DELIMETER);
		String memberid = (pos < 0) ? "" : cookieVal.substring(0, pos);
		String username = (pos < 0) ? "" : cookieVal.substring(pos + 1, pos1);
		String password = (pos1 < 0) ? "" : cookieVal.substring(pos1 + 1);

		return new String[] { memberid, username, password };
	}

	/**
	 * 以Cookie格式将指定字符串编码
	 * 
	 * @param memberid
	 * @param username
	 * @param password
	 * @return
	 */
	public static String encodePasswordCookie(String memberid, String username,
			String password) {
		StringBuffer buf = new StringBuffer();

		if ((memberid != null) && (username != null) && (password != null)) {
			byte[] bytes = (memberid + ENCODE_DELIMETER + username
					+ ENCODE_DELIMETER + password).getBytes();
			int b;

			for (int n = 0; n < bytes.length; n++) {
				b = bytes[n] ^ (ENCODE_XORMASK + n);
				buf.append((char) (ENCODE_CHAR_OFFSET1 + (b & 0x0F)));
				buf.append((char) (ENCODE_CHAR_OFFSET2 + ((b >> 4) & 0x0F)));
			}
		}

		return buf.toString();
	}

	/**
	 * 返回一个uuid字符串
	 * 
	 * @return
	 */
	public static String getUUID() {
		return LUUID.make();
	}

	/**
	 * 返回一个guid字符串
	 * 
	 * @return
	 */
	public static String getGUID() {
		return new LGUID().toString();
	}

	public static String encodePsw(String str) {
		return convert(str, pswMapEmc);
	}

	public static String decodePsw(String str) {
		return convert(str, pswMapDec);
	}

	/**
	 * 转换str内容为convertMap后返回
	 * 
	 * @param str
	 * @param convertMap
	 * @return
	 */
	public static String convert(String str, Map convertMap) {
		StringBuffer sb = new StringBuffer();
		char[] strs = str.toCharArray();
		for (int i = 0; i < strs.length; i++) {
			char c = strs[i];
			Character cc = (Character) convertMap.get(String.valueOf(c));
			if (cc == null) {
				sb.append(c);
			} else {
				sb.append(cc);
			}
		}
		return sb.toString();
	}
}
