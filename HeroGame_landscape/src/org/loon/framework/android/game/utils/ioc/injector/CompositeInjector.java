package org.loon.framework.android.game.utils.ioc.injector;

import java.util.Collection;
import java.util.Iterator;

import org.loon.framework.android.game.utils.CollectionUtils;

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
public class CompositeInjector implements Injector {

	private Collection injectors = CollectionUtils.createCollection();

	public void inject(Container container, Object target) {
		for (Iterator it = injectors.iterator(); it.hasNext();) {
			Injector injector = (Injector) it.next();

			injector.inject(container, target);
		}
	}

	public Collection injects() {
		return injectors;
	}

	public CompositeInjector addInjector(Injector injector) {
		injectors.add(injector);
		return this;
	}

}
