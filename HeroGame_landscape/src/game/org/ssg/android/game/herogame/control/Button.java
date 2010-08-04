package org.ssg.android.game.herogame.control;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.extend.DrawButton;
import org.loon.framework.android.game.utils.GraphicsUtils;

public class Button extends DrawButton{

	private OnTouchListener listener;
	public boolean isVisible = true;
	
	public Button(Screen screen, int no, int space, boolean isRow,
			LImage selectImage, LImage buttonImage) {
		super(screen, no, space, isRow, selectImage, buttonImage);
	}
	
	public void setOnTouchListener(OnTouchListener listener) {
		this.listener = listener;
	}
	
	public OnTouchListener getOnTouchListener() {
		return listener;
	}
	
	public static void initialize(final Screen screen,
			final Button[] buttons, final int space, final LImage checked,
			final LImage unchecked) {
		initialize(screen, buttons, space, false, checked,
				unchecked == null ? GraphicsUtils.getGray(checked) : unchecked);
	}

	public static void initialize(final Screen screen,
			final Button[] buttons, final int space, final boolean isRow,
			final LImage checked, final LImage unchecked) {
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = new Button(screen, i, space, isRow, checked,
					unchecked == null ? GraphicsUtils.getGray(checked)
							: unchecked);
			buttons[i].click = false;
			buttons[i].usable = true;
		}
	}
}
