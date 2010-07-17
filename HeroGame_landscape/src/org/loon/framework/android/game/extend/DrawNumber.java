package org.loon.framework.android.game.extend;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.utils.NumberUtils;

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
public class DrawNumber {
	// 0
	public static final int[][] ZERO = { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 0, 1 },
			{ 1, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 }, };

	// 1
	public static final int[][] ONE = { { 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 },
			{ 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, };

	// 2
	public static final int[][] TWO = { { 1, 1, 1 }, { 0, 0, 1 }, { 1, 1, 1 },
			{ 1, 0, 0 }, { 1, 0, 0 }, { 1, 1, 1 }, };

	// 3
	public static final int[][] THREE = { { 1, 1, 1 }, { 0, 0, 1 },
			{ 1, 1, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, { 1, 1, 1 }, };

	// 4
	public static final int[][] FOUR = { { 1, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 },
			{ 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, };

	// 5
	public static final int[][] FIVE = { { 1, 1, 1 }, { 1, 0, 0 }, { 1, 1, 1 },
			{ 0, 0, 1 }, { 0, 0, 1 }, { 1, 1, 1 }, };

	// 6
	public static final int[][] SIX = { { 1, 1, 1 }, { 1, 0, 0 }, { 1, 1, 1 },
			{ 1, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 }, };

	// 7
	public static final int[][] SEVEN = { { 1, 1, 1 }, { 0, 0, 1 },
			{ 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, };

	// 8
	public static final int[][] EIGHT = { { 1, 1, 1 }, { 1, 0, 1 },
			{ 1, 1, 1 }, { 1, 0, 1 }, { 1, 0, 1 }, { 1, 1, 1 }, };

	// 9
	public static final int[][] NINE = { { 1, 1, 1 }, { 1, 0, 1 }, { 1, 1, 1 },
			{ 0, 0, 1 }, { 0, 0, 1 }, { 0, 0, 1 }, };

	private int unit;

	private LColor color;

	public DrawNumber() {
		this(LColor.white);
	}

	public DrawNumber(LColor color) {
		this(color, 5);
	}

	public DrawNumber(int unit) {
		this(LColor.white, unit);
	}

	public DrawNumber(LColor color, int unit) {
		this.color = color;
		this.unit = unit;
	}

	public void setUnit(int unit) {
		this.unit = unit;
	}

	public void setColor(LColor color) {
		if (color == null) {
			return;
		}
		this.color = color;
	}

	public int getUnit() {
		return unit;
	}

	private int[][] getNum(int num) {
		if (num == 0) {
			return ZERO;
		} else if (num == 1) {
			return ONE;
		} else if (num == 2) {
			return TWO;
		} else if (num == 3) {
			return THREE;
		} else if (num == 4) {
			return FOUR;
		} else if (num == 5) {
			return FIVE;
		} else if (num == 6) {
			return SIX;
		} else if (num == 7) {
			return SEVEN;
		} else if (num == 8) {
			return EIGHT;
		} else if (num == 9) {
			return NINE;
		} else {
			return ZERO;
		}
	}

	public void drawNumber(LGraphics g, int x, int y, int[][] num) {
		g.setColor(color);
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 6; j++) {
				if (num[j][i] == 1)
					g.fillRect(x + (unit * i), y + (unit * j), unit, unit);
			}
		}
	}

	public void drawNumber(LGraphics g, int x, int y, String num) {
		int index, size = unit * 10, offset = unit / 2;
		for (int i = 0; i < num.length(); i++) {
			String number = num.substring(i, i + 1);
			if (NumberUtils.isNumber(number)) {
				index = Integer.parseInt(number);
				drawNumber(g, x + (unit * (4 * i)), y, getNum(index));
			} else {
				LFont oldFont = g.getFont();
				g.setFont(size);
				g.drawString(number, x + (unit * (4 * i)), y + size / 2
						+ offset);
				g.setFont(oldFont);
			}
		}
	}

}