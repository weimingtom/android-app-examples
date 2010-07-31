package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LFont;
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
	public CellButton extraButton, dropButton;
	private Button closeBtn;

	public static int INV_BUTTON_NUM = 15;
	public static int EQP_BUTTON_NUM = 10;

	private LImage bodyImage;

	private CellButton draggedButton, selectedButton;

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
		Item[] items = MainScreen.instance.hero.items;
		for (int i = 0; i < INV_BUTTON_NUM - 1; i++) {
			inventoryButtons[i].item = items[i];
			inventoryButtons[i].resetItemPos();
		}
	}

	public void refreshCell(int pos) {
		Item[] items = MainScreen.instance.hero.items;
		inventoryButtons[pos].item = items[pos];
		inventoryButtons[pos].resetItemPos();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawAbsoluteImageEx(g, bodyImage, 35, 42, 63, 216);
		drawButtonEx(g, closeBtn, scaledWidth - 70, 10);

		for (int i = 0; i < inventoryButtons.length; i++) {
			drawButton(g, inventoryButtons[i]);
		}

		if (extraButton.isVisible) {
			drawButton(g, extraButton);
		}

		if (selectedButton != null) {
			selectedButton.drawHighlight(g);
			if (selectedButton.item != null) {
				g.setAntiAlias(true);
				LFont l = g.getFont();
				g.setFont(12);
				int x = 280;
				int y = 70;
				drawString(g, "att:" + selectedButton.item.attr.att, x + 5,
						y + 5);
				drawString(g, "def:" + selectedButton.item.attr.def, x + 5,
						y + 30);
				drawString(g, "hp:" + selectedButton.item.attr.hp, x + 5,
						y + 55);
				g.setFont(l);
				g.setAntiAlias(false);
			}
		}

		drawButton(g, dropButton);

		if (draggedButton != null) {
			draggedButton.drawItem(g);
		}
	}

	private void initButtons() {
		initInventoryButtons();

		// extraButton
		LImage unchecked = GraphicsUtils.loadImage("assets/images/cell.png");
		extraButton = new CellButton(MainScreen.instance, INV_BUTTON_NUM, 0,
				false, unchecked, unchecked);
		extraButton.setName("");
		extraButton.setComplete(false);
		extraButton.setDrawXY(130, 240);
		extraButton.setOnTouchListener(new CellButtonOnTouchListener(
				extraButton));
		extraButton.isVisible = false;
		addOnTouchListener(extraButton.getOnTouchListener());

		unchecked = GraphicsUtils.loadImage("assets/images/dropcell.png");
		dropButton = new CellButton(MainScreen.instance, INV_BUTTON_NUM + 1, 0,
				false, unchecked, unchecked);
		dropButton.setName("");
		dropButton.setComplete(false);
		dropButton.setDrawXY(210, 240);
		dropButton.setOnTouchListener(new DefaultOnTouchListener(dropButton) {
			@Override
			public boolean onTouchDown(MotionEvent arg0) {
				return false;
			}

			@Override
			public boolean onTouchMove(MotionEvent arg0) {
				return true;
			}

			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				CellButton button = (CellButton) getRef();
				if (button.checkComplete() && draggedButton != null) {
					hero.items[draggedButton.getId()] = null;
					draggedButton.setItem(null);
					draggedButton.isDragged = false;
					draggedButton = null;
					selectedButton = null;
				}
				return false;
			}
		});
		addOnTouchListener(dropButton.getOnTouchListener());

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
						close();
					}
					button.setComplete(false);
				}
				return true;
			}
		});
		addOnTouchListener(closeBtn.getOnTouchListener());
	}

	public void initInventoryButtons() {
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
			inventoryButtons[i]
					.setOnTouchListener(new CellButtonOnTouchListener(
							inventoryButtons[i]));
			addOnTouchListener(inventoryButtons[i].getOnTouchListener());
		}
	}

	public void initEquipButtons() {
		equipButtons = new CellButton[EQP_BUTTON_NUM];
		LImage unchecked = GraphicsUtils.loadImage("assets/images/cell.png");
		CellButton.initialize(MainScreen.instance, equipButtons, 0, unchecked,
				unchecked);

		int[][] pos = new int[][] { {} };
		for (int i = 0; i < equipButtons.length; i++) {
			equipButtons[i].setName("");
			equipButtons[i].setComplete(false);
			int x = 130 + (i % 3) * 40;
			int y = 30 + 40 * (i / 3);
			equipButtons[i].setDrawXY(x, y);
			equipButtons[i].setOnTouchListener(new CellButtonOnTouchListener(
					equipButtons[i]));
			addOnTouchListener(equipButtons[i].getOnTouchListener());
		}
	}

	public boolean inOtherCells(CellButton cell) {
		for (int i = 0; i < inventoryButtons.length; i++) {
			// if (inventoryButtons[i] != cell) {
			if (inventoryButtons[i].checkComplete())
				return true;
			// }
		}
		if (dropButton.checkComplete()
				|| (extraButton.checkComplete() && extraButton.isVisible))
			return true;
		return false;
	}

	public void close() {
		MainScreen.checkLock();
		MainScreen.instance.topDialog = MainScreen.instance.defaultTopDialog;
		extraButton.item = null;
		extraButton.isVisible = false;
		draggedButton = null;
		selectedButton = null;
	}

	private class CellButtonOnTouchListener extends DefaultOnTouchListener {
		public CellButtonOnTouchListener(Button button) {
			super(button);
		}

		@Override
		public boolean onTouchDown(MotionEvent arg0) {
			CellButton button = (CellButton) getRef();
			if (!button.isVisible)
				return false;
			if (button.checkComplete()) {
				if (button.checkClick() != -1) {
					// if (selectedButton == button) {
					selectedButton = button;
					button.isDragged = true;
					button.itemX = (int) MainScreen.instance.touchX - 12;
					button.itemY = (int) MainScreen.instance.touchY - 12;
					draggedButton = button;
					// } else {
					// selectedButton = button;
					// }
				}
			}
			return false;
		}

		@Override
		public boolean onTouchMove(MotionEvent arg0) {
			CellButton button = (CellButton) getRef();
			if (!button.isVisible)
				return false;
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
			if (!button.isVisible)
				return false;
			if (button.isDragged && !inOtherCells(button)) {
				button.resetItemPos();
				button.isDragged = false;
				draggedButton = null;
			} else {
				if (button.checkComplete() && draggedButton != null) {
					Item temp;
					temp = button.item;
					button.setItem(draggedButton.item);
					draggedButton.setItem(temp);

					if (draggedButton != extraButton)
						hero.items[draggedButton.getId()] = draggedButton.item;
					if (button != extraButton)
						hero.items[button.getId()] = button.item;

					draggedButton.isDragged = false;
					draggedButton = null;
					selectedButton = button;
				}
			}
			return false;
		}
	}
}
