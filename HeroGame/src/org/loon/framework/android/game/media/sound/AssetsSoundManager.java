package org.loon.framework.android.game.media.sound;

import java.util.Iterator;
import java.util.Map.Entry;

import org.loon.framework.android.game.utils.collection.ArrayMap;

import android.content.Context;

/**
 * 
 * Copyright 2008 - 2010
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public class AssetsSoundManager {

	public static final int MAX_CLIPS = 50;

	private static AssetsSoundManager am;

	private ArrayMap sounds = new ArrayMap(MAX_CLIPS);

	private Context context;

	private int clipCount = 0;

	private boolean paused;

	private AssetsSound asound;

	final static public AssetsSoundManager getInstance(Context ctx) {
		if (am == null) {
			return (am = new AssetsSoundManager(ctx));
		}
		return am;
	}

	private AssetsSoundManager(Context ctx) {
		context = ctx;
	}

	public synchronized void playSound(String name, int vol) {
		if (paused) {
			return;
		}
		if (sounds.containsKey(name)) {
			((AssetsSound) sounds.get(name)).play();
		} else {
			if (clipCount > MAX_CLIPS) {
				int idx = sounds.size() - 1;
				String k = (String) sounds.keySet().toArray()[idx];
				AssetsSound clip = (AssetsSound) sounds.remove(k);
				clip.release();
				clip = null;
				clipCount--;
			}
			asound = new AssetsSound(context);
			asound.setDataSource(name);
			asound.play(vol);
			sounds.put(name, asound);
			clipCount++;
		}
	}

	public synchronized void playSound(String name, boolean loop) {
		if (paused) {
			return;
		}
		if (sounds.containsKey(name)) {
			((AssetsSound) sounds.get(name)).play();
		} else {
			if (clipCount > MAX_CLIPS) {
				int idx = sounds.size() - 1;
				String k = (String) sounds.keySet().toArray()[idx];
				AssetsSound clip = (AssetsSound) sounds.remove(k);
				clip.release();
				clip = null;
				clipCount--;
			}
			asound = new AssetsSound(context);
			asound.setDataSource(name);
			if (loop) {
				asound.loop();
			} else {
				asound.play();
			}
			sounds.put(name, asound);
			clipCount++;
		}
	}

	public synchronized void stopSoundAll() {
		if (sounds != null) {
			for (Iterator<?> it = sounds.iterator(); it.hasNext();) {
				Entry<?, ?> sound = (Entry<?, ?>) it.next();
				if (sound != null) {
					AssetsSound as = (AssetsSound) sound.getValue();
					if (as != null) {
						as.stop();
					}
				}
			}
		}
	}

	public synchronized void resetSound() {
		if (asound != null) {
			asound.reset();
		}
	}

	public synchronized void stopSound() {
		if (asound != null) {
			asound.stop();
			asound.release();
		}
	}

	public synchronized void stopPlayer() {
		if (asound != null) {
			asound.stopPlayer();
			asound.release();
		}
	}

	public synchronized void setSoundVolume(int vol) {
		if (asound != null) {
			asound.setVolume(vol);
		}
	}

	public synchronized void pause(boolean pause) {
		paused = pause;
	}

}
