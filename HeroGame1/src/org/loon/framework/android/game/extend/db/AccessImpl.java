package org.loon.framework.android.game.extend.db;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.loon.framework.android.game.core.LSystem;

/**
 * 
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
 * @version 0.1.1
 */

public class AccessImpl implements Access, DataInput, DataOutput {

	private RandomAccessFile delegate;

	private boolean r = false, w = false;

	public AccessImpl(RandomAccessFile file) throws FileNotFoundException {
		this.delegate = file;
	}

	public AccessImpl(File file, boolean read, boolean write)
			throws FileNotFoundException {
		this.r = read;
		this.w = write;
		String mode = "";
		if (this.r) {
			mode += "r";
		}
		if (this.w) {
			mode += "w";
		}
		try {
			this.delegate = new RandomAccessFile(file, mode);
		} catch (IOException e) {
			throw new FileNotFoundException(file.getAbsolutePath());
		}
	}

	public long getFilePointer() throws IOException {
		return delegate.getFilePointer();
	}

	public long length() throws IOException {
		return delegate.length();
	}

	public int read() throws IOException {
		return delegate.read();
	}

	public int read(byte[] b) throws IOException {
		return delegate.read(b);
	}

	public int read(byte[] b, int off, int len) throws IOException {
		return delegate.read(b, off, len);
	}

	public void seek(long pos) throws IOException {
		delegate.seek(pos);
	}

	public void setLength(long newLength) throws IOException {
		delegate.setLength(newLength);
	}

	public void close() throws IOException {
		delegate.close();
	}

	public boolean readBoolean() throws IOException {
		return delegate.readBoolean();
	}

	public byte readByte() throws IOException {
		return delegate.readByte();
	}

	public char readChar() throws IOException {
		return delegate.readChar();
	}

	public double readDouble() throws IOException {
		return delegate.readDouble();
	}

	public float readFloat() throws IOException {
		return delegate.readFloat();
	}

	public void readFully(byte[] b) throws IOException {
		delegate.readFully(b);
	}

	public void readFully(byte[] b, int off, int len) throws IOException {
		delegate.readFully(b, off, len);
	}

	public int readInt() throws IOException {
		return delegate.readInt();
	}

	public String readLine() throws IOException {
		return delegate.readLine();
	}

	public long readLong() throws IOException {
		return delegate.readLong();
	}

	public short readShort() throws IOException {
		return delegate.readShort();
	}

	public int readUnsignedByte() throws IOException {
		return delegate.readUnsignedByte();
	}

	public int readUnsignedShort() throws IOException {
		return delegate.readUnsignedShort();
	}

	public String readUTF() throws IOException {
		int len = delegate.readInt();
		if ((len < 0) || (len >= 16777216)) {
			throw new IOException("Bad Length Encoding!");
		}
		byte[] bytes = new byte[len];
		int l = delegate.read(bytes);
		if (l == -1) {
			throw new IOException("EOF while reading String!");
		}
		String s = new String(bytes, LSystem.encoding);
		return s;
	}

	public int skipBytes(int n) throws IOException {
		return delegate.skipBytes(n);
	}

	public void write(int b) throws IOException {
		delegate.write(b);
	}

	public void write(byte[] b) throws IOException {
		delegate.write(b);
	}

	public void write(byte[] b, int off, int len) throws IOException {
		delegate.write(b, off, len);
	}

	public void writeBoolean(boolean v) throws IOException {
		delegate.writeBoolean(v);
	}

	public void writeByte(int v) throws IOException {
		delegate.writeByte(v);
	}

	public void writeShort(int v) throws IOException {
		delegate.writeShort(v);
	}

	public void writeChar(int v) throws IOException {
		delegate.writeChar(v);
	}

	public void writeInt(int v) throws IOException {
		delegate.writeInt(v);
	}

	public void writeLong(long v) throws IOException {
		delegate.writeLong(v);
	}

	public void writeFloat(float v) throws IOException {
		delegate.writeFloat(v);
	}

	public void writeDouble(double v) throws IOException {
		delegate.writeDouble(v);
	}

	public void writeBytes(String s) throws IOException {
		delegate.writeBytes(s);
	}

	public void writeChars(String s) throws IOException {
		delegate.writeChars(s);
	}

	public void writeUTF(String str) throws IOException {
		byte[] string = str.getBytes(LSystem.encoding);
		if (string.length >= 16777216) {
			throw new IOException("String to long for encoding type!");
		}
		delegate.writeInt(string.length);
		delegate.write(string);
	}
}
