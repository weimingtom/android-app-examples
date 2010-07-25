package org.loon.framework.android.game.utils.ioc.injector.attribute;

import org.loon.framework.android.game.utils.ioc.injector.Container;
import org.loon.framework.android.game.utils.ioc.reflect.Reflector;

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
public abstract class AttributeAbstractInjector implements AttributeInjector {

	protected String attributeName;

	private Reflector reflector;

	public AttributeAbstractInjector(Reflector reflector, String attributeName) {
		this.attributeName = attributeName;
		this.reflector = reflector;
	}

	final public void inject(Container container, Object target) {
		Object result = getInstance(container, target);
		if (!(result instanceof Object[])) {
			result = new Object[] { result };
		}
		reflector.setInvoke(target, attributeName, (Object[]) result);
	}

	protected abstract Object getInstance(Container container, Object target);

}
