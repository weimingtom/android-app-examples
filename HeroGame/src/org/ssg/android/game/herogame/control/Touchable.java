package org.ssg.android.game.herogame.control;

import java.util.LinkedList;

public interface Touchable {
	
	public boolean isActive();
	
	public void setIsActive(boolean isActive);
	
	public LinkedList<OnTouchListener> getOnTouchListener();
	
	public void addOnTouchListener(OnTouchListener listener);
	
	public void removeOnToucListener(OnTouchListener listener);
	
	public boolean isTouched();
}
