package org.loon.framework.android.game.utils.ioc.injector;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.Iterator;

import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.ReflectorUtils;
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
public class ClassBindImpl implements ClassBind {

	private Class classDependency;
	private Collection constructorParameters = CollectionUtils
			.createCollection();

	public ClassBindImpl(Class classDependency) {
		this.classDependency = classDependency;
	}

	private void fillConstructorParameters() {
		if (this.constructorParameters.isEmpty()) {
			Collection constructors = CollectionUtils
					.createCollection(classDependency.getDeclaredConstructors());
			Constructor constructor = (Constructor) CollectionUtils
					.first(constructors);
			Collection collection = CollectionUtils
					.createCollection(constructor.getParameterTypes());
			CollectionUtils.visitor(collection, new Dispose() {
				public void accept(Object object) {
					ClassBindImpl.this.addKeyParam(object);
				}

				public void accept() {
				}
			});
		}
	}

	public Object instance(Container container) {

		fillConstructorParameters();
		Collection instances = CollectionUtils.createCollection();
		Object obj = null;
		if (constructorParameters.size() == 0) {
			obj = Reflector.getReflector(classDependency).newInstance();
		} else {
			for (Iterator it = this.constructorParameters.iterator(); it
					.hasNext();) {
				Bind dependency = (Bind) it.next();
				instances.add(dependency.instance(container));
			}
			obj = ReflectorUtils.invokeContructor(classDependency, instances);
		}
		return obj;
	}

	public ClassBind addInstanceParam(Object instance) {
		addConstructorParameter(InjectorFactory
				.createInstanceDependency(instance));
		return this;
	}

	public ClassBind addKeyParam(Object key) {
		addConstructorParameter(new BindDelegated(key));
		return this;
	}

	private void addConstructorParameter(Bind dependency) {
		this.constructorParameters.add(dependency);
	}

}
