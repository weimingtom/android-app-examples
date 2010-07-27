package org.ssg.android.game.herogame;

public class ItemAttr {
	public int category;
	public int att, def, hp;
	
	public void copyItemAttr(ItemAttr attr) {
		category = attr.category;
		att = attr.att;
		def = attr.def;
		hp = attr.hp;
	}
}

