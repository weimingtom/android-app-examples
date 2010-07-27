package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.AndroidGlobalSession;
import org.ssg.android.game.herogame.Hero;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class InventoryDialog extends Dialog {

	public static InventoryDialog instance;

	private Hero hero;

	private CellButton[] buttons;
	private Button closeBtn;

	private static int BUTTON_NUM = 9;
	
	private LImage bodyImage;
	
	private CellButton draggedButton;

	public InventoryDialog(String fileName, int scaledWidth, int scaledHeight) {
		super(fileName, scaledWidth, scaledHeight);
	}

	public InventoryDialog(Hero hero) {
		this("", 440, 312);
		instance = this;
		this.hero = hero;
		PADDING_X = 20;
		PADDING_Y = 20;
		rowNum = 8;
		colNum = 1;

		img = (LImage) AndroidGlobalSession.get("dialog_438_310");
		img1 = (LImage) AndroidGlobalSession.get("dialog_310_310");
		
		bodyImage = GraphicsUtils.loadImage("assets/images/body.png");
		
		initButtons();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawAbsoluteImageEx(g, bodyImage, 25, 37, 63, 216);
		drawButtonEx(g, closeBtn, scaledWidth - 70, 10);
		
		for (int i = 0; i < buttons.length; i++) {
			drawButton(g, buttons[i]);
		}
		
		if (draggedButton != null) {
			draggedButton.drawItem(g);
		}
	}

	private void initButtons() {
		buttons = new CellButton[BUTTON_NUM];
		LImage unchecked = GraphicsUtils
				.loadImage("assets/images/cell.png");
		CellButton.initialize(MainScreen.instance, buttons, 0, unchecked, unchecked);

		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setName("");
			buttons[i].setComplete(false);
			int x = 130 + (i % 3) * 40;
			int y = 30 + 40 * (i / 3);
			buttons[i].setDrawXY(x, y);
			buttons[i].resetItemPos();
			buttons[i]
					.setOnTouchListener(new DefaultOnTouchListener(buttons[i]) {
						@Override
						public boolean onTouchDown(MotionEvent arg0) {
							CellButton button = (CellButton) getRef();
							if (button.checkComplete()) {
								if (button.checkClick() != -1) {
									button.isDragged = true;
									button.itemX = (int) MainScreen.instance.touchX - 12;
									button.itemY = (int) MainScreen.instance.touchY - 12;
									draggedButton = button;
								}
							}
							return false;
						}
						
						@Override
						public boolean onTouchMove(MotionEvent arg0) {
							CellButton button = (CellButton) getRef();
							//if touchPoint overflows dialog border, reset drag position.
							if (!InventoryDialog.instance.isTouched()) {
								button.resetItemPos();
								button.isDragged = false;
								draggedButton = null;
							}
							if (button.isDragged) {
								button.itemX = (int) MainScreen.instance.touchX - 12;
								button.itemY = (int) MainScreen.instance.touchY - 12;
							}
							return true;
						}
						
						@Override
						public boolean onTouchUp(MotionEvent arg0) {
							CellButton button = (CellButton) getRef();
							if (button.isDragged) {
								button.resetItemPos();
								button.isDragged = false;
								draggedButton = null;
							}
							return false;
						}
					});
			addOnTouchListener(buttons[i].getOnTouchListener());
		}
		
		unchecked = GraphicsUtils.loadImage("assets/images/close.png");
		closeBtn = new Button(MainScreen.instance, 4, 0, false, unchecked,
				unchecked);
		closeBtn.setName("");
		closeBtn.setComplete(false);
		closeBtn.setOnTouchListener(new DefaultOnTouchListener(closeBtn) {
			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.isComplete()) {
					if (button.checkComplete()) {
						MainScreen.checkLock();
						MainScreen.instance.topDialog = MainScreen.instance.defaultTopDialog;
					}
					button.setComplete(false);
				}
				return true;
			}
		});
		addOnTouchListener(closeBtn.getOnTouchListener());
	}
}
