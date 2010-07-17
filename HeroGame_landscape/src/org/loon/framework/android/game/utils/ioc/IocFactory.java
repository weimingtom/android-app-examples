package org.loon.framework.android.game.utils.ioc;

import java.util.Map;

import org.loon.framework.android.game.core.LSystem;
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
public class IocFactory {

	final static Container defaultContainer = InjectorFactory.createContainer();

	public static Container getDefaultContainer() {
		return defaultContainer;
	}

	public static void initialize() {
		LSystem.gc();
		defaultContainer.start();
	}

	public static void destroy() {
		defaultContainer.stop();
		LSystem.gc();
	}

	public static Ioc bind(final Container container, final Class clazz) {
		return new IocBind(container, clazz);
	}

	public static Ioc bind(final Container container, final Class clazz,
			final int model) {
		return new IocBind(container, clazz, model);
	}

	public static Ioc bind(final Container container, final String className) {
		try {
			return new IocBind(container, Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage() + " Unable to load!");
		}
	}

	public static Ioc bind(final Container container, final String className,
			final Map args) {
		return bind(container, className, args.values().toArray());
	}

	public static Ioc bind(final Container container, final String className,
			final Object[] args) {
		try {
			return new IocBind(container, Class.forName(className), args);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage() + " Unable to load!");
		}
	}

	public static Ioc bind(final Container container, final Class clazz,
			final Object[] args) {
		return new IocBind(container, clazz, args);
	}

	public static Ioc bind(final Container container, final Class clazz,
			final Object[] args, final int model) {
		return new IocBind(container, clazz, args, model);
	}

	// ---------- 使用默认容器--------------//
	public static Ioc bind(final Class clazz) {
		return new IocBind(defaultContainer, clazz);
	}

	public static Ioc bind(final Class clazz, final int model) {
		return new IocBind(defaultContainer, clazz, model);
	}

	public static Ioc bind(final String className) {
		try {
			return new IocBind(defaultContainer, Class.forName(className));
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage() + " Unable to load!");
		}
	}

	public static Ioc bind(final String className, final Map args) {
		return bind(defaultContainer, className, args.values().toArray());
	}

	public static Ioc bind(final String className, final Object[] args) {
		try {
			return new IocBind(defaultContainer, Class.forName(className), args);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e.getMessage() + " Unable to load!");
		}
	}

	public static Ioc bind(final Class clazz, final Object[] args) {
		return new IocBind(defaultContainer, clazz, args);
	}

	public static Ioc bind(final Class clazz, final Object[] args,
			final int model) {
		return new IocBind(defaultContainer, clazz, args, model);
	}
}
