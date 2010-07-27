package org.ssg.android.game.herogame.control;

import android.view.MotionEvent;

public class DefaultOnTouchListener implements OnTouchListener{

	private Object ref;
	
	public Object getRef() {
		return ref;
	}

	public void setRef(Object ref) {
		this.ref = ref;
	}

	@Override
	public boolean onTouchDown(MotionEvent arg0) {
		Button button = (Button) getRef();
		if (button.checkComplete()) {
			return true;
		}
		return false;
	}

	public DefaultOnTouchListener(Object ref) {
		this.ref = ref;
	}

	public DefaultOnTouchListener() {

	}
	
	@Override
	public boolean onTouchMove(MotionEvent arg0) {
		Button button = (Button) getRef();
		if (button.isComplete()) {
			if (!button.checkComplete()) {
				button.setComplete(false);
			}
		}
		return true;
	}

	@Override
	public boolean onTouchUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
}
