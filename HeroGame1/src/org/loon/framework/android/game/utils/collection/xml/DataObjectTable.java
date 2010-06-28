package org.loon.framework.android.game.utils.collection.xml;

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
public interface DataObjectTable {

	public abstract String getEigenName();

	public abstract void setEigenName(String s);

	public abstract String getEigenValue();

	public abstract void setEigenValue(String s);

	public abstract boolean containsKey(String s);

	public abstract String getValue(String s);

	public abstract void setValue(String s, String s1);

	public abstract DataObject getDataObject();

	public abstract void setDataObjectTable(DataObjectTable dataobjecttable);

	public abstract DataObject get(String s);

	public abstract void add(String s, String s1);

	public abstract void add(DataObject dataobject);

	public abstract void add(String s, DataObject dataobject);

	public abstract void addStore(String s, DataObject dataobject);

	public abstract void set(DataObject dataobject);

	public abstract void add(int i, DataObject dataobject);

	public abstract DataObject get(int i);

	public abstract int size();

	public abstract void remove(int i);

	public abstract void remove(DataObject dataobject);

	public abstract void orderBy(String s);

	public abstract void orderByDesc(String s);

	public abstract void orderByAsc(String s);

	public abstract void orderByNumber(String s);

	public abstract void orderByDescNumber(String s);

	public abstract void orderByAscNumber(String s);

	public abstract void addNumberKey(String s);

	public abstract void sort(String s);

	public abstract void sortNumber(String s);

	public abstract void setReverse(boolean flag);
}
