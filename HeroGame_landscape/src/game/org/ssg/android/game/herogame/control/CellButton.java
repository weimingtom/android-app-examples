package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.Item;

public class CellButton extends Button {

	public Item item;
	public int itemX, itemY;
	public boolean isDragged = false;

	public CellButton(Screen screen, int no, int space, boolean isRow,
			LImage selectImage, LImage buttonImage) {
		super(screen, no, space, isRow, selectImage, buttonImage);
		// item = Item.getRandomItem();
		// resetItemPos();
	}

	public void resetItemPos() {
		if (item != null) {
			itemX = this.getX() + (this.getWidth() - item.getWidth()) / 2;
			itemY = this.getY() + (this.getHeight() - item.getHeight()) / 2;
		}
	}

	public void setItem(Item item) {
		this.item = item;
		resetItemPos();
	}

	@Override
	public void draw(LGraphics g) {
		super.draw(g);
		if (!isDragged)
			drawItem(g);
	}

	public void drawItem(LGraphics g) {
		if (item != null)
			g.drawImage(item.getImg(), itemX, itemY, itemX + 24, itemY + 24,
					item.count * 24, item.dir * 24, item.count * 24 + 24,
					item.dir * 24 + 24);
	}

	public void drawHighlight(LGraphics g) {
		g.setColor(LColor.yellow);
		g.setAntiAlias(true);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(LColor.white);
		g.setAntiAlias(false);
	}
	
	public void drawHighlightTarget(LGraphics g) {
		g.setColor(LColor.white);
		g.setAntiAlias(true);
		g.drawRect(getX(), getY(), getWidth(), getHeight());
		g.setColor(LColor.white);
		g.setAntiAlias(false);
	}
	
	public static void initialize(final Screen screen,
			final CellButton[] buttons, final int space, final LImage checked,
			final LImage unchecked) {
		initialize(screen, buttons, space, false, checked,
				unchecked == null ? GraphicsUtils.getGray(checked) : unchecked);
	}

	public static void initialize(final Screen screen,
			final CellButton[] buttons, final int space, final boolean isRow,
			final LImage checked, final LImage unchecked) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new CellButton(screen, i, space, isRow, checked,
					unchecked == null ? GraphicsUtils.getGray(checked)
							: unchecked);
			buttons[i].click = false;
			buttons[i].usable = true;
		}
	}
}
