package org.ssg.android.game.herogame;

import java.util.ArrayList;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.control.TextDialog;

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
public class NPC extends Sprite {

	public String racial;

	private String fileName;

	public TextDialog talkDialog;
	
	public String getFileName() {
		return fileName;
	}

	public NPC(String filename, int x, int y, int w, int h,
			Level level, String racial) {
		super(filename, x, y, w, h, level);
		fileName = filename;
		this.racial = racial;
		ArrayList<String> topics = new ArrayList<String>();
		topics.add("你好，欢迎来到商店！");
		topics.add("恩恩，快把东西给我瞧瞧！");
		topics.add("呵呵，好的。");
		topics.add("谢啦～");
		LImage image2 = GraphicsUtils.loadImage("assets/images/hero.png");
		LImage image1 = getImg();
		talkDialog = new TextDialog(MainScreen.instance.WIDTH - 10, 120, 8, 180,
				topics, image1, image2);
	}
	
	public void runDialog() {
		
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
