package org.loon.framework.android.game.utils.ioc.injector.attribute;

import org.loon.framework.android.game.utils.ioc.injector.CompositeInjector;
import org.loon.framework.android.game.utils.ioc.injector.Container;
import org.loon.framework.android.game.utils.ioc.injector.InjectorFactory;
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
public class AttributeInjectorBuilderImpl implements AttributeInjectorBuilder {

	private CompositeInjector compositeInjector = InjectorFactory
			.createCompositeInjector();

	public AttributeInjectorBuilder addAttributeKey(Reflector reflector,
			String attributeName, Object key) {
		this.compositeInjector.addInjector(new AttributeKeyInjector(reflector,
				attributeName, key));
		return this;
	}

	public AttributeInjectorBuilder addAttributeInstance(Reflector reflector,
			String attributeName, Object instance) {
		this.compositeInjector.addInjector(new AttributeInjectorInstance(
				reflector, attributeName, instance));
		return this;
	}

	public AttributeInjectorBuilder addAttribute(Reflector reflector,
			String attributeName) {
		this.compositeInjector.addInjector(new AutoKeyAttributeInjector(
				reflector, attributeName));
		return this;
	}

	public void setInjector(Object key, Container container) {
		container.addInjector(key, this.compositeInjector);
	}

}
