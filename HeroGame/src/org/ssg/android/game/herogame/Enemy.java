package org.ssg.android.game.herogame;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.ssg.android.game.herogame.control.BackGroundMap;

/**
 * Copyright 2008 - 2009 Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Enemy extends Role {

	public int ANIM_OFFSET_X = MainScreen.CS * 3 / 4;
	public int ANIM_OFFSET_Y = 1;

	private String fileName;
	private int exp = 10;

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public String getFileName() {
		return fileName;
	}

	public Enemy(String filename, int x, int y, int w, int h, BackGroundMap map) {
		this(filename, x, y, w, h, map, 20, 5, 0);
	}

	public Enemy(String filename, int x, int y, int w, int h,
			BackGroundMap map, int hp, int attack, int defence) {
		super(filename, x, y, w, h, map, hp, attack, defence);
		fileName = filename;
		HPx = getXs() * MainScreen.CS + getWidth() / 2 - ANIM_OFFSET_X;
		HPy = getYs() * MainScreen.CS + MainScreen.CS / 2;
	}

	@Override
	public void update() {

	}

	public void drawAnimation(LGraphics g, int offsetX, int offsetY, int counts) {
		this.draw(g, offsetX - ANIM_OFFSET_X, offsetY - ANIM_OFFSET_Y);

		if (counts == this.getCount()) {
			this.setCount(0);
		} else {
			this.setCount(this.getCount() + 1);
		}
	}
}
