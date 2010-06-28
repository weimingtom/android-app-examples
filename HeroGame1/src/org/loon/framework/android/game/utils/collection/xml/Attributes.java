package org.loon.framework.android.game.utils.collection.xml;

import java.util.Collection;
import java.util.Iterator;

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
public interface Attributes {

	public Object[] toArray();

	public Iterator iterator();

	public String toString();

	public boolean isEmpty();

	public int size();

	public Collection getCollection();

	public boolean containsKey(Object value);

	public Object[] getAttributesKeys();

	public Object getAttributeValue(Object attributeName);

	public Object getAttributeValue(int index);

	public boolean containsValue(Object value);

}
