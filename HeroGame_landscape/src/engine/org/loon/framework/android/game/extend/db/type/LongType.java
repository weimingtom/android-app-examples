package org.loon.framework.android.game.extend.db.type;

import org.loon.framework.android.game.extend.db.Serializer;

/**
 * 
 * Copyright 2008
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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class LongType implements Serializer {

	public byte[] getBytes(final Object o) {
		byte[] b = new byte[8];
		long v = Long.parseLong(o.toString());
		b[0] = (byte) (0xff & (v >> 56));
		b[1] = (byte) (0xff & (v >> 48));
		b[2] = (byte) (0xff & (v >> 40));
		b[3] = (byte) (0xff & (v >> 32));
		b[4] = (byte) (0xff & (v >> 24));
		b[5] = (byte) (0xff & (v >> 16));
		b[6] = (byte) (0xff & (v >> 8));
		b[7] = (byte) (0xff & v);
		return b;
	}

	public Object getObject(final byte[] bytes) {
		long v = (((long) (bytes[0] & 0xff) << 56)
				| ((long) (bytes[1] & 0xff) << 48)
				| ((long) (bytes[2] & 0xff) << 40)
				| ((long) (bytes[3] & 0xff) << 32)
				| ((long) (bytes[4] & 0xff) << 24)
				| ((long) (bytes[5] & 0xff) << 16)
				| ((long) (bytes[6] & 0xff) << 8) | ((long) (bytes[7] & 0xff)));
		return new Long(v);
	}

	public int getType() {
		return TypeBase.LONG;
	}
}
