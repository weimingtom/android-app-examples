package org.loon.framework.android.game;

import org.loon.framework.android.game.core.Handler;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.loon.framework.android.game.core.timer.SystemTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * 
 * Copyright 2008 - 2010
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
public class LGameView extends SurfaceView implements SurfaceHolder.Callback {

	private final static long MAX_INTERVAL = 1000L;

	final static private LFont fpsFont = LFont.getFont("Dialog", 0, 20);

	final static private int fpsX = 5;

	final static private int fpsY = 20;

	private volatile LImage logo;

	private boolean isFPS, isLogo, isDebug, paused, running;

	private int width, height;

	private long maxFrames, before, curTime, startTime, offsetTime, curFPS,
			calcInterval;

	private double frameCount;

	private volatile Bitmap currentScreen;

	private volatile SurfaceHolder surfaceHolder;

	private volatile CanvasThread mainLoop;

	private volatile LGraphics gl;

	private volatile Handler handler;

	private volatile Rect rect;

	public LGameView(LGameActivity activity) {
		this(activity, false);
	}

	public LGameView(LGameActivity activity, boolean isLandscape) {
		super(activity.getApplicationContext());
		try {
			LSystem.setupHandler(activity, this, isLandscape);
			this.handler = LSystem.getSystemHandler();
			this.handler.initScreen();
			this.createScreen();
			this.setBackgroundDrawable(null);
			this.setFPS(LSystem.DEFAULT_MAX_FPS);
			this.setFocusable(true);
			this.setFocusableInTouchMode(true);
		} catch (Exception e) {
		}
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}

	public boolean isPaused() {
		return paused;
	}

	/**
	 * 创建游戏窗体载体
	 */
	private void createScreen() {
		LImage image = new LImage(width = handler.getWidth(), height = handler
				.getHeight(), false);
		this.gl = image.getLGraphics();
		this.currentScreen = image.getBitmap();
		this.rect = new Rect(0, 0, width, height);
		this.surfaceHolder = getHolder();
		this.surfaceHolder.addCallback(this);
		this.surfaceHolder.setKeepScreenOn(true);

	}

	public void setScreen(IScreen screen) {
		this.handler.setScreen(screen);
	}

	public void destroyView() {
		try {
			synchronized (this) {
				handler.getAssetsSound().stopSoundAll();
				handler.destroy();
				LSystem.destroy();
				releaseResources();
				notifyAll();

			}
		} catch (Exception e) {
		}
	}

	public void setLogo(LImage img) {
		logo = img;
	}

	public LImage getLogo() {
		return logo;
	}

	private long innerClock() {
		long now = System.currentTimeMillis();
		long ret = now - before;
		before = now;
		return ret;
	}

	final private class CanvasThread extends Thread {

		private Canvas canvas;

		private int shake, num;

		/**
		 * 显示游戏logo
		 */
		private void showLogo() {

			try {

				long elapsedTime;
				int cx = 0, cy = 0;
				double delay;
				try {
					if (logo == null) {
						logo = GraphicsUtils.loadImage(
								"system/images/logo.png", false);
					}
					cx = LGameView.this.getWidth() / 2 - logo.getWidth() / 2;
					cy = LGameView.this.getHeight() / 2 - logo.getHeight() / 2;
				} catch (Exception e) {

				}
				float alpha = 0.0f;
				boolean firstTime = true;
				elapsedTime = innerClock();
				gl.setAntiAlias(true);
				while (alpha < 1.0f) {
					gl.drawClear();
					gl.setAlpha(alpha);
					gl.drawImage(logo, cx, cy);
					if (firstTime) {
						firstTime = false;
					}
					elapsedTime = innerClock();
					delay = 0.00065 * elapsedTime;
					if (delay > 0.22) {
						delay = 0.22 + (delay / 6);
					}
					alpha += delay;
					update();
				}
				while (num < 3000) {
					num += innerClock();
					update();
				}
				alpha = 1.0f;
				while (alpha > 0.0f) {
					gl.drawClear();
					gl.setAlpha(alpha);
					gl.drawImage(logo, cx, cy);
					elapsedTime = innerClock();
					delay = 0.00055 * elapsedTime;
					if (delay > 0.15) {
						delay = 0.15 + ((delay - 0.04) / 2);
					}
					alpha -= delay;
					update();
				}
				gl.setAntiAlias(false);
			} catch (Throwable e) {
			} finally {
				isLogo = false;
			}
		}

		/**
		 * 更新游戏画布
		 */
		private void update() {
			try {
				canvas = surfaceHolder.lockCanvas(rect);
				if (canvas == null) {
					return;
				}
				synchronized (surfaceHolder) {
					canvas.drawBitmap(currentScreen, 0, 0, null);
				}
			} finally {
				if (canvas != null) {
					surfaceHolder.unlockCanvasAndPost(canvas);
					gl.restore();
				}
			}
		}

