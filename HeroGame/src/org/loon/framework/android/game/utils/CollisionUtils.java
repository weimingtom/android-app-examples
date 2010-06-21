package org.loon.framework.android.game.utils;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.action.sprite.Mask;
import org.loon.framework.android.game.action.sprite.Sprite;
import org.loon.framework.android.game.core.graphics.geom.Line2D;
import org.loon.framework.android.game.core.graphics.geom.Point2D;

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
 * @version 0.1.2
 */
public class CollisionUtils {

	private static final double rad = Math.PI / 180;

	/**
	 * 获得两个矩形间距离
	 * 
	 * @param box1
	 * @param box2
	 * @return
	 */
	public static double getDistance(final RectBox box1, final RectBox box2) {
		final double xdiff = box1.x - box2.x;
		final double ydiff = box1.y - box2.y;
		return Math.sqrt(xdiff * xdiff + ydiff * ydiff);
	}

	/**
	 * 检查两个矩形是否发生了碰撞
	 * 
	 * @param rect1
	 * @param rect2
	 * @return
	 */
	public static boolean isRectToRect(RectBox rect1, RectBox rect2) {
		return rect1.intersects(rect2);
	}

	/**
	 * 判断两个圆形是否发生了碰撞
	 * 
	 * @param rect1
	 * @param rect2
	 * @return
	 */
	public static boolean isCircToCirc(RectBox rect1, RectBox rect2) {
		Point2D middle1 = CollisionUtils.getMiddlePoint(rect1);
		Point2D middle2 = CollisionUtils.getMiddlePoint(rect2);
		double distance = middle1.distance(middle2);
		double radius1 = rect1.getWidth() / 2;
		double radius2 = rect2.getWidth() / 2;
		return (distance - radius2) < radius1;
	}

	/**
	 * 检查矩形与圆形是否发生了碰撞
	 * 
	 * @param rect1
	 * @param rect2
	 * @return
	 */
	public static boolean isRectToCirc(RectBox rect1, RectBox rect2) {
		double radius = rect2.getWidth() / 2;
		Point2D middle = CollisionUtils.getMiddlePoint(rect2);
		Point2D upperLeft = new Point2D.Double(rect1.getMinX(), rect1.getMinY());
		Point2D upperRight = new Point2D.Double(rect1.getMaxX(), rect1
				.getMinY());
		Point2D downLeft = new Point2D.Double(rect1.getMinX(), rect1.getMaxY());
		Point2D downRight = new Point2D.Double(rect1.getMaxX(), rect1.getMaxY());
		boolean collided = true;
		if (!isPointToLine(upperLeft, upperRight, middle, radius)) {
			if (!isPointToLine(upperRight, downRight, middle, radius)) {
				if (!isPointToLine(upperLeft, downLeft, middle, radius)) {
					if (!isPointToLine(downLeft, downRight, middle, radius)) {
						collided = false;
					}
				}
			}
		}
		return collided;
	}

	public static boolean isPixelHit(Sprite src, Sprite dest) {
		return isPixelHit(src, dest, src.getMask(), dest.getMask());
	}

