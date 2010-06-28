package org.ssg.android.game.herogame;

import org.loon.framework.android.game.action.sprite.StatusBar;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;

import android.util.Log;

public class TitledAndBorderedStatusBar extends StatusBar {

	private static final long serialVersionUID = 1L;

	private String title;

	private int xx, yy;

	public TitledAndBorderedStatusBar(int value, int max, int x, int y,
			int width, int height, String title) {
		super(value, max, x + 28, y, width, height);
		this.title = title;
		this.xx = x;
		this.yy = y;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	int i = 0;

	@Override
	public void createUI(LGraphics g) {
		g.setAntiAlias(true);
		super.createUI(g);
		g.setFont(LFont.SIZE_SMALL + 2);
		int h = g.getFont().getSize();
		g.drawString(title, xx, yy + h / 2 + 1);
		g.drawRect(x() - 2, y() - 2, getWidth() + 4, getHeight() + 4);
		g.setAntiAlias(false);
	}
}
