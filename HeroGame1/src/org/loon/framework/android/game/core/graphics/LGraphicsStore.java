package org.loon.framework.android.game.core.graphics;

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
public class LGraphicsStore {

	private LFont font;

	private LColor color;

	private boolean antiAlias;

	public void save(LGraphics gl) {
		font = gl.getFont();
		color = gl.getColor();
		antiAlias = gl.isAntiAlias();
	}

	public void restore(LGraphics gl) {
		gl.setFont(font);
		gl.setColor(color);
		gl.setAntiAlias(antiAlias);
	}

	public LFont getFont() {
		return font;
	}

	public void setFont(LFont font) {
		this.font = font;
	}

	public LColor getColor() {
		return color;
	}

	public void setColor(LColor color) {
		this.color = color;
	}

	public boolean isAntiAlias() {
		return antiAlias;
	}

	public void setAntiAlias(boolean antiAlias) {
		this.antiAlias = antiAlias;
	}

}
