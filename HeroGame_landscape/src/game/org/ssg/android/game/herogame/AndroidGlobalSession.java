package org.ssg.android.game.herogame;

import java.util.HashMap;
import java.util.Random;

import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.util.NinePatchImage;

public class AndroidGlobalSession {

	public static HashMap<String, Object> resMap = new HashMap<String, Object>();

	public static void init() {
		ItemDefinition.init();
		
		resMap.put("hero_fight", new Hero("assets/images/anim_herofight.png",
				0, 0, 30, 32, null));

		resMap.put("skeleon", new Enemy("assets/images/anim_skeleonfight.png",
				0, 0, 32, 32, null, "skeleon"));

		Enemy temp = new Enemy("assets/images/anim_magefight.png", 0, 0, 41,
				32, null, "mage");
		temp.ANIM_OFFSET_X = MainScreen.CS;
		resMap.put("mage", temp);
		
		NinePatchImage ninePatchImg = new NinePatchImage("assets/images/window.9.png");
		resMap.put("dialog_472_312", ninePatchImg.createImage(472, 312));
//		resMap.put("dialog_310_310", ninePatchImg.createImage(310, 310));
		resMap.put("dialog_472_120", ninePatchImg.createImage(472, 120));
//		resMap.put("dialog_310_120", ninePatchImg.createImage(310, 120));
		
		GraphicsUtils.loadImage("assets/images/items.png");
	}
	
	public static void put(String key, Object value) {
		resMap.put(key, value);
	}
	
	public static Object get(String key) {
		return resMap.get(key);
	}

}
