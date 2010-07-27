package org.ssg.android.game.herogame;

import java.util.ArrayList;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.control.TextDialog;

public class NPC extends Sprite {

	public String racial;

	private String fileName;

	public TextDialog talkDialog;
	
	public ArrayList<String> topics;
	
	public boolean isTriggered = false, isOpened = false;
	
	public Item item;
	
	public String getFileName() {
		return fileName;
	}

	public NPC(String filename, int x, int y, int w, int h,
			Level level, String racial) {
		super(filename, x, y, w, h, level);
		fileName = filename;
		this.racial = racial;
		LImage image2 = GraphicsUtils.loadImage("assets/images/hero.png");
		LImage image1 = getImg();
		topics = new ArrayList<String>();
		talkDialog = new TextDialog(MainScreen.instance.WIDTH - 10, 120, 8, 180,
				topics, image1, image2);
	}
	
	public void addAction(String str) {
		topics.add(str);
	}

	public boolean hasAction() {
		return topics.size() > 0;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
    public void updateStatus(long elapsedTime) {
		if (!isTriggered)
			return;
			
        if (timer.action(elapsedTime)) {
            if (count < 4) {
                count++;
            } else {
            	isOpened = true;
            }
        }
        update();
    }
}
