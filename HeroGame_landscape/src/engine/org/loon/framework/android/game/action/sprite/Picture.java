package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.LObject;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;

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
public class Picture extends LObject implements ISprite {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1982153514439690901L;

	private boolean visible;

	private int width, height;

	private LImage image;

	public Picture(String fileName) {
		this(fileName, 0, 0);
	}

	public Picture(String fileName, int x, int y) {
		this(GraphicsUtils.loadImage(fileName,true), x, y);
	}

	public Picture(LImage image, int x, int y) {
		this.setImage(image);
		this.setLocation(x, y);
		this.visible = true;
	}

	public void createUI(LGraphics g) {
		if (visible) {
			g.drawImage(image, x(), y());
		}
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public void update(long timer) {
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public LImage getImage() {
		return image;
	}

	public void setImage(String fileName,boolean transparency) {
		this.image = GraphicsUtils.loadImage(fileName,transparency);
	}

	public void setImage(LImage image) {
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}

	public RectBox getCollisionBox() {
		return new RectBox(x(), y(), width, height);
	}

	public float getAlpha() {
		return 0;
	}

}
