package org.loon.framework.android.game.utils.ioc.reflect;

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
public abstract class BaseConverter implements Converter {

	protected final Class inputType;

	protected BaseConverter(Class inputType) {
		this.inputType = inputType;
	}

	public final Object convert(Object object) {
		if (object == null) {
			return null;
		}
		if (!supportsType(object.getClass())) {
			throw new IllegalArgumentException(object.getClass()
					+ " Not supported!");
		}
		return doConversion(object);
	}

	protected abstract Object doConversion(Object input);

	public boolean supportsType(Class type) {
		return (inputType.isAssignableFrom(type));
	}

	public Class getInputType() {
		return inputType;
	}
}