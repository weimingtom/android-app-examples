package org.loon.framework.android.game.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
public abstract class CompressionUtils {

	/**
	 * 直接从Zip文件中读取所需数据，并返回byte[]
	 * 
	 * @param fileName
	 * @param fname
	 * @return
	 */
	public static byte[] readBytesFormZipFile(String fileName, String fname) {
		byte[] bytes = null;
		ByteArrayOutputStream os = null;
		ZipInputStream in = null;
		try {
			os = new ByteArrayOutputStream(4096);
			in = new ZipInputStream(new FileInputStream(fileName));
			boolean found = false;
			while (!found) {
				ZipEntry entry = in.getNextEntry();
				if (fname.equalsIgnoreCase(entry.getName())) {
					found = true;
				}
			}
			int read;
			byte[] buffer = new byte[4096];
			while ((read = in.read(buffer)) >= 0) {
				os.write(buffer, 0, read);
			}
			bytes = os.toByteArray();
		} catch (Exception e) {
			bytes = null;
		} finally {
			try {
				if (os != null) {
					os.flush();
					os = null;
				}
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (Exception e) {
			}
		}

		return bytes;
	}

	/**
	 * 压缩指定byte[]文件
	 * 
	 * @param bytes
	 * @return
	 */
	public static final byte[] compress(byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException("byte[] is NULL !");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
		try {
			GZIPOutputStream gzip = new GZIPOutputStream(bos);
			gzip.write(bytes, 0, bytes.length);
			gzip.finish();
			byte[] fewerBytes = bos.toByteArray();
			gzip.close();
			bos.close();
			gzip = null;
			bos = null;
			return fewerBytes;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 解压缩指定byte[]
	 * 
	 * @param bytes
	 * @return
	 */
	public static final byte[] uncompress(final byte[] bytes) {
		if (bytes == null) {
			throw new NullPointerException("byte[] is NULL !");
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8192);
		byte[] buffer = new byte[bytes.length];
		int length;
		try {
			GZIPInputStream gis = new GZIPInputStream(bais);
			while ((length = gis.read(buffer)) != -1) {
				bos.write(buffer, 0, length);
			}
			byte[] moreBytes = bos.toByteArray();
			bos.close();
			bais.close();
			gis.close();
			bos = null;
			bais = null;
			gis = null;
			return moreBytes;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 解压缩指定zip文件
	 * 
	 * @param zipFile
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public void decompressFile(File zipFile) throws FileNotFoundException,
			IOException

	{
		BufferedOutputStream dest = null;
		FileInputStream fis = new FileInputStream(zipFile);
		ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
		String basePath = zipFile.getAbsolutePath().substring(0,
				zipFile.getAbsolutePath().lastIndexOf(File.separatorChar));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			if (entry.isDirectory()) {
				File folderFile = new File(basePath + "\\" + entry.getName());
				folderFile.mkdirs();
				continue;
			}
			int count;
			byte data[] = new byte[8192];
			FileOutputStream fos = new FileOutputStream(basePath
					+ File.separatorChar + entry.getName());
			dest = new BufferedOutputStream(fos, 8192);
			while ((count = zis.read(data, 0, 8192)) != -1) {
				dest.write(data, 0, count);
			}
			dest.flush();
			dest.close();
		}
		zis.close();
	}

	/**
	 * 解压缩指定文件
	 * 
	 * @param fileName
	 */
	public void decompressFile(String fileName) {
		try {
			decompressFile(new File(fileName));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
