package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;

import android.graphics.Matrix;
import android.graphics.Bitmap.Config;

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
public abstract class ThreadScreen extends Screen implements Runnable {

	private int bench, centerX, centerY;

	private int benchcount;

	private long benchtime;

	private long time, time1, time2;

	private long speed, sleepTime;

	private Thread gameThread;

	private LGraphics screenGraphics;

	private LImage screen;

	private boolean running;

	private Draw draw;

	private int max_fps;

	private int extWidth, extHeight;

	private float scaleWidth, scaleHeight;

	private boolean match;

	private Matrix matrix;

	public ThreadScreen() {
		init(getWidth(), getHeight());
	}

	public ThreadScreen(int width, int height) {
		init(width, height);
	}

	private void init(int width, int height) {
		this.running = true;
		this.match = true;
		this.setSynchroFPS(20);
		this.screen = LImage.createImage(width, height, Config.RGB_565);
		this.screenGraphics = screen.getLGraphics();
		this.centerX = this.getWidth() / 2 - width / 2;
		this.centerY = this.getHeight() / 2 - height / 2;
		this.gameThread = new Thread(this);
		this.gameThread.setPriority(Thread.MAX_PRIORITY);
		this.gameThread.start();
	}

	public void resizeScreen(int w, int h) {
		this.extWidth = w;
		this.extHeight = h;
		this.match = (extWidth == screen.getWidth() && extHeight == screen
				.getHeight());
		if (!match) {
			this.scaleWidth = (float) extWidth / screen.getWidth();
			this.scaleHeight = (float) extHeight / screen.getHeight();
			this.centerX = this.getWidth() / 2 - extWidth / 2;
			this.centerY = this.getHeight() / 2 - extHeight / 2;
			this.matrix = new Matrix();
			this.matrix.postScale(scaleWidth, scaleHeight);
			this.matrix.postTranslate(centerX, centerY);
		}
	}

	public void dispose() {
		try {
			running = false;
			gameThread = null;
		} catch (Exception e) {
		}
	}

	public int getBenchCount() {
		return bench;
	}

	private void task() {
		if (benchtime < System.currentTimeMillis()) {
			benchtime = System.currentTimeMillis() + 1000L;
			bench = benchcount;
			benchcount = 0;
		}
		benchcount++;
	}

	private void synchro() {
		try {
			time2 = time1;
			time1 = System.currentTimeMillis();
			time = speed - (time1 - time2);
			if (time >= 0L) {
				Thread.sleep(time);
			}
		} catch (Exception ex) {
		}
	}

	public void sleep(long i) {
		while (System.currentTimeMillis() < sleepTime + i) {
			;
		}
		sleepTime = System.currentTimeMillis();
	}
	
	public long getSleepTime(){
		return sleepTime;
	}

	public int getSynchroFPS() {
		return max_fps;
	}

	public void setSynchroFPS(int fps) {
		max_fps = fps;
		speed = 1000 / fps;
	}

	public synchronized Draw getDraw() {
		if (draw == null) {
			draw = new DrawImpl(this);
		}
		return draw;
	}

	public LImage getImage() {
		return screen;
	}

	public LGraphics getLGraphics() {
		return screenGraphics;
	}

	public void initialize() {

	}

	public abstract void drawScreen(LGraphics g);

	public synchronized void repaint() {
		drawScreen(screenGraphics);
	}

	public abstract void gameLoop();

	public void setRunning(boolean isRunning) {
		this.running = isRunning;
	}

	public boolean isRunning() {
		return running;
	}

	public void run() {
		initialize();
		do {
			task();
			gameLoop();
			synchro();
		} while (running);
	}

	public void draw(LGraphics g) {
		if (match) {
			g.drawImage(screen, centerX, centerY);
		} else {
			g.drawImage(screen, matrix);
		}
	}

	public int getCenterY() {
		return centerY;
	}

	public void setCenterY(int centerY) {
		this.centerY = centerY;
	}

	public void setCenterX(int centerX) {
		this.centerX = centerX;
	}

	public int getCenterX() {
		return centerX;
	}

	public int getExtWidth() {
		return extWidth;
	}

	public int getExtHeight() {
		return extHeight;
	}


}
