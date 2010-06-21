package org.loon.framework.android.game.core.graphics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.loon.framework.android.game.action.Action;
import org.loon.framework.android.game.action.IAction;
import org.loon.framework.android.game.action.MoveAction;
import org.loon.framework.android.game.action.sprite.ISprite;
import org.loon.framework.android.game.action.sprite.Sprites;
import org.loon.framework.android.game.core.IHandler;
import org.loon.framework.android.game.core.LInput;
import org.loon.framework.android.game.core.LKeyTyped;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.KeyEvent;
import android.view.MotionEvent;

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
public abstract class Screen implements IScreen, LInput {

	// 线程事件集合
	private final ArrayList<Runnable> runnables = new ArrayList<Runnable>();

	private int touchX, touchY, lastTouchX, lastTouchY, touchDX, touchDY,
			touchDirection;

	private float offsetTouchX, offsetMoveX, offsetTouchY, offsetMoveY;

	private long elapsedTime;

	private final static boolean[] touchDown, keyDown;

	private final static int[] touchPressed, touchReleased;

	private int pressedTouch, releasedTouch;

	private final static int[] keyPressed, keyReleased;

	private int pressedKey, releasedKey;

	private static Map<String, Action> keyMap, touchMap;

	private boolean isNext, isMoving;

	private int shake;

	private Bitmap currentScreen;

	private LKeyTyped keyTyped;

	private LInput baseInput;

	private IHandler handler;

	private int width, height, halfWidth, halfHeight;

	// 精灵集合
	private Sprites sprites;

	// 桌面集合
	private Desktop desktop;

	private Point touch = new Point(0, 0);

	static {
		keyDown = new boolean[255];
		keyPressed = keyReleased = new int[255];
		keyMap = new HashMap<String, Action>(255);
		touchDown = new boolean[10];
		touchPressed = touchReleased = new int[10];
		touchMap = new HashMap<String, Action>(10);
	}

	public Screen() {
		this.shake = -1;
		this.handler = LSystem.getSystemHandler();
		this.width = handler.getWidth();
		this.height = handler.getHeight();
		this.halfWidth = getWidth() / 2;
		this.halfHeight = getHeight() / 2;
		this.currentScreen = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		this.baseInput = this;
		this.pressedKey = releasedKey = 0;
		this.keyTyped = new LKeyTyped(this);
		this.pressedTouch = releasedTouch = 0;
		this.touchX = touchY = lastTouchX = lastTouchY = touchDX = touchDY = 0;
		this.sprites = new Sprites(width, height);
		this.desktop = new Desktop(baseInput, width, height);
		this.isNext = true;
		this.isMoving = false;
	}

	/**
	 * 访问指定的网站页面
	 * 
	 * @param url
	 */
	public void openBrowser(final String url) {
		if (handler != null) {
			handler.getLGameActivity().openBrowser(url);
		}
	}

	/**
	 * 广告是否显示
	 * 
	 * @return
	 */
	public boolean isVisibleAD() {
		if (handler != null) {
			return handler.getLGameActivity().isVisibleAD();
		}
		return false;
	}

	/**
	 * 隐藏广告
	 */
	public void hideAD() {
		if (handler != null) {
			handler.getLGameActivity().hideAD();
		}
	}

	/**
	 * 显示广告
	 */
	public void showAD() {
		if (handler != null) {
			handler.getLGameActivity().showAD();
		}
	}

	/**
	 * 选择的结果
	 * 
	 * @return
	 */
	public int getAndroidSelect() {
		if (handler != null) {
			return handler.getLGameActivity().getAndroidSelect();
		}
		return -2;
	}

	/**
	 * 弹出一个Android选框，用以显示指定的进度条
	 * 
	 * @param title
	 * @param message
	 * @return
	 */
	public AlertDialog showAndroidProgress(final String title,
			final String message) {
		if (handler != null) {
			return handler.getLGameActivity().showAndroidProgress(title,
					message);
		}
		return null;
	}

	/**
	 * 弹出一个Android选框，用以显示指定的信息
	 * 
	 * @param message
	 */
	public void showAndroidAlert(final String title, final String message) {
		if (handler != null) {
			handler.getLGameActivity().showAndroidAlert(title, message);
		}

	}

	/**
	 * 弹出一个Android选框，用以显示指定的异常数据
	 * 
	 * @param ex
	 */
	public void showAndroidExceptionMessageBox(final Exception ex) {
		if (handler != null) {
			handler.getLGameActivity().showAndroidException(ex);
		}
	}

