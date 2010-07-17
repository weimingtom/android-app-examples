package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.util.Log;
import android.view.MotionEvent;

public class DirectionGameController1 extends Dialog {

	private Button[] buttons;

	private static int BUTTON_NUM = 4;

	public DirectionGameController1(int x, int y) {
		this(null, 160, 160, x, y);
	}

	public DirectionGameController1(String fileName, int scaledWidth,
			int scaledHeight, int x, int y) {
		super(fileName, scaledWidth, scaledHeight, x, y);
		rowNum = 3;
		colNum = 1;
		initButtons();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawButtonEx(g, buttons[0], 60, 10);
		drawButtonEx(g, buttons[1], 20, 60);
		drawButtonEx(g, buttons[2], 100, 60);
		drawButtonEx(g, buttons[3], 60, 110);

	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];

		LImage checked = GraphicsUtils.loadImage("assets/images/arrow_up.png");
		LImage unchecked = GraphicsUtils
				.loadImage("assets/images/arrow_up_1.png");
		buttons[0] = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		initSingleButton(buttons[0], MainScreen.instance.goUpKey);

		checked = GraphicsUtils.loadImage("assets/images/arrow_left.png");
		unchecked = GraphicsUtils.loadImage("assets/images/arrow_left_1.png");
		buttons[1] = new Button(MainScreen.instance, 1, 0, false, checked,
				unchecked);
		initSingleButton(buttons[1], MainScreen.instance.goLeftKey);

		checked = GraphicsUtils.loadImage("assets/images/arrow_right.png");
		unchecked = GraphicsUtils.loadImage("assets/images/arrow_right_1.png");
		buttons[2] = new Button(MainScreen.instance, 2, 0, false, checked,
				unchecked);
		initSingleButton(buttons[2], MainScreen.instance.goRightKey);

		checked = GraphicsUtils.loadImage("assets/images/arrow_down.png");
		unchecked = GraphicsUtils.loadImage("assets/images/arrow_down_1.png");
		buttons[3] = new Button(MainScreen.instance, 3, 0, false, checked,
				unchecked);
		initSingleButton(buttons[3], MainScreen.instance.goDownKey);

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
					if (MainScreen.instance.heroFighting
							|| MainScreen.instance.enemyFighting) {
						return false;
					}
					keyPressThread = new Thread() {
						public void run() {
							try {
								while (true) {
									if (this.isInterrupted())
										throw new InterruptedException();
									key.press();
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
						keyPressThread.interrupt();
						keyPressThread = null;
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
						button.setComplete(false);
						keyPressThread.interrupt();
						keyPressThread = null;
					}
				}
				return true;
			}
		});
		addOnTouchListener(button.getOnTouchListener());
	}
}
