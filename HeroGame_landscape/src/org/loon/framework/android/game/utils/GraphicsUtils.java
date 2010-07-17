package org.loon.framework.android.game.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.core.graphics.geom.Shape;
import org.loon.framework.android.game.core.resource.Resources;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuffXfermode;
import android.graphics.Bitmap.Config;
import android.graphics.Shader.TileMode;

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
public class GraphicsUtils {

	final static private Matrix matrix = new Matrix();

	final static private Map<String, LImage> lazyImages = Collections
			.synchronizedMap(new HashMap<String, LImage>(
					LSystem.DEFAULT_MAX_CACHE_SIZE));

	final static private Map<String, Object> lazySplitMap = Collections
			.synchronizedMap(new HashMap<String, Object>(
					LSystem.DEFAULT_MAX_CACHE_SIZE));

	final static public BitmapFactory.Options ARGB4444options = new BitmapFactory.Options();

	final static public BitmapFactory.Options ARGB8888options = new BitmapFactory.Options();

	final static public BitmapFactory.Options RGB565options = new BitmapFactory.Options();

	static {
		ARGB8888options.inDither = false;
		ARGB8888options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		ARGB4444options.inDither = false;
		ARGB4444options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		RGB565options.inDither = false;
		RGB565options.inPreferredConfig = Bitmap.Config.RGB_565;
		try {
			BitmapFactory.Options.class.getField("inPurgeable").set(
					ARGB8888options, true);
			BitmapFactory.Options.class.getField("inPurgeable").set(
					ARGB4444options, true);
			BitmapFactory.Options.class.getField("inPurgeable").set(
					RGB565options, true);
		} catch (Exception e) {
		}
	}

	/**
	 * 加载标准位图文件
	 * 
	 * @param in
	 * @param transparency
	 * @return
	 */
	final static public Bitmap loadBitmap(InputStream in, boolean transparency) {
		return BitmapFactory.decodeStream(in, null,
				transparency ? ARGB4444options : RGB565options);
	}

	/**
	 * 加载标准位图文件
	 * 
	 * @param resName
	 * @param transparency
	 * @return
	 */
	final static public Bitmap loadBitmap(String resName, boolean transparency) {
		try {
			return BitmapFactory.decodeStream(Resources.openResource(resName),
					null, transparency ? ARGB4444options : RGB565options);
		} catch (IOException e) {
			throw new RuntimeException(resName + " not found!");
		}
	}

	/**
	 * 以指定大小加载指定的位图文件
	 * 
	 * @param resName
	 * @param width
	 * @param height
	 * @return
	 */
	final static public Bitmap loadScaleBitmap(String resName, int width,
			int height) {
		try {
			BitmapFactory.Options opts = new BitmapFactory.Options();
			opts.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(Resources.openResource(resName), null,
					opts);
			int scaleWidth = (int) Math.floor((double) opts.outWidth / width);
			int scaleHeight = (int) Math
					.floor((double) opts.outHeight / height);

			opts.inJustDecodeBounds = false;
			opts.inSampleSize = Math.min(scaleWidth, scaleHeight);

			return BitmapFactory.decodeStream(Resources.openResource(resName),
					null, opts);
		} catch (Exception e) {
			throw new RuntimeException(resName + " not found!");
		}
	}

	/**
	 * 以指定大小加载指定的LImage文件
	 * 
	 * @param resName
	 * @param width
	 * @param height
	 * @return
	 */
	final static public LImage loadScaleImage(String resName, int width,
			int height) {
		return new LImage(loadScaleBitmap(resName, width, height));
	}

	/**
	 * 加载LImage
	 * 
	 * @param in
	 * @return
	 */
	final static public LImage loadImage(InputStream in, boolean transparency) {
		return new LImage(BitmapFactory.decodeStream(in, null,
				transparency ? ARGB4444options : RGB565options));
	}

	/**
	 * 以ARGB8888格式加载LImage
	 * 
	 * @param in
	 * @param transparency
	 * @return
	 */
	final static public LImage load8888Image(String fileName) {
		return GraphicsUtils.load8888Image(Resources
				.getResourceAsStream(fileName));
	}

