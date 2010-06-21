package org.loon.framework.android.game.core.graphics.window.achieve;

import org.loon.framework.android.game.core.graphics.LComponent;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.window.UIFactory;

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
public class IPanel extends UIFactory {

	public IPanel() {
	}

	public String getUIName() {
		return "Panel";
	}

	public String[] getUIDescription() {
		return new String[] { "Panel" };
	}

	public LImage[] createUI(LComponent component, int w, int h) {
		LImage[] ui = LImage.createImage(2, w, h,true);
		return ui;
	}

	public void processUI(LComponent component, LImage[] ui) {
	}

	public void createUI(LGraphics g, int x, int y, LComponent component,
			LImage[] ui) {
		if (!component.isEnabled()) {
			g.drawImage(ui[1], x, y);
		} else {
			g.drawImage(ui[0], x, y);
		}
	}

}
