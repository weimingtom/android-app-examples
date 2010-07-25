package org.ssg.android.game.herogame.control;

import android.view.MotionEvent;

public interface OnTouchListener {

	public boolean onTouchDown(MotionEvent arg0);

	public boolean onTouchMove(MotionEvent arg0);

	public boolean onTouchUp(MotionEvent arg0);
}
