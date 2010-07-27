package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class ToolBar extends Dialog {

	private Button[] buttons;
	private static int BUTTON_NUM = 3;

	public ToolBar(int x, int y, int scaledWidth, int scaledHeight) {
		super("assets/images/leftpanelbg1.png", scaledWidth, scaledHeight, x, y);
		initButtons();
		isAlwaysShown = true;
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		if (MainScreen.instance.topDialog != HeroStatusDialog.instance) {
			buttons[0].setComplete(false);
			buttons[0].setSelect(false);
		}
		if (MainScreen.instance.topDialog != InventoryDialog.instance) {
			buttons[2].setComplete(false);
			buttons[2].setSelect(false);
		}
		drawButtonEx(g, buttons[1], 0, 0);
		drawButtonEx(g, buttons[0], 0, 35);
		drawButtonEx(g, buttons[2], 0, 100);
	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];
		LImage checked = GraphicsUtils.loadImage("assets/images/char2.png");
		LImage unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		buttons[0] = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		buttons[0].setName("");
		buttons[0].setComplete(false);
		buttons[0].setOnTouchListener(new DefaultOnTouchListener(buttons[0]) {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				Button button = (Button) getRef();
				HeroStatusDialog dialog = HeroStatusDialog.instance;
				if (button.checkComplete()) {
					if (button.checkClick() != -1) {
						if (MainScreen.instance.topDialog == null
								|| !MainScreen.instance.topDialog
										.equals(dialog)) {
							MainScreen.instance.topDialog = dialog;
							button.setComplete(true);
							button.setSelect(true);
						} else {
							MainScreen.checkLock();
							MainScreen.instance.topDialog = MainScreen.instance.defaultTopDialog;
							button.setComplete(false);
							button.setSelect(false);
						}
					} else {
						if (MainScreen.instance.topDialog != null
								&& MainScreen.instance.topDialog.equals(dialog)) {
							button.setComplete(true);
							button.setSelect(true);
						}
					}
					return true;
				}
				return false;
			}
		});
		addOnTouchListener(buttons[0].getOnTouchListener());
		
		unchecked = GraphicsUtils.loadImage("assets/images/expand.png");
		buttons[1] = new Button(MainScreen.instance, 1, 0, false, unchecked,
				unchecked);
		buttons[1].setName("");
		buttons[1].setComplete(false);
		buttons[1].setOnTouchListener(new DefaultOnTouchListener(buttons[1]) {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.checkComplete()) {
					if (button.checkClick() != -1) {
						if (MainScreen.instance.isShownLeftPanel == false) {
							MainScreen.instance.changeNext = true;
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
		addOnTouchListener(buttons[1].getOnTouchListener());
		

		checked = GraphicsUtils.loadImage("assets/images/char2.png");
		unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		buttons[2] = new Button(MainScreen.instance, 2, 0, false, checked,
				unchecked);
		buttons[2].setName("");
		buttons[2].setComplete(false);
		buttons[2].setOnTouchListener(new DefaultOnTouchListener(buttons[2]) {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				Button button = (Button) getRef();
				InventoryDialog dialog = InventoryDialog.instance;
				if (button.checkComplete()) {
					if (button.checkClick() != -1) {
						if (MainScreen.instance.topDialog == null
								|| !MainScreen.instance.topDialog
										.equals(dialog)) {
							MainScreen.instance.topDialog = dialog;
							button.setComplete(true);
							button.setSelect(true);
						} else {
							MainScreen.checkLock();
							MainScreen.instance.topDialog = MainScreen.instance.defaultTopDialog;
							button.setComplete(false);
							button.setSelect(false);
						}
					} else {
						if (MainScreen.instance.topDialog != null
								&& MainScreen.instance.topDialog.equals(dialog)) {
							button.setComplete(true);
							button.setSelect(true);
						}
					}
					return true;
				}
				return false;
			}
		});
		addOnTouchListener(buttons[2].getOnTouchListener());
	}
}
