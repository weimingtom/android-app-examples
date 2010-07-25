package org.loon.framework.android.game.action.sprite;

import java.io.IOException;

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
public class Mask {

	public int width;

	public int height;

	public boolean mask[][];

	public boolean full;

	public Mask(int width, int height) {
		this.width = width;
		this.height = height;
		this.mask = new boolean[width][height];
		this.full();
	}

	public Mask(boolean[][] mask, int width, int height) {
		this.width = width;
		this.height = height;
		this.mask = mask;
	}

	public Mask(Mask masker) {
		this.width = masker.width;
		this.height = masker.height;
		this.full = masker.full;
		this.mask = masker.mask;
	}

	public void full() {
		for (int y = 0, x; y < height; ++y) {
			for (x = 0; x < width; ++x) {
				mask[x][y] = true;
			}
		}
		full = true;
	}

	public void set(int x, int y, boolean val) {
		if (!(mask[x][y] = val) && full) {
			full = false;
		}
	}

	private void writeObject(java.io.ObjectOutputStream s) throws IOException {
		s.writeInt(width);
		s.writeInt(height);
		s.writeBoolean(full);
		if (mask != null) {
			s.writeBoolean(true);
			s.writeObject(mask);
		} else {
			s.writeBoolean(false);
		}
	}

	private void readObject(java.io.ObjectInputStream s) throws IOException,
			ClassNotFoundException {
		width = s.readInt();
		height = s.readInt();
		full = s.readBoolean();
		if (s.readBoolean()) {
			mask = (boolean[][]) s.readObject();
		}
	}

	public Object clone() {
		return new Mask(this);
	}

}