	/**
	 * 以ARGB8888格式加载LImage
	 * 
	 * @param in
	 * @param transparency
	 * @return
	 */
	final static public LImage load8888Image(InputStream in) {
		return new LImage(BitmapFactory.decodeStream(in, null, ARGB8888options));
	}

	/**
	 * 加载LImage
	 * 
	 * @param buffer
	 * @return
	 */
	final static public LImage loadImage(byte[] buffer, boolean transparency) {
		return new LImage(BitmapFactory.decodeByteArray(buffer, 0,
				buffer.length, transparency ? ARGB4444options : RGB565options));
	}

	/**
	 * 加载LImage
	 * 
	 * @param imageData
	 * @param imageOffset
	 * @param imageLength
	 * @return
	 */
	final static public LImage loadImage(byte[] imageData, int imageOffset,
			int imageLength, boolean transparency) {
		return new LImage(BitmapFactory.decodeByteArray(imageData, imageOffset,
				imageLength, transparency ? ARGB4444options : RGB565options));
	}

	/**
	 * 加载LImage
	 * 
	 * @param innerFileName
	 * @return
	 */
	final static public LImage loadImage(final String innerFileName,
			boolean transparency) {
		if (innerFileName == null) {
			return null;
		}
		if (lazyImages.size() > LSystem.DEFAULT_MAX_CACHE_SIZE) {
			lazyImages.clear();
			LSystem.gc();
		}
		String innerName = StringUtils.replaceIgnoreCase(innerFileName, "\\",
				"/");
		String keyName = innerName.toLowerCase();
		LImage image = (LImage) lazyImages.get(keyName);
		if (image != null && !image.isClose()) {
			return image;
		} else {
			InputStream in = null;
			try {
				in = Resources.openResource(innerFileName);
				image = loadImage(in, transparency);
				lazyImages.remove(keyName);
				lazyImages.put(keyName, image);
			} catch (Exception e) {
				throw new RuntimeException(innerFileName + " not found!");
			} finally {
				try {
					if (in != null) {
						in.close();
						in = null;
					}
				} catch (IOException e) {
					LSystem.gc();
				}
			}
		}
		if (image == null) {
			throw new RuntimeException(
					("File not found. ( " + innerFileName + " )").intern());
		}
		return image;
	}

	final static public LImage loadImage(final String innerFileName) {
		return GraphicsUtils.loadImage(innerFileName, false);
	}

	final static public LImage loadNotCacheImage(final String innerFileName,
			boolean transparency) {
		if (innerFileName == null) {
			return null;
		}
		String innerName = StringUtils.replaceIgnoreCase(innerFileName, "\\",
				"/");
		InputStream in = null;
		try {
			in = Resources.openResource(innerName);
			return loadImage(in, transparency);
		} catch (Exception e) {
			throw new RuntimeException(innerFileName + " not found!");
		} finally {
			try {
				if (in != null) {
					in.close();
					in = null;
				}
			} catch (IOException e) {
			}
		}

	}

	final static public LImage loadNotCacheImage(final String innerFileName) {
		return GraphicsUtils.loadNotCacheImage(innerFileName, false);
	}

	/**
	 * 加载网络中图像
	 * 
	 * @param string
	 * @return
	 */
	public static LImage loadWebImage(String string, boolean transparency) {
		LImage img = null;
		try {
			URL url = new URL(string);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.connect();
			InputStream is = http.getInputStream();
			img = GraphicsUtils.loadImage(is, transparency);
			if (img.getWidth() == 0 || img.getHeight() == 0) {
				img = null;
			}
			is.close();
		} catch (Exception e) {
			throw new RuntimeException(("File not found. ( " + string + " )")
					.intern());
		}
		return img;
	}

	/**
	 * 获得一组序号连续的图片
	 * 
	 * @param fileName
	 * @param range
	 *            (指定图片范围，如("1-2"))
	 * @return
	 */
	public static LImage[] loadSequenceImages(String fileName, String range,
			boolean transparency) {
		try {
			int start_range = -1;
			int end_range = -1;
			int images_count = 1;
			int minusIndex = range.indexOf('-');
			if ((minusIndex > 0) && (minusIndex < (range.length() - 1))) {
				try {
					start_range = Integer.parseInt(range.substring(0,
							minusIndex));
					end_range = Integer.parseInt(range
							.substring(minusIndex + 1));
					if (start_range < end_range) {
						images_count = end_range - start_range + 1;
					}
				} catch (Exception ex) {
				}
			}
			LImage[] images = new LImage[images_count];
			for (int i = 0; i < images_count; i++) {
				String imageName = fileName;
				if (images_count > 1) {
					int dotIndex = fileName.lastIndexOf('.');
					if (dotIndex >= 0) {
						imageName = fileName.substring(0, dotIndex)
								+ (start_range + i)
								+ fileName.substring(dotIndex);
					}
				}
				images[i] = GraphicsUtils.loadImage(imageName, transparency);
			}
			return images;
		} catch (Exception ex) {
		}
		return null;
	}

