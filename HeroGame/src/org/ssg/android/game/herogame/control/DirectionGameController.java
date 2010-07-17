package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class DirectionGameController extends Dialog {

	private Button[] buttons;

	private static int BUTTON_NUM = 4;

	public DirectionGameController(int x, int y) {
		this(null, 32, 396, x, y);
	}

	public DirectionGameController(String fileName, int scaledWidth,
			int scaledHeight, int x, int y) {
		super(fileName, scaledWidth, scaledHeight, x, y);
		initButtons();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawButtonEx(g, buttons[0], 0, 0);
		drawButtonEx(g, buttons[1], 0, 100);
		drawButtonEx(g, buttons[2], 0, 200);
		drawButtonEx(g, buttons[3], 0, 300);

	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];

		LImage unchecked = GraphicsUtils.loadImage("assets/images/up.png");
		LImage checked = GraphicsUtils
				.loadImage("assets/images/up_pressed.png");
		buttons[0] = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		initSingleButton(buttons[0], MainScreen.instance.goUpKey);

		unchecked = GraphicsUtils.loadImage("assets/images/down.png");
		checked = GraphicsUtils.loadImage("assets/images/down_pressed.png");
		buttons[1] = new Button(MainScreen.instance, 1, 0, false, checked,
				unchecked);
		initSingleButton(buttons[1], MainScreen.instance.goDownKey);
		
		unchecked = GraphicsUtils.loadImage("assets/images/left.png");
		checked = GraphicsUtils.loadImage("assets/images/left_pressed.png");
		buttons[2] = new Button(MainScreen.instance, 2, 0, false, checked,
				unchecked);
		initSingleButton(buttons[2], MainScreen.instance.goLeftKey);
		
		unchecked = GraphicsUtils.loadImage("assets/images/right.png");
		checked = GraphicsUtils.loadImage("assets/images/right_pressed.png");
		buttons[3] = new Button(MainScreen.instance, 3, 0, false, checked,
				unchecked);
		initSingleButton(buttons[3], MainScreen.instance.goRightKey);
	}

	private void initSingleButton(Button button, final ActionKey key) {
		button.setName("");
		button.setComplete(false);
		button.setOnTouchListener(new DefaultOnTouchListener(button) {
			public Thread keyPressThread;

			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.checkComplete()) {
					if (keyPressThread != null) {
						keyPressThread.interrupt();
						keyPressThread = null;
					}
					keyPressThread = new Thread() {
						public void run() {
							try {
								while (true) {
									if (this.isInterrupted())
										throw new InterruptedException();
									if (!MainScreen.instance.heroFighting
											&& !MainScreen.instance.enemyFighting) {
										key.press();
									}
									sleep(50);
								}
							} catch (InterruptedException e) {
								Log.e("aaa", "key press thread interrputed.");
							}
						}
					};
					keyPressThread.start();
					return true;
				}
				return false;
			}

			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.isComplete()) {
					if (button.checkComplete()) {
						SystemClock.sleep(50);
						keyPressThread.interrupt();
						keyPressThread = null;
						key.reset();
					}
					button.setComplete(false);
				}
				return true;
			}

			@Override
			public boolean onTouchMove(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.isComplete()) {
					if (!button.checkComplete()) {
						SystemClock.sleep(50);
						button.setComplete(false);
						keyPressThread.interrupt();
						keyPressThread = null;
						key.reset();
					}
				}
				return true;
			}
		});
		addOnTouchListener(button.getOnTouchListener());
	}
}
