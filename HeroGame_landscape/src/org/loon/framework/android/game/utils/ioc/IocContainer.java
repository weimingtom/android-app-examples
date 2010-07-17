package org.loon.framework.android.game.utils.ioc;

import java.util.HashSet;
import java.util.Set;

import org.loon.framework.android.game.utils.ioc.injector.Container;
import org.loon.framework.android.game.utils.ioc.injector.InjectorFactory;

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
public class IocContainer {

	final Container defaultContainer;

	final Set args;

	public IocContainer() {
		defaultContainer = InjectorFactory.createContainer();
		args = new HashSet(20);
	}

	public Object getInstance(Class clazz) {
		return defaultContainer.getInstance(clazz);
	}

	public void addValue(Object value) {
		args.add(value);
	}

	public void removeValues() {
		args.clear();
	}

	public void addConstructor(Class clazz) {
		IocFactory.bind(defaultContainer, clazz, args.toArray());
	}

	public void addConstructor(Class clazz, Object[] args) {
		IocFactory.bind(defaultContainer, clazz, args);
	}

	public Container getContainer() {
		return defaultContainer;
	}

	public void initialize() {
		defaultContainer.start();
	}

	public void destroy() {
		defaultContainer.stop();
		args.clear();
	}

	public void finalize() {
		destroy();
	}

}
