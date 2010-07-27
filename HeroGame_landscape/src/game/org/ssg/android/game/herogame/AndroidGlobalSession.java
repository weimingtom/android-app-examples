package org.ssg.android.game.herogame;

import java.util.HashMap;
import java.util.Random;

import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.util.NinePatchImage;

public class AndroidGlobalSession {

	public static HashMap<String, Object> resMap = new HashMap<String, Object>();

	public static void init() {
		resMap.put("hero_fight", new Hero("assets/images/anim_herofight.png",
				0, 0, 30, 32, null));

		resMap.put("skeleon", new Enemy("assets/images/anim_skeleonfight.png",
				0, 0, 32, 32, null, "skeleon"));

		Enemy temp = new Enemy("assets/images/anim_magefight.png", 0, 0, 41,
				32, null, "mage");
		temp.ANIM_OFFSET_X = MainScreen.CS;
		resMap.put("mage", temp);
		
		NinePatchImage ninePatchImg = new NinePatchImage("assets/images/window.9.png");
		resMap.put("dialog_438_310", ninePatchImg.createImage(438, 310));
		resMap.put("dialog_310_310", ninePatchImg.createImage(310, 310));
		resMap.put("dialog_438_120", ninePatchImg.createImage(438, 120));
		resMap.put("dialog_310_120", ninePatchImg.createImage(310, 120));
		
		GraphicsUtils.loadImage("assets/images/items.png");
	}

	private static void loadItems() {
		Random random = new Random();
		int dir = Math.abs(random.nextInt() % 11);
		int count = 0;
		switch (dir) {
		case 0:
			count = Math.abs(random.nextInt() % 5);
			break;
		case 1:
			count = Math.abs(random.nextInt() % 22);
			break;
		case 2:
			count = Math.abs(random.nextInt() % 14);
			break;
		case 3:
			count = Math.abs(random.nextInt() % 15);
			break;
		case 4:
			count = Math.abs(random.nextInt() % 12);
			break;
		case 5:
			count = Math.abs(random.nextInt() % 14);
			break;
		case 6:
			count = Math.abs(random.nextInt() % 12);
			break;
		case 7:
			count = Math.abs(random.nextInt() % 12);
			break;
		case 8:
			count = Math.abs(random.nextInt() % 19);
			break;
		case 9:
			count = Math.abs(random.nextInt() % 19);
			break;
		case 10:
			count = Math.abs(random.nextInt() % 9);
			break;
		case 11:
			count = 0;
			break;
		}

		Item item = new Item("assets/images/items.png", 0, 0, 32, 32, null, "item");
		item.count = count;
		item.dir = dir;
	}
	
	public static void put(String key, Object value) {
		resMap.put(key, value);
	}
	
	public static Object get(String key) {
		return resMap.get(key);
	}

}
