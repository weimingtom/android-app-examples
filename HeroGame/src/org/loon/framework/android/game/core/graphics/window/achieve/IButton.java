package org.loon.framework.android.game.core.graphics.window.achieve;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LComponent;
import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.geom.Insets;
import org.loon.framework.android.game.core.graphics.window.LButton;
import org.loon.framework.android.game.core.graphics.window.UIFactory;
import org.loon.framework.android.game.core.graphics.window.UIStatic;

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

public class IButton extends UIFactory {

	public IButton() {
		this.put("Background LColor", LColor.black);
		this.put("Background Over LColor", LColor.gray);
		this.put("Background Pressed LColor", LColor.gray);
		this.put("Background Border LColor", LColor.black);
		this.put("Background Disabled LColor", new LColor(139, 139, 139));

		this.put("Text LColor", LColor.white);
		this.put("Text Over LColor", LColor.white);
		this.put("Text Pressed LColor", LColor.white);
		this.put("Text Disabled LColor", new LColor(181, 181, 181));

		this.put("Text Horizontal Alignment Integer", UIStatic.CENTER);
		this.put("Text Vertical Alignment Integer", UIStatic.CENTER);
		this.put("Text Insets", new Insets(5, 5, 5, 5));
		this.put("Text Vertical Space Integer", new Integer(1));
	}

	public String getUIName() {
		return "Button";
	}

	public String[] getUIDescription() {
		return new String[] { "Button", "Button Over", "Button Pressed",
				"Button Disabled" };
	}

	public LImage[] createUI(LComponent component, int w, int h) {
		LImage[] ui = LImage.createImage(4, w, h,true);

		String[] color = new String[] { "Background LColor",
				"Background Over LColor", "Background Pressed LColor",
				"Background Disabled LColor" };

		LColor borderColor = (LColor) this.get("Background Border LColor",
				component);

		for (int i = 0; i < 4; i++) {
			LGraphics g = ui[i].getLGraphics();
			g.setColor((LColor) this.get(color[i], component));
			switch (i) {
			case 0:
				g.fill3DRect(0, 0, w - 1, h - 1, true);
				break;
			case 1:
				g.fillRect(0, 0, w - 1, h - 1);
				break;
			case 2:
				g.fill3DRect(0, 0, w - 1, h - 1, false);
				break;
			case 3:
				g.fill3DRect(0, 0, w - 1, h - 1, true);
				break;
			}
			if (borderColor != null) {
				g.setColor(borderColor);
				g.drawRect(0, 0, w - 1, h - 1);
			}
			g.dispose();
		}

		return ui;
	}

	public void processUI(LComponent component, LImage[] ui) {
		LButton button = (LButton) component;
		String text = button.getText();
		if (text != null) {
			int length = text.length();
			LFont font = button.getFont();
			if (length > 0) {
				for (int i = 0; i < 4; i++) {
					LGraphics g = ui[i].getLGraphics();
					g.setAntiAlias(true);
					g.setFont(font);
					g.setColor(button.getFontColor());
					g.drawString(text, button.getOffsetLeft()
							+ (button.getWidth() - font.stringWidth(text)) / 2,
							button.getOffsetTop()
									+ (button.getHeight() - font
											.getLineHeight()) / 2
									+ font.getLineHeight());
					g.dispose();
				}
			}
		}
	}

	public void createUI(LGraphics g, int x, int y, LComponent component,
			LImage[] buttonImage) {
		LButton button = (LButton) component;
		if (!button.isEnabled()) {
			g.drawImage(buttonImage[3], x, y);
		} else if (button.isTouchPressed()) {
			g.drawImage(buttonImage[2], x, y);
		} else if (button.isTouchOver()) {
			g.drawImage(buttonImage[1], x, y);
		} else {
			g.drawImage(buttonImage[0], x, y);
		}
	}

}
