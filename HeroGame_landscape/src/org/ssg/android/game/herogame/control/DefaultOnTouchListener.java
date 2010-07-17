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
		// TODO Auto-generated method stub
		return false;
	}

	public DefaultOnTouchListener(Object ref) {
		this.ref = ref;
	}

	@Override
	public boolean onTouchMove(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onTouchUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
}
