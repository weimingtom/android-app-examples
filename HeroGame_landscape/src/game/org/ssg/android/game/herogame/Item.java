package org.ssg.android.game.herogame;

import java.util.ArrayList;
import java.util.Random;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.control.TextDialog;

public class Item extends NPC {

	public Item(String filename, int x, int y, int w, int h, Level level,
			String racial) {
		super(filename, x, y, w, h, level, racial);
	}

	public static Item getRandomItem() {
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

		Item item = new Item("assets/images/items.png", 0, 0, 24, 24, null,
				"item");
		item.count = count;
		item.dir = dir;

		return item;
	}

	private int i = 0;

	public void drawAnimation(LGraphics g, int offsetX, int offsetY) {
		if (i == 30) {
			return;
		}
		if (i == 25) {
			super.draw(g, offsetX, offsetY - i);
		} else {
			super.draw(g, offsetX, offsetY - i);
			i += 2;
		}
	}
}
