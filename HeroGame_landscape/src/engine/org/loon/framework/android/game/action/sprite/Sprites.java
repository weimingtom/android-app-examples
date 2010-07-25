package org.loon.framework.android.game.action.sprite;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.utils.CollectionUtils;

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
public class Sprites implements Serializable {

	private static final long serialVersionUID = 7460335325994101982L;

	private static final Comparator<Object> DEFAULT_COMPARATOR = new Comparator<Object>() {
		public int compare(Object o1, Object o2) {
			return ((ISprite) o2).getLayer() - ((ISprite) o1).getLayer();
		}
	};

	private Comparator<Object> comparator = Sprites.DEFAULT_COMPARATOR;

	private int capacity = 100;

	private ISprite[] sprites;

	private int size;

	private int width, height;

	public Sprites(int w, int h) {
		this.width = w;
		this.height = h;
		this.sprites = new ISprite[capacity];
	}

	/**
	 * 设定指定对象到图层最前
	 * 
	 * @param sprite
	 */
	public void sendToFront(ISprite sprite) {
		if (this.size <= 1 || this.sprites[0] == sprite) {
			return;
		}
		for (int i = 0; i < this.size; i++) {
			if (this.sprites[i] == sprite) {
				this.sprites = (ISprite[]) CollectionUtils.cut(this.sprites, i);
				this.sprites = (ISprite[]) CollectionUtils.expand(this.sprites,
						1, false);
				this.sprites[0] = sprite;
				this.sortSprites();
				break;
			}
		}
	}

	/**
	 * 设定指定对象到图层最后
	 * 
	 * @param sprite
	 */
	public void sendToBack(ISprite sprite) {
		if (this.size <= 1 || this.sprites[this.size - 1] == sprite) {
			return;
		}

		for (int i = 0; i < this.size; i++) {
			if (this.sprites[i] == sprite) {
				this.sprites = (ISprite[]) CollectionUtils.cut(this.sprites, i);
				this.sprites = (ISprite[]) CollectionUtils.expand(this.sprites,
						1, true);
				this.sprites[this.size - 1] = sprite;
				this.sortSprites();
				break;
			}
		}
	}

	/**
	 * 按所在层级排序
	 * 
	 */
	public void sortSprites() {
		Arrays.sort(this.sprites, this.comparator);
	}

	/**
	 * 扩充当前集合容量
	 * 
	 * @param capacity
	 */
	private void expandCapacity(int capacity) {
		if (sprites.length < capacity) {
			ISprite[] bagArray = new ISprite[capacity];
			System.arraycopy(sprites, 0, bagArray, 0, size);
			sprites = bagArray;
		}
	}

	/**
	 * 压缩当前集合容量
	 * 
	 * @param capacity
	 */
	private void compressCapacity(int capacity) {
		if (capacity + this.size < sprites.length) {
			ISprite[] newArray = new ISprite[this.size + 2];
			System.arraycopy(sprites, 0, newArray, 0, this.size);
			sprites = newArray;
		}
	}

	/**
	 * 在指定索引处插入一个精灵
	 * 
	 * @param index
	 * @param sprite
	 * @return
	 */
	public synchronized boolean add(int index, ISprite sprite) {
		if (sprite == null) {
			return false;
		}
		if (index > this.size) {
			index = this.size;
		}
		if (index == this.size) {
			this.add(sprite);
		} else {
			System.arraycopy(this.sprites, index, this.sprites, index + 1,
					this.size - index);
			this.sprites[index] = sprite;
			if (++this.size >= this.sprites.length) {
				expandCapacity((size + 1) * 2);
			}
		}
		return sprites[index] != null;
	}

	public synchronized ISprite getSprite(int index) {
		if (index < 0 || index > size || index >= sprites.length) {
			return null;
		}
		return sprites[index];
	}

	/**
	 * 顺序添加精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public synchronized boolean add(ISprite sprite) {
		if (contains(sprite)) {
			return false;
		}
		if (this.size == this.sprites.length) {
			expandCapacity((size + 1) * 2);
		}
		return (sprites[size++] = sprite) != null;
	}

	/**
	 * 检查指定精灵是否存在
	 * 
	 * @param sprite
	 * @return
	 */
	public synchronized boolean contains(ISprite sprite) {
		if (sprite == null) {
			return false;
		}
		if (sprites == null) {
			return false;
		}
		for (int i = 0; i < size; i++) {
			if (sprites[i] != null && sprite.equals(sprites[i])) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 删除指定索引处精灵
	 * 
	 * @param index
	 * @return
	 */
	public synchronized ISprite remove(int index) {
		ISprite removed = this.sprites[index];
		int size = this.size - index - 1;
		if (size > 0) {
			System
					.arraycopy(this.sprites, index + 1, this.sprites, index,
							size);
		}
		this.sprites[--this.size] = null;
		return removed;
	}

	/**
	 * 清空所有精灵
	 * 
	 */
	public synchronized void removeAll() {
		clear();
		this.sprites = new Sprite[0];
	}

	/**
	 * 删除指定精灵
	 * 
	 * @param sprite
	 * @return
	 */
	public synchronized boolean remove(ISprite sprite) {
		if (sprite == null) {
			return false;
		}
		if (sprites == null) {
			return false;
		}
		boolean removed = false;
		for (int i = size; i > 0; i--) {
			if (sprite.equals(sprites[i])) {
				removed = true;
				size--;
				sprites[i] = sprites[size];
				sprites[size] = null;
				if (size == 0) {
					sprites = null;
				} else {
					compressCapacity(2);
				}
				return removed;
			}
		}
		return removed;
	}

	/**
	 * 删除指定范围内精灵
	 * 
	 * @param startIndex
	 * @param endIndex
	 */
	public synchronized void remove(int startIndex, int endIndex) {
		int numMoved = this.size - endIndex;
		System.arraycopy(this.sprites, endIndex, this.sprites, startIndex,
				numMoved);
		int newSize = this.size - (endIndex - startIndex);
		while (this.size != newSize) {
			this.sprites[--this.size] = null;
		}
	}

	/**
	 * 清空当前精灵集合
	 * 
	 */
	public synchronized void clear() {
		for (int i = 0; i < sprites.length; i++) {
			sprites[i] = null;
		}
		size = 0;
	}

	/**
	 * 刷新事务
	 * 
	 * @param elapsedTime
	 */
	public void update(long elapsedTime) {
		for (int i = 0; i < this.size; i++) {
			if (this.sprites[i].isVisible()) {
				this.sprites[i].update(elapsedTime);
			}
		}
	}

	/**
	 * 刷新图像
	 * 
	 * @param g
	 */
	public void createUI(final LGraphics g) {
		for (int i = 0; i < this.size; i++) {
			if (this.sprites[i].isVisible()) {
				sprites[i].createUI(g);
			}
		}
	}

	public ISprite[] getSprites() {
		return this.sprites;
	}

	public int size() {
		return this.size;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

}
