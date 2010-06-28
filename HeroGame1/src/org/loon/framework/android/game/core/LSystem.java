package org.loon.framework.android.game.core;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Random;

import org.loon.framework.android.game.LGameActivity;
import org.loon.framework.android.game.LGameView;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.core.timer.NanoTimer;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.StringUtils;

import android.app.Activity;
import android.view.View;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class LSystem {

	// 框架名
	final static public String FRAMEWORK = "LGame-Android";

	// 秒
	final static public long SECOND = 1000;

	// 分
	final static public long MINUTE = SECOND * 60;

	// 小时
	final static public long HOUR = MINUTE * 60;

	// 天
	final static public long DAY = HOUR * 24;

	// 周
	final static public long WEEK = DAY * 7;

	// 理论上一年
	final static public long YEAR = DAY * 365;

	// 行分隔符
	final static public String LS = System.getProperty("line.separator", "\n");

	// 文件分割符
	final static public String FS = System.getProperty("file.separator", "\\");

	// 随机数
	final static public Random random = new Random();

	final static public int DEFAULT_MAX_CACHE_SIZE = 60;

	final static public String encoding = "UTF-8";

	final static public int FONT_TYPE = 15;

	final static public int FONT_SIZE = 1;

	final static public String FONT_NAME = "Serif";

	final static public int DEFAULT_MAX_FPS = 50;

	private static Handler handler;

	private static String temp_file;

	public static void destroy() {
		GraphicsUtils.destroyImages();
		Resources.destroy();
		LSystem.gc();
	}

	public static void exit() {
		synchronized (handler) {
			try {
				if (handler != null) {
					((LGameView) handler.getView()).setRunning(false);
					handler.getLGameActivity().finish();
				}
			} catch (Exception e) {
			}
		}
	}

	public static int getRandom(int i, int j) {
		return i + random.nextInt((j - i) + 1);
	}

	public static SystemTimer getSystemTimer() {
		return new NanoTimer();
	}

	public static float getRadian(float degree) {
		return (float) ((Math.PI / 180) * degree);
	}

	public static float getDegree(float radian) {
		return (float) ((180 / Math.PI) * radian);
	}

	public static float getEllipsisX(float degree, float princAxis) {
		return (float) Math.cos(degree) * princAxis;
	}

	public static float getEllipsisY(float degree, float conAxis) {
		return (float) Math.sin(degree) * conAxis;
	}

	public static void setupHandler(LGameActivity activity, View view,
			boolean landscape) {
		handler = new Handler(activity, view, landscape);
	}

	public static String getVersionName() {
		if (handler != null) {
			return handler.getLGameActivity().getVersionName();
		}
		return null;
	}

	public static File getCacheFile() {
		return handler.getLGameActivity().getCacheDir();
	}

	public static File getCacheFile(String fileName) {
		final String file = LSystem.getCacheFileName();
		fileName = StringUtils.replaceIgnoreCase(fileName, "\\", "/");
		if (file != null) {
			if (fileName.startsWith("/") || fileName.startsWith("\\")) {
				fileName = fileName.substring(1, fileName.length());
			}
			if (file.endsWith("/") || file.endsWith("\\")) {
				return new File(LSystem.getCacheFileName() + fileName);
			} else {
				return new File(LSystem.getCacheFileName() + LSystem.FS
						+ fileName);
			}
		} else {
			return new File(fileName);
		}
	}

	public static String getCacheFileName() {
		if (temp_file == null) {
			Activity activity = LSystem.getSystemHandler().getLGameActivity();
			if (activity != null) {
				temp_file = activity.getCacheDir().getAbsolutePath();
			}
		}
		return temp_file;
	}

	public static Handler getSystemHandler() {
		return handler;
	}

	/**
	 * 申请回收系统资源
	 * 
	 */
	final public static void gc() {
		System.gc();
	}

	/**
	 * 以指定范围内的指定概率执行gc
	 * 
	 * @param size
	 * @param rand
	 */
	final public static void gc(final int size, final long rand) {
		if (rand > size) {
			throw new RuntimeException(
					("GC random probability " + rand + " > " + size).intern());
		}
		if (LSystem.random.nextInt(size) <= rand) {
			LSystem.gc();
		}
	}

	/**
	 * 以指定概率使用gc回收系统资源
	 * 
	 * @param rand
	 */
	final public static void gc(final long rand) {
		gc(100, rand);
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param buffer
	 * @return
	 */
	public static final InputStream read(byte[] buffer) {
		return new BufferedInputStream(new ByteArrayInputStream(buffer));
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param file
	 * @return
	 */
	public static final InputStream read(File file) {
		try {
			return new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	/**
	 * 读取指定文件到InputStream
	 * 
	 * @param fileName
	 * @return
	 */
	public static final InputStream read(String fileName) {
		return read(new File(fileName));
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 */
	final public static void loadPropertiesFileToSystem(final File file) {
		Properties properties = System.getProperties();
		InputStream in = LSystem.read(file);
		loadProperties(properties, in, file.getName());
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param file
	 * @return
	 */
	final public static Properties loadPropertiesFromFile(final File file) {
		if (file == null) {
			return null;
		}
		Properties properties = new Properties();
		try {
			InputStream in = LSystem.read(file);
			loadProperties(properties, in, file.getName());
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return properties;
	}

	/**
	 * 加载Properties文件
	 * 
	 * @param properties
	 * @param inputStream
	 * @param fileName
	 */
	private static void loadProperties(Properties properties,
			InputStream inputStream, String fileName) {
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				throw new RuntimeException(
						("error closing input stream from file " + fileName
								+ ", ignoring , " + e.getMessage()).intern());
			}
		}
	}

	/**
	 * 写入整型数据到OutputStream
	 * 
	 * @param out
	 * @param number
	 */
	public final static void writeInt(final OutputStream out, final int number) {
		byte[] bytes = new byte[4];
		try {
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) ((number >> (i * 8)) & 0xff);
			}
			out.write(bytes);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * 从InputStream中获得整型数据
	 * 
	 * @param in
	 * @return
	 */
	final static public int readInt(final InputStream in) {
		int data = -1;
		try {
			data = (in.read() & 0xff);
			data |= ((in.read() & 0xff) << 8);
			data |= ((in.read() & 0xff) << 16);
			data |= ((in.read() & 0xff) << 24);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		return data;
	}

}
