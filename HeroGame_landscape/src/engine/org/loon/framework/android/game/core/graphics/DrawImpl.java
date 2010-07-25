package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;

/**
 * 
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
class DrawImpl implements Draw {

	private int xPoints[] = new int[4];

	private int yPoints[] = new int[4];

	private LImage offsetScreen;

	private LGraphics offsetGraphics;

	private LFont font;

	public DrawImpl(ThreadScreen screen) {
		this.offsetScreen = screen.getImage();
		this.offsetGraphics = screen.getLGraphics();
		this.font = LFont.getFont("Monospaced", 0, 20);
	}

	public int rgbToPixel(int r, int g, int b) {
		return LColor.getPixel(r, g, b);
	}

	public void setFont(String fontName, int style, int size) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setFont(LFont.getFont(fontName, style, size));
	}

	public void drawText(String message, int x, int y, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.drawString(message, x, y);
	}

	public void drawText(String message, int x, int y, LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.drawString(message, x, y);
	}

	public void line(int x, int y, int w, int h, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.drawLine(x, y, w, h);
	}

	public void drawImage(LImage img, int x, int y) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.drawImage(img, x, y);
	}

	public void drawImage(LImage img, int x, int y, int w, int h) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.drawImage(img, x, y, w, h);
	}

	public void drawImage(LImage img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2);
	}

	public void drawImage(LImage image, int x, int y, int w, int h, int w1,
			int w2) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.drawImage(image, x, y, x + w, y + h, w1, w2, w1 + w, w2
				+ h);
	}

	public void drawImage(LImage image, int x, int y, int d) {
		if (offsetGraphics == null) {
			return;
		}
		switch (d) {
		case 0:
			offsetGraphics.drawImage(image, x, y);
			break;

		case 1:
			offsetGraphics.drawMirrorImage(image, x, y, false);
			break;

		case 2:
			offsetGraphics.drawFlipImage(image, x, y, false);
			break;

		case 3:
			offsetGraphics.drawReverseImage(image, x, y, false);
			break;
		}
	}

	public void line(int x, int y, int w, int h, LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.drawLine(x, y, w, h);
	}

	public void circleFill(int x, int y, int w, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.fillOval(x - w / 2, y - w / 2, w, w);
	}

	public void circleFill(int x, int y, int w, LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.fillOval(x - w / 2, y - w / 2, w, w);
	}

	public void circle(int x, int y, int w, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.drawArc(x - w / 2, y - w / 2, w, w, 0, 360);
	}

	public void circle(int x, int y, int w, LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.drawArc(x - w / 2, y - w / 2, w, w, 0, 360);

	}

	public void rect(int x, int y, int w, int h, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.drawRect(x, y, w, h);
	}

	public void rect(int x, int y, int w, int h, LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.drawRect(x, y, w, h);
	}

	public void fillRect(int x, int y, int w, int h, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		xPoints[0] = x;
		yPoints[0] = y;
		xPoints[1] = x + w;
		yPoints[1] = y;
		xPoints[2] = x + w;
		yPoints[2] = y + h;
		xPoints[3] = x;
		yPoints[3] = y + h;
		offsetGraphics.fillPolygon(xPoints, yPoints, 4);
	}

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints,
			LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.fillPolygon(xPoints, yPoints, nPoints);
	}

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints,
			LColor color) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(color);
		offsetGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints, int pixel) {
		if (offsetGraphics == null) {
			return;
		}
		offsetGraphics.setColor(pixel);
		offsetGraphics.drawPolygon(xPoints, yPoints, nPoints);
	}

	public void surfaceCopy(LGraphics g) {
		g.drawImage(offsetScreen, 0, 0);
	}

	public int getFontSize() {
		if (font != null) {
			return font.getSize();
		} else {
			return 0;
		}
	}

	public int getHeight() {
		return offsetScreen.getHeight();
	}

	public int getWidth() {
		return offsetScreen.getWidth();
	}

}
