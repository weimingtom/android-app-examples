package org.ssg.android.game.herogame;

import java.util.HashMap;

public class AndroidGlobalSession {

	public static HashMap<String, Sprite> resMap = new HashMap<String, Sprite>();

	public static void init() {
		resMap.put("hero_fight", new Hero("assets/images/anim_herofight.png",
				0, 0, 30, 32, null));

		resMap.put("skeleon", new Enemy("assets/images/anim_skeleonfight.png",
				0, 0, 32, 32, null, "skeleon"));

		Enemy temp = new Enemy("assets/images/anim_magefight.png", 0, 0, 41,
				32, null, "mage");
		temp.ANIM_OFFSET_X = MainScreen.CS;
		resMap.put("mage", temp);
	}

	public static Sprite get(String key) {
		return resMap.get(key);
	}

}
