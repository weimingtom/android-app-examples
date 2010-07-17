package org.loon.framework.android.game.command;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.StringTokenizer;

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
public abstract class Conversion implements Expression {

	final static private String ops[] = { "\\+", "\\-", "\\*", "\\/", "\\(",
			"\\)" };

	/**
	 * 是否为字母与数字混合
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isEnglishAndNumeric(String value) {
		if (value == null || value.trim().length() == 0)
			return true;
		for (int i = 0; i < value.length(); i++) {
			char letter = value.charAt(i);
			if ((97 > letter || letter > 122) && (65 > letter || letter > 90)
					&& (48 > letter || letter > 57))
				return false;
		}

		return true;
	}

	/**
	 * 分解字符
	 * 
	 * @param string
	 * @param tag
	 * @return
	 */
	public static String[] split(final String string, final String tag) {
		StringTokenizer str = new StringTokenizer(string, tag);
		String[] result = new String[str.countTokens()];
		int index = 0;
		for (; str.hasMoreTokens();) {
			result[index++] = str.nextToken();
		}
		return result;
	}

	/**
	 * 分解字符,并返回为list
	 * 
	 * @param string
	 * @param tag
	 * @return
	 */
	public static List splitToList(final String string, final String tag) {
		return Arrays.asList(split(string, tag));
	}

	/**
	 * 是否数字
	 * 
	 * @param value
	 * @return
	 */
	public static boolean isNumber(Object value) {
		try {
			Integer.parseInt((String) value);
		} catch (NumberFormatException ne) {
			return false;
		}
		return true;
	}

	/**
	 * 是否汉字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(Object value) {
		boolean result = false;
		try {
			char[] chars = ((String) value).toCharArray();
			for (int i = 0; i < chars.length; i++) {
				byte[] bytes = ("" + chars[i]).getBytes();
				if (bytes.length == 2) {
					int[] ints = new int[2];
					ints[0] = bytes[0] & 0xff;
					ints[1] = bytes[1] & 0xff;
					if (ints[0] >= 0x81 && ints[0] <= 0xFE && ints[1] >= 0x40
							&& ints[1] <= 0xFE) {
						result = true;
						break;
					}
				}
			}
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * 四则运算
	 * 
	 * @param flag
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Integer operate(String flag, String v1, String v2) {
		return operate(flag.toCharArray()[0], new Integer(v1).intValue(),
				new Integer(v2).intValue());
	}

	/**
	 * 四则运算
	 * 
	 * @param flag
	 * @param v1
	 * @param v2
	 * @return
	 */
	public static Integer operate(char flag, int v1, int v2) {
		switch (flag) {
		case '+':
			return new Integer(v1 + v2);
		case '-':
			return new Integer(v1 + (v2 > 0 ? -v2 : +v2));
		case '*':
			return new Integer(v1 * v2);
		case '/':
			return new Integer(v1 / v2);
		}
		return new Integer(0);
	}

	/**
	 * 替换指定字符
	 * 
	 * @param line
	 * @param oldString
	 * @param newString
	 * @return
	 */
	protected static String replaceMatch(String line, String oldString,
			String newString) {
		int i = 0;
		int j = 0;
		if ((i = line.indexOf(oldString, i)) >= 0) {
			char line2[] = line.toCharArray();
			char newString2[] = newString.toCharArray();
			int oLength = oldString.length();
			StringBuffer buffer = new StringBuffer(line2.length);
			buffer.append(line2, 0, i).append(newString2);
			i += oLength;
			for (j = i; (i = line.indexOf(oldString, i)) > 0; j = i) {
				buffer.append(line2, j, i - j).append(newString2);
				i += oLength;
			}
			buffer.append(line2, j, line2.length - j);
			return buffer.toString();
		} else {
			return line;
		}
	}

	// 数学表达式运算类
	protected Compute compute = new Compute();

	/**
	 * 四则表达式运算
	 */
	class Compute {

		private int sort(String flag) {
			return sort(flag.toCharArray()[0]);
		}

		/**
		 * 排序优先
		 * 
		 * @param flag
		 * @return
		 */
		private int sort(char flag) {
			int result = 0;
			switch (flag) {
			case '+':
				result = 0;
				break;
			case '-':
				result = 0;
				break;
			case '*':
				result = 1;
				break;
			case '/':
				result = 1;
				break;
			default:
				result = -1;
			}
			return result;
		}

		/**
		 * 验证是否为四则运
		 * 
		 * @param exp
		 * @return
		 */
		private boolean exp(String exp) {
			return exp.indexOf("+") != -1 || exp.indexOf("-") != -1
					|| exp.indexOf("*") != -1 || exp.indexOf("/") != -1;
		}

		/**
		 * 解析表达
		 * 
		 * @param exp
		 * @return
		 */
		public Integer parse(Object exp) {
			return parse(exp.toString());
		}

		/**
		 * 解析表达
		 * 
		 * @param exp
		 * @return
		 */
		public Integer parse(String exp) {
			int endIndex = 0;
			int startIndex = exp.indexOf("(", endIndex);
			for (; startIndex != -1;) {
				endIndex = exp.indexOf(")", startIndex) + 1;
				String segment = exp.substring(startIndex, endIndex);
				Integer tResult = match(segment.replaceAll("(", "").replaceAll(
						")", ""));
				exp = exp.replaceAll(segment, String.valueOf(tResult));
				startIndex = exp.indexOf("(", 0);
			}
			return match(exp);
		}

		/**
		 * 自动匹配四则运算表达式，并返回
		 * 
		 * @param exp
		 * @return
		 */
		private Integer match(String exp) {
			if (!isNumber(exp.substring(0, 1))) {
				exp = ("0" + exp).intern();
			}
			for (int i = 0; i < ops.length; i++) {
				String operator = ops[i];
				exp = exp.replaceAll(operator, (FLAG + operator + FLAG)
						.intern());
			}
			String v1 = null;
			String v2 = null;
			Stack stack = new Stack();
			String exps[] = Conversion.split(exp, FLAG);
			String type = null;
			int sort1 = -1;
			int sort2 = -1;
			for (int i = 0; i < exps.length; i++) {
				if (exp(exps[i])) {
					if (type == null) {
						type = exps[i];
					} else {
						sort1 = sort(type);
						sort2 = sort(exps[i]);
						if (sort1 >= sort2) {
							v2 = String.valueOf(((type.indexOf("+") != -1)
									|| (type.indexOf("/") != -1)
									|| (type.indexOf("*") != -1) ? "" : type)
									+ stack.pop());
							v1 = String.valueOf(stack.pop());
							stack.push(Conversion.operate(type, v1, v2));
							type = exps[i];
						} else if (sort1 < sort2) {
							v1 = String.valueOf((stack.pop()));
							v2 = String.valueOf(exps[i + 1]);
							stack.push(operate(exps[i], v1, v2));
							i++;
						}
					}
				} else {
					stack.push(exps[i]);
				}
			}
			v1 = String.valueOf(stack.pop());
			v2 = String.valueOf(stack.pop());
			return operate(type, v2, v1);
		}
	}
}