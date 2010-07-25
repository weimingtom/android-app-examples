package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * 
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * 
 * under the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class SpriteImage extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982900453464314946L;

	public boolean visible = true;

	private LImage image;

	private boolean isOpaque = true;

	private int[] pixels;

	private Mask mask;

	protected int width, height;

	public SpriteImage(String fileName) {
		this(fileName, 0, 0);
	}

	public SpriteImage(LImage img) {
		this(img, 0, 0);
	}

	public SpriteImage(LImage img, int x, int y) {
		this(img, x, y, img.getWidth(), img.getHeight());
	}

	public SpriteImage(LImage img, int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;
		this.bind(img);
	}

	public SpriteImage(SpriteImage image) {
		this(image, 0, 0);
	}

	public SpriteImage(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName,true), x, y);
	}

	public SpriteImage(SpriteImage image, int x, int y) {
		this.setLocation(x, y);
		this.width = image.width;
		this.height = image.height;
		this.bind(image.getImage());
	}

	public SpriteImage(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;
		this.bind(null);
	}

	private void bind(LImage img) {
		this.image = img;
		this.pixels = img.getPixels();
		int size = width * height;
		for (int i = 0; i < size; i++) {
			if (pixels[i] != 0xffffff) {
				pixels[i] = LColor.premultiply(pixels[i]);
				if (isOpaque && (pixels[i] >>> 24) < 255) {
					isOpaque = false;
				}
			}
		}
		makeMask();
	}

	/**
	 * 生成像素掩码
	 * 
	 */
	private void makeMask() {
		boolean[][] alphas = new boolean[width][height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				alphas[x][y] = isTransparent(x, y);
			}
		}
		mask = new Mask(alphas, width, height);
	}

	/**
	 * 判定指定像素点是否透明
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isTransparent(int x, int y) {
		if (x < 0 || y < 0 || x >= width || y >= height) {
			return true;
		} else if (isOpaque) {
			return false;
		} else {
			return (pixels[x + width * y] & 0xff000000) == 0xff000000;
		}
	}

	/**
	 * 绘制图像到当前画布
	 */
	public void createUI(LGraphics g) {
		if (visible) {
			g.drawImage(image, x(), y());
		}
	}

	public void update(long timer) {

	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void move(int x, int y) {
		this.move(x, y);
	}

	public void setLocation(int x, int y) {
		this.setX(x);
		this.setY(y);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public SpriteImage copy() {
		return new SpriteImage(this);
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

	public LImage getImage() {
		return image;
	}

	public float getAlpha() {
		return 0;
	}

	public Mask getMask() {
		return mask;
	}

	public boolean isOpaque() {
		return isOpaque;
	}
}
