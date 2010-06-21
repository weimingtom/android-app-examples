package org.loon.framework.android.game.utils.ioc.injector.attribute;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.ioc.injector.Bind;
import org.loon.framework.android.game.utils.ioc.injector.BindInterceptor;
import org.loon.framework.android.game.utils.ioc.injector.BindMediator;
import org.loon.framework.android.game.utils.ioc.injector.ClassBind;
import org.loon.framework.android.game.utils.ioc.injector.ClassBindImpl;
import org.loon.framework.android.game.utils.ioc.injector.ComponentFactory;
import org.loon.framework.android.game.utils.ioc.injector.Container;
import org.loon.framework.android.game.utils.ioc.injector.Injector;
import org.loon.framework.android.game.utils.ioc.injector.Interceptor;
import org.loon.framework.android.game.utils.ioc.injector.Start;

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
public abstract class AbstractContainer implements Container {

	private Map injectors = CollectionUtils.createMap();

	private Map binds = CollectionUtils.createMap();

	private Interceptor interceptor = this.createInterceptor();

	private boolean started;

	private ComponentFactory factory;

	public AbstractContainer(ComponentFactory factory) {
		this.factory = factory;
	}

	public void inject(Object key, Object target) {
		Injector injector = this.getInjector(key);

		if (injector != null && target != null) {

			injector.inject(this, target);
		}
	}

	private Injector getInjector(Object key) {
		return (Injector) this.injectors.get(key);
	}

	public Container addInjector(Object key, Injector injector) {
		this.injectors.put(key, injector);
		return this;
	}

	private Object getKey(Object key) {

		Object rightKey = key;
		Collection keys = CollectionUtils.createList();

		if (!binds.containsKey(key) && key instanceof Class) {
			for (Iterator it = binds.keySet().iterator(); it.hasNext();) {
				Object candidateKey = (Object) it.next();

				if (candidateKey instanceof Class) {
					keys
							.add(((Class) key)
									.isAssignableFrom((Class) candidateKey) ? candidateKey
									: key);
				}
			}
			rightKey = CollectionUtils.first(keys);
		}
		return rightKey;
	}

	public Object getInstance(Object key) {
		Object rightKey = getKey(key);
		Object target = null;

		this.interceptor.before(rightKey);
		target = getInstanceFromBind(rightKey);
		this.interceptor.after(rightKey);
		inject(rightKey, target);

		return target;
	}

	public void inject(Object target) {
		for (Iterator it = this.injectors.keySet().iterator(); it.hasNext();) {
			Object key = it.next();
			if (key instanceof Class) {
				Class classKey = (Class) key;

				if (classKey.isAssignableFrom(target.getClass())) {
					Injector injector = getInjector(classKey);
					injector.inject(this, target);
				}
			}
		}
	}

	public Object getAttributeValue(Object key) {
		Object rightKey = key;
		Collection keys = CollectionUtils.createList();

		if (binds.containsKey(key)) {
			for (Iterator it = binds.keySet().iterator(); it.hasNext();) {
				Object candidateKey = (Object) it.next();
				keys.add(candidateKey);
			}

			rightKey = CollectionUtils.first(keys);
		}
		return rightKey;
	}

	public Container addBind(Object key, Bind dependency) {
		this.binds.put(key, this.factory.createBindMediator(dependency, this));
		return this;
	}

	private Interceptor createInterceptor() {
		return new BindInterceptor();
	}

	private Object getInstanceFromBind(Object key) {
		BindMediator result = getBindMediator(key);
		if (result != null) {
			return result.getInstance();
		}
		return result;

	}

	private BindMediator getBindMediator(Object key) {
		return (BindMediator) this.binds.get(key);
	}

	public Bind getBind(Object key) {
		BindMediator bindMediator = getBindMediator(key);
		return bindMediator.getBind();
	}

	public Container addInstanceBind(Object key, Object instance) {
		return this.addBind(key, this.factory.createInstanceBind(instance));
	}

	public AttributeInjectorBuilder addAttributeInjector(Object key) {
		AttributeInjectorBuilder attributeInjector = this.factory
				.createAttributeInjectorBuilder();
		attributeInjector.setInjector(key, this);
		return attributeInjector;
	}

	public ClassBind addClassBind(Object key, Class classDependency) {
		ClassBind dependency = new ClassBindImpl(classDependency);
		this.addBind(key, dependency);
		return dependency;
	}

	public void start() {
		if (!this.started) {
			for (Iterator it = binds.values().iterator(); it.hasNext();) {
				Start startable = (Start) it.next();
				startable.start();
			}
			this.started = true;
		}
	}

	public void stop() {
		if (this.started) {
			for (Iterator it = binds.values().iterator(); it.hasNext();) {
				Start startable = (Start) it.next();
				startable.stop();
			}
			this.started = false;
		}
	}
}