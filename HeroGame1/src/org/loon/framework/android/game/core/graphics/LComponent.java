package org.loon.framework.android.game.core.graphics;

import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.LInput;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.graphics.window.UIFactory;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.view.KeyEvent;

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
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */

public abstract class LComponent extends LObject {

	// 容器
	private LContainer parent;

	// 渲染器
	protected UIFactory factory;

	protected boolean elastic;

	protected LImage[] ui;

	private LImage[] imageUI;

	protected boolean isFull;

	private boolean processUI;

	// 渲染状态
	public boolean customRendering;

	// 透明度
	protected float alpha = 1.0f;

	// 组件坐标与大小
	private int width, height;

	// 屏幕位置
	protected int screenX, screenY;

	// 操作提示
	protected String tooltip;

	protected Rect oldClip;
	// 提示组件
	protected LComponent tooltipParent;

	// 组件标记
	protected boolean visible = true;

	protected boolean enabled = true;

	// 是否为焦点
	protected boolean focusable = true;

	// 是否已选中
	protected boolean selected = false;

	// 资源缓冲
	private final HashMap<Object, Object> UI_RES = new HashMap<Object, Object>();

	protected Desktop desktop = Desktop.EMPTY_DESKTOP;

	protected LInput input;

	protected LImage background;

	/**
	 * 构造可用组件
	 * 
	 * @param x
	 * @param y
	 * @param width
	 * @param height
	 */
	public LComponent(int x, int y, int width, int height) {
		this.setLocation(x, y);
		this.width = width;
		this.height = height;

		if (this.width == 0) {
			this.width = 10;
		}
		if (this.height == 0) {
			this.height = 10;
		}
	}

	/**
	 * 返回当前组件对象是否为容器
	 * 
	 * @return
	 */
	public boolean isContainer() {
		return false;
	}

	/**
	 * 更新组件状态
	 * 
	 */
	public void update(long timer) {

	}

	/**
	 * 渲染当前组件画面于指定绘图器之上
	 * 
	 * @param g
	 */
	public void createUI(LGraphics g) {
		if (!this.visible) {
			return;
		}
		try {
			if (this.elastic) {
				this.oldClip = g.getClip();
				g.clipRect(this.getScreenX(), this.getScreenY(), this
						.getWidth(), this.getHeight());
			}
			// 变更透明度
			if (alpha > 0.1 && alpha < 1.0) {
				g.setAlpha(alpha);
				if (background != null) {
					g.drawImage(background, this.screenX, this.screenY,
							this.width, this.height);
				}
				if (this.customRendering) {
					this.createCustomUI(g, this.screenX, this.screenY,
							this.width, this.height);
				} else {
					this.factory.createUI(g, this.screenX, this.screenY, this,
							this.ui);

				}
				g.setAlpha(1.0F);
				// 不变更
			} else {
				if (background != null) {
					g.drawImage(background, this.screenX, this.screenY,
							this.width, this.height);
				}
				if (this.customRendering) {
					this.createCustomUI(g, this.screenX, this.screenY,
							this.width, this.height);
				} else {
					this.factory.createUI(g, this.screenX, this.screenY, this,
							this.ui);

				}
			}
			if (this.elastic) {
				g.setClip(this.oldClip);
			}
		} catch (Exception ex) {
		
		}
	}

