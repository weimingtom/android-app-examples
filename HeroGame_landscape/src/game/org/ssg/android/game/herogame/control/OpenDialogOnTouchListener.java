package org.ssg.android.game.herogame.control;

import org.ssg.android.game.herogame.MainScreen;

import android.view.MotionEvent;

public class OpenDialogOnTouchListener extends DefaultOnTouchListener {
	private Dialog dialog;

	public OpenDialogOnTouchListener(Button button, Dialog dialog) {
		super(button);
		this.dialog = dialog;
	}

	@Override
	public boolean onTouchDown(MotionEvent arg0) {
		Button button = (Button) getRef();
		if (button.checkComplete()) {
			button.setComplete(true);
			button.setSelect(true);
		}
		return false;
	}

	@Override
	public boolean onTouchUp(MotionEvent arg0) {
		Button button = (Button) getRef();
		if (button.isComplete() && button.checkComplete()
				&& !dialog.equals(MainScreen.instance.getTopDialog())) {
			MainScreen.instance.setTopDialog(dialog);
		}
		button.setComplete(false);
		button.setSelect(false);
		return true;
	}
}
