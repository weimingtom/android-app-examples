package org.loon.framework.android.game.utils.collection.xml;

import java.util.ArrayList;
import java.util.List;

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
public class Reference extends DataObjectImpl implements ReferenceConstants {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1982090188643117553L;

	private List columnNames;

	private List conditions;

	private List signs;

	public Reference() {
		columnNames = new ArrayList();
		conditions = new ArrayList();
		signs = new ArrayList();
	}

	public void or(String columnName, String columnValue) {
		add(columnName, "=", columnValue, "String", "OR");
	}

	public void or(String columnName, int columnValue) {
		add(columnName, "=", String.valueOf(columnValue), "int", "OR");
	}

	public void or(String columnName, String columnValue, String type) {
		add(columnName, "=", columnValue, type, "OR");
	}

	public void or(String columnName, String sign, String columnValue,
			String type) {
		add(columnName, sign, columnValue, type, "OR");
	}

	public void add(String columnName, String columnValue) {
		add(columnName, "=", columnValue, "String", "AND");
	}

	public void add(String columnName, int columnValue) {
		add(columnName, "=", String.valueOf(columnValue), "int", "AND");
	}

	public void add(String columnName, String columnValue, String type) {
		add(columnName, "=", columnValue, type, "AND");
	}

	public void add(String columnName, String sign, String columnValue,
			String type) {
		add(columnName, sign, columnValue, type, "AND");
	}

	public void add(String columnName, String sign, String columnValue,
			String columnType, String condition) {
		columnNames.add(columnName);
		conditions.add(condition);
		signs.add(sign);
		set(columnName, columnValue, columnType);
	}

	public int size() {
		return columnNames.size();
	}

	public String getColumnName(int index) {
		return (String) columnNames.get(index);
	}

	public String getSign(int index) {
		return (String) signs.get(index);
	}

	public String getColumnValue(int index) {
		String columnName = getColumnName(index);
		return get(columnName);
	}

	public String getColumnType(int index) {
		String columnName = getColumnName(index);
		return getType(columnName);
	}

	public String getCondition(int index) {
		return (String) conditions.get(index);
	}

}
