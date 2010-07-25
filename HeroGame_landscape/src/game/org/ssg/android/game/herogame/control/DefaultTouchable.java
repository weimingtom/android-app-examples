package org.ssg.android.game.herogame.control;

import java.util.LinkedList;

import org.ssg.android.game.herogame.MainScreen;

public class DefaultTouchable implements Touchable {

	private boolean isActive;
	private LinkedList<OnTouchListener> listeners;

	@Override
	public boolean isActive() {
		return isActive;
	}

	@Override
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	@Override
	public LinkedList<OnTouchListener> getOnTouchListener() {
		return listeners;
	}

	@Override
	public void addOnTouchListener(OnTouchListener listener) {
		if (listeners == null) {
			listeners = new LinkedList<OnTouchListener>();
		}
		listeners.add(listener);
	}

	@Override
	public void removeOnToucListener(OnTouchListener listener) {
		if (listeners != null) {
			listeners.remove(listener);
		}
	}

	@Override
	public boolean isTouched() {
		return false;
	}

}
