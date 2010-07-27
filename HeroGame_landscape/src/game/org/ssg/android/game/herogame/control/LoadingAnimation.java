package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.action.sprite.WaitAnimation;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.timer.LTimer;

public class LoadingAnimation extends WaitAnimation {

	private LTimer timer;
	
	private static final String message0 = "Loading";
	private static final String message1 = "Loading.";
	private static final String message2 = "Loading..";
	private static final String message3 = "Loading...";
	private static final String message4 = "Loading....";
	private static final String message5 = "Loading.....";
	private static final String message6 = "Loading......";
	private String message = "";
	
	private int dot = 1;

	public LoadingAnimation(int width, int height) {
		super(width, height);
		timer = new LTimer(100);
	}

	public void update(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			next();
			switch (dot / 2) {
			case 0:
				message = message0;
				dot++;
				break;
			case 1:
				message = message1;
				dot++;
				break;
			case 2:
				message = message2;
				dot++;
				break;
			case 3:
				message = message3;
				dot++;
				break;
			case 4:
				message = message4;
				dot++;
				break;
			case 5:
				message = message5;
				dot++;
				break;
			case 6:
				message = message6;
				dot = 1;
				break;
			}
		}
	}
	
	@Override
	public void draw(LGraphics g, int x, int y, int w, int h) {
		super.draw(g, x, y, w, h);
		g.setColor(LColor.white);
		g.setAlpha(0.8f);
		g.setAntiAlias(true);
		g.drawString(message, x + w - 100, y + h - 20);
	}
}
