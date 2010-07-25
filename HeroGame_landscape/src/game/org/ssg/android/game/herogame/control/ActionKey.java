package org.ssg.android.game.herogame.control;

public class ActionKey {

	static final int PRESS_ONLY = 1, STATE_RELEASED = 0, STATE_PRESSED = 1,
			STATE_WAITING_FOR_RELEASE = 2;

	int mode, amount, state;

	public ActionKey() {
		this(0);
	}

	public ActionKey(int mode) {
		this.mode = mode;
		reset();
	}

	public void reset() {
		state = STATE_RELEASED;
		amount = 0;
	}

	public void press() {
		if (state != STATE_WAITING_FOR_RELEASE) {
			amount++;
			state = STATE_PRESSED;
		}
	}

	public void release() {
		state = STATE_RELEASED;
	}

	public boolean isPressed() {
		if (amount != 0) {
			amount--;
			if (state == STATE_RELEASED) {
				amount = 0;
			} else if (mode == PRESS_ONLY) {
				state = STATE_WAITING_FOR_RELEASE;
				amount = 0;
			}
			return true;
		}
		return false;
	}
}
