package org.loon.framework.android.game.utils.ioc.injector.attribute;

import org.loon.framework.android.game.utils.ReflectorUtils;
import org.loon.framework.android.game.utils.ioc.injector.Container;
import org.loon.framework.android.game.utils.ioc.reflect.Reflector;

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
public class AutoKeyAttributeInjector extends AttributeAbstractInjector {

	public AutoKeyAttributeInjector(Reflector reflector, String attributeName) {
		super(reflector, attributeName);
	}

	private Class inspect(Object target) {
		return ReflectorUtils.getParameterType(target.getClass(),
				this.attributeName, "set");
	}

	protected Object getInstance(Container container, Object target) {
		Class key = inspect(target);
		return container.getInstance(key);
	}

}
