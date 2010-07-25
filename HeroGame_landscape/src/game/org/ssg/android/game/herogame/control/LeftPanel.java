package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class LeftPanel extends Dialog {

	public LImage leftPanelBG;
	
	private Button[] buttons;

	private static int BUTTON_NUM = 5;

	public LeftPanel(int x, int y) {
		this(null, 160, 320, x, y);
	}

	public LeftPanel(String fileName, int scaledWidth,
			int scaledHeight, int x, int y) {
		super(fileName, scaledWidth, scaledHeight, x, y);
		leftPanelBG = GraphicsUtils.loadImage("assets/images/leftpanelbg.png");
		initButtons();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		g.drawImage(leftPanelBG, 0, 0);
		drawButtonEx(g, buttons[4], 0, 0);
		drawButtonEx(g, buttons[0], 44, 191);//up
		drawButtonEx(g, buttons[1], 44, 280);//down
		drawButtonEx(g, buttons[2], 0, 239);//left
		drawButtonEx(g, buttons[3], 106, 239);//right

	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];

		LImage unchecked = GraphicsUtils.loadImage("assets/images/up.png");
		LImage checked = GraphicsUtils
				.loadImage("assets/images/up1.png");
		buttons[0] = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		initSingleButton(buttons[0], MainScreen.instance.goUpKey);

		unchecked = GraphicsUtils.loadImage("assets/images/down.png");
		checked = GraphicsUtils.loadImage("assets/images/down1.png");
		buttons[1] = new Button(MainScreen.instance, 1, 0, false, checked,
				unchecked);
		initSingleButton(buttons[1], MainScreen.instance.goDownKey);
		
		unchecked = GraphicsUtils.loadImage("assets/images/left.png");
		checked = GraphicsUtils.loadImage("assets/images/left1.png");
		buttons[2] = new Button(MainScreen.instance, 2, 0, false, checked,
				unchecked);
		initSingleButton(buttons[2], MainScreen.instance.goLeftKey);
		
		unchecked = GraphicsUtils.loadImage("assets/images/right.png");
		checked = GraphicsUtils.loadImage("assets/images/right1.png");
		buttons[3] = new Button(MainScreen.instance, 3, 0, false, checked,
				unchecked);
		initSingleButton(buttons[3], MainScreen.instance.goRightKey);
		
		unchecked = GraphicsUtils.loadImage("assets/images/unexpand.png");
		buttons[4] = new Button(MainScreen.instance, 4, 0, false, unchecked,
				unchecked);
		buttons[4].setName("");
		buttons[4].setComplete(false);
		buttons[4].setOnTouchListener(new DefaultOnTouchListener(buttons[4]) {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.checkComplete()) {
					if (button.checkClick() != -1) {
						if (MainScreen.instance.isShownLeftPanel) {
							MainScreen.instance.changeNext = false;
							button.setComplete(true);
							button.setSelect(true);
						}
					}
					return true;
				}
				return false;
			}
			
			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				return false;
			}
		});
		addOnTouchListener(buttons[4].getOnTouchListener());
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
									sleep(100);
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
//						SystemClock.sleep(50);
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
//						SystemClock.sleep(50);
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
