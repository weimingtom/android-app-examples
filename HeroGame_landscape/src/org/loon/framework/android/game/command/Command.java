package org.loon.framework.android.game.command;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.utils.collection.ArrayMap;

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
public class Command extends Conversion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final static private Map cacheScript = Collections
			.synchronizedMap(new HashMap(1500));

	private String cacheCommandName;

	// 函数列表
	final public ArrayMap functions = new ArrayMap(20);

	// 变量列表
	final Map setEnvironmentList = Collections.synchronizedMap(new HashMap(20));

	// 条件分支列表
	ArrayMap conditionEnvironmentList = new ArrayMap(30);

	// 读入连续数据
	final static private StringBuffer reader = new StringBuffer(3000);

	// 注释标记中
	private boolean flaging = false;

	// 判断标记中
	private boolean ifing = false;

	// 函数标记中
	private boolean functioning = false;

	// 分支标记
	private boolean esleflag = false;

	private boolean backIfBool = false;

	private String executeCommand;

	private String nowPosFlagName;

	private boolean addCommand;

	private boolean isInnerCommand;

	private boolean isRead;

	private boolean isCall;

	private boolean isCache;

	private boolean if_bool;

	private boolean elseif_bool;

	private Command innerCommand;

	private String commandString;

	private List temps;

	private List printTags;

	private List randTags;

	private int scriptSize;

	private int offsetPos;

	// 脚本数据列表
	private List scriptList;

	// 脚本名
	private String scriptName;

	/**
	 * 构造函数，载入指定脚本文件
	 * 
	 * @param fileName
	 */
	public Command(String fileName) {
		formatCommand(fileName);
	}

	/**
	 * 构造函数，载入指定list脚本
	 * 
	 * @param resource
	 */
	public Command(String fileName, List resource) {
		formatCommand("function", resource);
	}

	public void formatCommand(String fileName) {
		formatCommand(fileName, Command.includeFile(fileName));
	}

	public void formatCommand(String name, List resource) {
		setEnvironmentList.clear();
		conditionEnvironmentList.clear();
		// 默认选择项
		setEnvironmentList.put(V_SELECT_KEY, "-1");
		scriptName = name;
		scriptList = resource;
		scriptSize = scriptList.size();
		offsetPos = 0;
		flaging = false;
		ifing = false;
		isCache = true;
		esleflag = false;
		backIfBool = false;
	}

	private boolean setupIF(String commandString, String nowPosFlagName,
			Map setEnvironmentList, Map conditionEnvironmentList) {
		boolean result = false;
		conditionEnvironmentList.put(nowPosFlagName, new Boolean(false));
		try {
			List temps = commandSplit(commandString);
			Object valueA = (String) temps.get(1);
			Object valueB = (String) temps.get(3);
			valueA = setEnvironmentList.get(valueA) == null ? valueA
					: setEnvironmentList.get(valueA);
			valueB = setEnvironmentList.get(valueB) == null ? valueB
					: setEnvironmentList.get(valueB);

			// 非纯数字
			if (!isNumber(valueB)) {
				try {
					// 尝试四则运算公式匹配
					valueB = compute.parse(valueB);
				} catch (Exception e) {
				}
			}
			String condition = (String) temps.get(2);

			// 无法判定
			if (valueA == null || valueB == null) {
				conditionEnvironmentList
						.put(nowPosFlagName, new Boolean(false));
			}

			// 相等
			if ("==".equals(condition)) {

				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = valueA.toString().equals(valueB.toString())));
				// 非等
			} else if ("!=".equals(condition)) {
				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = !valueA.toString().equals(valueB.toString())));
				// 大于
			} else if (">".equals(condition)) {
				int numberA = Integer.parseInt(valueA.toString());
				int numberB = Integer.parseInt(valueB.toString());
				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = numberA > numberB));
				// 小于
			} else if ("<".equals(condition)) {
				int numberA = Integer.parseInt(valueA.toString());
				int numberB = Integer.parseInt(valueB.toString());
				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = numberA < numberB));

				// 大于等于
			} else if (">=".equals(condition)) {
				int numberA = Integer.parseInt(valueA.toString());
				int numberB = Integer.parseInt(valueB.toString());
				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = numberA >= numberB));
				// 小于等于
			} else if ("<=".equals(condition)) {
				int numberA = Integer.parseInt(valueA.toString());
				int numberB = Integer.parseInt(valueB.toString());
				conditionEnvironmentList.put(nowPosFlagName, new Boolean(
						result = numberA <= numberB));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 打开脚本缓存
	 * 
	 */
	public void openCache() {
		isCache = true;
	}

	/**
	 * 关闭脚本缓存
	 * 
	 */
	public void closeCache() {
		isCache = false;
	}

	/**
	 * 当前脚本行缓存名
	 * 
	 * @return
	 */
	public String nowCacheOffsetName() {
		return (scriptName + FLAG + offsetPos + FLAG + commandString)
				.toLowerCase();
	}

	/**
	 * 重启脚本缓存
	 * 
	 */
	public static void resetCache() {
		cacheScript.clear();
	}

	public boolean isRead() {
		return isRead;
	}

	public void setRead(boolean isRead) {
		this.isRead = isRead;
	}

	/**
	 * 返回当前的读入数据集合
	 * 
	 * @return
	 */
	public synchronized String[] getReads() {
		String result = reader.toString();
		result = result.replaceAll(SELECTS_TAG, "");
		return split(result, FLAG);
	}

	/**
	 * 返回指定索引的读入数据
	 * 
	 * @param index
	 * @return
	 */
	public synchronized String getRead(int index) {
		try {
			return getReads()[index];
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 截取第一次出现的指定标记
	 * 
	 * @param messages
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static String getNameTag(String messages, String startString,
			String endString) {
		List results = getNameTags(messages, startString, endString);
		return (results == null || results.size() == 0) ? null
				: (String) results.get(0);
	}

	/**
	 * 截取指定标记内容为list
	 * 
	 * @param messages
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static List getNameTags(String messages, String startString,
			String endString) {
		return Command.getNameTags(messages.toCharArray(), startString
				.toCharArray(), endString.toCharArray());
	}

	/**
	 * 截取指定标记内容为list
	 * 
	 * @param messages
	 * @param startString
	 * @param endString
	 * @return
	 */
	public static List getNameTags(char[] messages, char[] startString,
			char[] endString) {
		int dlength = messages.length;
		int slength = startString.length;
		int elength = endString.length;
		List tagList = new ArrayList(10);
		boolean lookup = false;
		int lookupStartIndex = 0;
		int lookupEndIndex = 0;
		int length;
		StringBuffer sbr = new StringBuffer(100);
		for (int i = 0; i < dlength; i++) {
			char tag = messages[i];
			if (tag == startString[lookupStartIndex]) {
				lookupStartIndex++;
			}
			if (lookupStartIndex == slength) {
				lookupStartIndex = 0;
				lookup = true;
			}
			if (lookup) {
				sbr.append(tag);
			}
			if (tag == endString[lookupEndIndex]) {
				lookupEndIndex++;
			}
			if (lookupEndIndex == elength) {
				lookupEndIndex = 0;
				lookup = false;
				length = sbr.length();
				if (length > 0) {
					tagList.add(sbr.substring(1, sbr.length() - elength));
					sbr.delete(0, length);
				}
			}
		}
		return tagList;
	}

	/**
	 * 注入选择变量
	 * 
	 * @param type
	 */
	public void select(int type) {
		if (innerCommand != null) {
			innerCommand.setVariable(V_SELECT_KEY, String.valueOf(type));
		}
		setVariable(V_SELECT_KEY, String.valueOf(type));
	}

	public String getSelect() {
		return (String) getVariable(V_SELECT_KEY);
	}

	/**
	 * 插入变量
	 * 
	 * @param key
	 * @param value
	 */
	public void setVariable(String key, Object value) {
		setEnvironmentList.put(key, value);
	}

	/**
	 * 插入变量集合
	 * 
	 * @param vars
	 */
	public void setVariables(Map vars) {
		setEnvironmentList.putAll(vars);
	}

	/**
	 * 返回变量集合
	 * 
	 * @return
	 */
	public Map getVariables() {
		return setEnvironmentList;
	}

	public Object getVariable(String key) {
		return setEnvironmentList.get(key);
	}

	/**
	 * 删除变量
	 * 
	 * @param key
	 */
	public void removeVariable(String key) {
		setEnvironmentList.remove(key);
	}

	/**
	 * 判定脚本是否允许继续解析
	 * 
	 * @return
	 */
	public boolean next() {
		return (offsetPos < scriptSize);
	}

	/**
	 * 跳转向指定索引位置
	 * 
	 * @param offset
	 * @return
	 */
	public boolean gotoIndex(final int offset) {
		boolean result = offset < scriptSize && offset > 0
				&& offset != offsetPos;
		if (result) {
			offsetPos = offset;
		}
		return result;
	}

	/**
	 * 批处理执行脚本，并返回可用list结果
	 * 
	 * @return
	 */
	public List batchToList() {
		List reslist = new ArrayList(scriptSize);
		for (; next();) {
			String execute = doExecute();
			if (execute != null) {
				reslist.add(execute);
			}
		}
		return reslist;
	}

	/**
	 * 批处理执行脚本，并返回可用string结果
	 * 
	 * @return
	 */
	public String batchToString() {
		StringBuffer resString = new StringBuffer(scriptSize * 10);
		for (; next();) {
			String execute = doExecute();
			if (execute != null) {
				resString.append(execute);
				resString.append("\n");
			}
		}
		return resString.toString();
	}

	private void setupSET() {
		if (commandString.startsWith(SET_TAG)) {
			List temps = commandSplit(commandString);
			int len = temps.size();
			String result = null;
			if (len == 4) {
				result = temps.get(3).toString();
			} else if (len > 4) {
				StringBuffer sbr = new StringBuffer(len);
				for (int i = 3; i < temps.size(); i++) {
					sbr.append(temps.get(i));
				}
				result = sbr.toString();
			}

			if (result != null) {
				// 替换已有变量字符
				Set set = setEnvironmentList.entrySet();
				for (Iterator it = set.iterator(); it.hasNext();) {
					Entry entry = (Entry) it.next();
					result = replaceMatch(result, (String) entry.getKey(),
							entry.getValue().toString());
				}
				// 当为普通字符串时
				if (result.startsWith("\"") && result.endsWith("\"")) {
					setEnvironmentList.put(temps.get(1), result.substring(1,
							result.length() - 1));
				} else if (isChinese(result) || isEnglishAndNumeric(result)) {
					setEnvironmentList.put(temps.get(1), result);
				} else {
					// 当为数学表达式时
					setEnvironmentList.put(temps.get(1), compute.parse(result));

				}
			}
			addCommand = false;
		}

	}

	/**
	 * 随机数处理
	 * 
	 */
	private void setupRandom() {
		// 随机数判定
		if (commandString.indexOf(RAND_TAG) != -1) {
			randTags = Command.getNameTags(commandString, RAND_TAG
					+ BRACKET_LEFT_TAG, BRACKET_RIGHT_TAG);
			if (randTags != null) {
				for (Iterator it = randTags.iterator(); it.hasNext();) {
					String key = (String) it.next();
					Object value = setEnvironmentList.get(key);
					// 已存在变量
					if (value != null) {
						commandString = Command
								.replaceMatch(
										commandString,
										(RAND_TAG + BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
												.intern(), value.toString());
						// 设定有随机数生成范围
					} else if (isNumber(key)) {
						commandString = Command
								.replaceMatch(
										commandString,
										(RAND_TAG + BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
												.intern(),
										String
												.valueOf(GLOBAL_RAND
														.nextInt(Integer
																.parseInt((String) key))));
						// 无设定
					} else {
						commandString = Command
								.replaceMatch(
										commandString,
										(RAND_TAG + BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
												.intern(), String
												.valueOf(GLOBAL_RAND.nextInt()));
					}
				}
			}
		}
	}

	private void innerCallTrue() {
		isCall = true;
		isInnerCommand = true;
	}

	private void innerCallFalse() {
		isCall = false;
		isInnerCommand = false;
		innerCommand = null;
	}

	/**
	 * 逐行执行脚本命令
	 * 
	 * @return
	 */
	public synchronized String doExecute() {

		executeCommand = null;

		addCommand = true;

		isInnerCommand = (innerCommand != null);

		if_bool = false;

		elseif_bool = false;

		try {
			// 执行call命令
			if (isInnerCommand && isCall) {
				setVariables(innerCommand.getVariables());
				if (innerCommand.next()) {
					return innerCommand.doExecute();
				} else {
					innerCallFalse();
					return executeCommand;
				}
				// 执行内部脚本
			} else if (isInnerCommand && !isCall) {
				setVariables(innerCommand.getVariables());
				if (innerCommand.next()) {
					return innerCommand.doExecute();
				} else {
					innerCommand = null;
					isInnerCommand = false;
					offsetPos++;
					return executeCommand;
				}
			}

			nowPosFlagName = String.valueOf(offsetPos);
			int length = conditionEnvironmentList.size();
			if (length > 0) {
				Object ifResult = conditionEnvironmentList.get(length - 1);
				if (ifResult != null) {
					backIfBool = ((Boolean) ifResult).booleanValue();
				}
			}

			// 获得全行命令
			commandString = ((String) scriptList.get(offsetPos));
			// 清空脚本缓存
			if (commandString.startsWith(RESET_CACHE_TAG)) {
				resetCache();
				return executeCommand;
			}

			if (isCache) {
				// 获得缓存命令行名
				cacheCommandName = nowCacheOffsetName();
				// 读取缓存的脚本
				Object cache = cacheScript.get(cacheCommandName);
				if (cache != null) {
					return (String) cache;
				}
			}

			// 注释中
			if (flaging) {
				flaging = !(commandString.startsWith(FLAG_LS_E_TAG) || commandString
						.endsWith(FLAG_LS_E_TAG));
				return executeCommand;
			}

			if (!flaging) {
				// 全局注释
				if (commandString.startsWith(FLAG_LS_B_TAG)
						&& !commandString.endsWith(FLAG_LS_E_TAG)) {
					flaging = true;
					return executeCommand;
				} else if (commandString.startsWith(FLAG_LS_B_TAG)
						&& commandString.endsWith(FLAG_LS_E_TAG)) {
					return executeCommand;
				}
			}

			// 执行随机数标记
			setupRandom();

			// 执行获取变量标记
			setupSET();

			// 结束脚本中代码段标记
			if (commandString.endsWith(END_TAG)) {
				functioning = false;
				return executeCommand;
			}

			// 标注脚本中代码段标记
			if (commandString.startsWith(BEGIN_TAG)) {
				temps = commandSplit(commandString);
				if (temps.size() == 2) {
					functioning = true;
					functions.put(temps.get(1), new ArrayList(10));
					return executeCommand;
				}
			}

			// 开始记录代码段
			if (functioning) {
				ArrayList function = (ArrayList) functions
						.get(functions.size() - 1);
				function.add(commandString);
				return executeCommand;
			}

			// 执行代码段调用标记
			if (commandString.startsWith(CALL_TAG) && !isCall) {
				temps = commandSplit(commandString);
				if (temps.size() == 2) {
					String functionName = (String) temps.get(1);
					List funs = (ArrayList) functions.get(functionName);
					if (funs != null) {
						innerCommand = new Command(scriptName + FLAG
								+ functionName, funs);
						innerCommand.closeCache();
						innerCommand.setVariables(getVariables());
						innerCallTrue();
						return null;
					}
				}
			}

			if (!if_bool && !elseif_bool) {
				// 获得循序结构条件
				if_bool = commandString.startsWith(IF_TAG);
				elseif_bool = commandString.startsWith(ELSE_TAG);
			}
			// 条件判断a
			if (if_bool) {
				esleflag = setupIF(commandString, nowPosFlagName,
						setEnvironmentList, conditionEnvironmentList);
				addCommand = false;
				ifing = true;
				// 条件判断b
			} else if (elseif_bool) {
				String[] value = split(commandString, " ");
				if (!backIfBool && !esleflag) {
					// 存在if判断
					if (value.length > 1 && IF_TAG.equals(value[1])) {
						esleflag = setupIF(commandString.replaceAll(ELSE_TAG,
								"").trim(), nowPosFlagName, setEnvironmentList,
								conditionEnvironmentList);
						addCommand = false;
					}
				} else {
					addCommand = false;
					conditionEnvironmentList.put(nowPosFlagName, new Boolean(
							false));
				}

			}
			// 分支结束
			if (commandString.startsWith(IF_END_TAG)) {
				conditionEnvironmentList.clear();
				backIfBool = false;
				addCommand = false;
				ifing = false;
				if_bool = false;
				elseif_bool = false;
				return null;
			}
			if (backIfBool) {
				// 加载内部脚本
				if (commandString.startsWith(INCLUDE_TAG)) {
					temps = commandSplit(commandString);
					String fileName = (String) temps.get(1);
					if (fileName != null) {
						innerCommand = new Command(fileName);
						isInnerCommand = true;
						return null;
					}
				}
			}
			// 选择项列表结束
			if (commandString.startsWith(OUT_TAG)) {
				isRead = false;
				addCommand = false;
				executeCommand = (SELECTS_TAG + " " + reader.toString())
						.intern();
			}
			// 累计选择项
			if (isRead) {
				reader.append(commandString);
				reader.append(FLAG);
				addCommand = false;
			}
			// 选择项列表
			if (commandString.startsWith(IN_TAG)) {
				reader.delete(0, reader.length());
				isRead = true;
				return executeCommand;
			}

			// 输出脚本判断
			if (addCommand && ifing) {
				if (backIfBool && esleflag) {
					executeCommand = commandString;
				}

			} else if (addCommand) {
				executeCommand = commandString;
			}

			// 替换脚本字符串内容
			if (executeCommand != null) {
				printTags = Command.getNameTags(executeCommand, PRINT_TAG
						+ BRACKET_LEFT_TAG, BRACKET_RIGHT_TAG);
				if (printTags != null) {
					for (Iterator it = printTags.iterator(); it.hasNext();) {
						String key = (String) it.next();
						Object value = setEnvironmentList.get(key);
						if (value != null) {
							executeCommand = Command
									.replaceMatch(
											executeCommand,
											(PRINT_TAG + BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
													.intern(), value.toString());
						} else {
							executeCommand = Command
									.replaceMatch(
											executeCommand,
											(PRINT_TAG + BRACKET_LEFT_TAG + key + BRACKET_RIGHT_TAG)
													.intern(), key);
						}

					}

				}

				if (isCache) {
					// 注入脚本缓存
					cacheScript.put(cacheCommandName, executeCommand);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (!isInnerCommand) {
				offsetPos++;
			}
		}
		return executeCommand;
	}

	/**
	 * 包含指定脚本内容
	 * 
	 * @param fileName
	 * @return
	 */
	private static List includeFile(String fileName) {
		InputStream in = null;
		BufferedReader reader = null;
		List result = new ArrayList(1000);
		try {
			in = Resources.getResourceAsStream(fileName);
			reader = new BufferedReader(new InputStreamReader(in,
					LSystem.encoding));
			String record = null;
			while ((record = reader.readLine()) != null) {
				record = record.trim();
				if (record.length() > 0) {
					if (!(record.startsWith(FLAG_L_TAG)
							|| record.startsWith(FLAG_C_TAG) || record
							.startsWith(FLAG_I_TAG))) {
						result.add(record);
					}
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	/**
	 * 过滤指定脚本文件内容为list
	 * 
	 * @param src
	 * @return
	 */
	public static List commandSplit(final String src) {
		String[] cmds;
		String result = src.trim();
		result = result.replaceAll("\r", "");
		result = (FLAG + result).intern();
		result = result.replaceAll(" ", FLAG);
		result = result.replaceAll("\t", FLAG);
		result = result.replaceAll("<=", (FLAG + "<=" + FLAG).intern());
		result = result.replaceAll(">=", (FLAG + ">=" + FLAG).intern());
		result = result.replaceAll("==", (FLAG + "==" + FLAG).intern());
		result = result.replaceAll("!=", (FLAG + "!=" + FLAG).intern());
		if (result.indexOf("<=") == -1) {
			result = result.replaceAll("<", (FLAG + "<" + FLAG).intern());
		}
		if (result.indexOf(">=") == -1) {
			result = result.replaceAll(">", (FLAG + ">" + FLAG).intern());
		}
		result = result.replaceAll((FLAG + "{2,}").intern(), FLAG);
		result = result.substring(1);
		cmds = result.split(FLAG);
		return Arrays.asList(cmds);
	}

}