package org.loon.framework.android.game.core.graphics.window;

import org.loon.framework.android.game.action.sprite.Animation;
import org.loon.framework.android.game.action.sprite.SpriteImage;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LContainer;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.window.achieve.IPrint;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.view.KeyEvent;

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
public class LMessage extends LContainer {

	private Animation animation = new Animation();

	private LFont messageFont = LFont.getFont("Dialog", 20);

	private LColor fontColor = LColor.white;

	private long printTime, totalDuration;

	private int dx, dy, dw, dh;

	private IPrint print;

	public LMessage(int width, int height) {
		this(0, 0, width, height);
	}

	public LMessage(int x, int y, int width, int height) {
		this((LImage) null, x, y, width, height);
	}

	public LMessage(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName,true), x, y);
	}

	public LMessage(LImage formImage, int x, int y) {
		this(formImage, x, y, formImage.getWidth(), formImage.getHeight());
	}

	public LMessage(LImage formImage, int x, int y, int width, int height) {
		super(x, y, width, height);
		if (formImage == null) {
			this.setBackground(new LImage(width, height,true));
			this.setAlpha(0.3F);
		} else {
			this.setBackground(formImage);
			if (width == -1) {
				width = formImage.getWidth();
			}
			if (height == -1) {
				height = formImage.getHeight();
			}
		}
		this.print = new IPrint(getLocation(), width, height);
		this.setTipIcon("system/images/creese.png");
		this.totalDuration = 100;
		this.customRendering = true;
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public void complete() {
		print.complete();
	}

	public void setLeftOffset(int left) {
		print.setLeftOffset(left);
	}

	public void setTopOffset(int top) {
		print.setTopOffset(top);
	}

	public int getLeftOffset() {
		return print.getLeftOffset();
	}

	public int getTopOffset() {
		return print.getTopOffset();
	}

	public int getMessageLength() {
		return print.getMessageLength();
	}

	public void setMessageLength(int messageLength) {
		print.setMessageLength(messageLength);
	}

	public void setTipIcon(String fileName) {
		print.setCreeseIcon(GraphicsUtils.loadImage(fileName,true));
	}

	public void setTipIcon(LImage icon) {
		print.setCreeseIcon(icon);
	}

	public void setNotTipIcon() {
		print.setCreeseIcon(null);
	}

	public void setDelay(long delay) {
		this.totalDuration = (delay < 1 ? 1 : delay);
	}

	public boolean isComplete() {
		return print.isComplete();
	}

	public void setPauseIconAnimationLocation(int dx, int dy) {
		this.dx = dx;
		this.dy = dy;
	}

	public void setMessage(String context, boolean isComplete) {
		print.setMessage(context, isComplete);
	}

	public void setMessage(String context) {
		print.setMessage(context);
	}

	/**
	 * 处理点击事件（请重载实现）
	 * 
	 */
	public void doClick() {
	}

	protected void processTouchClicked() {
		this.doClick();
	}

	protected void processKeyPressed() {
		if (this.isSelected()
				&& this.input.getKeyPressed() == KeyEvent.KEYCODE_ENTER) {
			this.doClick();
		}
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			if (print.isComplete()) {
				animation.update(elapsedTime);
			}
			printTime += elapsedTime;
			if (printTime >= totalDuration) {
				printTime = printTime % totalDuration;
				print.next();
			}
		}
	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (visible) {
			LColor oldColor = g.getColor();
			LFont oldFont = g.getFont();
			g.setColor(fontColor);
			g.setFont(messageFont);
			print.draw(g, fontColor);
			g.setColor(oldColor);
			g.setFont(oldFont);
			if (print.isComplete()) {
				if (animation.getSpriteImage() != null) {
					g.setAlpha(1.0F);
					g.drawImage(animation.getSpriteImage().getImage(), dx, dy);
				}
			}
		}
	}

	protected void processTouchDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.dx += this.input.getTouchDX();
			this.dy += this.input.getTouchDY();
			this.move(this.input.getTouchDX(), this.input.getTouchDY());
		}
	}

	public void setPauseIconAnimation(Animation animation) {
		this.animation = animation;
		if (animation != null) {
			SpriteImage image = animation.getSpriteImage(0);
			if (image != null) {
				this.dw = image.getWidth();
				this.dh = image.getHeight();
				this.setPauseIconAnimationLocation(getScreenX() + getWidth()
						+ dw / 2, getScreenY() + getHeight() - dh);
			}
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

	public String getUIName() {
		return "Message";
	}

}
