package org.loon.framework.android.game.extend.db.type;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.loon.framework.android.game.extend.db.Access;
import org.loon.framework.android.game.extend.db.Serializer;
import org.loon.framework.android.game.utils.FileUtils;

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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class TypeUtils {

	final static public Serializer STRING = TypeUtils
			.switchSerializer(TypeBase.STRING);

	final static public Serializer OBJECT = TypeUtils
			.switchSerializer(TypeBase.STRING);

	final static public Serializer NULL = TypeUtils
			.switchSerializer(TypeBase.NULL);

	final static public Serializer INTEGER = TypeUtils
			.switchSerializer(TypeBase.INTEGER);

	final static public Serializer BYTES = TypeUtils
			.switchSerializer(TypeBase.BYTES);

	final static public Serializer LONG = TypeUtils
			.switchSerializer(TypeBase.LONG);

	// crc32参数表，共有256项
	final static private long[] crcTable = new long[256];

	// 静态初始化参数表
	static {
		long crc;
		int n, k;
		for (n = 0; n < 256; n++) {
			crc = (long) n;
			for (k = 0; k < 8; k++) {
				if ((crc & 1) == 1) {
					crc = 0xEDB88320L ^ (crc >> 1);
				} else {
					crc = crc >> 1;
				}
			}
			crcTable[n] = crc;
		}
	}

	/**
	 * 压缩序列化对象为Byte[]
	 * 
	 * @param obj
	 * @param isCompress
	 * @return
	 */
	public static byte[] getCompressBytes(final Object obj, boolean isCompress) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		ObjectOutputStream out;
		byte[] buffer = null;
		try {
			BufferedOutputStream os = new BufferedOutputStream(bout);
			out = new ObjectOutputStream(os);
			out.writeObject(obj);
			os.flush();
			out.flush();
			buffer = bout.toByteArray();
			bout.close();
			os.close();
			out.close();
		} catch (IOException e) {
			throw new RuntimeException("getCompressBytes an exception !", e);
		}
		if (isCompress) {
			buffer = FileUtils.compress(buffer);
		}
		return buffer;
	}

	/**
	 * 返回序列化对象为Byte[]
	 * 
	 * @param obj
	 * @return
	 */
	public static byte[] getBytes(final Object obj) {
		return getCompressBytes(obj, false);
	}

	/**
	 * 解压序列化对象
	 * 
	 * @param buffer
	 * @param isDecompress
	 * @return
	 */
	public static Object getDecompressObject(final byte[] buffer,
			final boolean isDecompress) {
		if (buffer == null) {
			return null;
		}
		ByteArrayInputStream bi;
		bi = new ByteArrayInputStream(isDecompress ? FileUtils
				.uncompress(buffer) : buffer);
		ObjectInputStream oi;
		Object obj = null;
		try {
			oi = new ObjectInputStream(new BufferedInputStream(bi));
			obj = oi.readObject();
			bi.close();
			oi.close();
		} catch (Exception e) {
			throw new RuntimeException("getDecompressObject an exception !", e);
		}
		return obj;
	}

	/**
	 * 获得序列化对象
	 * 
	 * @param bytes
	 * @return
	 */
	public static Object getObject(final byte[] bytes) {
		return getDecompressObject(bytes, false);
	}

	/**
	 * 根据密码异或文本数据库数据字符串，简单加密（设定加密文本长度限制为 1 - 8193，过长过短不处理）
	 * 
	 * @param data
	 * @param pass
	 */
	final static public void xorData(final byte[] data, final byte[] passBytes) {
		int dlength = data.length;
		if (dlength > 1 && dlength < 8193) {
			int len = passBytes.length;
			for (int i = 0; i < dlength; i++) {
				data[i] ^= passBytes[i % len];
			}
		}
	}

	/**
	 * 写入2字节整型数据
	 * 
	 * @param access
	 * @param number
	 */
	final static public void writeWORD(final Access access, final int number) {
		byte[] bytes = new byte[2];
		try {
			for (int i = 0; i < 2; i++) {
				bytes[i] = (byte) ((number >> (i * 8)) & 0xff);
			}
			access.write(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 写入4字节整型数据
	 * 
	 * @param access
	 * @param number
	 */
	final static public void writeDWORD(final Access access, final int number) {
		byte[] bytes = new byte[4];
		try {
			for (int i = 0; i < 4; i++) {
				bytes[i] = (byte) ((number >> (i * 8)) & 0xff);
			}
			access.write(bytes);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	final static public short readWORD(final Access access) {
		short data = -1;
		try {
			data = (short) (access.read() & 0xff);
			data |= (short) ((access.read() & 0xff) << 8);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}

	final static public int readDWORD(final Access access) {
		int data = -1;
		try {
			data = (access.read() & 0xff);
			data |= ((access.read() & 0xff) << 8);
			data |= ((access.read() & 0xff) << 16);
			data |= ((access.read() & 0xff) << 24);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return data;
	}

	final static public int CRC32(final byte[] buf, final int startPos,
			final int endPos) {
		long c = 0xFFFFFFFFL;
		for (int i = startPos; i < endPos; i++) {
			c = (crcTable[(int) ((c ^ buf[i]) & 0xFF)] ^ (c >> 8));
		}
		return (int) (c ^ 0xFFFFFFFFL);
	}

	final static public Serializer switchSerializer(final int type) {
		Serializer serializerValueType = new NullType();
		switch (type) {
		case -1:
			break;
		case 0:
			serializerValueType = new IntType();
			break;
		case 1:
			serializerValueType = new LongType();
			break;
		case 2:
			serializerValueType = new StringType();
			break;
		case 3:
			serializerValueType = new BytesType();
			break;
		case 4:
			serializerValueType = new ObjectType();
			break;
		default:
		}
		return serializerValueType;
	}

	final static public Object switchObject(final int type, final Object value) {
		Object result = null;
		switch (type) {
		case -1:
			break;
		case 0:
			result = new Integer(value.toString());
			break;
		case 1:
			result = new Long(value.toString());
			break;
		case 2:
			result = value.toString();
			break;
		case 3:
			result = Byte.valueOf(value.toString());
			break;
		case 4:
			result = value;
			break;
		default:
		}
		return result;
	}

}
