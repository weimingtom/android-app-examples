package org.loon.framework.android.game.extend.db;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.loon.framework.android.game.extend.db.index.IndexList;
import org.loon.framework.android.game.extend.db.type.TypeBase;
import org.loon.framework.android.game.extend.db.type.TypeUtils;
import org.loon.framework.android.game.utils.CollectionUtils;
import org.loon.framework.android.game.utils.FileUtils;
import org.loon.framework.android.game.utils.collection.ArraySet;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
class MDBImpl implements MDB {

	private Set keys = new ArraySet(CollectionUtils.INITIAL_CAPACITY);

	private String tableName = null;

	private volatile int tableType = -1;

	private AccessData lockData;

	private File dataFile;

	private String filePath;

	// private String dir;

	private boolean open = false;

	/*
	 * private static String getFileName(final String name) { File file =
	 * FileUtils.getSystemFile(name); String temp = file.getAbsolutePath(); int
	 * index = temp.lastIndexOf(LSystem.FS); if (index == -1) { return ""; }
	 * else { return temp.substring(index + 1); } }
	 */

	public MDBImpl(String fileName) {
		// this.filePath = getFileName(fileName);
		// this.dir = fileName.replaceAll(filePath, "");
		this.filePath = FileUtils.getSystemFile(fileName).getAbsolutePath();
	}

	/**
	 * 验证表格状况
	 * 
	 */
	private synchronized void validateTable() {
		if (tableName == null || isClose()) {
			throw new RuntimeException("Loon database is closed!");
		}
	}

	/**
	 * 打开指定表格
	 */
	public synchronized void openTable(String tableName) {
		openTable(tableName, TypeBase.OBJECT);
	}

	/**
	 * 以指定类型，打开指定表格
	 */
	public synchronized void openTable(String tableName, int type) {
		this.tableName = tableName;
		this.tableType = type;

	}

	/**
	 * 数据库表格名集合
	 * 
	 * @return
	 */
	public synchronized Set getTables() {
		return lockData.getKeys();
	}

	/**
	 * 打开指定表格中数据
	 */
	public Object openTableObject(String tableName, String key, int type) {
		openTable(tableName, type);
		return select(key);
	}

	public Object openTableObject(String tableName, String key) {
		return openTableObject(tableName, key, TypeBase.OBJECT);
	}

	/**
	 * 删除指定表格
	 */
	public synchronized boolean deleteTable() {
		try {
			validateTable();
			lockData.delIndex(tableName);
		} catch (IOException e) {
			open = false;
			return false;
		}
		return true;
	}

	/**
	 * 返回全部表格名
	 * 
	 * @return
	 */
	public synchronized Set openTables() {
		return keys;
	}

	/**
	 * 获得指定表格所有键名
	 * 
	 * @return
	 */
	public synchronized Set getTableKey() {
		IndexList index = getTable();
		if (index != null) {
			return index.getKeys();
		}
		return new HashSet(0);
	}

	/**
	 * 获得当前数据库表格列表
	 */
	public synchronized Map getTableList() {
		Set keys = getTableKey();
		Map result = new HashMap(10);
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String name = (String) it.next();
			if (name != null) {
				result.put(name, select(name));
			}
		}
		return result;
	}

	private synchronized IndexList getTable() {
		return getIndex(this.tableName, this.tableType);
	}

	/**
	 * 获得指定的主键
	 * 
	 * @param indexName
	 * @param type
	 * @return
	 */
	final synchronized private IndexList getIndex(final String indexName,
			final int type) {
		IndexList index = null;
		validateTable();
		try {
			Serializer value = TypeUtils.switchSerializer(type);
			index = lockData.getIndex(indexName, TypeUtils.STRING, value);
			if (index == null) {
				index = lockData.makeIndex(indexName, TypeUtils.STRING, value);
				keys.add(indexName);
			}

		} catch (IOException e) {
			open = false;
		}
		return index;

	}

	/**
	 * 插入
	 */
	public synchronized void insert(String key, Object value) {
		IndexList list = getTable();
		if (list != null) {
			list.put(key, value);
		}
	}

	/**
	 * 查询指定数据
	 */
	public synchronized Object select(String key) {
		IndexList index = getTable();
		if (index == null) {
			return null;
		}
		Object result = index.get(key);
		return result;
	}

	/**
	 * 修改
	 */
	public synchronized void update(String key, Object value) {
		insert(key, value);
	}

	/**
	 * 删除指定索引
	 */
	public synchronized Object deleteIndex(long keyIndex) {
		long index = 0;
		Set set = getTableKey();
		for (Iterator it = set.iterator(); it.hasNext();) {
			String name = (String) it.next();
			index++;
			if (keyIndex == index) {
				return delete(name);
			}

		}
		return null;
	}

	/**
	 * 删除指定键
	 */
	public synchronized Object delete(String key) {
		Object result = null;
		IndexList list = getTable();
		if (list != null) {
			result = list.remove(key);
		}
		return result;
	}

	public synchronized Object deleteTableAndReturn(String key) {
		Object object = select(key);
		deleteTable();
		return object;
	}

	public synchronized void begin() {
		begin(null);
	}

	public synchronized void begin(final String password) {
		if (isClose()) {
			try {
				FileUtils.makedirs(filePath);
				dataFile = new File(filePath);
				lockData = new AccessData(dataFile, password, !dataFile
						.exists());
			} catch (IOException e) {
				open = false;
				throw new RuntimeException(e);
			}
			open = true;
		}
	}

	public synchronized boolean isClose() {
		return !open;
	}

	public synchronized void end() {
		if (lockData == null) {
			return;
		}
		try {
			lockData.close();
			lockData.getAccess().close();
		} catch (IOException e) {
			open = false;
			throw new RuntimeException(e);
		}
		open = false;
	}

	public synchronized boolean removeFile() {
		if (!open) {
			return new File(this.filePath).delete();
		} else {
			throw new RuntimeException("Database not closed!");
		}
	}

}
