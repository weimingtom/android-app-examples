package org.loon.framework.android.game.utils.collection;

import java.util.LinkedHashMap;
import java.util.Map;

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
public class LRUMap extends LinkedHashMap {

	private static final long serialVersionUID = 1982090161297627627L;

	private static final float FACTOR = 0.75f;

	private final int size;

	public LRUMap(final int cacheSize) {
		this(cacheSize, FACTOR);
	}

	public LRUMap(final int cacheSize, final float fFactor) {
		super((int) Math.ceil(cacheSize / fFactor) + 1, fFactor, true);
		size = cacheSize;
	}

	protected boolean removeEntry(final Map.Entry eldest) {
		return size() > size;
	}

}
