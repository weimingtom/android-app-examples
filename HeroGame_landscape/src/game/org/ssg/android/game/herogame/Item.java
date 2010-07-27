package org.ssg.android.game.herogame;

import java.util.Random;

import org.loon.framework.android.game.core.graphics.LGraphics;

public class Item extends NPC{

	public ItemAttr attr = new ItemAttr();
	
	public Item(String filename, int x, int y, int w, int h, Level level,
			String racial) {
		super(filename, x, y, w, h, level, racial);
	}

	public static Item getRandomItem() {
		Random random = new Random();
		int dir = Math.abs(random.nextInt() % (ItemDefinition.itemSet.length - 1));
		int count = 0;
		switch (dir) {
		case 0:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[0].length - 1));
			break;
		case 1:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[1].length - 1));
			break;
		case 2:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[2].length - 1));
			break;
		case 3:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[3].length - 1));
			break;
		case 4:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[4].length - 1));
			break;
		case 5:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[5].length - 1));
			break;
		case 6:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[6].length - 1));
			break;
		case 7:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[7].length - 1));
			break;
		case 8:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[8].length - 1));
			break;
		case 9:
			count = Math.abs(random.nextInt() % (ItemDefinition.itemSet[9].length - 1));
			break;
//		case 10:
//			count = Math.abs(random.nextInt() % 9);
//			break;
//		case 11:
//			count = 0;
//			break;
		}

		Item item = new Item("assets/images/items.png", 0, 0, 24, 24, null,
				"item");
		item.count = count;
		item.dir = dir;
		item.attr.copyItemAttr(ItemDefinition.itemSet[dir][count]);

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
