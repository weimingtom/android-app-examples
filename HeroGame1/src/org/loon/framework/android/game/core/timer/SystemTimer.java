package org.loon.framework.android.game.core.timer;

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
public class SystemTimer {

	private Thread granularityThread;

	private long lastTime;

	private long virtualTime;

	private boolean running = false;

	public void start() {
		lastTime = System.currentTimeMillis();
		virtualTime = 0;
		running = true;
	}

	public void stop() {
		running = false;
		setHighSleepGranularity(false);
	}

	private final boolean getHighSleepGranularity() {
		return (granularityThread != null && granularityThread.isAlive());
	}

	private void setHighSleepGranularity(boolean high) {
		if (high != getHighSleepGranularity()) {
			if (high) {
				startGranularityThread();
			} else {
				stopGranularityThread();
			}
		}
	}

	private final synchronized void startGranularityThread() {
		Thread t = new Thread() {
			public void run() {
				while (granularityThread == this && running) {
					try {
						Thread.sleep(Integer.MAX_VALUE);
					} catch (InterruptedException ex) {
					}
				}
			}
		};
		t.setDaemon(true);
		try {
			t.start();
		} catch (IllegalThreadStateException ex) {
			t = null;
		}
		granularityThread = t;
	}

	private final synchronized void stopGranularityThread() {
		if (granularityThread != null) {
			Thread t = granularityThread;
			granularityThread = null;
			if (t.isAlive()) {
				try {
					t.interrupt();
				} catch (Exception ex) {
					granularityThread = t;
				}
			}
		}
	}

	public long getTimeMillis() {
		long time = System.currentTimeMillis();
		if (time > lastTime) {
			virtualTime += time - lastTime;
		}
		lastTime = time;

		return virtualTime;
	}

	public long getTimeMicros() {
		return getTimeMillis() * 1000;
	}
}
