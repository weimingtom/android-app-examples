package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.util.NinePatchImage;

import android.view.MotionEvent;

public class CircleGameController {

	private int bgX, bgY;
	private int ballX, ballY;

	private LImage backgroundImage, ballImage;
	private int bgWidth = 240;
	private int bgHeight = 240;
	private int ballWidth = 40;
	private int ballHeight = 40;
	private double sin, cos, distance;
	private int centerX, centerY;

	public CircleGameController(int x, int y) {
		this("assets/window.9.png", x, y);
	}

	public CircleGameController(String fileName, int x, int y) {
		this.bgX = x;
		this.bgY = y;
		centerX = bgX + bgWidth / 2;
		centerY = bgY + bgHeight / 2;
		backgroundImage = new NinePatchImage(fileName).createImage(bgWidth,
				bgHeight);
		ballImage = GraphicsUtils.loadImage("assets/ball.png", true);
		ballX = x + bgWidth / 2 - ballWidth / 2;
		ballY = y + bgHeight / 2 - ballHeight / 2;
	}

	public void draw(LGraphics g) {
		g.drawArc(bgX, bgY, bgWidth, bgHeight, 0, 360);
		g.drawImage(backgroundImage, bgX, bgY, bgX + bgWidth, bgY + bgHeight,
				0, 0, bgWidth, bgHeight);
		g.drawImage(ballImage, ballX, ballY, ballX + ballWidth, ballY
				+ ballHeight, 0, 0, ballWidth, ballHeight);
		g.setColor(LColor.orange);
		g.drawLine(centerX, centerY, ballX + ballWidth / 2, ballY + ballHeight
				/ 2);
	}

	public boolean onTouchDown(MotionEvent e) {
		checkTouch(e);
		return false;
	}

	public boolean onTouchMove(MotionEvent e) {
		checkTouch(e);
		return false;
	}

	public boolean onTouchUp(MotionEvent e) {
		ballX = bgX + bgWidth / 2 - ballWidth / 2;
		ballY = bgY + bgHeight / 2 - ballHeight / 2;
		return false;
	}

	private synchronized void checkTouch(MotionEvent e) {
		int x = (int) e.getX();
		int y = (int) e.getY();
		distance = Math.sqrt((centerX - x) * (centerX - x) + (centerY - y)
				* (centerY - y));
		sin = Math.abs(x - centerX) / distance;
		cos = Math.abs(y - centerY) / distance;
		if (distance > bgWidth / 2 - ballWidth / 2) {
			int offsetX = (int) ((bgWidth / 2 - ballWidth / 2) * sin);
			if (x < centerX)
				x = centerX - offsetX;
			else
				x = centerX + offsetX;

			int offsetY = (int) ((bgHeight / 2 - ballHeight / 2) * cos);
			if (y < centerY)
				y = centerY - offsetY;
			else
				y = centerY + offsetY;
		}
		ballX = x - ballWidth / 2;
		ballY = y - ballHeight / 2;
	}
}
