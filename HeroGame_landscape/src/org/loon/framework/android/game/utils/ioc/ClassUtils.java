package org.loon.framework.android.game.utils.ioc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.utils.CollectionUtils;

/**
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
public class ClassUtils {

	final static Map lazyMap = Collections.synchronizedMap(new HashMap(1000));

	final static private String CLASS_FILE_SUFFIX = ".class";

	final static private char PACKAGE_SEPARATOR = '.';

	final static private Map baseTypeMap = CollectionUtils.createMap(9);

	final static private Map baseClassMap = CollectionUtils.createMap(9);

	// 类与对象的对应关系
	static {
		baseClassMap.put(Character.class, Character.TYPE);
		baseClassMap.put(Integer.class, Integer.TYPE);
		baseClassMap.put(Long.class, Long.TYPE);
		baseClassMap.put(Short.class, Short.TYPE);
		baseClassMap.put(Float.class, Float.TYPE);
		baseClassMap.put(Boolean.class, Boolean.TYPE);
		baseClassMap.put(Double.class, Double.TYPE);
		baseClassMap.put(Byte.class, Byte.TYPE);
		baseClassMap.put(Void.class, Void.TYPE);
	}

	// 对象与类的对应关系
	static {
		baseTypeMap.put(Boolean.TYPE, Boolean.class);
		baseTypeMap.put(Byte.TYPE, Byte.class);
		baseTypeMap.put(Character.TYPE, Character.class);
		baseTypeMap.put(Double.TYPE, Double.class);
		baseTypeMap.put(Float.TYPE, Float.class);
		baseTypeMap.put(Integer.TYPE, Integer.class);
		baseTypeMap.put(Long.TYPE, Long.class);
		baseTypeMap.put(Short.TYPE, Short.class);
		baseTypeMap.put(Void.TYPE, Void.class);
	}

	final static public String getClassToType(final Object object) {
		Class clazz = object.getClass();
		Class type = (Class) baseClassMap.get(clazz);
		return type == null ? clazz.getName() : type.toString();
	}

	final static public Class getTypeToClass(final Class clazz) {
		return (Class) baseClassMap.get(clazz);
	}

	final static public Map getBaseTypes() {
		return baseTypeMap;
	}

	final public static boolean equals(ClassLoader cl, final ClassLoader other) {
		while (cl != null) {
			if (cl == other) {
				return true;
			}
			cl = cl.getParent();
		}
		return false;
	}

	public static String getResourcePath(String path, String extension) {
		if (extension == null) {
			return path;
		}
		extension = "." + extension;
		if (path.endsWith(extension)) {
			return path;
		}
		return path.replace('.', '/') + extension;
	}

	public static String getClassFileName(Class clazz) {
		String className = clazz.getName();
		int lastDotIndex = className.lastIndexOf(PACKAGE_SEPARATOR);
		return className.substring(lastDotIndex + 1) + CLASS_FILE_SUFFIX;
	}

	final static public ClassMethod getFieldInspector(final Class clazz) {
		Object object = lazyMap.get(clazz);
		if (object == null) {
			object = new ClassMethod(clazz, false);
			lazyMap.put(clazz, object);
		}
		return (ClassMethod) object;
	}
}
