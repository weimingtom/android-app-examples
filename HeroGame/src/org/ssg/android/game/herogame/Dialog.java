package org.ssg.android.game.herogame;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;

public class Dialog {

	private NinePatchImage ninePatchImg;
	private LImage img;
	private int scaledWidth, scaledHeight;
	private int x, y;
	private boolean isShown;

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
	}

	public Dialog(String fileName, int scaledWidth, int scaledHeight) {
		ninePatchImg = new NinePatchImage(fileName);
		this.scaledWidth = scaledWidth;
		this.scaledHeight = scaledHeight;
		x = (MainScreen.WIDTH - scaledWidth) / 2;
		y = (MainScreen.HEIGHT - scaledHeight) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		img = ninePatchImg.createImage(scaledWidth, scaledHeight);
		isShown = true;
	}

	public void draw(LGraphics g) {
		if (!isShown)
			return;
		g.drawImage(img, x, y, x + scaledWidth, y + scaledHeight, 0, 0,
				scaledWidth, scaledHeight);
	}
}
