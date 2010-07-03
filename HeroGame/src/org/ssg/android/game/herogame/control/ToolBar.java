package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class ToolBar extends DefaultTouchable {

	private int x, y, width, height;
	private Button[] buttons;
	private static int BUTTON_NUM = 1;
	private LImage backgroundImage;

	public ToolBar(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		buttons = new Button[BUTTON_NUM];
		LImage checked = GraphicsUtils.loadImage("assets/images/char2.png");
		LImage unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		Button.initialize(MainScreen.instance, buttons, 0, checked, unchecked);

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setDrawXY(x + (width / BUTTON_NUM) * i + 5, y);
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
									if (!dialog.isShown()) {
										dialog.setShown(true);
										button.setComplete(true);
										button.setSelect(true);
									} else {
										dialog.setShown(false);
										button.setComplete(false);
										button.setSelect(false);
									}
								} else {
									if (dialog.isShown()) {
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

		backgroundImage = GraphicsUtils.loadImage("assets/images/buttombg.png");
	}

	public void draw(LGraphics g) {
		g.drawImage(backgroundImage, x, y, x + width, y + height, 0, 0, width,
				height);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].draw(g);
		}
	}

	@Override
	public boolean isTouched() {
		if (((double) MainScreen.instance.getTouch().x > x
				&& (double) MainScreen.instance.getTouch().x < x
						+ (double) this.width
				&& (double) MainScreen.instance.getTouch().y > y && (double) MainScreen.instance
				.getTouch().y < y + (double) this.height)) {
			return true;
		}
		return false;
	}
}
