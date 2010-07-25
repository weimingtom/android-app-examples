package org.ssg.android.game.herogame.control;

import java.util.ArrayList;
import java.util.Iterator;

import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.ssg.android.game.herogame.AndroidGlobalSession;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class TextDialog extends Dialog {

	public ArrayList<String> messages;
	private String message;
	public LImage image1, image2, dialogImage;
	public int PADDING_X = 15, PADDING_Y = 15;
	private Iterator<String> iter;

	public TextDialog(int scaledWidth, int scaledHeight, int x, int y,
			ArrayList<String> messages, LImage im1, LImage im2) {
		super("", scaledWidth, scaledHeight, x, y);
		
		img = (LImage) AndroidGlobalSession.get("dialog_438_120");
		img1 = (LImage) AndroidGlobalSession.get("dialog_310_120");
		
		this.image1 = im1;
		this.image2 = im2;
		this.messages = messages;
		resetDialog();
		addOnTouchListener(new DefaultOnTouchListener() {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				if (iter.hasNext()) {
					message = iter.next();
					if (dialogImage.equals(image1)) {
						dialogImage = image2;
					} else {
						dialogImage = image1;
					}
				} else {
					MainScreen.instance.cleanDialog = true;
				}
				return false;
			}
		});
	}

	public void resetDialog() {
		scaledWidth = MainScreen.instance.WIDTH - 10;
		iter = messages.iterator();
		message = iter.next();
		dialogImage = image1;
	}
	
	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		int xx = x + PADDING_X;
		int yy = y + PADDING_Y;
		//FIXME: should all adjust icon to 32*32;
		if (dialogImage.equals(image1)) {
			g.drawImage(dialogImage, xx, yy, xx + 32, yy + 32, 0, 0, 32, 32);
		} else {
			g.drawImage(dialogImage, xx, yy, xx + 32, yy + 32, 0, 0, 20, 32);
		}
		LFont l = g.getFont();
		g.setFont(16);
		g.drawString(message, xx + 50, yy + g.getFont().getSize());
		g.setFont(l);
	}
}
