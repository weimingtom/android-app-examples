package org.ssg.android.game.herogame;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.timer.LTimer;
import org.ssg.android.game.herogame.control.BackGroundMap;

import android.util.Log;

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
	public static final int ANIM_HIT_FRAME = 9;
	public static final int ANIM_FINAL_FRAME = 18;
	public String racial;

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

	public Enemy(String filename, int x, int y, int w, int h, BackGroundMap map, String racial) {
		this(filename, x, y, w, h, map, 20, 5, 0, racial);
	}

	public Enemy(String filename, int x, int y, int w, int h,
			BackGroundMap map, int hp, int attack, int defence, String racial) {
		super(filename, x, y, w, h, map, hp, attack, defence);
		fileName = filename;
		resetHPxy();
		
	    hit = 80;
		strength = 10;
		element_set = 100;
		dex = 10;
		agi = 10;
		this.racial = racial;
	}

	@Override
	public void resetHPxy() {
		HPx = getXs() * MainScreen.CS + getWidth() / 2 - ANIM_OFFSET_X;
		HPy = getYs() * MainScreen.CS + MainScreen.CS / 2;
		frameNo = 0;
		damage = -1;
		count = 0;
	}
	
	@Override
	public void update() {

	}

	public void drawFightingAnim(LGraphics g, int offsetX, int offsetY) {
		this.draw(g, offsetX - ANIM_OFFSET_X, offsetY - ANIM_OFFSET_Y);
	}
	
	public boolean updateFightingAnim(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			if (ANIM_FINAL_FRAME == this.getCount()) {
				this.setCount(0);
			} else {
				this.setCount(this.getCount() + 1);
			}
			return true;
		}
		return false;
	}
}