	/**
	 * 水平翻转分组图像顺序
	 * 
	 * @param pixels
	 * @return
	 */
	public static LImage[][] getFlipHorizintalImage2D(LImage[][] pixels) {
		int w = pixels.length;
		int h = pixels[0].length;
		LImage pixel[][] = new LImage[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				pixel[i][j] = pixels[j][i];
			}
		}
		return pixel;
	}

	/**
	 * 水平翻转当前图像
	 * 
	 * @return
	 */
	public static LImage rotateImage(final LImage image) {
		return rotate(image, 180);
	}

	/**
	 * 旋转图像为指定角度
	 * 
	 * @param degree
	 * @return
	 */
	public static LImage rotateImage(final LImage image, final int angdeg,
			final boolean d) {
		int w = image.getWidth();
		int h = image.getHeight();
		LImage img = LImage.createImage(w, h, true);
		LGraphics g = img.getLGraphics();
		g.setAntiAlias(true);
		g.rotate(d ? -Math.toRadians(angdeg) : Math.toRadians(angdeg), w / 2,
				h / 2);
		g.drawImage(image, 0, 0);
		g.setAntiAlias(false);
		g.dispose();
		return img;
	}

	/**
	 * 创建一个指定形状和填充色的LImage
	 * 
	 * @param shape
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static LImage createShapeImage(Shape shape, LColor c1, LColor c2) {
		Rectangle rect = shape.getBounds();
		LImage image = LImage.createImage(rect.width, rect.height, false);
		LGraphics g = image.getLGraphics();
		g.setColor(c1);
		g.fill(shape);
		g.setColor(c2);
		g.draw(shape);
		g.dispose();
		return image;
	}

	/**
	 * 剪切指定图像
	 * 
	 * @param image
	 * @param objectWidth
	 * @param objectHeight
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public static LImage drawClipImage(final LImage image, int objectWidth,
			int objectHeight, int x1, int y1, int x2, int y2) {
		LImage buffer = LImage.createImage(objectWidth, objectHeight, true);
		LGraphics g = buffer.getLGraphics();
		g.drawImage(image, 0, 0, objectWidth, objectHeight, x1, y1, x2, y2);
		g.dispose();
		g = null;
		return buffer;
	}

	/**
	 * 剪切指定图像
	 * 
	 * @param image
	 * @param objectWidth
	 * @param objectHeight
	 * @param x
	 * @param y
	 * @return
	 */
	public static LImage drawClipImage(final LImage image, int objectWidth,
			int objectHeight, int x, int y, boolean transparency) {
		LImage buffer = LImage.createImage(objectWidth, objectHeight,
				transparency);
		LGraphics graphics = buffer.getLGraphics();
		graphics.drawImage(image, 0, 0, objectWidth, objectHeight, x, y, x
				+ objectWidth, objectHeight + y);
		graphics.dispose();
		graphics = null;
		return buffer;
	}

	/**
	 * 剪切指定图像
	 * 
	 * @param image
	 * @param objectWidth
	 * @param objectHeight
	 * @param x
	 * @param y
	 * @return
	 */
	public static LImage drawClipImage(final LImage image, int objectWidth,
			int objectHeight, int x, int y) {
		return GraphicsUtils.drawClipImage(image, objectWidth, objectHeight, x,
				y, false);
	}

	/**
	 * 分解整图为图片数组
	 * 
	 * @param fileName
	 * @param row
	 * @param col
	 * @return
	 */
	public static LImage[][] getSplit2Images(String fileName, int row, int col,
			boolean isFiltrate, boolean transparency) {
		String keyName = (fileName + row + col + isFiltrate).intern()
				.toLowerCase().trim();
		if (lazySplitMap.size() > LSystem.DEFAULT_MAX_CACHE_SIZE / 3) {
			lazySplitMap.clear();
			System.gc();
		}
		Object objs = lazySplitMap.get(keyName);
		if (objs == null) {
			LImage image = GraphicsUtils.loadImage(fileName, transparency);
			objs = getSplit2Images(image, row, col, isFiltrate);
			lazySplitMap.put(keyName, objs);
		}
		return (LImage[][]) objs;
	}

	/**
	 * 分割指定图像为image[][]
	 * 
	 * @param fileName
	 * @param row
	 * @param col
	 * @return
	 */
	public static LImage[][] getSplit2Images(String fileName, int row, int col,
			boolean transparency) {
		return getSplit2Images(fileName, row, col, false, transparency);
	}

	/**
	 * 分割指定图像为image[]
	 * 
	 * @param image
	 * @param row
	 * @param col
	 * @return
	 */

	public static LImage[][] getSplit2Images(LImage image, int row, int col,
			boolean isFiltrate) {
		int wlength = image.getWidth() / row;
		int hlength = image.getHeight() / col;
		LImage[][] abufferedimage = new LImage[row][col];
		for (int y = 0; y < hlength; y++) {
			for (int x = 0; x < wlength; x++) {
				abufferedimage[x][y] = LImage.createImage(row, col, true);
				LGraphics g = abufferedimage[x][y].getLGraphics();
				g.drawImage(image, 0, 0, row, col, (x * row), (y * col), row
						+ (x * row), col + (y * col));
				g.dispose();
				int pixels[] = (int[]) abufferedimage[x][y].getPixels();
				if (isFiltrate) {
					for (int i = 0; i < pixels.length; i++) {
						LColor c = new LColor(pixels[i]);
						if ((c.getBlue() == 247 && c.getGreen() == 0 && c
								.getBlue() == 255)
								|| (c.getBlue() == 255 && c.getGreen() == 0 && c
										.getBlue() == 255)
								|| (c.getBlue() == 0 && c.getGreen() == 0 && c
										.getBlue() == 0)) {
							pixels[i] = 0;
						}
					}
				}
				abufferedimage[x][y].setPixels(pixels, wlength, hlength);
			}
		}
		return abufferedimage;
	}

	/**
	 * 将指定图像转化为灰色位图
	 * 
	 * @param image
	 * @return
	 */
	public static LImage getGray(LImage image) {
		int width = image.getWidth(), height = image.getHeight();
		Bitmap dst = Bitmap
				.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		Bitmap src = image.getBitmap();
		int r, g, b, pixel;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixel = src.getPixel(x, y);
				if (pixel != 0) {
					r = (pixel & 0xFF0000) >> 16;
					g = (pixel & 0xFF00) >> 8;
					b = (pixel & 0xFF);
					pixel = (int) (r * 0.299 + g * 0.587 + b * 0.114);
					pixel = ((pixel << 16) ^ 0xFF0000)
							+ ((pixel << 8) ^ 0xFF00) + ((pixel) ^ 0xFF);
					pixel = (pixel * -1) - 1;
					dst.setPixel(x, y, pixel);
				}
			}
		}
		return new LImage(dst);
	}

	/**
	 * 改变指定Image大小
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @return
	 */
	public static LImage getResize(LImage image, int w, int h) {
		return new LImage(GraphicsUtils.getResize(image.getBitmap(), w, h));
	}

	/**
	 * 改变指定Bitmap大小
	 * 
	 * @param image
	 * @param w
	 * @param h
	 * @return
	 */
	public static Bitmap getResize(Bitmap image, int w, int h) {
		int width = image.getWidth();
		int height = image.getHeight();
		if (width == w && height == h) {
			return image;
		}
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		matrix.reset();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(image, 0, 0, width, height,
				matrix, false);
		return resizedBitmap;
	}

	/**
	 * 返回一个Matrix
	 * 
	 * @return
	 */
	public static Matrix getMatrix() {
		matrix.reset();
		return matrix;
	}

	/**
	 * 拆分指定图像
	 * 
	 * @param fileName
	 * @param row
	 * @param col
	 * @return
	 */
	public static LImage[] getSplitImages(String fileName, int row, int col,
			boolean transparency) {
		return getSplitImages(GraphicsUtils.loadImage(fileName, transparency),
				row, col);
	}

	/**
	 * 拆分指定图像
	 * 
	 * @param image
	 * @param row
	 * @param col
	 * @return
	 */
	public static LImage[] getSplitImages(LImage image, int row, int col) {
		int frame = 0;
		int wlength = image.getWidth() / row;
		int hlength = image.getHeight() / col;
		int total = wlength * hlength;
		LImage[] images = new LImage[total];
		for (int y = 0; y < hlength; y++) {
			for (int x = 0; x < wlength; x++) {
				images[frame] = new LImage(row, col, true);
				LGraphics g = images[frame].getLGraphics();
				g.drawImage(image, 0, 0, row, col, (x * row), (y * col), row
						+ (x * row), col + (y * col));
				g.dispose();
				frame++;
			}
		}
		return images;
	}

	/**
	 * copy指定图像到目标图形中
	 * 
	 * @param target
	 * @param source
	 * @return
	 */
	public static LImage copy(LImage target, LImage source) {
		LGraphics g = target.getLGraphics();
		g.drawImage(source, 0, 0);
		g.dispose();
		return target;
	}

	/**
	 * 旋转指定图像为指定角度
	 * 
	 * @param bmp
	 * @param degrees
	 * @return
	 */
	public static Bitmap rotate(Bitmap bmp, float degrees) {
		if (degrees % 360 != 0) {
			Matrix matrix = new Matrix();
			matrix.postRotate(degrees);
			if (bmp != null) {
				Bitmap dst = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp
						.getHeight(), matrix, false);

				return dst;
			}
		} else {
			return bmp;
		}
		return null;
	}

	/**
	 * 旋转指定图像为指定角度
	 * 
	 * @param img
	 * @param degrees
	 * @return
	 */
	public static LImage rotate(LImage img, float degrees) {
		return new LImage(img);
	}

	/**
	 * 将指定颜色透明化
	 * 
	 * @param img
	 * @param color
	 */
	public static void transparencyColor(LImage img, int color) {
		int[] basePixels = img.getPixels();
		int length = basePixels.length;
		int pixel = LColor.getRGB(color);
		for (int i = 0; i < length; i++) {
			if (basePixels[i] == color || basePixels[i] == pixel) {
				basePixels[i] = Color.TRANSPARENT;
			}
		}
		img.setPixels(basePixels, img.getWidth(), img.getHeight());
	}

	/**
	 * 将指定颜色透明化
	 * 
	 * @param img
	 * @param color
	 */
	public static void transparencyColor(LImage img, LColor color) {
		int[] basePixels = img.getPixels();
		int length = basePixels.length;
		int pixel = color.getARGB();
		int pixel1 = color.getRGB();
		for (int i = 0; i < length; i++) {
			if (basePixels[i] == pixel || basePixels[i] == pixel1) {
				basePixels[i] = Color.TRANSPARENT;
			}
		}
		img.setPixels(basePixels, img.getWidth(), img.getHeight());
	}

	/**
	 * 为图像添加光反射效果
	 * 
	 * @param originalImage
	 * @return
	 */
	public static Bitmap reflect(Bitmap originalImage) {
		final int reflectionGap = 4;

		int width = originalImage.getWidth();
		int height = originalImage.getHeight();

		Matrix matrix = new Matrix();
		matrix.preScale(1, -1);

		Bitmap reflectionImage = Bitmap.createBitmap(originalImage, 0,
				height / 2, width, height / 2, matrix, false);

		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height / 2), Config.ARGB_8888);

		Canvas canvas = new Canvas(bitmapWithReflection);
		canvas.drawBitmap(originalImage, 0, 0, null);
		Paint deafaultPaint = new Paint();
		canvas
				.drawRect(0, height, width, height + reflectionGap,
						deafaultPaint);
		canvas.drawBitmap(reflectionImage, 0, height + reflectionGap, null);

		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(0,
				originalImage.getHeight(), 0, bitmapWithReflection.getHeight()
						+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		paint.setXfermode(new PorterDuffXfermode(
				android.graphics.PorterDuff.Mode.DST_IN));
		canvas.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		return bitmapWithReflection;
	}

	/**
	 * 矫正指定位图大小
	 * 
	 * @param baseImage
	 * @param width
	 * @param height
	 * @return
	 */
	public static Bitmap fitBitmap(Bitmap baseImage, int width, int height) {
		Point pt = calculateFitBitmap(baseImage, width, height, null);
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(baseImage, pt.x, pt.y,
				true);
		return resizedBitmap;
	}

	/**
	 * 矫正指定LImage图像大小
	 * 
	 * @param image
	 * @param width
	 * @param height
	 * @return
	 */
	public static LImage fitImage(LImage image, int width, int height) {
		Bitmap bitmap = image.getBitmap();
		Point pt = calculateFitBitmap(bitmap, width, height, null);
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, pt.x, pt.y,
				true);
		return new LImage(resizedBitmap);
	}

	/**
	 * 矫正指定图像为选定范围内的合适大小
	 * 
	 * @param baseImage
	 * @param width
	 * @param height
	 * @param receiver
	 *            (返回值)
	 * @return
	 */
	final static public Point calculateFitBitmap(Bitmap baseImage, int width,
			int height, Point receiver) {
		if (baseImage == null) {
			throw new RuntimeException("Image is null");
		}
		if (receiver == null) {
			receiver = new Point();
		}
		int dw = width;
		int dh = height;
		if (dw != 0 && dh != 0) {
			double waspect = (double) dw / baseImage.getWidth();
			double haspect = (double) dh / baseImage.getHeight();
			if (waspect > haspect) {
				dw = (int) (baseImage.getWidth() * haspect);
			} else {
				dh = (int) (baseImage.getHeight() * waspect);
			}
		}
		receiver.x = dw;
		receiver.y = dh;
		return receiver;
	}

	/**
	 * 加载双图对比过滤图像
	 * 
	 * @param innerName
	 * @return
	 */
	final static public LImage loadDoubleFilterImage(final String innerName) {
		LImage result = GraphicsUtils.loadImage(innerName);
		return GraphicsUtils.loadDoubleFilterImage(result, Math.round(result
				.getWidth() / 2), Math.round(result.getHeight()));
	}

	/**
	 * 加载双图对比过滤图像
	 * 
	 * @param innerName
	 * @param width
	 * @param height
	 * @return
	 */
	final static public LImage loadFilterGameImage(final String innerName,
			final int width, final int height) {
		return GraphicsUtils.loadDoubleFilterImage(GraphicsUtils
				.loadImage(innerName), width, height);
	}

	/**
	 * 加载双图对比过滤图像
	 * 
	 * @param img
	 * @param width
	 * @param height
	 * @return
	 */
	final static public LImage loadDoubleFilterImage(final LImage img,
			final int width, final int height) {
		LImage img1 = GraphicsUtils.drawClipImage(img, width, height, 0, 0,
				true);
		LImage img2 = GraphicsUtils.drawClipImage(img, width, height, width, 0,
				true);
		int[] basePixels1 = img1.getPixels();
		int[] basePixels2 = img2.getPixels();
		int length = basePixels2.length;
		int pixel = LColor.getPixel(200, 200, 200);
		for (int i = 0; i < length; i++) {
			if (basePixels2[i] >= pixel) {
				basePixels2[i] = 0xffffff;
			} else {
				basePixels2[i] = basePixels1[i];
			}
		}
		img1.dispose();
		img1 = null;
		img2.setPixels(basePixels2, width, height);
		return img2;
	}

	/**
	 * 将指定图像保存为PNG格式
	 * 
	 * @param bitmap
	 * @param output
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean saveAsPNG(Bitmap bitmap, String fileName)
			throws FileNotFoundException {
		return bitmap.compress(Bitmap.CompressFormat.PNG, 1,
				new FileOutputStream(fileName));
	}

	/**
	 * 将指定图像保存为PNG格式
	 * 
	 * @param image
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 */
	public static boolean saveAsPNG(LImage image, String fileName)
			throws FileNotFoundException {
		return image.getBitmap().compress(Bitmap.CompressFormat.PNG, 1,
				new FileOutputStream(fileName));
	}

	/**
	 * 清空缓存
	 */
	public static void destroyImages() {
		lazyImages.clear();
		lazySplitMap.clear();
	}

}
