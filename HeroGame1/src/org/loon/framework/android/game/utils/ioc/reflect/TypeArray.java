package org.loon.framework.android.game.utils.ioc.reflect;

import java.util.Arrays;

import org.loon.framework.android.game.utils.ReflectorUtils;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class TypeArray {

	protected final Class[] types;

	private int hashcode;

	private boolean aliased;

	public TypeArray(final Class[] types) {
		this.types = (Class[]) (types != null ? types.clone() : new Class[0]);
		this.hashcode = ReflectorUtils.arrayHashCode(this.types);
	}

	public int hashCode() {
		return hashcode;
	}

	public boolean equals(Object obj) {
		if (obj != null && TypeArray.class.equals(obj.getClass())) {
			TypeArray other = (TypeArray) obj;
			return (Arrays.equals(this.types, other.types) && this.aliased == other.aliased);
		}
		return false;
	}

	public Class[] getParameterTypes() {
		return (Class[]) types.clone();
	}

	public boolean isAliased() {
		return aliased;
	}

	public void setAliased(boolean aliased) {
		if (this.aliased != aliased) {
			if (aliased) {
				hashcode += 19;
			} else {
				hashcode -= 19;
			}
			this.aliased = aliased;
		}
	}
}