	/**
	 * 弹出一个Android选择框，并返回选择结果
	 * 
	 * @param text
	 * @return
	 */
	public int showAndroidSelectMessageBox(final String title,
			final String[] text) {
		if (handler != null) {
			return handler.getLGameActivity().showAndroidSelect(title, text);
		}
		return -1;
	}

	/**
	 * 播放Assets中的音频文件
	 * 
	 * @param file
	 * @param loop
	 */
	public void playtAssetsMusic(final String file, final boolean loop) {
		if (handler != null) {
			handler.getAssetsSound().playSound(file, loop);
		}
	}

	/**
	 * 设置Assets中的音频文件音量
	 * 
	 * @param vol
	 */
	public void resetAssetsMusic(final int vol) {
		if (handler != null) {
			handler.getAssetsSound().setSoundVolume(vol);
		}
	}

	/**
	 * 重置Assets中的音频文件
	 * 
	 */
	public void resetAssetsMusic() {
		if (handler != null) {
			handler.getAssetsSound().resetSound();
		}
	}

	/**
	 * 中断Assets中的音频文件
	 */
	public void stopAssetsMusic() {
		if (handler != null) {
			handler.getAssetsSound().stopSound();
		}
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(Bitmap screen) {
		if (screen != null) {
			if (screen.getWidth() != getWidth()
					|| screen.getHeight() != getHeight()) {
				screen = GraphicsUtils.getResize(screen, getWidth(),
						getHeight());
			}
			currentScreen = screen;
		}
	}

	/**
	 * 设定背景图像
	 * 
	 * @param screen
	 */
	public void setBackground(LImage screen) {
		setBackground(screen.getBitmap());
	}

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 */
	public void setBackground(String fileName, boolean transparency) {
		this.setBackground(getImage(fileName, transparency));
	}

	/**
	 * 设定背景图像
	 * 
	 * @param fileName
	 */
	public void setBackground(String fileName) {
		this.setBackground(getImage(fileName, false));
	}

	/**
	 * 返回背景图像
	 * 
	 * @return
	 */
	public Bitmap getBackground() {
		return currentScreen;
	}

	public void setBackground(LColor color) {
		LImage image = new LImage(currentScreen);
		LGraphics g = image.getLGraphics();
		g.backgroundColor(color);
		g.dispose();
	}

	public void destroy() {
		this.callEvents(false);
		this.isNext = false;
		this.currentScreen = null;
		this.desktop = null;
		this.sprites = null;
		this.dispose();
	}

	/**
	 * 释放函数内资源
	 * 
	 */
	public void dispose() {

	}

	public Desktop getDesktop() {
		return desktop;
	}

	public Sprites getSprites() {
		return sprites;
	}

	/**
	 * 添加游戏精灵
	 * 
	 * @param sprite
	 */
	public void add(ISprite sprite) {
		sprites.add(sprite);
	}

	public void remove(ISprite sprite) {
		sprites.remove(sprite);
	}

	/**
	 * 添加游戏组件
	 * 
	 * @param comp
	 */
	public void add(LComponent comp) {
		desktop.add(comp);
	}

	public void remove(LComponent comp) {
		desktop.remove(comp);
	}

	public void removeAll() {
		sprites.removeAll();
		desktop.getContentPane().clear();
	}

	public void centerOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				/ 2 - object.getHeight() / 2);
	}

	public void topOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, 0);
	}

	public void leftOn(final LObject object) {
		object.setLocation(0, getHeight() / 2 - object.getHeight() / 2);
	}

	public void rightOn(final LObject object) {
		object.setLocation(getWidth() - object.getWidth(), getHeight() / 2
				- object.getHeight() / 2);
	}

	public void bottomOn(final LObject object) {
		object.setLocation(getWidth() / 2 - object.getWidth() / 2, getHeight()
				- object.getHeight());
	}

	/**
	 * 获得背景震动频率
	 */
	public int getShakeNumber() {
		return shake;
	}

	/**
	 * 设定背景震动频率
	 * 
	 * @param shake
	 */
	public void setShakeNumber(int shake) {
		this.shake = shake;
	}

	/**
	 * 增减一个线程事件
	 * 
	 * @param runnable
	 */
	public final void callEvent(Runnable runnable) {
		synchronized (runnables) {
			runnables.add(runnable);
		}
	}

	/**
	 * 暂停指定的线程事件
	 * 
	 * @param runnable
	 */
	public final void callEventWait(Runnable runnable) {
		synchronized (runnable) {
			synchronized (runnables) {
				runnables.add(runnable);
			}
			try {
				runnable.wait();
			} catch (InterruptedException ex) {
			}
		}
	}

	/**
	 * 中断所有线程事件
	 * 
	 */
	public final void callEventInterrupt() {
		synchronized (runnables) {
			for (Iterator<Runnable> it = runnables.iterator(); it.hasNext();) {
				Object running = it.next();
				synchronized (running) {
					if (running instanceof Thread) {
						((Thread) running).setPriority(Thread.MIN_PRIORITY);
						((Thread) running).interrupt();
					}
				}
			}
		}
	}

	/**
	 * 运行线程事件
	 * 
	 */
	public final void callEvents() {
		callEvents(true);
	}

	/**
	 * 执行或中断指定的线程事件
	 * 
	 * @param execute
	 */
	private final void callEvents(boolean execute) {
		if (!execute) {
			synchronized (runnables) {
				runnables.clear();
			}
			return;
		}
		if (runnables.size() == 0) {
			return;
		}
		ArrayList<Runnable> runnableList;
		synchronized (runnables) {
			runnableList = new ArrayList<Runnable>(runnables);
			runnables.clear();
		}
		for (Iterator<Runnable> it = runnableList.iterator(); it.hasNext();) {
			Object running = it.next();
			synchronized (running) {
				try {
					if (running instanceof Thread) {
						Thread thread = (Thread) running;
						if (!thread.isAlive()) {
							thread.start();
						}

					} else {
						((Runnable) running).run();
					}
				} catch (Exception ex) {
				}
				running.notifyAll();
			}
		}
		runnableList = null;
	}

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public void addKeyEvents(int keyCode, String key, IAction action) {
		addKeyEvents(keyCode, new Action(action, key));
	}

	/**
	 * 添加键盘事件
	 * 
	 * @param keyCode
	 * @param action
	 */
	public void addKeyEvents(int keyCode, Action action) {
		keyMap.put(String.valueOf(keyCode), action);
	}

	/**
	 * 删除键盘事件
	 * 
	 * @param keyCode
	 */
	public void removeKeyEvents(int keyCode) {
		keyMap.remove(String.valueOf(keyCode));
	}

	/**
	 * 清空键盘事件
	 * 
	 */
	public void clearKeyEvents() {
		keyMap.clear();
	}

	/**
	 * 添加触摸屏事件
	 * 
	 * @param mouseCode
	 * @param action
	 */
	public void addTouchEvents(int touchCode, String key, IAction action) {
		addTouchEvents(touchCode, new Action(action, key));
	}

	/**
	 * 添加鼠标事件
	 * 
	 * @param touchCode
	 * @param action
	 */
	public void addTouchEvents(int touchCode, Action action) {
		touchMap.put(String.valueOf(touchCode), action);
	}

	/**
	 * 删除鼠标事件
	 * 
	 * @param touchCode
	 */
	public void removeTouchEvents(int touchCode) {
		touchMap.remove(String.valueOf(touchCode));
	}

	/**
	 * 清空鼠标事件
	 * 
	 */
	public void clearTouchEvents() {
		touchMap.clear();
	}

	public LImage getWebImage(String url, boolean transparency) {
		return GraphicsUtils.loadWebImage(url, transparency);
	}

	public LImage getImage(String fileName, boolean transparency) {
		return GraphicsUtils.loadImage(fileName, transparency);
	}

	public LImage[] getSplitImages(String fileName, int row, int col,
			boolean transparency) {
		return getSplitImages(getImage(fileName, transparency), row, col);
	}

	public LImage[] getSplitImages(LImage image, int row, int col) {
		return GraphicsUtils.getSplitImages(image, row, col);
	}

	public synchronized void createUI(LGraphics g) {
		draw(g);
		if (sprites != null) {
			sprites.createUI(g);
		}
		if (desktop != null) {
			desktop.createUI(g);
		}
	}

	public abstract void draw(LGraphics g);

	public void runTimer(LTimerContext timer) {
		this.elapsedTime = timer.getTimeSinceLastUpdate();
		if (sprites != null && this.sprites.size() > 0) {
			this.sprites.update(elapsedTime);
		}
		if (desktop != null
				&& this.desktop.getContentPane().getComponentCount() > 0) {
			this.desktop.update(elapsedTime);
		}
		this.updateAction(keyMap, 0, elapsedTime);
		this.updateAction(touchMap, 1, elapsedTime);
		this.baseInput.update(elapsedTime);
		this.alter(timer);
	}

	public boolean next() {
		return isNext;
	}

	public void setNext(boolean next) {
		this.isNext = next;
	}

	public void alter(LTimerContext timer) {

	}

	/**
	 * 触发指定事件
	 * 
	 * @param actions
	 * @param timer
	 */
	private void updateAction(Map<String, Action> actions, int type, long timer) {
		int length = actions.size();
		if (length == 0) {
			return;
		}
		Action keyAction = null;
		switch (type) {
		case 0:
			keyAction = (Action) actions.get(String
					.valueOf(keyPressed[pressedKey]));
			break;
		case 1:
			keyAction = (Action) actions.get(String
					.valueOf(touchPressed[pressedTouch]));
			break;
		}
		if (keyAction != null) {
			if (keyAction.isPressed()) {
				IAction action = keyAction.getIAction();
				action.doAction(timer);
				if (!(action instanceof MoveAction)) {
					keyAction.reset();
				}
			}
		}
	}

	/**
	 * 设定游戏窗体
	 */
	public void setScreen(IScreen screen) {
		if (handler != null) {
			this.destroy();
			this.handler.setScreen(screen);
		}
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	/**
	 * 刷新基础设置
	 */
	public void refresh() {
		keyTyped.refresh();
		for (int i = 0; i < touchDown.length; i++) {
			touchDown[i] = false;
		}
		pressedTouch = releasedTouch = 0;
		touchDX = touchDY = 0;
		for (int i = 0; i < keyDown.length; i++) {
			keyDown[i] = false;
		}
		pressedKey = releasedKey = 0;
	}

	/**
	 * 对外的线程暂停器
	 * 
	 * @param timeMillis
	 */
	public void pause(long timeMillis) {
		try {
			Thread.sleep(timeMillis);
		} catch (InterruptedException e) {
			throw new RuntimeException("Interrupted in pause !");
		}
	}

	public Point getTouch() {
		touch.set(touchX, touchY);
		return touch;
	}

	public boolean isClick() {
		return touchDown[MotionEvent.ACTION_DOWN];
	}

	public boolean isTouchClick() {
		return baseInput.isTouchPressed(MotionEvent.ACTION_DOWN);
	}

	public boolean isTouchClickUp() {
		return baseInput.isTouchPressed(MotionEvent.ACTION_UP);
	}

	public int getTouchPressed() {
		return (pressedTouch > 0) ? touchPressed[pressedTouch - 1]
				: LInput.NO_BUTTON;
	}

	public int getTouchReleased() {
		return (releasedTouch > 0) ? touchReleased[releasedTouch - 1]
				: LInput.NO_BUTTON;
	}

	public boolean isTouchPressed(int button) {
		if (!isTouchReleased(button)) {
			return false;
		}
		try {
			for (int i = 0; i < this.pressedTouch; i++) {
				if (touchPressed[i] == button) {
					return true;
				}
			}
			return touchPressed[0] == button;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isTouchReleased(int button) {
		try {
			for (int i = 0; i < this.releasedTouch; i++) {
				if (touchReleased[i] == button) {
					return true;
				}
			}
			return touchReleased[0] == button;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean keyDown(int keyCode) {
		return baseInput.isKeyDown(keyCode);
	}

	public boolean keyPressed(int keyCode) {
		return baseInput.isKeyPressed(keyCode);
	}

	public int getTouchX() {
		return touchX;
	}

	public int getTouchY() {
		return touchY;
	}

	public int getTouchDX() {
		return touchDX;
	}

	public int getTouchDY() {
		return touchDY;
	}

	public boolean[] getTouchDown() {
		return touchDown;
	}

	public boolean isTouchDown(int button) {
		return touchDown[button];
	}

	public int getKeyPressed() {
		return (pressedKey > 0) ? keyPressed[pressedKey - 1] : LInput.NO_KEY;
	}

	public boolean isKeyPressed(int keyCode) {
		for (int i = 0; i < pressedKey; i++) {
			if (keyPressed[i] == keyCode) {
				return true;
			}
		}
		return false;
	}

	public int getKeyReleased() {
		return (releasedKey > 0) ? keyReleased[releasedKey - 1] : LInput.NO_KEY;
	}

	public boolean isKeyReleased(int keyCode) {
		for (int i = 0; i < releasedKey; i++) {
			if (keyReleased[i] == keyCode) {
				return true;
			}
		}
		return false;
	}

	public boolean[] getKeyDown() {
		return keyDown;
	}

	public boolean isKeyDown(int keyCode) {
		return keyDown[keyCode];
	}

	public int getKeyTyped() {
		return keyTyped.getKeyTyped();
	}

	public boolean isKeyTyped(int keyCode) {
		return keyTyped.isKeyTyped(keyCode);
	}

	public long getRepeatDelay() {
		return keyTyped.getRepeatDelay();
	}

	/**
	 * 设定重复延迟
	 * 
	 * @param delay
	 */
	public void setRepeatDelay(long delay) {
		keyTyped.setRepeatDelay(delay);
	}

	public long getRepeatRate() {
		return keyTyped.getRepeatRate();
	}

	/**
	 * 设定重复延迟比率
	 * 
	 * @param rate
	 */
	public void setRepeatRate(long rate) {
		keyTyped.setRepeatRate(rate);
	}

	public boolean onDownUpEvent(int keyCode, KeyEvent e) {
		try {
			keyDown[keyCode] = true;
			keyPressed[pressedKey] = keyCode;
			pressedKey++;
			if (keyMap.size() > 0) {
				Action action = (Action) keyMap.get(String.valueOf(keyCode));
				if (action != null) {
					action.press();
				}
			}
		} catch (Exception ex) {

		}
		return onKeyDown(keyCode, e);
	}

	public boolean onKeyUpEvent(int keyCode, KeyEvent e) {
		try {
			keyDown[keyCode] = false;
			keyReleased[releasedKey] = keyCode;
			releasedKey++;
			if (keyMap.size() > 0) {
				Action action = (Action) keyMap.get(String.valueOf(keyCode));
				if (action != null) {
					action.release();
				}
			}
		} catch (Exception ex) {

		}
		return onKeyUp(keyCode, e);
	}

	public boolean onTouchEvent(MotionEvent e) {
		int code = e.getAction();
		try {
			touchX = Math.round(e.getX());
			touchY = Math.round(e.getY());
			switch (code) {
			case MotionEvent.ACTION_DOWN:
				offsetTouchX = touchX;
				offsetTouchY = touchY;
				isMoving = false;
				touchDown[code] = true;
				touchPressed[pressedTouch] = code;
				touchDX = lastTouchX = touchX;
				touchDY = lastTouchY = touchY;
				pressedTouch++;
				if (touchMap.size() > 0) {
					Action action = (Action) touchMap.get(String.valueOf(code));
					if (action != null) {
						action.press();
					}
				}
				if ((touchX < halfWidth) && (touchY < halfHeight)) {
					this.touchDirection = LInput.UPPER_LEFT;
				} else if ((touchX >= halfWidth) && (touchY < halfHeight)) {
					this.touchDirection = LInput.UPPER_RIGHT;
				} else if ((touchX < halfWidth) && (touchY >= halfHeight)) {
					this.touchDirection = LInput.LOWER_LEFT;
				} else {
					this.touchDirection = LInput.LOWER_RIGHT;
				}
				return onTouchDown(e);
			case MotionEvent.ACTION_UP:
				isMoving = false;
				touchDown[code] = false;
				touchReleased[releasedTouch] = code;
				touchDX = lastTouchX = touchX;
				touchDY = lastTouchY = touchY;
				releasedTouch++;
				if (touchMap.size() > 0) {
					Action action = (Action) touchMap.get(String.valueOf(code));
					if (action != null) {
						action.release();
					}
				}
				return onTouchUp(e);
			case MotionEvent.ACTION_MOVE:
				offsetMoveX = touchX;
				offsetMoveY = touchY;
				if (Math.abs(offsetTouchX - offsetMoveX) > 5
						|| Math.abs(offsetTouchY - offsetMoveY) > 5) {
					isMoving = true;
					return onTouchMove(e);
				}
			}
		} catch (Exception ex) {
ex.printStackTrace();
		}
		return false;
	}

	/**
	 * 刷新触摸屏及键盘数据
	 */
	public void update(long timer) {
		keyTyped.update(timer);
		pressedTouch = releasedTouch = pressedKey = releasedKey = 0;
		touchDX = touchX - lastTouchX;
		touchDY = touchY - lastTouchY;
		lastTouchX = touchX;
		lastTouchY = touchY;
	}

	public boolean isMoving() {
		return isMoving;
	}

	public int getHalfWidth() {
		return halfWidth;
	}

	public int getHalfHeight() {
		return halfHeight;
	}

	public int getTouchDirection() {
		return touchDirection;
	}

}
