package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.geom.BasicStroke;
import org.loon.framework.android.game.core.graphics.geom.GeneralPath;
import org.loon.framework.android.game.core.graphics.geom.PathIterator;
import org.loon.framework.android.game.core.graphics.geom.Polygon;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.core.graphics.geom.Shape;
import org.loon.framework.android.game.core.graphics.geom.Stroke;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelXorXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint.Style;

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
public final class LGraphics {

	private final static double RAD_360 = Math.PI / 180 * 360;

	private LGraphicsStore store;

	private float[] mirror = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };

	private Matrix tmp_matrix = new Matrix();

	private Bitmap bitmap;

	private Canvas canvas;

	private Paint paint;

	private Rect clip;

	private LFont font;

	private boolean isClose;
	
	public int panelX = 0, panelY = 0;

	public LGraphics(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.canvas = new Canvas(bitmap);
		this.canvas.clipRect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		this.paint = new Paint();
		this.clip = canvas.getClipBounds();
		this.setFont(LFont.getFont());
		this.canvas.save(Canvas.CLIP_SAVE_FLAG);
	}

	public LGraphics(Bitmap bitmap, boolean flag) {
		this.bitmap = bitmap;
		this.canvas = new Canvas(bitmap);
	}

	public void save() {
		if (store == null) {
			store = new LGraphicsStore();
		}
		this.store.save(this);
	}

	public void restore() {
		if (store != null) {
			this.store.restore(this);
		}
	}

	public float[] getMatrix() {
		float[] f = new float[9];
		canvas.getMatrix().getValues(f);
		return f;
	}

	public LGraphics create() {
		return this;
	}

	public boolean isAntiAlias() {
		return paint.isAntiAlias();
	}

	public void setAntiAlias(boolean flag) {
		paint.setAntiAlias(flag);
	}

	public void setAlphaValue(int alpha) {
		paint.setAlpha(alpha);
	}

	public void setAlpha(float alpha) {
		setAlphaValue((int) (255 * alpha));
	}

	public float getAlpha() {
		return paint.getAlpha() / 255;
	}

	public float getAlphaValue() {
		return paint.getAlpha();
	}

	public void setColor(int r, int g, int b) {
		paint.setColor(LColor.getRGB(r, g, b));
	}

	public void setColorValue(int pixels) {
		paint.setColor(pixels);
	}

	public void setColor(int pixels) {
		paint.setColor(LColor.getRGB(pixels));
	}

	public void setColor(int r, int g, int b, int a) {
		paint.setColor(LColor.getARGB(r, g, b, a));
	}

	public void setColor(LColor c) {
		paint.setColor(c.getRGB());
	}

	public void setColorAll(LColor c) {
		paint.setColor(c.getRGB());
		canvas.drawColor(c.getRGB());
	}

	public LColor getColor() {
		return new LColor(paint.getColor());
	}

	public LFont getFont() {
		return font;
	}

	public void setFont(int size) {
		setFont(LFont.getFont(LSystem.FONT_NAME, 0, size));
	}

	public void setFont(String familyName, int style, int size) {
		setFont(LFont.getFont(familyName, style, size));
	}

	public void setFont(LFont font) {
		Paint typefacePaint = font.getTypefacePaint();
		if (this.paint != null) {
			this.paint.setTextSize(font.getSize());
			this.paint.setTypeface(typefacePaint.getTypeface());
			this.paint.setUnderlineText(typefacePaint.isUnderlineText());
		} else {
			this.paint = new Paint(typefacePaint);
		}
		this.font = font;
	}

	public boolean hitClip(int x, int y, int width, int height) {
		return getClipBounds().intersects(new Rectangle(x, y, width, height));
	}

	public Rectangle getClipBounds() {
		Rect r = canvas.getClipBounds();
		return new Rectangle(r.left, r.top, r.width(), r.height());
	}

	public void draw(Shape s) {
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawPath(getPath(s), paint);
		paint.setStyle(tmp);
	}

	public void fill(Shape s) {
		canvas.drawPath(getPath(s), paint);
	}

	public void fillRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		canvas.drawRoundRect(new RectF(x, y, x + width, y + height), arcWidth,
				arcHeight, paint);
	}

	private Path getPath(Shape s) {
		Path path = new Path();
		PathIterator pi = s.getPathIterator(null);
		while (pi.isDone() == false) {
			getSegment(pi, path);
			pi.next();
		}
		return path;
	}

	private void getSegment(PathIterator pi, Path path) {
		float[] coordinates = new float[6];
		int type = pi.currentSegment(coordinates);
		switch (type) {
		case PathIterator.SEG_MOVETO:
			path.moveTo(coordinates[0], coordinates[1]);
			break;
		case PathIterator.SEG_LINETO:
			path.lineTo(coordinates[0], coordinates[1]);
			break;
		case PathIterator.SEG_QUADTO:
			path.quadTo(coordinates[0], coordinates[1], coordinates[2],
					coordinates[3]);
			break;
		case PathIterator.SEG_CUBICTO:
			path.cubicTo(coordinates[0], coordinates[1], coordinates[2],
					coordinates[3], coordinates[4], coordinates[5]);
			break;
		case PathIterator.SEG_CLOSE:
			path.close();
			break;
		default:
			break;
		}
	}

	public void setXORMode(LColor color) {
		paint.setXfermode(new PixelXorXfermode(color.getRGB()));
	}

	public void setPaintMode() {
		paint.setXfermode(null);
	}

	public void setFill() {
		paint.setStyle(Style.FILL);
	}

	public Stroke getStroke() {
		if (paint != null) {
			return new BasicStroke(paint.getStrokeWidth(), paint.getStrokeCap()
					.ordinal(), paint.getStrokeJoin().ordinal());
		}
		return null;
	}

	public void setStroke(Stroke s) {
		BasicStroke bs = (BasicStroke) s;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(bs.getLineWidth());
		int cap = bs.getEndCap();
		if (cap == 0) {
			paint.setStrokeCap(Paint.Cap.BUTT);
		} else if (cap == 1) {
			paint.setStrokeCap(Paint.Cap.ROUND);
		} else if (cap == 2) {
			paint.setStrokeCap(Paint.Cap.SQUARE);
		}
		int join = bs.getLineJoin();
		if (join == 0) {
			paint.setStrokeJoin(Paint.Join.MITER);
		} else if (join == 1) {
			paint.setStrokeJoin(Paint.Join.ROUND);
		} else if (join == 2) {
			paint.setStrokeJoin(Paint.Join.BEVEL);
		}
	}

	public void rotate(double theta) {
		Matrix matrix = canvas.getMatrix();
		matrix.preRotate((float) LSystem.getDegree((float) (RAD_360 - theta)));
		canvas.concat(matrix);
	}

	public void rotate(double theta, double x, double y) {
		Matrix matrix = canvas.getMatrix();
		matrix.preRotate((float) LSystem.getDegree((float) theta), (float) x,
				(float) y);
		canvas.concat(matrix);
	}

	public void scale(double sx, double sy) {
		Matrix matrix = canvas.getMatrix();
		matrix.setScale((float) sx, (float) sy);
		canvas.concat(matrix);
	}

	public void rectFill(int x, int y, int width, int height, LColor color) {
		setColor(color);
		fillRect(x, y, width, height);
	}

	public void rectDraw(int x, int y, int width, int height, LColor color) {
		setColor(color);
		drawRect(x, y, width, height);
	}

	public void rectOval(int x, int y, int width, int height, LColor color) {
		setColor(color);
		drawOval(x, y, width, height);
		fillOval(x, y, width, height);
	}

	public void drawSixStart(LColor color, int x, int y, int r) {
		setColor(color);
		drawTriangle(color, x, y, r);
		drawRTriangle(color, x, y, r);
	}

	public void drawTriangle(LColor color, int x, int y, int r) {
		int x1 = x;
		int y1 = y - r;
		int x2 = x - (int) (r * Math.cos(Math.PI / 6));
		int y2 = y + (int) (r * Math.sin(Math.PI / 6));
		int x3 = x + (int) (r * Math.cos(Math.PI / 6));
		int y3 = y + (int) (r * Math.sin(Math.PI / 6));
		int[] xpos = new int[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		int[] ypos = new int[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	public void drawRTriangle(LColor color, int x, int y, int r) {
		int x1 = x;
		int y1 = y + r;
		int x2 = x - (int) (r * Math.cos(Math.PI / 6.0));
		int y2 = y - (int) (r * Math.sin(Math.PI / 6.0));
		int x3 = x + (int) (r * Math.cos(Math.PI / 6.0));
		int y3 = y - (int) (r * Math.sin(Math.PI / 6.0));
		int[] xpos = new int[3];
		xpos[0] = x1;
		xpos[1] = x2;
		xpos[2] = x3;
		int[] ypos = new int[3];
		ypos[0] = y1;
		ypos[1] = y2;
		ypos[2] = y3;
		setColor(color);
		fillPolygon(xpos, ypos, 3);
	}

	public void drawBitmap(Bitmap bitmap, int x, int y) {
		canvas.drawBitmap(bitmap, x, y, paint);
	}

	public void drawImage(String fileName, int x, int y, int w, int h) {
		drawImage(GraphicsUtils.loadImage(fileName, true), x, y, w, h);
	}

	public void drawImage(String fileName, int x, int y) {
		drawImage(GraphicsUtils.loadImage(fileName, true), x, y);
	}

	public void drawImage(LImage img, int x, int y) {
		if (img != null) {
			canvas.drawBitmap(img.getBitmap(), x + panelX, y + panelY, paint);
		}
	}

	public void drawBitmap(Bitmap bitmap, int x, int y, int w, int h) {
		Rect srcR = new Rect(0, 0, w, h);
		Rect dstR = new Rect(x, y, x + w, y + h);
		canvas.drawBitmap(bitmap, srcR, dstR, paint);
	}

	public void drawImage(LImage img, Matrix marMatrix, boolean filter) {
		if (img != null) {
			paint.setFilterBitmap(filter);
			canvas.drawBitmap(img.getBitmap(), marMatrix, paint);
			paint.setFilterBitmap(false);
		}
	}

	public void drawImage(LImage img, Matrix marMatrix) {
		canvas.drawBitmap(img.getBitmap(), marMatrix, paint);

	}

	public void drawBitmap(Bitmap bitmap, Matrix marMatrix, boolean filter) {
		paint.setFilterBitmap(filter);
		canvas.drawBitmap(bitmap, marMatrix, paint);
		paint.setFilterBitmap(false);
	}

	public void drawBitmap(Bitmap bitmap, Matrix marMatrix) {
		canvas.drawBitmap(bitmap, marMatrix, paint);
	}

	public void drawMirrorBitmap(Bitmap bitmap, int x, int y, boolean filter) {
		if (bitmap != null) {
			tmp_matrix.reset();
			tmp_matrix.setValues(mirror);
			int width = bitmap.getWidth();
			int height = bitmap.getHeight();
			canvas.drawBitmap(Bitmap.createBitmap(bitmap, 0, 0, width, height,
					tmp_matrix, filter), x, y, paint);
		}
	}

	public void drawMirrorImage(LImage img, int x, int y, boolean filter) {
		drawMirrorBitmap(img.getBitmap(), x, y, filter);
	}

	public void drawFlipImage(Bitmap bitmap, int x, int y, boolean filter) {
		if (bitmap != null) {
			tmp_matrix.reset();
			tmp_matrix.postRotate(180);
			Bitmap dst = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), tmp_matrix, filter);
			canvas.drawBitmap(dst, x, y, paint);
		}
	}

	public void drawFlipImage(LImage img, int x, int y, boolean filter) {
		drawFlipImage(img, x, y, filter);
	}

	public void drawReverseBitmap(Bitmap bitmap, int x, int y, boolean filter) {
		if (bitmap != null) {
			tmp_matrix.reset();
			tmp_matrix.postRotate(270);
			Bitmap dst = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), tmp_matrix, filter);
			canvas.drawBitmap(dst, x, y, paint);
		}
	}

	public void drawReverseImage(LImage img, int x, int y, boolean filter) {
		drawReverseBitmap(img.getBitmap(), x, y, filter);
	}

	public void drawImage(LImage img, int x, int y, int w, int h) {
		if (img != null) {
			int width = img.getWidth();
			int height = img.getHeight();
			if (width >= w || height >= h) {
				Rect srcR = new Rect(0, 0, w, h);
				Rect dstR = new Rect(x + panelX, y + panelY, x + w + panelX, y + h + panelY);
				canvas.drawBitmap(img.getBitmap(), srcR, dstR, paint);
			} else {
				float scaleWidth = ((float) w) / width;
				float scaleHeight = ((float) h) / height;
				tmp_matrix.reset();
				tmp_matrix.postScale(scaleWidth, scaleHeight);
				tmp_matrix.postTranslate(x + panelX, y + panelY);
				drawImage(img, tmp_matrix, false);
			}
		}
	}

	public void drawBitmap(Bitmap bitmap, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1) {
		canvas.drawBitmap(bitmap, new Rect(x1, y1, w1, h1),
				new Rect(x, y, w, h), paint);
	}

	public void drawBitmap(Bitmap bitmap, Rect r1, Rect r2) {
		canvas.drawBitmap(bitmap, r1, r2, null);
	}

	public void drawImage(LImage img, int x, int y, int w, int h, int x1,
			int y1, int w1, int h1) {
		if (img != null) {
			x = x + panelX;
			y = y + panelY;
			w = w + panelX;
			h = h + panelY;
			canvas.drawBitmap(img.getBitmap(), new Rect(x1, y1, w1, h1),
					new Rect(x, y, w, h), paint);
		}
	}

	public void drawArc(int x, int y, int width, int height, int sa, int ea) {
		if (paint == null) {
			paint = new Paint();
		}
		paint.setStrokeWidth(0);
		canvas.drawArc(new RectF(x, y, x + width, y + height), 360 - (ea + sa),
				ea, true, paint);
	}

	public void drawLine(int x1, int y1, int x2, int y2) {
		if (x1 > x2) {
			x1++;
		} else {
			x2++;
		}
		if (y1 > y2) {
			y1++;
		} else {
			y2++;
		}
		x1 = x1 + panelX;
		y1 = y1 + panelY;
		x2 = x2 + panelX;
		y2 = y2 + panelY;
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	public void drawRect(int x, int y, int width, int height) {
		x = x + panelX;
		y = y + panelY;
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(x, y, x + width, y + height, paint);
		paint.setStyle(tmp);
	}

	public void drawBytes(byte[] message, int offset, int length, int x, int y) {
		drawString(new String(message, offset, length), x, y);
	}

	public void drawChars(char[] message, int offset, int length, int x, int y) {
		canvas.drawText(message, offset, length, x, y, paint);
	}

	public void drawString(String message, int x, int y) {
		Paint.Style tmp = paint.getStyle();
		paint.setStrokeWidth(0);
		canvas.drawText(message.toCharArray(), 0, message.toCharArray().length,
				x + panelX, y + panelY, paint);
		paint.setStyle(tmp);
	}

	public void drawSubString(String message, int x, int y, int w, int h, int i1) {
		canvas.drawText(message, x, x + y, w, h, paint);
	}

	public void drawString(String message, float x, float y) {
		Path pth = new Path();
		paint.getTextPath(message, 0, message.length(), x + panelX, y + panelY, pth);
		canvas.drawPath(pth, paint);
	}

	public void drawStyleString(String message, int x, int y, int color,
			int color1) {
		x = x + panelX;
		y = y + panelY;
		paint.setColor(color);
		canvas.drawText(message, x + 1, y, paint);
		canvas.drawText(message, x - 1, y, paint);
		canvas.drawText(message, x, y + 1, paint);
		canvas.drawText(message, x, y - 1, paint);
		paint.setColor(color1);
		canvas.drawText(message, x, y, paint);
	}

	public void drawStyleString(String message, int x, int y, LColor c1,
			LColor c2) {
		x = x + panelX;
		y = y + panelY;
		paint.setColor(c1.getRGB());
		canvas.drawText(message, x + 1, y, paint);
		canvas.drawText(message, x - 1, y, paint);
		canvas.drawText(message, x, y + 1, paint);
		canvas.drawText(message, x, y - 1, paint);
		paint.setColor(c2.getRGB());
		canvas.drawText(message, x, y, paint);
	}

	public void drawClear() {
		paint.setColor(Color.BLACK);
		canvas.drawColor(Color.BLACK);
	}

	public void backgroundColor(LColor color) {
		paint.setColor(color.getRGB());
		canvas.drawColor(color.getRGB());
	}

	public void fillArc(int x, int y, int width, int height, int startAngle,
			int arcAngle) {
		canvas.drawArc(new RectF(x, y, x + width, y + height), startAngle,
				arcAngle, true, paint);
	}

	public void fillOval(int x, int y, int width, int height) {
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
	}

	public void fillPolygon(int[] xpoints, int[] ypoints, int npoints) {
		canvas.save(Canvas.CLIP_SAVE_FLAG);
		GeneralPath filledPolygon = new GeneralPath(GeneralPath.WIND_EVEN_ODD,
				npoints);
		filledPolygon.moveTo(xpoints[0], ypoints[0]);
		for (int index = 1; index < xpoints.length; index++) {
			filledPolygon.lineTo(xpoints[index], ypoints[index]);
		}
		filledPolygon.closePath();

		Path path = getPath(filledPolygon);

		canvas.clipPath(path);
		canvas.drawPath(path, paint);
		canvas.restore();
	}

	public void fillPolygon(Polygon p) {
		fillPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public void fillRect(int x, int y, int width, int height) {
		x = x + panelX;
		y = y + panelY;
		canvas.drawRect(x, y, x + width, y + height, paint);
	}

	public void fillAlphaRect(int x, int y, int w, int h, LColor c) {
		fillAlphaRect(x, y, w, h, c.getRGB());
	}

	public void fillAlphaRect(int x, int y, int w, int h, int pixel) {
		int color = paint.getColor();
		paint.setColor(pixel);
		float f = x;
		float f1 = y;
		float f2 = x + w;
		float f3 = y + h;
		canvas.drawRect(f, f1, f2, f3, paint);
		paint.setColor(color);
	}

	public void fillTriangle(int x, int y, int w, int h, int w1, int h1) {
		Path path = new Path();
		float f = x;
		float f1 = y;
		path.moveTo(f, f1);
		float f2 = w;
		float f3 = h;
		path.lineTo(f2, f3);
		float f4 = w1;
		float f5 = h1;
		path.lineTo(f4, f5);
		canvas.drawPath(path, paint);
	}

	public void fill3DRect(int x, int y, int width, int height, boolean raised) {
		LColor color = getColor();
		LColor colorUp, colorDown;
		if (raised) {
			colorUp = color.brighter();
			colorDown = color.darker();
			setColor(color);
		} else {
			colorUp = color.darker();
			colorDown = color.brighter();
			setColor(colorUp);
		}

		width--;
		height--;
		fillRect(x + 1, y + 1, width - 1, height - 1);

		setColor(colorUp);
		fillRect(x, y, width, 1);
		fillRect(x, y + 1, 1, height);

		setColor(colorDown);
		fillRect(x + width, y, 1, height);
		fillRect(x + 1, y + height, width, 1);
	}

	public void draw3DRect(Rectangle rect, LColor back, boolean down) {
		int x1 = rect.x;
		int y1 = rect.y;
		int x2 = rect.x + rect.width - 1;
		int y2 = rect.y + rect.height - 1;
		if (!down) {
			setColor(back);
			drawLine(x1, y1, x1, y2);
			drawLine(x1, y1, x2, y2);
			setColor(back.brighter());
			drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 1);
			drawLine(x1 + 1, y1 + 1, x2 - 1, y1 + 1);
			setColor(LColor.black);
			drawLine(x1, y2, x2, y2);
			drawLine(x2, y1, x2, y2);
			setColor(back.darker());
			drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
			drawLine(x2 - 1, y1 + 2, x2 - 1, y2 - 1);
		} else {
			setColor(LColor.black);
			drawLine(x1, y1, x1, y2);
			drawLine(x1, y1, x2, y1);
			setColor(back.darker());
			drawLine(x1 + 1, y1 + 1, x1 + 1, y2 - 1);
			drawLine(x1 + 1, y1 + 1, x2 - 1, y1 + 1);
			setColor(back.brighter());
			drawLine(x1, y2, x2, y2);
			drawLine(x2, y1, x2, y2);
			setColor(back);
			drawLine(x1 + 1, y2 - 1, x2 - 1, y2 - 1);
			drawLine(x2 - 1, y1 + 2, x2 - 1, y2 - 1);
		}
	}

	public void draw3DRect(int x, int y, int width, int height, boolean raised) {
		LColor color = getColor();
		LColor colorUp, colorDown;
		if (raised) {
			colorUp = color.brighter();
			colorDown = color.darker();
		} else {
			colorUp = color.darker();
			colorDown = color.brighter();
		}

		setColor(colorUp);
		fillRect(x, y, width, 1);
		fillRect(x, y + 1, 1, height);

		setColor(colorDown);
		fillRect(x + width, y, 1, height);
		fillRect(x + 1, y + height, width, 1);
	}

	public int getClipHeight() {
		return clip.bottom - clip.top;
	}

	public int getClipWidth() {
		return clip.right - clip.left;
	}

	public int getClipX() {
		return clip.left;
	}

	public int getClipY() {
		return clip.top;
	}

	public void clearRect(int x, int y, int width, int height) {
		fillRect(x, y, width, height);
	}

	public void clearScreen(int x, int y, int width, int height) {
		canvas.clipRect(x, y, x + width, y + height);
		LColor c = getColor();
		if (c != null) {
			canvas
					.drawARGB(c.getAlpha(), c.getBlue(), c.getGreen(), c
							.getRed());
		} else {
			canvas.drawARGB(0xff, 0xff, 0xff, 0xff);
		}
	}

	public void copyArea(int x, int y, int width, int height, int dx, int dy) {
		if (x < 0) {
			width += x;
			x = 0;
		}
		if (y < 0) {
			height += y;
			y = 0;
		}
		if (x + width > bitmap.getWidth()) {
			width = bitmap.getWidth() - x;
		}
		if (y + height > bitmap.getHeight()) {
			height = bitmap.getHeight() - y;
		}
		Bitmap copy = Bitmap.createBitmap(bitmap, x, y, width, height);
		canvas.drawBitmap(copy, x + dx, y + dy, null);
	}

	public void clipRect(int x, int y, int width, int height) {
		canvas.clipRect(x, y, x + width, y + height);
		clip = canvas.getClipBounds();
	}

	public void shear(double shx, double shy) {
		Matrix martix = canvas.getMatrix();
		martix.setSkew((float) shx, (float) shy);
		canvas.concat(martix);
	}

	public void translate(double tx, double ty) {
		Matrix martix = canvas.getMatrix();
		martix.setTranslate((float) tx, (float) ty);
		canvas.concat(martix);
	}

	public void translate(int x, int y) {
		Matrix martix = canvas.getMatrix();
		martix.setTranslate((float) x, (float) y);
		canvas.concat(martix);
	}

	public void setClip(Rect rect) {
		setClip(rect.left, rect.top, rect.width(), rect.height());
	}

	public void setClip(int x, int y, int width, int height) {
		if (x == clip.left && x + width == clip.right && y == clip.top
				&& y + height == clip.bottom) {
			return;
		}
		if (x < clip.left || x + width > clip.right || y < clip.top
				|| y + height > clip.bottom) {
			canvas.restore();
			canvas.save(Canvas.CLIP_SAVE_FLAG);
		}
		clip.left = x;
		clip.top = y;
		clip.right = x + width;
		clip.bottom = y + height;
		canvas.clipRect(clip);
	}

	public void drawOval(int x, int y, int width, int height) {
		Paint.Style tmp = paint.getStyle();
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawOval(new RectF(x, y, x + width, y + height), paint);
		paint.setStyle(tmp);
	}

	public void drawPolygon(int[] xpoints, int[] ypoints, int npoints) {
		canvas.drawLine(xpoints[npoints - 1], ypoints[npoints - 1], xpoints[0],
				ypoints[0], paint);
		for (int i = 0; i < npoints - 1; i++) {
			canvas.drawLine(xpoints[i], ypoints[i], xpoints[i + 1],
					ypoints[i + 1], paint);
		}
	}

	public void drawPolygon(Polygon p) {
		drawPolygon(p.xpoints, p.ypoints, p.npoints);
	}

	public void drawPolyline(int[] xpoints, int[] ypoints, int npoints) {
		for (int i = 0; i < npoints - 1; i++) {
			drawLine(xpoints[i], ypoints[i], xpoints[i + 1], ypoints[i + 1]);
		}
	}

	public void drawRoundRect(int x, int y, int width, int height,
			int arcWidth, int arcHeight) {
		if (paint == null) {
			paint = new Paint();
		}
		canvas.drawRoundRect(new RectF(x, y, width, height), arcWidth,
				arcHeight, paint);
	}

	public Canvas getCanvas() {
		return canvas;
	}

	public Paint getPaint() {
		return paint;
	}

	public Rect getClip() {
		return clip;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public void setBitmap(Bitmap bitmap) {
		this.canvas.setBitmap(bitmap);
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	protected boolean isClose() {
		return isClose;
	}

	public void dispose() {
		isClose = true;
		font = null;
		paint = null;
		canvas = null;
	}

}
