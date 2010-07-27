package org.ssg.android.game.herogame.control;

import java.util.Iterator;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.AndroidGlobalSession;
import org.ssg.android.game.herogame.Hero;
import org.ssg.android.game.herogame.Item;
import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class InventoryDialog extends Dialog {

	public static InventoryDialog instance;

	private Hero hero;

	private CellButton[] inventoryButtons, equipButtons;
	public CellButton extraButton;
	private Button closeBtn;

	public static int INV_BUTTON_NUM = 15;

	private LImage bodyImage;

	private CellButton draggedButton;
	
//	public Item[][] items = new Item[5][3];

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
		
		refreshInvetory();
	}
	
	public void refreshInvetory() {
		List<Item> itemList = MainScreen.instance.hero.items;
		int k = 0;
		for (Iterator<Item> it = itemList.iterator(); it.hasNext();) {
//			items[i][j] = it.next();
			inventoryButtons[k].item = it.next();
			inventoryButtons[k].resetItemPos();
			k++;
//			i = k / 3;
//			j = k % 3;
		}
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawAbsoluteImageEx(g, bodyImage, 25, 37, 63, 216);
		drawButtonEx(g, closeBtn, scaledWidth - 70, 10);

		for (int i = 0; i < inventoryButtons.length; i++) {
			drawButton(g, inventoryButtons[i]);
		}
		
		drawButton(g, extraButton);

		if (draggedButton != null) {
			draggedButton.drawItem(g);
		}
	}

	private void initButtons() {
		inventoryButtons = new CellButton[INV_BUTTON_NUM];
		LImage unchecked = GraphicsUtils.loadImage("assets/images/cell.png");
		CellButton.initialize(MainScreen.instance, inventoryButtons, 0,
				unchecked, unchecked);

		for (int i = 0; i < inventoryButtons.length; i++) {
			inventoryButtons[i].setName("");
			inventoryButtons[i].setComplete(false);
			int x = 130 + (i % 3) * 40;
			int y = 30 + 40 * (i / 3);
			inventoryButtons[i].setDrawXY(x, y);
			inventoryButtons[i].setOnTouchListener(new DefaultOnTouchListener(
					inventoryButtons[i]) {
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
					// if touchPoint overflows dialog border, reset drag
					// position.
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
			addOnTouchListener(inventoryButtons[i].getOnTouchListener());
		}

		// extraButton
		unchecked = GraphicsUtils.loadImage("assets/images/cell.png");
		extraButton = new CellButton(MainScreen.instance, INV_BUTTON_NUM, 0,
				false, unchecked, unchecked);
		extraButton.setName("");
		extraButton.setComplete(false);
		extraButton.setDrawXY(130, 240);
		extraButton.setOnTouchListener(new DefaultOnTouchListener(extraButton) {
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
				// if touchPoint overflows dialog border, reset drag position.
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
		addOnTouchListener(extraButton.getOnTouchListener());

		// closeButton
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
