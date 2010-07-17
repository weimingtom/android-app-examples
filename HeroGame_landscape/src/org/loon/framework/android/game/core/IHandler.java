package org.loon.framework.android.game.core;

import org.loon.framework.android.game.LGameActivity;
import org.loon.framework.android.game.core.graphics.IScreen;
import org.loon.framework.android.game.core.graphics.geom.Dimension;
import org.loon.framework.android.game.media.sound.AssetsSoundManager;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

/**
 * 
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1.0
 */
public interface IHandler{
	public boolean onTouchEvent(MotionEvent e);
	public boolean onKeyDown(int keyCode, KeyEvent e);

	public boolean onKeyUp(int keyCode, KeyEvent e) ;
	public abstract AssetsSoundManager getAssetsSound();

	public abstract void destroy();

	public abstract void initScreen();

	public abstract void setScreen(final IScreen control);

	public abstract IScreen getScreen();

	public abstract int getWidth();

	public abstract int getHeight();

	public abstract Dimension getScreenDimension();

	public abstract View getView();

	public abstract LGameActivity getLGameActivity();

	public abstract Context getContext();

	public abstract Window getWindow();

	public abstract WindowManager getWindowManager();

}
