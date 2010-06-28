package org.loon.framework.android.game.core.graphics.window;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.action.sprite.WaitAnimation;
import org.loon.framework.android.game.core.LSystem;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LContainer;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.loon.framework.android.game.utils.PassWordUtils;
import org.loon.framework.android.game.utils.http.InternalDownload;
import org.loon.framework.android.game.utils.http.WebClient;

import android.view.KeyEvent;

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
public class LPicture extends LContainer {

	private Map lazyImage = Collections.synchronizedMap(new HashMap(
			LSystem.DEFAULT_MAX_CACHE_SIZE));

	private LTimer timer;

	private LImage image;

	private WaitAnimation wait;

	private int states;

	private Thread thread;

	public LPicture(int x, int y, int w, int h) {
		super(x, y, w, h);
		this.timer = new LTimer(100);
		this.wait = new WaitAnimation(w, h);
		this.setBackground(LColor.white);
		this.wait.setRunning(true);
		this.customRendering = true;
		this.setElastic(true);
		this.setLocked(true);
		this.setLayer(100);
	}

	public void notBackground() {
		setBackground((LImage) null);
	}

	public void loadImage(final String fileName, final boolean transparency) {
		if (thread == null) {
			thread = new Thread(new Runnable() {
				public void run() {
					image = GraphicsUtils.loadImage(fileName, transparency);
				}
			});
			thread.start();
			thread = null;
		}
	}

	public void loadImage(final LImage img) {
		this.image = img;
	}

	public void loadWebImage(final String url, final boolean transparency) {
		if (thread == null) {
			final String key = PassWordUtils.toMD5(url);
			if (key != null) {
				if (lazyImage.size() > LSystem.DEFAULT_MAX_CACHE_SIZE) {
					lazyImage.clear();
				}
				image = (LImage) lazyImage.get(key);
				if (image == null) {
					thread = new Thread(new Runnable() {
						public void run() {
							WebClient client = new WebClient(url);
							InternalDownload inner = client
									.getInternalDownload();
							inner.download();
							while (states != InternalDownload.COMPLETE
									& states != InternalDownload.ERROR) {
								states = inner.getStatus();
							}
							if (states == InternalDownload.COMPLETE) {
								image = GraphicsUtils.loadImage(inner
										.getBytes(), transparency);
								lazyImage.put(key, image);
							}
							inner.reset();
							inner = null;
						}
					});
					thread.start();
					thread = null;
				}
			}
		}
	}

	public LImage getImage() {
		return image;
	}

	public void doClick() {

	}

	public void dispose() {
		super.dispose();
		timer = null;
		wait = null;
		if (image != null) {
			image.dispose();
			image = null;
		}
	}

	protected void processTouchPressed() {
		if (this.input.isTouchClick()) {
			this.doClick();
		}
	}

	protected void processKeyPressed() {
		if (this.input.getKeyPressed() == KeyEvent.KEYCODE_ENTER) {
			this.doClick();
		}
	}

	public void update(long elapsedTime) {
		if (visible) {
			super.update(elapsedTime);
			if (timer.action(elapsedTime) && image == null) {
				wait.next();
			}
		}
	}

	protected void createCustomUI(LGraphics g, int x, int y, int w, int h) {
		if (visible) {
			if (image == null) {
				wait.draw(g, x, y, w, h);
			} else {
				g.drawImage(image, x, y, w, h);
			}
		}
	}

	protected void processTouchDragged() {
		if (!locked) {
			if (getContainer() != null) {
				getContainer().sendToFront(this);
			}
			this.move(this.input.getTouchDX(), this.input.getTouchDY());
		}
	}

	public int getStates() {
		return states;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public String getUIName() {
		return "Picture";
	}

}