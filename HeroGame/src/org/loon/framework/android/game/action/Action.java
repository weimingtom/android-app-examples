package org.loon.framework.android.game.action;

/**
 * Copyright 2008 - 2009
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public class Action implements IAction {

	private IAction action;
	private int state;
	private int amount;
	private int behavior;
	private String name;

	public static final int DETECT_FIRST_PRESS = 0;
	public static final int NORMAL = 1;

	public static final int WAITING_FOR_RELEASE = 0;
	public static final int RELEASED = 1;
	public static final int PRESSED = 2;

	public Action(IAction impl) {
		this(impl, "", NORMAL);
	}

	public Action(IAction impl, String name) {
		this(impl, name, NORMAL);
	}

	public Action(IAction impl, String name, int behavior) {
		setIAction(impl);
		this.name = name;
		setBehavior(behavior);
		reset();
	}

	public synchronized void setIAction(IAction action) {
		this.action = action;
	}

	public synchronized IAction getIAction() {
		return action;
	}

	public synchronized void doAction(long timer) {
		action.doAction(timer);
	}

	public synchronized void press() {
		press(1);
	}

	public synchronized void press(int amt) {
		if (getState() != WAITING_FOR_RELEASE) {
			amount += amt;
			state = PRESSED;
		}
	}

	public synchronized void release() {
		state = RELEASED;
	}

	public synchronized boolean isPressed() {
		return (getAmount() != 0);
	}

	public synchronized int getAmount() {
		int amt = amount;
		if (amt != 0) {
			if (getState() == RELEASED) {
				amount = 0;
			} else if (getBehavior() == DETECT_FIRST_PRESS) {
				state = WAITING_FOR_RELEASE;
				amount = 0;
			}
		}
		return amt;
	}

	public synchronized void reset() {
		state = RELEASED;
		amount = 0;
	}

	public synchronized void setBehavior(int behavior) {
		this.behavior = behavior;
	}

	public synchronized int getBehavior() {
		return behavior;
	}

	public synchronized int getState() {
		return state;
	}

	public synchronized String getName() {
		return name;
	}

}
