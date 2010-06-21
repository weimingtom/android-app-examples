package org.loon.framework.android.game.core.graphics.window;

import java.util.List;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LContainer;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.view.KeyEvent;
import android.view.MotionEvent;

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
public class LSelect extends LContainer {

	private LFont messageFont = LFont.getFont(20);

	private LColor fontColor = LColor.white;

	private int left, top, type, tmp_flag;

	private int sizeFont, doubleSizeFont, tmpOffset, messageLeft, nLeft,
			messageTop, nTop, selectSize, selectFlag;

	private float autoAlpha;

	private LTimer delay;

	private String[] selects;

	private String message, result;

	private LImage cursor, buoyage;

	private boolean isAutoAlpha, isSelect;

	public LSelect(int x, int y, int width, int height) {
		this((LImage) null, x, y, width, height);
	}

	public LSelect(String fileName) {
		this(fileName, 0, 0);
	}

	public LSelect(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName,true), x, y);
	}

	public LSelect(LImage formImage) {
		this(formImage, 0, 0);
	}

	public LSelect(LImage formImage, int x, int y) {
		this(formImage, x, y, formImage.getWidth(), formImage.getHeight());
	}

	public LSelect(LImage formImage, int x, int y, int width, int height) {
		super(x, y, width, height);
		if (formImage == null) {
			this.setBackground(new LImage(width, height,true));
			this.setAlpha(0.3F);
		} else {
			this.setBackground(formImage);
		}
		this.customRendering = true;
		this.selectFlag = 1;
		this.tmpOffset = -(width / 10);
		this.delay = new LTimer(150);
		this.autoAlpha = 0.25F;
		this.isAutoAlpha = true;
		this.setCursor("system/images/creese.png");
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public void setLeftOffset(int left) {
		this.left = left;
	}

	public void setTopOffset(int top) {
		this.top = top;
	}

	public int getLeftOffset() {
		return left;
	}

	public int getTopOffset() {
		return top;
	}

	public int getResultIndex() {
		return selectFlag - 1;
	}

	public void setDelay(long timer) {
		delay.setDelay(timer);
	}

	public long getDelay() {
		return delay.getDelay();
	}

	public String getResult() {
		return result;
	}

	private static String[] getListToStrings(List list) {
		if (list == null || list.size() == 0)
			return null;
		String[] result = new String[list.size()];
		for (int i = 0; i < result.length; i++) {
			result[i] = (String) list.get(i);
		}
		return result;
	}

	public void setMessage(String message, List list) {
		setMessage(message, getListToStrings(list));
	}

	public void setMessage(String[] selects) {
		setMessage(null, selects);
	}

	public void setMessage(String message, String[] selects) {
		this.message = message;
		this.selects = selects;
		this.selectSize = selects.length;
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			if (isAutoAlpha && buoyage != null) {
				if (delay.action(elapsedTime)) {
					if (autoAlpha < 0.95F) {
						autoAlpha += 0.05F;
					} else {
						autoAlpha = 0.25F;
					}
				}
			}
		}
	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (visible) {
			LColor oldColor = g.getColor();
			LFont oldFont = g.getFont();
			g.setColor(fontColor);
			g.setFont(messageFont);
			sizeFont = messageFont.getSize();
			doubleSizeFont = sizeFont * 2;
			messageLeft = (x + doubleSizeFont + sizeFont / 2) + tmpOffset
					+ left;
			messageTop = y + doubleSizeFont + top;
			g.setAntiAlias(true);
			if (message != null) {
				g.drawString(message, messageLeft, messageTop);
			}
			if (selects != null) {
				nLeft = messageLeft - sizeFont / 4;
				for (int i = 0; i < selects.length; i++) {
					type = i + 1;
					tmp_flag = messageTop + (doubleSizeFont * type);
					nTop = tmp_flag - sizeFont / 4;
					isSelect = (type == (selectFlag > 0 ? selectFlag : 1));
					if ((buoyage != null) && isSelect) {
						g.setAlpha(autoAlpha);
						g.drawImage(buoyage, nLeft, nTop - buoyage.getHeight()
								/ 2);
						g.setAlpha(1.0F);
					}
					g.drawString(selects[i], messageLeft, tmp_flag);
					if ((cursor != null) && isSelect) {
						g.drawImage(cursor, nLeft, nTop - cursor.getHeight()
								/ 2);
					}

				}
			}
			g.setAntiAlias(false);
			g.setColor(oldColor);
			g.setFont(oldFont);
		}
	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {

	}

	protected void processTouchClicked() {
		if (this.input.getTouchReleased() == MotionEvent.ACTION_UP) {
			if ((selects != null) && (selectFlag > 0)) {
				this.result = selects[selectFlag - 1];
			}
			this.doClick();
		}
	}

	protected synchronized void processTouchMoved() {
		if (selects != null) {
			selectFlag = selectSize
					- ((screenY + getHeight() + top - sizeFont - input
							.getTouchY()) / doubleSizeFont);
			if (selectFlag < 1) {
				selectFlag = 0;
			}
			if (selectFlag > selectSize) {
				selectFlag = selectSize;
			}
		}
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.KEYCODE_ENTER) {
			this.doClick();
		}
	}

	protected void processTouchDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getTouchDX(), this.input.getTouchDY());
		}
	}

	public LColor getFontColor() {
		return fontColor;
	}

	public void setFontColor(LColor fontColor) {
		this.fontColor = fontColor;
	}

	public LFont getMessageFont() {
		return messageFont;
	}

	public void setMessageFont(LFont messageFont) {
		this.messageFont = messageFont;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	protected void validateSize() {
		super.validateSize();
	}

	public LImage getCursor() {
		return cursor;
	}

	public void setNotCursor() {
		this.cursor = null;
	}

	public void setCursor(LImage cursor) {
		this.cursor = cursor;
	}

	public void setCursor(String fileName) {
		setCursor(GraphicsUtils.loadImage(fileName,true));
	}

	public LImage getBuoyage() {
		return buoyage;
	}

	public void setNotBuoyage() {
		this.cursor = null;
	}

	public void setBuoyage(LImage buoyage) {
		this.buoyage = buoyage;
	}

	public void setBuoyage(String fileName) {
		setBuoyage(GraphicsUtils.loadImage(fileName,true));
	}

	public boolean isFlashBuoyage() {
		return isAutoAlpha;
	}

	public void setFlashBuoyage(boolean flashBuoyage) {
		this.isAutoAlpha = flashBuoyage;
	}

	public String getUIName() {
		return "Select";
	}
}
