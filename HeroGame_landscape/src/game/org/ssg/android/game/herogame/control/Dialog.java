package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;
import org.ssg.android.game.herogame.util.NinePatchImage;

public class Dialog extends DefaultTouchable {

	private NinePatchImage ninePatchImg;
	public LImage img, img1;
	public int scaledWidth, scaledHeight;
	protected int x, y;
	public boolean isShown;

	protected int PADDING_X;
	protected int PADDING_Y;
	protected int rowNum = 1;
	protected int colNum = 1;
	protected int[] curWidth;

	public boolean isAlwaysShown = false;
	public boolean isFirst = false;
	public String fileName;

	public boolean isShown() {
		return isShown;
	}

	public void setShown(boolean isShown) {
		this.isShown = isShown;
		setIsActive(isShown);
	}

	public Dialog(String fileName, int scaledWidth, int scaledHeight, int x,
			int y) {
		this(fileName, scaledWidth, scaledHeight);
		this.x = x;
		this.y = y;
	}

	public Dialog(String fileName, int scaledWidth, int scaledHeight) {
		this.scaledWidth = scaledWidth;
		this.scaledHeight = scaledHeight;
		x = (MainScreen.instance.WIDTH - scaledWidth) / 2;
		y = (MainScreen.instance.HEIGHT - scaledHeight) / 2;
		if (x < 0)
			x = 0;
		if (y < 0)
			y = 0;
		this.fileName = fileName;
		resetDialogBG();
		isShown = true;
	}

	public void resetDialogBG() {
		if (fileName != null && !fileName.equals("")) {
			if (fileName.contains(".9.")) {
				ninePatchImg = new NinePatchImage(fileName);
				img = ninePatchImg.createImage(scaledWidth, scaledHeight);
				img1 = ninePatchImg.createImage(320 - 8, 320 - 8);
			} else {
				img = GraphicsUtils.loadImage(fileName, true);
			}
		}
	}

	public void draw(LGraphics g) {
		if (!isShown)
			return;
		if (img != null) {
			if (!MainScreen.instance.isShownLeftPanel) {
				g.drawImage(img, x, y, x + scaledWidth, y + scaledHeight, 0, 0,
						scaledWidth, scaledHeight);
			} else {
				g.drawImage(img1, x, y, x + scaledWidth, y + scaledHeight, 0,
						0, scaledWidth, scaledHeight);
			}
		}
	}

	protected void drawAbsoluteImage(LGraphics g, LImage image, int row,
			int col, int width, int height) {
		drawAbsoluteImage(g, image, row, col, width, height, rowNum, colNum);
	}

	protected void drawAbsoluteString(LGraphics g, String value, int row,
			int col) {
		drawAbsoluteString(g, value, row, col, rowNum, colNum);
	}

	protected void drawButton(LGraphics g, Button button, int row, int col) {
		drawButton(g, button, row, col, rowNum, colNum);
	}

	protected void drawAbsoluteImage(LGraphics g, LImage image, int row,
			int col, int width, int height, int rowNum, int colNum) {
		int cellWidth = (scaledWidth - 2 * PADDING_X) / colNum;
		int cellHeight = (scaledHeight - 2 * PADDING_Y) / rowNum;
		int absoluteX = x + PADDING_X + col * cellWidth;
		int absoluteY = y + PADDING_Y + row * cellHeight
				+ (cellHeight - height) / 2;
		if (getCurWidth(row) > 0)
			absoluteX = getCurWidth(row) + 5;
		g.drawImage(image, absoluteX, absoluteY, absoluteX + width, absoluteY
				+ height, 0, 0, width, height);
		setCurWidth(row, absoluteX + width);
	}

	protected void drawAbsoluteString(LGraphics g, String value, int row,
			int col, int rowNum, int colNum) {
		int cellWidth = (scaledWidth - 2 * PADDING_X) / colNum;
		int cellHeight = (scaledHeight - 2 * PADDING_Y) / rowNum;
		int absoluteX = x + PADDING_X + col * cellWidth;
		int absoluteY = y + PADDING_Y + row * cellHeight
				+ (cellHeight - g.getFont().getSize()) / 2;
		if (getCurWidth(row) > 0)
			absoluteX = getCurWidth(row) + 5;
		g.drawString(value, absoluteX, absoluteY + g.getFont().getSize());
		setCurWidth(row, absoluteX + g.getFont().stringWidth(value));
	}

	protected void drawButton(LGraphics g, Button button, int row, int col,
			int rowNum, int colNum) {
		int cellWidth = (scaledWidth - 2 * PADDING_X) / colNum;
		int cellHeight = (scaledHeight - 2 * PADDING_Y) / rowNum;
		int absoluteX = x + PADDING_X + col * cellWidth;
		int absoluteY = y + PADDING_Y + row * cellHeight
				+ (cellHeight - button.getHeight()) / 2;
		if (getCurWidth(row) > 0)
			absoluteX = getCurWidth(row) + 5;
		button.setDrawXY(absoluteX, absoluteY);
		button.draw(g);
		setCurWidth(row, absoluteX + button.getWidth());
	}

	protected void drawAbsoluteImageEx(LGraphics g, LImage image, int x, int y,
			int width, int height) {
		g.drawImage(image, x + this.x, y + this.y, x + this.x + width, y
				+ this.y + height, 0, 0, width, height);
	}

	protected void drawAbsoluteStringEx(LGraphics g, String value, int x, int y) {
		g.drawString(value, x + this.x, y + this.y + g.getFont().getSize());
	}

	protected void drawButtonEx(LGraphics g, Button button, int x, int y) {
		button.setDrawXY(x + this.x, y + this.y);
		button.draw(g);
	}

	protected void drawButton(LGraphics g, Button button) {
		button.draw(g);
	}

	protected void drawRect(LGraphics g, int x, int y, int w, int h) {
		g.drawRect(x + this.x, y + this.y, w, h);
	}
	
	protected void drawString(LGraphics g, String message, int x, int y) {
		g.drawString(message, x + this.x, y + this.y + g.getFont().getSize());
	}
	
	@Override
	public boolean isTouched() {
		if (((double) MainScreen.instance.getTouch().x > x
				&& (double) MainScreen.instance.getTouch().x < x
						+ (double) this.scaledWidth
				&& (double) MainScreen.instance.getTouch().y > y && (double) MainScreen.instance
				.getTouch().y < y + (double) this.scaledHeight)) {
			return true;
		}
		return false;
	}

	private void setCurWidth(int row, int width) {
		if (curWidth == null) {
			curWidth = new int[rowNum];
		}
		curWidth[row] += width + 5;
	}

	private int getCurWidth(int row) {
		if (curWidth == null) {
			curWidth = new int[rowNum];
		}
		return curWidth[row];
	}
}
