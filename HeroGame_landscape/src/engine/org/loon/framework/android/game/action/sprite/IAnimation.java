package org.loon.framework.android.game.action.sprite;

import org.loon.framework.android.game.core.graphics.LImage;

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
public interface IAnimation {

	/**
	 * 添加一个动画图像
	 * 
	 * @param image
	 * @param timer
	 */
	public void addFrame(SpriteImage image, long timer);

	/**
	 * 添加一个动画图像
	 * 
	 * @param image
	 * @param timer
	 */
	public void addFrame(LImage image, long timer);

	/**
	 * 添加一个动画图像
	 * 
	 * @param fileName
	 * @param timer
	 */
	public void addFrame(String fileName, long timer);

	/**
	 * 开始执行动画
	 * 
	 */
	public void start();

	/**
	 * 更新当前动画
	 * 
	 * @param timer
	 */
	public void update(long timer);

	/**
	 * 返回当前动画图象
	 * 
	 * @return
	 */
	public SpriteImage getSpriteImage();

	/**
	 * 返回当前动画图象
	 * 
	 * @param index
	 * @return
	 */
	public SpriteImage getSpriteImage(int index);

	/**
	 * 返回当前动画索引
	 * 
	 * @return
	 */
	public int getCurrentFrameIndex();

	public float getAlpha();

	public int getTotalFrames();

}
