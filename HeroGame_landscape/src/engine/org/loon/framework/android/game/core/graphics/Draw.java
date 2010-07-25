package org.loon.framework.android.game.core.graphics;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;


/**
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
public interface Draw {

	public int rgbToPixel(int r, int g, int b);

	public void setFont(String fontName, int style, int size);

	public void drawText(String message, int x, int y, int pixel);

	public void drawText(String message, int x, int y, LColor color);

	public void line(int x, int y, int w, int h, int pixel);

	public void drawImage(LImage img, int x, int y);

	public void drawImage(LImage img, int x, int y, int w, int h);

	public void drawImage(LImage img, int dx1, int dy1, int dx2, int dy2,
			int sx1, int sy1, int sx2, int sy2);

	public void drawImage(LImage image, int x, int y, int w, int h, int w1,
			int w2);

	public void drawImage(LImage image, int x, int y, int d);

	public void line(int x, int y, int w, int h, LColor color);

	public void circleFill(int x, int y, int w, int pixel);

	public void circleFill(int x, int y, int w, LColor color);

	public void circle(int x, int y, int w, int pixel);

	public void circle(int x, int y, int w, LColor color);

	public void rect(int x, int y, int w, int h, int pixel);

	public void rect(int x, int y, int w, int h, LColor color);

	public void fillRect(int x, int y, int w, int h, int pixel);

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints, int pixel);

	public void fillPolygon(int xPoints[], int yPoints[], int nPoints,
			LColor color);

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints,
			LColor color);

	public void drawPolygon(int xPoints[], int yPoints[], int nPoints, int pixel);

	public void surfaceCopy(LGraphics g);

	public int getFontSize();

	public int getHeight();

	public int getWidth();

}

