package org.loon.framework.android.game.core.graphics;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;

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
public class LImage {

	private static final String flag = "|";

	private Map<String, LImage> subs;

	private Bitmap bitmap;

	private LGraphics g;

	private int width, height;

	private boolean close;

	public static LImage createImage(InputStream in, boolean transparency) {
		return GraphicsUtils.loadImage(in, transparency);
	}

	public static LImage createImage(byte[] buffer, boolean transparency) {
		return GraphicsUtils.loadImage(buffer, transparency);
	}

	public static LImage createImage(int width, int height, boolean transparency) {
		return new LImage(width, height, transparency);
	}

	public static LImage createImage(int width, int height, Config config) {
		return new LImage(width, height, config);
	}

	public static LImage createImage(byte[] imageData, int imageOffset,
			int imageLength, boolean transparency) {
		return GraphicsUtils.loadImage(imageData, imageOffset, imageLength,
				transparency);
	}

	public static LImage createImage(String fileName) {
		return GraphicsUtils.loadImage(fileName, false);
	}

	/**
	 * 以指定像素集合生成LImage文件
	 * 
	 * @param rgb
	 * @param width
	 * @param height
	 * @param processAlpha
	 * @return
	 */
	public static final LImage createRGBImage(int[] rgb, int width, int height,
			boolean processAlpha) {
		Bitmap bitmap = null;
		try {
			Bitmap.Config config;
			if (processAlpha) {
				config = Bitmap.Config.ARGB_4444;
			} else {
				config = Bitmap.Config.RGB_565;
			}
			bitmap = Bitmap.createBitmap(rgb, width, height, config);
		} catch (Exception e) {
			e.printStackTrace();
			LSystem.gc();
			Bitmap.Config config;
			if (processAlpha) {
				config = Bitmap.Config.ARGB_4444;
			} else {
				config = Bitmap.Config.RGB_565;
			}
			bitmap = Bitmap.createBitmap(rgb, width, height, config);
		}
		return new LImage(bitmap);
	}

	/**
	 * 创建指定数量的LImage文件
	 * 
	 * @param count
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public static LImage[] createImage(int count, int w, int h,
			boolean transparency) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, transparency);
		}
		return image;
	}

	/**
	 * 创建指定数量的LImage
	 * 
	 * @param count
	 * @param w
	 * @param h
	 * @param config
	 * @return
	 */
	public static LImage[] createImage(int count, int w, int h, Config config) {
		LImage[] image = new LImage[count];
		for (int i = 0; i < image.length; i++) {
			image[i] = new LImage(w, h, config);
		}
		return image;
	}

	public LImage(int width, int height) {
		this(width, height, false);
	}