	/**
	 * 检查指定精灵像素点是否发生碰撞
	 * 
	 * @param src
	 * @param dest
	 * @param mhome
	 * @param mdest
	 * @return
	 */
	public static boolean isPixelHit(ISprite src, ISprite dest, Mask mhome,
			Mask mdest) {
		try {
			if (src == null || dest == null) {
				return false;
			}
			if (mhome == null || mdest == null) {
				return false;
			}
			if (mhome.full && mdest.full) {
				return true;
			}
			if (src.getWidth() != mhome.width
					|| src.getHeight() != mhome.height
					|| dest.getWidth() != mdest.width
					|| dest.getHeight() != mdest.height) {
				return trueHitResized(src, dest, mhome, mdest);
			}
			final double x = src.getX();
			final double y = src.getY();
			final double x1 = dest.getX();
			final double y1 = dest.getY();
			final int xstart = (int) Math.max(x, x1);
			final int ystart = (int) Math.max(y, y1);
			final int xend = (int) Math.min(x + mhome.width - 1, x1
					+ mdest.width - 1);
			final int yend = (int) Math.min(y + mhome.height - 1, y1
					+ mdest.height - 1);
			final int Toty = Math.abs(yend - ystart);
			final int Totx = Math.abs(xend - xstart);
			final int xstarth = Math.abs(xstart - (int) x);
			final int ystarth = Math.abs(ystart - (int) y);
			final int xstartd = Math.abs(xstart - (int) x1);
			final int ystartd = Math.abs(ystart - (int) y1);
			int ox, oy;
			int ny, ny1, nx, nx1;
			if (!(mhome.full || mdest.full)) {
				for (oy = 0; oy < Toty; oy++) {
					ny = ystarth + oy;
					ny1 = ystartd + oy;
					for (ox = 0; ox < Totx; ox++) {
						nx = xstarth + ox;
						nx1 = xstartd + ox;
						if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1])
							return true;
					}
				}
			} else {
				Mask mask = mhome.full ? mdest : mhome;
				final int ys = mhome.full ? ystartd : ystarth;
				final int xs = mhome.full ? xstartd : xstarth;
				for (oy = 0; oy < Toty; oy++) {
					ny = ys + oy;
					for (ox = 0; ox < Totx; ox++) {
						nx = xs + ox;
						if (mask.mask[nx][ny])
							return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean trueHitAlpha(Sprite home, Sprite dest, float alpha,
			float beta) {
		return newAlphaTest(home, dest, home.getMask(), dest.getMask(), alpha,
				beta);
	}

	public static boolean newAlphaTest(ISprite home, ISprite dest, Mask mhome,
			Mask mdest, float alpha, float beta) {
		alpha *= rad;
		beta *= rad;
		final int x = (int) home.getX();
		final int y = (int) home.getY();
		final int x1 = (int) dest.getX();
		final int y1 = (int) dest.getY();
		final int width = home.getWidth();
		final int height = home.getHeight();
		final int width1 = dest.getWidth();
		final int height1 = dest.getHeight();
		final int xstart = (int) Math.min(x, x1);
		final int ystart = (int) Math.min(y, y1);
		final int relativex = x - xstart;
		final int relativex1 = x1 - xstart;
		final int relativey = y - ystart;
		final int relativey1 = y1 - ystart;
		final int diag = (int) Math.sqrt(width * width + height * height);
		final int diag1 = (int) Math.sqrt(width1 * width1 + height1 * height1);
		final int totX = diag + diag1 + 2;
		final int totY = diag + diag1 + 4;
		int area[][] = new int[totX][totY];
		final int acx = diag / 2 + relativex;
		final int acy = diag / 2 + relativey;
		final int acx1 = diag1 / 2 + relativex1;
		final int acy1 = diag1 / 2 + relativey1;
		final int cx = width / 2;
		final int cy = height / 2;
		final int cx1 = width1 / 2;
		final int cy1 = height1 / 2;
		final float scalex = (float) width / (float) mhome.width;
		final float scaley = (float) height / (float) mhome.height;
		final float scalex1 = (float) width1 / (float) mdest.width;
		final float scaley1 = (float) height1 / (float) mdest.height;
		int i = 0, j = 0, i1 = 0, j1 = 0;
		int px, py;
		int npx;
		boolean noend = true, noend1 = true;
		final double sinalpha = Math.sin(alpha);
		final double cosalpha = Math.cos(alpha);
		final double sinbeta = Math.sin(beta);
		final double cosbeta = Math.cos(beta);
		while (true) {
			if (noend) {
				if (mhome.mask[(int) (i / scalex)][(int) (j / scaley)]) {
					npx = px = i - cx;
					py = j - cy;
					npx = (int) Math
							.round(((double) px * cosalpha - (double) py
									* sinalpha));
					py = (int) Math.round(((double) py * cosalpha + (double) px
							* sinalpha));
					npx += acx;
					py += acy;
					if (py < totY && npx < totX) {
						if (area[npx][py] == 2) {
							return true;
						}
						area[npx][py] = 1;
					}
				}
				++i;
				if (i == width) {
					i = 0;
					++j;
					if (j == height) {
						if (noend1 == false)
							return false;
						noend = false;
					}
				}
			}
			if (noend1) {
				if (mdest.mask[(int) (i1 / scalex1)][(int) (j1 / scaley1)]) {
					npx = px = i1 - cx1;
					py = j1 - cy1;
					npx = (int) Math.round(((double) px * cosbeta - (double) py
							* sinbeta));
					py = (int) Math.round(((double) py * cosbeta + (double) px
							* sinbeta));
					npx += acx1;
					py += acy1;
					if (py < totY && npx < totX) {
						if (area[npx][py] == 1) {
							return true;
						}
						area[npx][py] = 2;
					}
				}
				++i1;
				if (i1 == width1 - 1) {
					i1 = 0;
					++j1;
					if (j1 == height1 - 1) {
						if (noend == false)
							return false;
						noend1 = false;
					}
				}
			}
		}
	}

	public static boolean trueHitResized(ISprite src, ISprite dest, Mask mhome,
			Mask mdest) {
		try {
			final double x = src.getX();
			final double y = src.getY();
			final double x1 = dest.getX();
			final double y1 = dest.getY();
			final int xstart = (int) Math.max(x, x1);
			final int ystart = (int) Math.max(y, y1);
			final int xend = (int) Math.min(x + src.getWidth() - 1, x1
					+ dest.getWidth() - 1);
			final int yend = (int) Math.min(y + src.getHeight() - 1, y1
					+ dest.getHeight() - 1);
			final int Toty = Math.abs(yend - ystart);
			final int Totx = Math.abs(xend - xstart);
			final int xstarth = Math.abs(xstart - (int) x);
			final int ystarth = Math.abs(ystart - (int) y);
			final int xstartd = Math.abs(xstart - (int) x1);
			final int ystartd = Math.abs(ystart - (int) y1);
			int ox, oy;
			final float fhx = (float) src.getWidth() / (float) mhome.width;
			final float fhy = (float) src.getHeight() / (float) mhome.height;
			final float fdx = (float) dest.getWidth() / (float) mdest.width;
			final float fdy = (float) dest.getHeight() / (float) mdest.height;
			int ny, ny1, nx, nx1;
			if (!(mhome.full || mdest.full)) {
				for (oy = 0; oy < Toty; oy++) {
					ny = (int) ((ystarth + oy) / fhy);
					ny1 = (int) ((ystartd + oy) / fdy);
					for (ox = 0; ox < Totx; ox++) {
						nx = (int) ((xstarth + ox) / fhx);
						nx1 = (int) ((xstartd + ox) / fdx);
						if (mhome.mask[nx][ny] && mdest.mask[nx1][ny1])
							return true;
					}
				}
			} else {
				Mask mask = mhome.full ? mdest : mhome;
				final int ys = mhome.full ? ystartd : ystarth;
				final int xs = mhome.full ? xstartd : xstarth;
				final float factorx = mhome.full ? fdx : fhx;
				final float factory = mhome.full ? fdy : fhy;
				for (oy = 0; oy < Toty; oy++) {
					ny = (int) ((ys + oy) / factory);
					for (ox = 0; ox < Totx; ox++) {
						nx = (int) ((xs + ox) / factorx);
						if (mask.mask[nx][ny])
							return true;
					}
				}
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * 换算点线距离
	 * 
	 * @param point1
	 * @param point2
	 * @param middle
	 * @param radius
	 * @return
	 */
	private static boolean isPointToLine(Point2D point1, Point2D point2,
			Point2D middle, double radius) {
		Line2D line = new Line2D.Double(point1, point2);
		double distance = line.ptLineDist(middle);
		return distance < radius;
	}

	/**
	 * 返回中间距离的Point2D形式
	 * 
	 * @param rectangle
	 * @return
	 */
	private static Point2D getMiddlePoint(RectBox rectangle) {
		return new Point2D.Double(rectangle.getCenterX(), rectangle
				.getCenterY());
	}

}
