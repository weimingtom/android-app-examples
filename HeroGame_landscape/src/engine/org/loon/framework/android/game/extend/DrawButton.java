package org.loon.framework.android.game.extend;

import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Color;

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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class DrawButton {

	private int id;

	public boolean usable;

	private boolean complete;

	public boolean click;

	private boolean select;

	private String label;

	private int width;

	private int height;

	private int x;

	private int y;

	private LImage buttonImage;

	private LImage selectImage;

	private Screen screen;
	
	public String identity = "";
	
	public static void initialize(final Screen screen,
			final DrawButton[] buttons, final int space, final LImage checked,
			final LImage unchecked) {
		DrawButton.initialize(screen, buttons, space, false, checked,
				unchecked == null ? GraphicsUtils.getGray(checked) : unchecked);
	}

	public static void initialize(final Screen screen,
			final DrawButton[] buttons, final int space, final boolean isRow,
			final LImage checked, final LImage unchecked) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new DrawButton(screen, i, space, isRow, checked,
					unchecked == null ? GraphicsUtils.getGray(checked)
							: unchecked);
			buttons[i].click = false;
			buttons[i].usable = true;
		}
	}

	public DrawButton(final Screen screen, final int no, final int space,
			final boolean isRow, final LImage selectImage,
			final LImage buttonImage) {
		this.screen = screen;
		this.id = no;
		this.buttonImage = buttonImage;
		this.selectImage = selectImage;
		this.usable = true;
		this.label = ("button" + no).intern();
		this.width = selectImage.getWidth();
		this.height = selectImage.getHeight();
		if (isRow) {
			this.x = space;
			this.y = 5 + (this.height + space) * id;
		} else {
			this.x = (this.width + space) * id;
			this.y = 5 + this.height;
		}
	}

	public void setDrawXY(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public LImage getSelectImage() {
		return selectImage;
	}

	public void setSelectImage(LImage selectImage) {
		this.selectImage = selectImage;
	}

	public void draw(final LGraphics g) {
		this.draw(g, null, 0, 0);
	}

	public void draw(final LGraphics g, final String fontName, final int type,
			final int size) {
		if (!this.click) {
			return;
		}
		LFont font = g.getFont();
		g.setAlpha(0.9f);
		if (complete || this.select) {
			g.drawImage(selectImage, (int) x, (int) y, this.width, this.height);
		} else {
			g.drawImage(buttonImage, (int) x, (int) y, this.width, this.height);
		}
		g.setAlpha(1.0f);
		if (fontName != null) {
			g.setFont(fontName, type, size);
		}
		int fontSize = g.getFont().stringWidth(label);
		int color = Color.WHITE;
		boolean isSelect = (complete && usable) || select;
		g.setAntiAlias(true);
		g.drawStyleString(label, (int) x + this.width / 2 - fontSize / 2,
				(int) y + this.height / 2 + 5, isSelect ? Color.RED
						: Color.BLACK, color);
		g.setAntiAlias(false);
		g.setFont(font);
	}

	public void setName(final String name) {
		this.click = true;
		label = name;

	}

	public int checkClick() {
		if (!this.click || !usable) {
			return -1;
		}
		if (screen.isTouchClick() && complete) {
			return id;
		}
		return -1;
	}

	public boolean checkComplete() {
		if (!this.click) {
			return false;
		}
		if (((double) screen.getTouch().x > x
				&& (double) screen.getTouch().x < x + (double) this.width
				&& (double) screen.getTouch().y > y && (double) screen
				.getTouch().y < y + (double) this.height)) {
			this.complete = true;
		} else {
			this.complete = false;
		}

		return this.complete;
	}

	public static boolean isAllUnchecked(final DrawButton[] buttons) {
		boolean result;
		for (int i = 0; i < buttons.length; i++) {
			result = buttons[i].isComplete();
			if (result) {
				return false;
			}
		}
		return true;
	}

	public boolean isClick() {
		return click;
	}

	public void setClick(boolean click) {
		this.click = click;
	}

	public LImage getImage() {
		return buttonImage;
	}

	public void setImage(LImage buttonImage) {
		this.buttonImage = buttonImage;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public boolean isUsable() {
		return usable;
	}

	public void setUsable(boolean usable) {
		this.usable = usable;
	}

	public boolean isSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
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

	public void move(int x, int y) {
		this.x = x;
		this.y = y;
	}

}