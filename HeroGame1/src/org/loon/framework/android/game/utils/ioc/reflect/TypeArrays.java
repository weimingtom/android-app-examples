package org.loon.framework.android.game.utils.ioc.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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
public class TypeArrays extends TypeArray {

	public static final String CONSTRUCTOR_METHOD_NAME = "<init>";

	private final String name;

	private final int hashCode;

	public static TypeArrays getNamedTypeArray(Method method) {
		return new TypeArrays(method, method.getParameterTypes());
	}

	public static TypeArrays getNamedTypeArray(Constructor constructor) {
		return new TypeArrays(CONSTRUCTOR_METHOD_NAME, constructor
				.getParameterTypes());
	}

	public TypeArrays(Method method, Class[] types) {
		this(ReflectorUtils.getMethodName(method), types);
	}

	public TypeArrays(String methodName, Class[] types) {
		super(types);
		this.name = methodName;
		this.hashCode = ((super.hashCode() * 17) + (name.hashCode() * 31));
	}

	public String getName() {
		return name;
	}

	public boolean equals(Object obj) {
		if (obj != null && TypeArrays.class.equals(obj.getClass())) {
			TypeArrays other = (TypeArrays) obj;
			return (this.name.equals(other.name) && Arrays.equals(this.types,
					other.types));
		}
		return false;
	}

	public int hashCode() {
		return hashCode;
	}

}
