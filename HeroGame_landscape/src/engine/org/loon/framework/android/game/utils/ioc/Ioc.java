package org.loon.framework.android.game.utils.ioc;

import java.util.Set;

import org.loon.framework.android.game.utils.ioc.injector.Container;

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
public interface Ioc extends IControl {

	/**
	 * 返回当前Ioc对象中的Feild集合
	 * 
	 * @return
	 */
	public Set getFeilds();

	/**
	 * 获得一个指定的Feild数值
	 * 
	 * @param name
	 * @return
	 */
	public Object getFeild(final String name);

	/**
	 * 检查当前Ioc对象是否为指定接口的实现
	 * 
	 * @param clazz
	 * @return
	 */
	public boolean isImplInterface(final Class clazz);

	/**
	 * 检查当前Ioc对象是否为指定接口的实现
	 * 
	 * @param className
	 * @return
	 */
	public boolean isImplInterface(final String className);

	/**
	 * 指定一个函数，传参并执行
	 * 
	 * @param methodName
	 * @param args
	 * @return
	 * @throws Exception
	 */
	public Object doInvoke(final String methodName, final Object[] args)
			throws Exception;

	/**
	 * 指定一个函数并执行
	 * 
	 * @param methodName
	 * @return
	 * @throws Exception
	 */
	public Object doInvoke(String methodName) throws Exception;

	/**
	 * 获得Ioc中一个子类的Ioc对象
	 * 
	 * @param methodName
	 * @return
	 */
	public Ioc getChild(String methodName);

	/**
	 * 返回当前容器
	 * 
	 * @return
	 */
	public Container getContainer();

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(String attributeName, int value);

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(String attributeName, long value);

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(String attributeName, double value);

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(String attributeName, float value);

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(String attributeName, boolean value);

	/**
	 * 注入指定方法
	 * 
	 * @param attributeName
	 * @param value
	 */
	public void setMethod(final String attributeName, final Object value);

}
