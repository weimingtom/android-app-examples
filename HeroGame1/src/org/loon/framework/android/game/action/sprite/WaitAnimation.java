package org.loon.framework.android.game.action.sprite;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.geom.Ellipse2D;
import org.loon.framework.android.game.core.graphics.geom.Shape;

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
public class WaitAnimation {

	private static final LColor defaultBlackColor = new LColor(0.5f, 0.5f, 0.5f);

	private static final LColor defaultWhiteColor = new LColor(240, 240, 240);

	private LColor color = defaultBlackColor;

	private double r;

	private final List<Object> list;

	private boolean isRunning = false;

	private static final double sx = 1.0D, sy = 1.0D;

	public WaitAnimation(int width, int height) {
		int r1 = width / 8, r2 = height / 8;
		this.r = (r1 < r2 ? r1 : r2) / 2;
		this.list = new ArrayList<Object>(Arrays.asList(new Object[] {
				new Ellipse2D.Double(sx + 3 * r, sy + 0 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 5 * r, sy + 1 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 6 * r, sy + 3 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 5 * r, sy + 5 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 3 * r, sy + 6 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 1 * r, sy + 5 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 0 * r, sy + 3 * r, 2 * r, 2 * r),
				new Ellipse2D.Double(sx + 1 * r, sy + 1 * r, 2 * r, 2 * r) }));
	}

	public void setColor(LColor color) {
		this.color = color;
	}

	public void black() {
		this.color = defaultBlackColor;
	}

	public void white() {
		this.color = defaultWhiteColor;
	}

	public void next() {
		if (isRunning) {
			list.add(list.remove(0));
		}
	}

	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public void draw(LGraphics g, int x, int y, int w, int h) {
		LColor oldColor = g.getColor();
		g.setAntiAlias(true);
		g.setColor(color);
		float alpha = 0.0f;
		int nx = x + w / 2 - (int) r * 4, ny = y + h / 2 - (int) r * 4;
		g.translate(nx, ny);
		for (Iterator<Object> it = list.iterator(); it.hasNext();) {
			Shape s = (Shape) it.next();
			alpha = isRunning ? alpha + 0.1f : 0.5f;
			g.setAlpha(alpha);
			g.fill(s);
		}
		g.setAntiAlias(false);
		g.setAlpha(1.0F);
		g.translate(-nx, -ny);
		g.setColor(oldColor);
	}

}
