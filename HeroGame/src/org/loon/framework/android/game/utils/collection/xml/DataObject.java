package org.loon.framework.android.game.utils.collection.xml;

import java.util.List;
import java.util.Map;

import org.loon.framework.android.game.core.LSystem;

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
public interface DataObject {

	public static final String TYPE_STRING = "String";

	public static final String TYPE_INT = "int";

	public static final String TYPE_LONG = "long";

	public static final String TYPE_DOUBLE = "double";

	public static final String TYPE_BOOLEAN = "boolean";

	public static final String LS = LSystem.LS;

	public static final String FS = LSystem.FS;

	public static final String TAB = "\t";

	public static final String DQ = "\"";

	public abstract int valueSize();

	public abstract int objectSize();

	public abstract boolean containsValueKey(String s);

	public abstract boolean containsValue(String s);

	public abstract boolean containsObjectKey(String s);

	public abstract boolean containsObjectValue(String s);

	public abstract String[] getValueKeys();

	public abstract String[] getObjectKeys();

	public abstract String getLanguage();

	public abstract void setLanguage(String s);

	public abstract String get(String s);

	public abstract int getInt(String s);

	public abstract String getType(String s);

	public abstract void set(String s, String s1);

	public abstract void set(String s, String s1, String s2);

	public abstract void set(String s, int i);

	public abstract void set(String s, long l);

	public abstract void set(String s, boolean flag);

	public abstract void set(String s, DataObject dataobject);

	public abstract void set(DataObject dataobject);

	public abstract Object[] getValues();

	public abstract Object[] getValues(String s);

	public abstract void change(String s, String s1);

	public abstract void remove(String s);

	public abstract String getEigenName();

	public abstract void setEigenName(String s);

	public abstract boolean isEigenBoolean();

	public abstract void setEigenBoolean(boolean flag);

	public abstract String getEigenValue(String s);

	public abstract void setEigenValue(String s, String s1);

	public abstract void setReverse(boolean flag);

	public abstract Object getObject(String s);

	public abstract Object getObject(int i);

	public abstract void setObject(String s, Object obj);

	public abstract List getObjects();

	public abstract Map getMap();

	public abstract byte[] getBytes();

	public abstract void setBytes(byte abyte0[]);

	public abstract DataObject getDataObject();

	public abstract void setDataObject(DataObject dataobject);

	public abstract DataObjectTable getDataObjectTable();

}
