package org.loon.framework.android.game.action.map;

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
public class Size {

	private int height;

	private int width;

	public Size(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * 克隆一个新的Size
	 * 
	 * @param with
	 * @return
	 */
	public Size clone(Size with) {
		int h = height;
		int w = width;
		if (h < with.getHeight()) {
			h = with.getHeight();
		}
		if (w < with.getWidth()) {
			w = with.getWidth();
		}
		return new Size(w, h);
	}

	public Size subtract(Size with) {
		int h = height;
		int w = width;
		if (h > with.getHeight()) {
			h = with.getHeight();
		}
		if (w > with.getWidth()) {
			w = with.getWidth();
		}
		return new Size(w, h);
	}

	public boolean contains(int x, int y) {
		return (x >= 0 && y >= 0 && x < width && y < height);
	}

	public boolean equals(Object o) {
		if (o != null && o instanceof Size) {
			Size oo = (Size) o;
			return oo.height == height && oo.width == width;
		}
		return false;
	}

	public int hashCode() {
		return width * height;
	}
}
