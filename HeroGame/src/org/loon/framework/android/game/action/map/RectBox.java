package org.loon.framework.android.game.action.map;

import android.graphics.RectF;

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
public class RectBox {

	public int x;

	public int y;

	public int width;

	public int height;

	public RectBox() {
		setBounds(0, 0, 0, 0);
	}

	public RectBox(int x, int y, int width, int height) {
		setBounds(x, y, width, height);
	}

	public RectBox(RectBox rect) {
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	public void setBounds(RectBox rect) {
		setBounds(rect.x, rect.y, rect.width, rect.height);
	}

	public void setBounds(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public double getMinX() {
		return getX();
	}

	public double getMinY() {
		return getY();
	}

	public double getMaxX() {
		return getX() + getWidth();
	}

	public double getMaxY() {
		return getY() + getHeight();
	}

	public double getCenterX() {
		return getX() + getWidth() / 2.0;
	}

	public double getCenterY() {
		return getY() + getHeight() / 2.0;
	}

	public RectF getRectF() {
		return new RectF(getX(), getY(), getWidth(), getHeight());
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public boolean equals(Object obj) {
		if (obj instanceof RectBox) {
			RectBox rect = (RectBox) obj;
			return equals(rect.x, rect.y, rect.width, rect.height);
		} else {
			return false;
		}
	}

	public boolean equals(int x, int y, int width, int height) {
		return (this.x == x && this.y == y && this.width == width && this.height == height);
	}

	public int getArea() {
		return width * height;
	}

	/**
	 * 检查是否包含指定坐标
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean contains(int x, int y) {
		return (x >= this.x && y >= this.y && x < this.x + this.width && y < this.y
				+ this.height);
	}

	/**
	 * 检查是否包含指定坐标
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean contains(int x, int y, int width, int height) {
		return (x >= this.x && y >= this.y && x + width <= this.x + this.width && y
				+ height <= this.y + this.height);
	}

	/**
	 * 检查是否包含指定坐标
	 * 
	 * @param rect
	 * @return
	 */
	public boolean contains(RectBox rect) {
		return contains(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * 设定矩形选框交集
	 * 
	 * @param rect
	 * @return
	 */
	public boolean intersects(RectBox rect) {
		return intersects(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * 设定矩形选框交集
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 * @return
	 */
	public boolean intersects(int x, int y, int width, int height) {
		return x + width > this.x && x < this.x + this.width
				&& y + height > this.y && y < this.y + this.height;
	}

	/**
	 * 设定矩形选框交集
	 * 
	 * @param rect
	 */
	public void intersection(RectBox rect) {
		intersection(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * 设定矩形选框交集
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void intersection(int x, int y, int width, int height) {
		int x1 = Math.max(this.x, x);
		int y1 = Math.max(this.y, y);
		int x2 = Math.min(this.x + this.width - 1, x + width - 1);
		int y2 = Math.min(this.y + this.height - 1, y + height - 1);
		setBounds(x1, y1, Math.max(0, x2 - x1 + 1), Math.max(0, y2 - y1 + 1));
	}

	/**
	 * 合并矩形选框
	 * 
	 * @param rect
	 */
	public void union(RectBox rect) {
		union(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * 合并矩形选框
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public void union(int x, int y, int width, int height) {
		int x1 = Math.min(this.x, x);
		int y1 = Math.min(this.y, y);
		int x2 = Math.max(this.x + this.width - 1, x + width - 1);
		int y2 = Math.max(this.y + this.height - 1, y + height - 1);
		setBounds(x1, y1, x2 - x1 + 1, y2 - y1 + 1);
	}
}
