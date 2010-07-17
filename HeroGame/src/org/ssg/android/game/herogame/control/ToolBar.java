package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class ToolBar extends Dialog {

	private Button[] buttons;
	private static int BUTTON_NUM = 1;

	public ToolBar(int x, int y, int scaledWidth, int scaledHeight) {
		super("assets/images/buttombg.png", scaledWidth, scaledHeight, x, y);
		initButtons();
		isAlwaysShown = true;
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawButton(g, buttons[0]);
	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];
		LImage checked = GraphicsUtils.loadImage("assets/images/char2.png");
		LImage unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		Button.initialize(MainScreen.instance, buttons, 0, checked, unchecked);

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDrawXY(x + (scaledWidth / BUTTON_NUM) * i + 5, y);
			buttons[i].setName("");
			buttons[i].setComplete(false);
			buttons[i]
					.setOnTouchListener(new DefaultOnTouchListener(buttons[i]) {
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
											&& MainScreen.instance.topDialog
													.equals(dialog)) {
										button.setComplete(true);
										button.setSelect(true);
									}
								}
								return true;
							}
							return false;
						}
					});
			addOnTouchListener(buttons[i].getOnTouchListener());
		}
	}
}
