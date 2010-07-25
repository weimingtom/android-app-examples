package org.loon.framework.android.game.extend.db.type;

import org.loon.framework.android.game.extend.db.Serializer;
import org.loon.framework.android.game.utils.FileUtils;

/**
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
 * @version 0.1.2
 */
public class BytesType implements Serializer {

	public byte[] getBytes(final Object obj) {
		if (obj instanceof byte[]) {
			return FileUtils.compress((byte[]) obj);
		} else {
			throw new RuntimeException("Input type exception!");
		}
	}

	public Object getObject(final byte[] bytes) {
		return FileUtils.uncompress(bytes);
	}

	public int getType() {
		return TypeBase.BYTES;
	}

}
