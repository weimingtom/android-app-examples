package org.loon.framework.android.game.extend.db;

import java.util.Set;

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
public interface MEMDB {

	public void begin();

	public boolean isClose();

	public void openTable(String tableName);

	public Object select(String key);

	public void insert(String key, Object value);

	public void update(String key, Object value);

	public Object delete(String key);

	public Object deleteOpenTableALL(String key);

	public boolean deleteTable(String tableName);

	public void end();

	public Set getTableKey();

}