	/**
	 * 自定义UI
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 */
	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
	}

	public boolean intersects(int x1, int y1) {
		return (this.visible)
				&& (x1 >= this.screenX && x1 <= this.screenX + this.width
						&& y1 >= this.screenY && y1 <= this.screenY
						+ this.height);
	}

	public boolean intersects(LComponent comp) {
		return (this.visible)
				&& (comp.isVisible())
				&& (this.screenX + this.width >= comp.screenX
						&& this.screenX <= comp.screenX + comp.width
						&& this.screenY + this.height >= comp.screenY && this.screenY <= comp.screenY
						+ comp.height);
	}

	public void dispose() {
		this.desktop.setComponentStat(this, false);
		if (this.parent != null) {
			this.parent.remove(this);
		}
		this.desktop = Desktop.EMPTY_DESKTOP;
		this.input = null;
		this.parent = null;
		if (ui != null) {
			for (int i = 0; i < ui.length; i++) {
				ui[i].dispose();
			}
			this.ui = null;
		}
		if (imageUI != null) {
			for (int i = 0; i < imageUI.length; i++) {
				imageUI[i].dispose();
			}
			this.imageUI = null;
		}
		if (background != null) {
			this.background.dispose();
			this.background = null;
		}
		this.factory = null;
		this.selected = false;
		this.visible = false;
	}

	public boolean isVisible() {
		return this.visible;
	}

	public void setVisible(boolean visible) {
		if (this.visible == visible) {
			return;
		}
		this.visible = visible;
		this.desktop.setComponentStat(this, this.visible);
	}

	public boolean isEnabled() {
		return (this.parent == null) ? this.enabled
				: (this.enabled && this.parent.isEnabled());
	}

	public void setEnabled(boolean b) {
		if (this.enabled == b) {
			return;
		}
		this.enabled = b;
		this.desktop.setComponentStat(this, this.enabled);
	}

	public boolean isSelected() {
		return this.selected;
	}

	final void setSelected(boolean b) {
		this.selected = b;
	}

	public boolean requestFocus() {
		return this.desktop.selectComponent(this);
	}

	public void transferFocus() {
		if (this.isSelected() && this.parent != null) {
			this.parent.transferFocus(this);
		}
	}

	public void transferFocusBackward() {
		if (this.isSelected() && this.parent != null) {
			this.parent.transferFocusBackward(this);
		}
	}

	public boolean isFocusable() {
		return this.focusable;
	}

	public void setFocusable(boolean b) {
		this.focusable = b;
	}

	public LContainer getContainer() {
		return this.parent;
	}

	final void setContainer(LContainer container) {
		this.parent = container;

		this.validatePosition();
	}

	final void setDesktop(Desktop desktop) {
		if (this.desktop == desktop) {
			return;
		}

		this.desktop = desktop;
		this.input = desktop.input;

		if (this.factory == null) {
			this.factory = desktop.getUIConfig().createUIFactory(
					this.getUIName());
		}

		this.createUI();
	}

	public void setBounds(double dx, double dy, int width, int height) {
		if (this.getX() != dx || this.getY() != dy) {
			super.setLocation(dx, dy);
			this.validatePosition();
		}
		if (this.width != width || this.height != height) {
			this.width = width;
			this.height = height;
			if (width == 0) {
				width = 1;
			}
			if (height == 0) {
				height = 1;
			}

			this.createUI();
			this.validateSize();
		}
	}

	public void setLocation(double dx, double dy) {
		if (this.getX() != dx || this.getY() != dy) {
			super.setLocation(dx, dy);
			this.validatePosition();
		}
	}

	public void move(double dx, double dy) {
		if (dx != 0 || dy != 0) {
			super.move(dx, dy);
			this.validatePosition();
		}
	}

	public void setSize(int w, int h) {
		if (this.width != w || this.height != h) {
			this.width = w;
			this.height = h;
			if (this.width == 0) {
				this.width = 1;
			}
			if (this.height == 0) {
				this.height = 1;
			}

			this.createUI();
			this.validateSize();
		}
	}

	protected void validateSize() {
	}

	protected void validatePosition() {
		this.screenX = (int) ((this.parent == null) ? this.getX() : this.getX()
				+ this.parent.getScreenX());
		this.screenY = (int) ((this.parent == null) ? this.getY() : this.getY()
				+ this.parent.getScreenY());
	}

	public int getScreenX() {
		return this.screenX;
	}

	public int getScreenY() {
		return this.screenY;
	}

	protected void setHeight(int height) {
		this.height = height;
	}

	protected void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public RectBox getCollisionBox() {
		return new RectBox(screenX, screenY, width, height);
	}

	public String getToolTipText() {
		return this.tooltip;
	}

	public void setToolTipText(String text) {
		this.tooltip = text;
	}

	public LComponent getToolTipParent() {
		return this.tooltipParent;
	}

	public void setToolTipParent(LComponent tipParent) {
		this.tooltipParent = tipParent;
	}

	// 鼠标操作
	protected void processTouchPressed() {

	}

	protected void processTouchReleased() {
	}

	protected void processTouchClicked() {
	}

	protected void processTouchMoved() {
	}

	protected void processTouchDragged() {
	}

	protected void processTouchEntered() {
	}

	protected void processTouchExited() {
	}

	// 键盘操作
	protected void processKeyPressed() {

	}

	protected void processKeyReleased() {
	}

	void keyPressed() {
		this.checkFocusKey();
		this.processKeyPressed();
	}

	/**
	 * 检测键盘事件焦点
	 * 
	 */
	protected void checkFocusKey() {
		if (this.input.getKeyPressed() == KeyEvent.KEYCODE_ENTER) {
			if (this.input.isKeyDown(KeyEvent.KEYCODE_SHIFT_LEFT) == false
					&& this.input.isKeyDown(KeyEvent.KEYCODE_SHIFT_RIGHT) == false) {
				this.transferFocus();
			} else {
				this.transferFocusBackward();
			}
		}
	}

	public float getAlpha() {
		return alpha;
	}

	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}

	public LImage[] getImageUI() {
		return this.imageUI;
	}

	public void setImageUI(LImage[] imageUI, boolean processUI) {
		if (imageUI != null) {
			this.width = imageUI[0].getWidth();
			this.height = imageUI[0].getHeight();
		}

		this.imageUI = imageUI;
		this.processUI = processUI;

		this.createUI();
	}

	public void setImageUI(int index, LImage imageUI) {
		if (imageUI != null) {
			this.width = imageUI.getWidth();
			this.height = imageUI.getHeight();
		}
		this.imageUI[index] = imageUI;
		this.processUI = true;
		this.createUI();
	}

	public Map<Object, Object> getUIResource() {
		return this.UI_RES;
	}

	public UIFactory getUIRenderer() {
		return this.factory;
	}

	public void setUIRenderer(UIFactory factory) {
		this.factory = factory;

		this.createUI();
	}

	public abstract String getUIName();

	protected void createUI() {
		if (this.desktop == Desktop.EMPTY_DESKTOP) {
			return;
		}
		if (this.customRendering) {
			this.createCustomUI(this.width, this.height);
			this.processCustomUI();
		} else if (this.imageUI != null
				&& this.factory.getUIDescription().length == this.imageUI.length) {
			if (this.width != this.imageUI[0].getWidth()
					|| this.height != this.imageUI[0].getHeight()) {
				throw new RuntimeException(
						this
								+ ("Invalid Operation! Can not change component size when using external UI size ("
										+ this.width + "," + this.height + ") ")
										.intern());
			}

			if (!this.processUI) {
				this.ui = this.imageUI;
			} else {
				this.ui = new LImage[this.imageUI.length];
				for (int i = 0; i < this.ui.length; i++) {
					this.ui[i] = LImage.createImage(this.imageUI[i].getWidth(),
							this.imageUI[i].getHeight(), Config.ARGB_8888);
					LGraphics g = this.ui[i].getLGraphics();
					g.drawImage(this.imageUI[i], 0, 0);
					g.dispose();
				}
				this.processExternalUI();
			}
		} else {
			this.createRenderedUI(this.width, this.height);
			this.processRenderedUI();
		}
	}

	public LImage getBackground() {
		return background;
	}

	public void clearBackground() {
		this.setBackground(LImage.createImage(1, 1, Config.ARGB_4444));
	}

	public void setBackground(String fileName, boolean t) {
		this.setBackground(GraphicsUtils.loadImage(fileName, t));
	}

	public void setBackground(LColor color) {
		LImage image = LImage.createImage(getWidth(), getHeight(),
				Config.RGB_565);
		LGraphics g = image.getLGraphics();
		g.setColorAll(color);
		g.dispose();
		setBackground(image);
	}

	public void setBackground(LImage background) {
		this.background = background;
		this.setAlpha(1.0f);
	}

	protected void createRenderedUI(int w, int h) {
		this.ui = this.factory.createUI(this, w, h);
	}

	protected void processRenderedUI() {
		this.factory.processUI(this, this.ui);
	}

	protected void createCustomUI(int w, int h) {
	}

	protected void processCustomUI() {
	}

	protected void processExternalUI() {
		this.factory.processUI(this, this.ui);
	}

	public void validateUI() {
		this.createUI();
	}

}
