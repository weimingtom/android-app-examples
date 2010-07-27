package org.ssg.android.game.herogame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.loon.framework.android.game.core.resource.Resources;

import android.util.Log;

public class ItemDefinition {

	public final static int CATEGORY_BOTTLE = 0;
	public final static int CATEGORY_WEAPON = 1;
	public final static int CATEGORY_HAT = 2;
	public final static int CATEGORY_ARMOR = 3;
	public final static int CATEGORY_LEG = 4;
	public final static int CATEGORY_HAND = 5;
	public final static int CATEGORY_SHEILD = 6;
	public final static int CATEGORY_FOOT = 7;
	public final static int CATEGORY_RING = 8;
	public final static int CATEGORY_NECKLACE = 9;
	public final static int CATEGORY_JWEL = 10;
	public final static int CATEGORY_WING = 11;
	
	public static ItemAttr[][] itemSet = new ItemAttr[10][];
	public static ItemAttr[] bottleSet = new ItemAttr[6];
	public static ItemAttr[] weaponSet = new ItemAttr[23];
	public static ItemAttr[] hatSet = new ItemAttr[15];
	public static ItemAttr[] armorSet = new ItemAttr[16];
	public static ItemAttr[] legSet = new ItemAttr[13];
	public static ItemAttr[] handSet = new ItemAttr[15];
	public static ItemAttr[] sheildSet = new ItemAttr[13];
	public static ItemAttr[] footSet = new ItemAttr[13];
	public static ItemAttr[] ringSet = new ItemAttr[20];
	public static ItemAttr[] necklaceSet = new ItemAttr[20];
	
	public static void init() {
		itemSet[0] = bottleSet;
		itemSet[1] = weaponSet;
		itemSet[2] = hatSet;
		itemSet[3] = armorSet;
		itemSet[4] = legSet;
		itemSet[5] = handSet;
		itemSet[6] = sheildSet;
		itemSet[7] = footSet;
		itemSet[8] = ringSet;
		itemSet[9] = necklaceSet;
		loadItemsFromFile();
	}
	
	private static void loadItemsFromFile(){
        InputStream in = Resources.getResourceAsStream("assets/ItemDefinition");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = null;
        int rowNo = 0, colNo = 0;
        String[] col, attrs, attr;
        String name;
        int value;
        ItemAttr tempAttr;
        try {
            while ((result = reader.readLine()) != null) {
                if (result.startsWith("-")) {
                	rowNo = Integer.parseInt(result.substring(1, result.length()));
                } else {
                	col = result.split(",");
                	colNo = Integer.parseInt(col[0]);
                	
                	tempAttr = new ItemAttr();
                	tempAttr.category = rowNo;
                	itemSet[rowNo][colNo] = tempAttr;
					
                	attrs = col[1].split("/");
                	for (int i = 0; i < attrs.length; i++) {
                		attr = attrs[i].split(":");
                		name = attr[0];
                		value = Integer.parseInt(attr[1]);
                		if (name.equals("att")) {
                			tempAttr.att = value;
                		} else {
                			if (name.equals("hp")) {
                				tempAttr.hp = value;
                			} else {
                				if (name.equals("def")) {
                					tempAttr.def = value;
                				}
                			}
                		}
                	}
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                }
            }
        }
    }
}
