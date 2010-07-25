package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;

public class InfoBox {

	private int curX, curY;
	private boolean isVisible;
	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	public int getCurX() {
		return curX;
	}

	public void setCurX(int curX) {
		this.curX = curX;
	}

	public int getCurY() {
		return curY;
	}

	public void setCurY(int curY) {
		this.curY = curY;
	}

	public InfoBox() {
		curX = curY = -1;
		isVisible = false;
	}
	
	public void draw(LGraphics g, int offsetX, int offsetY) {
		if (isVisible)
			g.drawImage("assets/images/touchbox.png", curX * 32 + offsetX, curY * 32 + offsetY, 32, 32);
	}
}
