package org.loon.framework.android.game.extend;

import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;

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
 * @email��ceponline@yahoo.com.cn
 * @version 0.1
 */
public class MessageDialogSplit {

	private LImage leftUpImage;

	private LImage rightUpImage;

	private LImage leftCenterImage;

	private LImage rightCenterImage;

	private LImage leftDownImage;

	private LImage rightDownImage;

	private LImage centerImage;

	private LImage upImage;

	private LImage downImage;

	private LImage leftImage;

	private LImage rightImage;

	private int space_size;

	private int space_width;

	private int space_height;

	private LImage image;

	public MessageDialogSplit(final LImage image, int spaceWidth,
			int spaceHeight, int spaceSize) {
		this.image = image;
		this.space_width = spaceWidth;
		this.space_height = spaceHeight;
		this.space_size = spaceSize;

	}

	public void split() {
		leftUpImage = GraphicsUtils.drawClipImage(image, space_size,
				space_size, 0, 0);
		rightUpImage = GraphicsUtils.drawClipImage(image, space_size,
				space_size, space_size + space_width, 0);
		leftCenterImage = GraphicsUtils.drawClipImage(image, space_size,
				space_height, 0, space_size);
		rightCenterImage = GraphicsUtils.drawClipImage(image, space_size,
				space_height, space_size + space_width, space_size);
		leftDownImage = GraphicsUtils.drawClipImage(image, space_size,
				space_size, 0, space_size + space_height);
		rightDownImage = GraphicsUtils
				.drawClipImage(image, space_size, space_size, space_size
						+ space_width, space_size + space_height);
		centerImage = GraphicsUtils.drawClipImage(image, space_width,
				space_height, space_size, space_size);
		upImage = GraphicsUtils.drawClipImage(image, space_width, space_size,
				space_size, 0);
		downImage = GraphicsUtils.drawClipImage(image, space_width, space_size,
				space_size, space_size + space_height);
		leftImage = GraphicsUtils.drawClipImage(image, space_size,
				space_height, 0, space_size);
		rightImage = GraphicsUtils.drawClipImage(image, space_size,
				space_height, space_width + space_size, space_size);
	}

	public LImage getCenterImage() {
		return centerImage;
	}

	public void setCenterImage(LImage centerImage) {
		this.centerImage = centerImage;
	}

	public LImage getDownImage() {
		return downImage;
	}

	public void setDownImage(LImage downImage) {
		this.downImage = downImage;
	}

	public LImage getImage() {
		return image;
	}

	public void setImage(LImage image) {
		this.image = image;
	}

	public LImage getLeftCenterImage() {
		return leftCenterImage;
	}

	public void setLeftCenterImage(LImage leftCenterImage) {
		this.leftCenterImage = leftCenterImage;
	}

	public LImage getLeftDownImage() {
		return leftDownImage;
	}

	public void setLeftDownImage(LImage leftDownImage) {
		this.leftDownImage = leftDownImage;
	}

	public LImage getLeftImage() {
		return leftImage;
	}

	public void setLeftImage(LImage leftImage) {
		this.leftImage = leftImage;
	}

	public LImage getLeftUpImage() {
		return leftUpImage;
	}

	public void setLeftUpImage(LImage leftUpImage) {
		this.leftUpImage = leftUpImage;
	}

	public LImage getRightCenterImage() {
		return rightCenterImage;
	}

	public void setRightCenterImage(LImage rightCenterImage) {
		this.rightCenterImage = rightCenterImage;
	}

	public LImage getRightDownImage() {
		return rightDownImage;
	}

	public void setRightDownImage(LImage rightDownImage) {
		this.rightDownImage = rightDownImage;
	}

	public LImage getRightImage() {
		return rightImage;
	}

	public void setRightImage(LImage rightImage) {
		this.rightImage = rightImage;
	}

	public LImage getRightUpImage() {
		return rightUpImage;
	}

	public void setRightUpImage(LImage rightUpImage) {
		this.rightUpImage = rightUpImage;
	}

	public int getSpaceSize() {
		return space_size;
	}

	public int getSpaceWidth() {
		return space_width;
	}

	public int getSpaceHeight() {
		return space_height;
	}

	public LImage getUpImage() {
		return upImage;
	}

	public void setUpImage(LImage upImage) {
		this.upImage = upImage;
	}
}
