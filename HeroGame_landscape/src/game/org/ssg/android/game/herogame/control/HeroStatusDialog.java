package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.extend.DrawButton;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.Hero;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class HeroStatusDialog extends Dialog {

	public static HeroStatusDialog instance;

	private static final String DIALOG_BG = "assets/images/window.9.png";

	private Hero hero;

	private Button[] buttons;

	private static int BUTTON_NUM = 1;

	public HeroStatusDialog(String fileName, int scaledWidth, int scaledHeight) {
		super(fileName, scaledWidth, scaledHeight);
	}

	public HeroStatusDialog(Hero hero) {
		this(DIALOG_BG, 440, 312);
		instance = this;
		this.hero = hero;
		PADDING_X = 20;
		PADDING_Y = 20;
		rowNum = 8;
		colNum = 1;

		initButtons();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		LImage img = hero.getImg();
		drawAbsoluteImage(g, img, 0, 0, 20, 32, 8, 2);

		g.setAntiAlias(true);
		LFont l = g.getFont();
//		g.setFont(LFont.SIZE_SMALL);
		drawAbsoluteString(g, "Hero Name: " + "Sharyu", 0, 1, 8, 2);
		drawAbsoluteString(g, "Hero Level: " + hero.getLevel(), 1, 0);
		drawAbsoluteString(g, "Hit Point: " + hero.getHp() + "/"
				+ hero.getMaxHP(), 2, 0);
		drawAbsoluteString(g, "Experience: " + hero.getExp() + "/"
				+ hero.getEXPtoNextLevel(), 3, 0);
		drawAbsoluteString(g, "Points Remaining: " + hero.getAvailablePoints(),
				4, 0);

		drawAbsoluteString(g, "Attack: " + hero.getAttack(), 5, 0, 8, 3);

		drawAbsoluteString(g, "Defence: " + hero.getDefence(), 6, 0);
		drawAbsoluteString(g, "MaxHP: " + hero.getMaxHP(), 7, 0);
		g.setFont(l);
		g.setAntiAlias(false);

		for (int i = 0; i < buttons.length; i++) {
			drawButton(g, buttons[i], 5, 1 + i, 8, 3);
		}

		curWidth = null;
	}

	private void initButtons() {
		buttons = new Button[BUTTON_NUM];
		LImage checked = GraphicsUtils
				.loadImage("assets/images/addpoint_down.png");
		LImage unchecked = GraphicsUtils
				.loadImage("assets/images/addpoint_up.png");
		Button.initialize(MainScreen.instance, buttons, 0, checked, unchecked);

		for (int i = 0; i < buttons.length; i++) {
			// buttons[i].setDrawXY(x + (scaledWidth / BUTTON_NUM) * i + 5, y +
			// 5);
			buttons[i].setName("");
			buttons[i].setComplete(false);
			buttons[i]
					.setOnTouchListener(new DefaultOnTouchListener(buttons[i]) {
						@Override
						public boolean onTouchUp(MotionEvent arg0) {
							Button button = (Button) getRef();
							if (button.isComplete()) {
								if (button.checkComplete()) {
									if (hero.getAvailablePoints() > 0) {
										hero.setAttack(hero.getAttack() + 1);
										hero.setAvailablePoints(hero
												.getAvailablePoints() - 1);
									}
								}
								button.setComplete(false);
							}
							return true;
						}
					});
			addOnTouchListener(buttons[i].getOnTouchListener());
		}
	}
}
