package org.ssg.android.game.herogame.control;

import java.util.HashMap;

import org.loon.framework.android.game.core.graphics.LFont;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.AndroidGlobalSession;
import org.ssg.android.game.herogame.Hero;
import org.ssg.android.game.herogame.Item;
import org.ssg.android.game.herogame.ItemDefinition;
import org.ssg.android.game.herogame.MainScreen;

import android.util.Log;
import android.view.MotionEvent;

public class InventoryDialog extends Dialog {

	public static InventoryDialog instance;

	private Hero hero;

	private CellButton[] inventoryButtons, equipButtons;
	public Button dropButton, loadButton, unloadButton, useButton;
	private Button closeBtn;

	public static int INV_BUTTON_NUM = 20;
	public static int EQP_BUTTON_NUM = 9;

	private LImage bodyImage;

	public CellButton extraButton, selectedButton, targetButton;

	private HashMap<Integer, CellButton> equipButtonMap = new HashMap<Integer, CellButton>();

	public boolean isItemOverFlows = false;

	public InventoryDialog(String fileName, int scaledWidth, int scaledHeight) {
		super(fileName, scaledWidth, scaledHeight);
	}

	public InventoryDialog(Hero hero) {
		this("", 472, 312);
		instance = this;
		this.hero = hero;
		PADDING_X = 20;
		PADDING_Y = 20;
		rowNum = 8;
		colNum = 1;

		img = (LImage) AndroidGlobalSession.get("dialog_472_312");

		bodyImage = GraphicsUtils.loadImage("assets/images/body.png");

		initButtons();

		for (int i = 0; i < InventoryDialog.INV_BUTTON_NUM - 1; i++)
			inventoryButtons[i].item = Item.getRandomItem();

		refreshInvetory();
	}

	public void refreshInvetory() {
		for (int i = 0; i < INV_BUTTON_NUM; i++) {
			inventoryButtons[i].resetItemPos();
		}
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isShown())
			return;

		drawAbsoluteImageEx(g, bodyImage, 71, 45, 63, 216);
		drawButtonEx(g, closeBtn, scaledWidth - 70, 10);

		for (int i = 0; i < inventoryButtons.length; i++) {
			drawButton(g, inventoryButtons[i]);
		}

		for (int i = 0; i < equipButtons.length; i++) {
			drawButton(g, equipButtons[i]);
		}

		if (extraButton.isVisible) {
			drawButton(g, extraButton);
		}

