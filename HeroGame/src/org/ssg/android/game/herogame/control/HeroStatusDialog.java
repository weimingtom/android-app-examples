package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.ssg.android.game.herogame.Hero;

public class HeroStatusDialog extends Dialog {

	private static final String DIALOG_BG = "assets/images/window.9.png";
	private Hero hero;
	public HeroStatusDialog(String fileName, int scaledWidth, int scaledHeight) {
		super(fileName, scaledWidth, scaledHeight);
	}

	public HeroStatusDialog(Hero hero) {
		this(DIALOG_BG, 300, 460);
		this.hero = hero;
	}
	
	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if(!isShown())
			return;
		LImage img = hero.getImg();
		g.drawImage(img, x + 20, y + 20, x + 40, y + 52, 0, 0,
				20, 32);
		g.drawString(hero.getAvailablePoints() + "", x + 100, y + 100);
	}
}
