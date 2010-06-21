package org.loon.framework.android.game.core;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import org.loon.framework.android.game.utils.PassWordUtils;

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

public class LUUID {

	static private SecureRandom random = new SecureRandom();

	static private String base = PassWordUtils.toHexString(getAddress())
			+ PassWordUtils.toHexString(System.identityHashCode(random));

	final static private byte[] DEFAULT_ADDRESS = new byte[] { (byte) 127,
			(byte) 0, (byte) 0, (byte) 1 };

	private LUUID() {
	}

	/**
	 * 生成一组UUID
	 * 
	 * @return
	 */
	public static String make() {
		StringBuffer buf = new StringBuffer(base.length() * 2);
		buf.append(base);
		int lowTime = (int) System.currentTimeMillis() >> 32;
		PassWordUtils.appendHex(buf, lowTime);
		PassWordUtils.appendHex(buf, random.nextInt());
		return buf.toString();
	}

	private static byte[] getAddress() {
		try {
			return InetAddress.getLocalHost().getAddress();
		} catch (UnknownHostException ignore) {
			return DEFAULT_ADDRESS;
		}
	}

}