		if (selectedButton != null) {
			selectedButton.drawHighlight(g);
			if (selectedButton.item != null) {
				g.setAntiAlias(true);
				LFont l = g.getFont();
				g.setFont(14);
				int x = 180;
				int y = 30;
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

		drawButton(g, loadButton);
		drawButton(g, unloadButton);
		drawButton(g, useButton);

		if (targetButton != null) {
			targetButton.drawHighlightTarget(g);
		}

		drawButton(g, dropButton);
	}

	private void reArrangeInventory() {
		int i, j;
		for (j = 0; j < inventoryButtons.length; j++) {
			if (inventoryButtons[j].item == null)
				break;
		}
		for (i = j; i < inventoryButtons.length; i++) {
			if (inventoryButtons[i + 1].item == null) {
				inventoryButtons[i].item = null;
				break;
			}
			inventoryButtons[i].item = inventoryButtons[i + 1].item;
			inventoryButtons[i].resetItemPos();
		}
		if (extraButton.isVisible && extraButton.item != null) {
			int pos = inventoryButtons.length - 1;
			inventoryButtons[pos].item = extraButton.item;
			extraButton.item = null;
			inventoryButtons[pos].resetItemPos();
		}
	}

	private void setSelectedButton(CellButton button) {
		if (button.item == null)
			return;

		selectedButton = button;

		if (button.identity.equals("inventory")
				|| button.identity.equals("extra")) {
			int category = button.item.attr.category;
			if (category == ItemDefinition.CATEGORY_BOTTLE
					|| category == ItemDefinition.CATEGORY_JWEL
					|| category == ItemDefinition.CATEGORY_WING) {
				InventoryDialog.instance.setFeatureButtonVisible(false, false,
						true);
				targetButton = null;
			} else {
				targetButton = equipButtonMap.get(category);
				InventoryDialog.instance.setFeatureButtonVisible(true, false,
						false);
			}
		} else {
			InventoryDialog.instance
					.setFeatureButtonVisible(false, true, false);
		}
	}

	private void initButtons() {
		initInventoryButtons();
		initEquipButtons();

		// extraButton
		LImage checked;
		LImage unchecked = GraphicsUtils.loadImage("assets/images/cell.png");
		extraButton = new CellButton(MainScreen.instance, INV_BUTTON_NUM, 0,
				false, unchecked, unchecked);
		extraButton.setName("");
		extraButton.setComplete(false);
		extraButton.identity = "extra";
		extraButton.setDrawXY(256, 250);
		extraButton.setOnTouchListener(new CellButtonOnTouchListener(
				extraButton));
		extraButton.isVisible = false;
		addOnTouchListener(extraButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/dropcell_press.png");
		unchecked = GraphicsUtils.loadImage("assets/images/dropcell.png");
		dropButton = new Button(MainScreen.instance, INV_BUTTON_NUM + 1, 0,
				false, checked, unchecked);
		dropButton.setName("");
		dropButton.setComplete(false);
		dropButton.setDrawXY(376, 250);
		dropButton.setOnTouchListener(new DefaultOnTouchListener(dropButton) {
			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (button.isComplete() && button.checkComplete()) {
					if (selectedButton != null) {
						if (selectedButton.equals(extraButton)) {
							extraButton.item = null;
						} else {
							selectedButton.item = null;
							selectedButton = null;
							reArrangeInventory();
						}
					}
					button.setComplete(false);
					return true;
				}
				return false;
			}
		});
		addOnTouchListener(dropButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/equip_press.png");
		unchecked = GraphicsUtils.loadImage("assets/images/equip.png");
		loadButton = new Button(MainScreen.instance, 0, 0, false, checked,
				unchecked);
		loadButton.setName("");
		loadButton.setComplete(false);
		loadButton.setDrawXY(216, 250);
		loadButton.isVisible = false;
		loadButton.setOnTouchListener(new DefaultOnTouchListener(loadButton) {
			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (!button.isVisible)
					return false;

				if (button.isComplete() && button.checkComplete()) {
					if (selectedButton != null) {
						if (selectedButton.equals(extraButton)) {
							if (targetButton != null) {
								Item tempItem;
								tempItem = extraButton.item;
								extraButton.setItem(targetButton.item);
								targetButton.setItem(tempItem);
							}
						} else {
							Item tempItem;
							tempItem = targetButton.item;
							targetButton.setItem(selectedButton.item);
							Log.e("aaa", selectedButton.item + "");
							if (tempItem == null) {
								selectedButton.item = null;
								reArrangeInventory();
								setSelectedButton(targetButton);
								targetButton = null;
								Log.e("aaa", selectedButton.item + "");
							} else {
								selectedButton.setItem(tempItem);
							}
						}
					}
					button.setComplete(false);
					return true;
				}
				return false;
			}
		});
		addOnTouchListener(loadButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/unload_press.png");
		unchecked = GraphicsUtils.loadImage("assets/images/unload.png");
		unloadButton = new Button(MainScreen.instance, 100, 0, false, checked,
				unchecked);
		unloadButton.setName("");
		unloadButton.setComplete(false);
		unloadButton.setDrawXY(216, 250);
		unloadButton.isVisible = false;
		unloadButton
				.setOnTouchListener(new DefaultOnTouchListener(unloadButton) {
					@Override
					public boolean onTouchUp(MotionEvent arg0) {
						Button button = (Button) getRef();
						if (!button.isVisible)
							return false;

						if (button.isComplete() && button.checkComplete()) {
							if (selectedButton != null) {
								CellButton free = null;
								int len = inventoryButtons.length;
								for (int i = 0; i < len; i++) {
									if (inventoryButtons[i].item == null) {
										free = inventoryButtons[i];
										break;
									}
								}
								if (free == null) {
									// alertMessage
								} else {
									free.item = selectedButton.item;
									free.resetItemPos();
									selectedButton.item = null;
									setSelectedButton(free);
								}
							}
							button.setComplete(false);
							return true;
						}
						return false;
					}
				});
		addOnTouchListener(unloadButton.getOnTouchListener());

		checked = GraphicsUtils.loadImage("assets/images/use_press.png");
		unchecked = GraphicsUtils.loadImage("assets/images/use.png");
		useButton = new Button(MainScreen.instance, 101, 0, false, checked,
				unchecked);
		useButton.setName("");
		useButton.setComplete(false);
		useButton.setDrawXY(216, 250);
		useButton.isVisible = false;
		useButton.setOnTouchListener(new DefaultOnTouchListener(useButton) {
			@Override
			public boolean onTouchUp(MotionEvent arg0) {
				Button button = (Button) getRef();
				if (!button.isVisible)
					return false;

				if (button.isComplete() && button.checkComplete()) {
					if (selectedButton != null) {
						// add hero status;
						selectedButton.item = null;
						selectedButton = null;
					}
					button.setComplete(false);
					return true;
				}
				return false;
			}
		});
		addOnTouchListener(useButton.getOnTouchListener());

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
				if (button.isComplete() && button.checkComplete()) {
					close();
					button.setComplete(false);
					return true;
				}
				return false;
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
			inventoryButtons[i].identity = "inventory";
			int x = 256 + (i % 4) * 40;
			int y = 30 + 40 * (i / 4);
			inventoryButtons[i].setDrawXY(x, y);
			inventoryButtons[i]
					.setOnTouchListener(new CellButtonOnTouchListener(
							inventoryButtons[i]));
			addOnTouchListener(inventoryButtons[i].getOnTouchListener());
		}
	}

	public void initEquipButtons() {
		equipButtons = new CellButton[EQP_BUTTON_NUM];
		LImage unchecked = GraphicsUtils.loadImage(
				"assets/images/cell_equip.png", true);
		CellButton.initialize(MainScreen.instance, equipButtons, 0, unchecked,
				unchecked);

		Position[] pos = new Position[] { new Position(85, 21),
				new Position(33, 50), new Position(85, 92),
				new Position(33, 127), new Position(131, 127),
				new Position(85, 173), new Position(131, 173),
				new Position(33, 218), new Position(85, 246) };
		for (int i = 0; i < equipButtons.length; i++) {
			equipButtons[i].setName("");
			equipButtons[i].setComplete(false);
			equipButtons[i].identity = "equip";
			equipButtons[i].setDrawXY(pos[i].x, pos[i].y);
			equipButtons[i].setOnTouchListener(new CellButtonOnTouchListener(
					equipButtons[i]) {
				@Override
				public boolean onTouchDown(MotionEvent arg0) {
					CellButton button = (CellButton) getRef();

					if (button.checkComplete() && button.checkClick() != -1) {
						if (button.item == null)
							return false;

						if (selectedButton == button) {
							selectedButton = null;
							targetButton = null;
							setFeatureButtonVisible(false, false, false);
						} else {
							targetButton = null;
							setSelectedButton(button);
						}
						return true;
					}
					return false;
				}
			});
			addOnTouchListener(equipButtons[i].getOnTouchListener());
		}

		equipButtonMap.put(ItemDefinition.CATEGORY_WEAPON, equipButtons[3]);
		equipButtonMap.put(ItemDefinition.CATEGORY_HAT, equipButtons[0]);
		equipButtonMap.put(ItemDefinition.CATEGORY_ARMOR, equipButtons[2]);
		equipButtonMap.put(ItemDefinition.CATEGORY_LEG, equipButtons[5]);
		equipButtonMap.put(ItemDefinition.CATEGORY_HAND, equipButtons[4]);
		equipButtonMap.put(ItemDefinition.CATEGORY_SHEILD, equipButtons[6]);
		equipButtonMap.put(ItemDefinition.CATEGORY_FOOT, equipButtons[8]);
		equipButtonMap.put(ItemDefinition.CATEGORY_RING, equipButtons[7]);
		equipButtonMap.put(ItemDefinition.CATEGORY_NECKLACE, equipButtons[1]);
	}

	public boolean inOtherCells(CellButton cell) {
		for (int i = 0; i < inventoryButtons.length; i++) {
			if (inventoryButtons[i].checkComplete())
				return true;
		}
		for (int i = 0; i < equipButtons.length; i++) {
			if (equipButtons[i].checkComplete())
				return true;
		}
		if (dropButton.checkComplete()
				|| (extraButton.checkComplete() && extraButton.isVisible))
			return true;
		return false;
	}

	public void close() {
		MainScreen.checkLock();
		MainScreen.instance.setDefaultTopDialog();
		extraButton.item = null;
		extraButton.isVisible = false;
		selectedButton = null;
		isItemOverFlows = false;
		setFeatureButtonVisible(false, false, false);
	}

	private class CellButtonOnTouchListener extends DefaultOnTouchListener {
		public CellButtonOnTouchListener(Button button) {
			super(button);
		}

		// default for inventoryButtons and extraButton.
		@Override
		public boolean onTouchDown(MotionEvent arg0) {
			CellButton button = (CellButton) getRef();

			if (button.checkComplete() && button.checkClick() != -1) {
				if (button.item == null)
					return false;

				if (selectedButton == button) {
					selectedButton = null;
					targetButton = null;
					setFeatureButtonVisible(false, false, false);
				} else {
					setSelectedButton(button);
				}
				return true;
			}
			return false;
		}
	}

	public void setFeatureButtonVisible(boolean flag1, boolean flag2,
			boolean flag3) {
		loadButton.isVisible = flag1;
		unloadButton.isVisible = flag2;
		useButton.isVisible = flag3;
	}

	public void addItem(Item item) {
		for (int i = 0; i < INV_BUTTON_NUM; i++) {
			if (inventoryButtons[i].item == null) {
				inventoryButtons[i].item = item;
				inventoryButtons[i].resetItemPos();
				return;
			}
		}
		InventoryDialog.instance.extraButton.isVisible = true;
		InventoryDialog.instance.extraButton.setItem(item);
		isItemOverFlows = true;
	}
}
