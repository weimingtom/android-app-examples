package org.loon.framework.android.game.core;

import org.loon.framework.android.game.core.timer.LTimer;

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
public class LKeyTyped {

	private LInput bsInput;

	private LTimer repeatDelayTimer;

	private LTimer repeatRateTimer;

	private int key;

	private int keyTyped;

	/**
	 * 输入设备,延迟,比率
	 * 
	 * @param bsInput
	 * @param repeatDelay
	 * @param repeatRate
	 */
	public LKeyTyped(LInput bsInput, int repeatDelay, int repeatRate) {
		this.bsInput = bsInput;
		this.repeatDelayTimer = new LTimer(repeatDelay);
		this.repeatRateTimer = new LTimer(repeatRate);
		this.repeatDelayTimer.setActive(false);
		this.key = this.keyTyped = LInput.NO_KEY;
	}

	public LKeyTyped(LInput bsInput) {
		this(bsInput, 450, 40);
	}

	public void update(long elapsedTime) {
		this.keyTyped = this.bsInput.getKeyPressed();
		if (this.keyTyped != LInput.NO_KEY) {
			this.key = this.keyTyped;
			this.repeatDelayTimer.setActive(true);

		} else {
			if (this.bsInput.getKeyReleased() == this.key) {
				this.key = LInput.NO_KEY;
				this.repeatDelayTimer.setActive(false);
			} else if (this.key != LInput.NO_KEY) {
				if (this.repeatDelayTimer.isActive()) {
					if (this.repeatDelayTimer.action(elapsedTime)) {
						this.repeatDelayTimer.setActive(false);
						this.repeatRateTimer.refresh();
						this.keyTyped = this.key;
					}
				} else {
					if (this.repeatRateTimer.action(elapsedTime)) {
						this.keyTyped = this.key;
					}
				}
			}
		}
	}

	public void refresh() {
		this.repeatDelayTimer.refresh();
		this.repeatRateTimer.refresh();
		this.repeatDelayTimer.setActive(false);
		this.key = this.keyTyped = LInput.NO_KEY;
	}

	public int getKeyTyped() {
		return this.keyTyped;
	}

	public boolean isKeyTyped(int keyCode) {
		return (this.keyTyped == keyCode);
	}

	public long getRepeatDelay() {
		return this.repeatDelayTimer.getDelay();
	}

	public void setRepeatDelay(long delay) {
		this.repeatDelayTimer.setDelay(delay);
	}

	public long getRepeatRate() {
		return this.repeatRateTimer.getDelay();
	}

	public void setRepeatRate(long rate) {
		this.repeatRateTimer.setDelay(rate);
	}
}
