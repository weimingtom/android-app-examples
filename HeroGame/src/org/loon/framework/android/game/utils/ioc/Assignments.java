package org.loon.framework.android.game.utils.ioc;

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
public class Assignments {

	/**
	 * 分配键值对用
	 */
	private static final long serialVersionUID = 4058101387775241982L;

	private Object keyValue;

	private Object valValue;

	public Assignments(Object key, Object value) {
		this.setKey(key);
		this.setValue(value);
	}

	public Object getKey() {
		return keyValue;
	}

	public String getKeyString() {
		return keyValue == null ? "" : keyValue.toString();
	}

	public void setKey(Object key) {
		this.keyValue = key;
	}

	public String getValueString() {
		return valValue == null ? "" : valValue.toString();
	}

	public Object getValue() {
		return valValue;
	}

	public void setValue(Object value) {
		this.valValue = value;
	}

}
