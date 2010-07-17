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
public class IntType implements Serializer {

	public byte[] getBytes(final Object o) {
		byte[] b = new byte[4];
		int v = Integer.parseInt(o.toString());
		b[0] = (byte) (0xff & (v >> 24));
		b[1] = (byte) (0xff & (v >> 16));
		b[2] = (byte) (0xff & (v >> 8));
		b[3] = (byte) (0xff & v);
		return b;
	}

	public Object getObject(final byte[] bytes) {
		int v = (((int) (bytes[0] & 0xff) << 24)
				| ((int) (bytes[1] & 0xff) << 16)
				| ((int) (bytes[2] & 0xff) << 8) | ((int) (bytes[3] & 0xff)));
		return new Integer(v);
	}

	public int getType() {
		return TypeBase.INTEGER;
	}

}