		/**
		 * 游戏窗体主循环
		 */
		public void run() {
			if (isLogo) {
				showLogo();
			}
			try {
				final LTimerContext timerContext = new LTimerContext();
				final SystemTimer timer = LSystem.getSystemTimer();
				IScreen screen;
				timerContext.setTimeMillis(startTime = timer.getTimeMillis());
				do {
					try {
						canvas = surfaceHolder.lockCanvas(rect);
						synchronized (surfaceHolder) {
							if (paused || !isFocusable()) {
								pause(500);
								continue;
							}
							screen = handler.getScreen();
							if (!screen.next()) {
								continue;
							}
							shake = screen.getShakeNumber();
							if (shake > 0) {
								gl.drawBitmap(screen.getBackground(), shake / 2
										- LSystem.random.nextInt(shake), shake
										/ 2 - LSystem.random.nextInt(shake));
							} else if (shake == -1) {
								gl.drawBitmap(screen.getBackground(), 0, 0);
							}
							curTime = timer.getTimeMillis();
							timerContext.setTimeSinceLastUpdate(curTime
									- timerContext.getTimeMillis());
							timerContext
									.setSleepTimeMillis((offsetTime - timerContext
											.getTimeSinceLastUpdate())
											- timerContext
													.getOverSleepTimeMillis());
							if (timerContext.getSleepTimeMillis() > 0) {
								this.pause(timerContext.getSleepTimeMillis());
								timerContext.setOverSleepTimeMillis((timer
										.getTimeMillis() - curTime)
										- timerContext.getSleepTimeMillis());
							} else {
								timerContext.setOverSleepTimeMillis(0L);
							}
							timerContext.setTimeMillis(timer.getTimeMillis());
							screen.callEvents();
							screen.runTimer(timerContext);
							screen.createUI(gl);
							if (isFPS) {
								tickFrames();
								gl.setAntiAlias(true);
								gl.setFont(fpsFont);
								gl.setColor(LColor.white);
								gl.drawString("FPS:" + curFPS, fpsX, fpsY);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						if (canvas != null) {
							canvas.drawBitmap(currentScreen, 0, 0, null);
							surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}
				} while (running);
			} catch (Exception ex) {
				ex.getStackTrace();
				if (isDebug) {
					try {
						// 弹出异常
						handler.getLGameActivity().showAndroidException(ex);
					} catch (Exception e) {
					}
				}
			} finally {
				destroyView();
			}
		}

		private final void pause(long sleep) {
			try {
				Thread.sleep(sleep);
			} catch (InterruptedException e) {
			}
		}

		private void tickFrames() {
			frameCount++;
			calcInterval += offsetTime;
			if (calcInterval >= MAX_INTERVAL) {
				long timeNow = System.currentTimeMillis();
				long realElapsedTime = timeNow - startTime;
				curFPS = (long) ((frameCount / realElapsedTime) * MAX_INTERVAL);
				frameCount = 0L;
				calcInterval = 0L;
				startTime = timeNow;
			}
		}

	}

	public void setDebug(boolean debug) {
		this.isDebug = debug;
	}

	public boolean isDebug() {
		return isDebug;
	}

	public Thread getMainLoop() {
		return mainLoop;
	}

	public void setFPS(long frames) {
		this.maxFrames = frames;
		this.offsetTime = (long) (1.0 / maxFrames * MAX_INTERVAL);
	}

	public long getMaxFPS() {
		return this.maxFrames;
	}

	public long getCurrentFPS() {
		return this.curFPS;
	}

	public void setShowFPS(boolean isFPS) {
		this.isFPS = isFPS;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public boolean isShowLogo() {
		return isLogo;
	}

	public void setShowLogo(boolean showLogo) {
		this.isLogo = showLogo;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		try {
			if (!running) {
				this.setRunning(true);
				if (mainLoop == null) {
					this.mainLoop = new CanvasThread();
					this.mainLoop.setPriority(Thread.MAX_PRIORITY - 2);
					this.mainLoop.start();
				}
			}
		} catch (Exception e) {

		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (!paused) {
				if (mainLoop != null) {
					this.setRunning(false);
					this.mainLoop = null;
				}
			}
		} catch (Exception e) {

		}
	}

	private void stopThread() {
		try {
			if (mainLoop != null) {
				boolean result = true;
				setRunning(false);
				while (result) {
					try {
						mainLoop.join();
						result = false;
					} catch (InterruptedException e) {
					}
				}

			}
		} catch (Exception e) {

		}
	}

	private void releaseResources() {
		try {
			if (surfaceHolder != null) {
				surfaceHolder.removeCallback(this);
				surfaceHolder = null;
			}
			stopThread();
		} catch (Exception e) {

		}
	}

	public Handler getGameHandler() {
		return handler;
	}

}
