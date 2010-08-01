package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class ToolBar extends Dialog {

	private Button expandButton, charButton, inventoryButton;

	public ToolBar(int x, int y, int scaledWidth, int scaledHeight) {
		super("assets/images/leftpanelbg1.png", scaledWidth, scaledHeight, x, y);
		initButtons();
		isAlwaysShown = true;
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawButton(g, expandButton);
		drawButton(g, charButton);
		drawButton(g, inventoryButton);
	}

	private void initButtons() {
		LImage checked;
		LImage unchecked = GraphicsUtils.loadImage("assets/images/expand.png");
		expandButton = new Button(MainScreen.instance, 1, 0, false, unchecked,
				unchecked);
		expandButton.setName("");
		expandButton.setComplete(false);
		expandButton.setDrawXY(0, 0);
		expandButton
				.setOnTouchListener(new DefaultOnTouchListener(expandButton) {
					@Override
					public boolean onTouchDown(MotionEvent arg0) {
						Button button = (Button) getRef();
						if (button.checkComplete()) {
							if (MainScreen.instance.isShownLeftPanel == false) {
								MainScreen.instance.changeNext = true;
								button.setComplete(true);
								button.setSelect(true);
							}
						}
						return false;
					}
				});
		addOnTouchListener(expandButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/char2.png");
		unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		charButton = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		charButton.setName("");
		charButton.setComplete(false);
		charButton.setDrawXY(0, 35);
		charButton.setOnTouchListener(new OpenDialogOnTouchListener(
				charButton, HeroStatusDialog.instance));
		addOnTouchListener(charButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/char2.png");
		unchecked = GraphicsUtils.loadImage("assets/images/char1.png");
		inventoryButton = new Button(MainScreen.instance, 2, 0, false, checked,
				unchecked);
		inventoryButton.setName("");
		inventoryButton.setComplete(false);
		inventoryButton.setDrawXY(0, 100);
		inventoryButton.setOnTouchListener(new OpenDialogOnTouchListener(
				inventoryButton, InventoryDialog.instance));
		addOnTouchListener(inventoryButton.getOnTouchListener());
	}
}
