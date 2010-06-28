package org.loon.framework.android.game.extend.db.type;

import java.io.UnsupportedEncodingException;

import org.loon.framework.android.game.core.LSystem;
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class StringType implements Serializer {

	public byte[] getBytes(Object obj) {
		if (obj == null)
			return null;
		try {
			return ((String) obj).getBytes(LSystem.encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported Encoding!");
		}
	}

	public Object getObject(byte[] bytes) {
		try {
			return new String(bytes, LSystem.encoding);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Unsupported Encoding!");
		}
	}

	public int getType() {
		return TypeBase.STRING;
	}

}