	/**
	 * 构建一个LImage(true:ARGB4444或false:RGB565)
	 * 
	 * @param width
	 * @param height
	 * @param transparency
	 */
	public LImage(int width, int height, boolean transparency) {
		try {
			LSystem.gc(50, 1);
			this.width = width;
			this.height = height;
			if (transparency) {
				this.bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.ARGB_4444);
			} else {
				this.bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.RGB_565);
			}
		} catch (Exception e) {
			try {
				LSystem.gc();
				this.width = width;
				this.height = height;
				this.bitmap = Bitmap.createBitmap(width, height,
						Bitmap.Config.RGB_565);
			} catch (Exception ex) {

			}
		}
	}

	public LImage(int width, int height, Config config) {
		this.width = width;
		this.height = height;
		this.bitmap = Bitmap.createBitmap(width, height, config);
	}

	public LImage(LImage img) {
		this(img.getBitmap());
	}

	public LImage(Bitmap bitmap) {
		this.width = bitmap.getWidth();
		this.height = bitmap.getHeight();
		this.bitmap = bitmap;
	}

	public LImage clone() {
		return new LImage(Bitmap.createBitmap(bitmap));
	}

	public LGraphics getLGraphics() {
		if (g == null || g.isClose()) {
			g = new LGraphics(bitmap);
		}
		return g;
	}

	public LGraphics create() {
		return new LGraphics(bitmap, true);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public int getWidth() {
		return bitmap.getWidth();
	}

	public int getHeight() {
		return bitmap.getHeight();
	}

	public int[] getPixels() {
		int pixels[] = new int[width * height];
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		return pixels;
	}

	public int[] getPixels(int pixels[]) {
		bitmap.getPixels(pixels, 0, width, 0, 0, width, height);
		return pixels;
	}

	public int[] getPixels(int x, int y, int w, int h) {
		int[] pixels = new int[w * h];
		bitmap.getPixels(pixels, 0, w, x, y, w, h);
		return pixels;
	}

	public int[] getPixels(int offset, int stride, int x, int y, int width,
			int height) {
		int pixels[] = new int[width * height];
		bitmap.getPixels(pixels, offset, stride, x, y, width, height);
		return pixels;
	}

	public int[] getPixels(int pixels[], int offset, int stride, int x, int y,
			int width, int height) {
		bitmap.getPixels(pixels, offset, stride, x, y, width, height);
		return pixels;
	}

	public void setPixels(int[] pixels, int width, int height) {
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
	}

	public void setPixels(int[] pixels, int offset, int stride, int x, int y,
			int width, int height) {
		bitmap.setPixels(pixels, offset, stride, x, y, width, height);
	}

	public int[] setPixels(int[] pixels, int x, int y, int w, int h) {
		bitmap.setPixels(pixels, 0, width, x, y, width, height);
		return pixels;
	}

	public int getPixel(int x, int y) {
		return bitmap.getPixel(x, y);
	}

	public int getRGB(int x, int y) {
		return bitmap.getPixel(x, y);
	}

	public void setPixel(LColor c, int x, int y) {
		bitmap.setPixel(x, y, c.getRGB());
	}

	public void setPixel(int rgb, int x, int y) {
		bitmap.setPixel(x, y, rgb);
	}

	public void setRGB(int x, int y, int rgb) {
		bitmap.setPixel(x, y, rgb);
	}

	public void setRGB(int x, int y, LColor color) {
		bitmap.setPixel(x, y, color.getRGB());
	}

	/**
	 * 变更图像Config设置
	 * 
	 * @param config
	 */
	public void convertConfig(android.graphics.Bitmap.Config config) {
		if (!bitmap.getConfig().equals(config)) {
			boolean flag = bitmap.isMutable();
			Bitmap tmp = bitmap.copy(config, flag);
			bitmap = tmp;
			if (g != null && !g.isClose()) {
				g.dispose();
				g = new LGraphics(bitmap);
			}
		}
	}

	/**
	 * 截小图(有缓存)
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage getCacheSubImage(int x, int y, int w, int h) {
		return getCacheSubImage(x, y, w, h, true);
	}

	/**
	 * 截小图(有缓存)
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public LImage getCacheSubImage(int x, int y, int w, int h,
			boolean transparency) {
		if (subs == null) {
			subs = Collections.synchronizedMap(new HashMap<String, LImage>(10));
		}
		String key = x + flag + y + flag + w + flag + h;
		LImage img = (LImage) subs.get(key);
		if (img == null) {
			subs.put(key, img = GraphicsUtils.drawClipImage(this, w, h, x, y,
					transparency));
		}
		return img;
	}

	/**
	 * 截小图
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param transparency
	 * @return
	 */
	public LImage getSubImage(int x, int y, int w, int h, boolean transparency) {
		return GraphicsUtils.drawClipImage(this, w, h, x, y, transparency);
	}

	/**
	 * 截小图
	 * 
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage getSubImage(int x, int y, int w, int h) {
		return GraphicsUtils.drawClipImage(this, w, h, x, y, true);
	}

	/**
	 * 扩充图像为指定大小
	 * 
	 * @param w
	 * @param h
	 * @return
	 */
	public LImage scaledInstance(int w, int h) {
		int width = getWidth();
		int height = getHeight();
		if (width == w && height == h) {
			return this;
		}
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, w, h, true);
		return new LImage(resizedBitmap);
	}

	/**
	 * 判定当前LImage是否已被关闭
	 * 
	 * @return
	 */
	public boolean isClose() {
		return close || bitmap == null
				|| (bitmap != null ? bitmap.isRecycled() : false);
	}

	/**
	 * 资源释放
	 * 
	 */
	public void dispose() {
		subs = null;
		close = true;
		bitmap.recycle();
		bitmap = null;
	}

}
