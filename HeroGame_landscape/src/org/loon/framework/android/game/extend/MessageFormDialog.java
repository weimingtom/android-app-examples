package org.loon.framework.android.game.extend;

import java.util.HashMap;
import java.util.Map;

import org.loon.framework.android.game.core.graphics.LGraphics;
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
 * @email ceponline@yahoo.com.cn
 * @version 0.1
 */
public class MessageFormDialog {

	final static private int rmxp_space_width = 10, rmxp_space_height = 10,
			rmxp_space_size = 27;

	final static private Map lazyImages = new HashMap(10);

	public final static LImage getRMXPDialog(String fileName, int width,
			int height) {
		return MessageFormDialog.getRMXPDialog(GraphicsUtils
				.loadImage(fileName,true), width, height);
	}

	public final static LImage getRMXPloadBuoyage(String fileName, int width,
			int height) {
		return getRMXPloadBuoyage(GraphicsUtils.loadImage(fileName,true), width,
				height);
	}

	public final static LImage getRMXPloadBuoyage(LImage rmxpImage, int width,
			int height) {
		String keyName = ("buoyage" + width + "|" + height).intern();
		LImage lazyImage = (LImage) lazyImages.get(keyName);
		if (lazyImage == null) {
			LImage image, left, right, center, up, down = null;
			int objWidth = 32;
			int objHeight = 32;
			int x1 = 128;
			int x2 = 160;
			int y1 = 64;
			int y2 = 96;
			int k = 1;
			try {
				image = GraphicsUtils.drawClipImage(rmxpImage, objWidth,
						objHeight, x1, y1, x2, y2);
				lazyImage = LImage.createImage(width, height,true);
				LGraphics g = lazyImage.getLGraphics();
				left = GraphicsUtils.drawClipImage(image, k, height, 0, 0, k,
						objHeight);
				right = GraphicsUtils.drawClipImage(image, k, height, objWidth
						- k, 0, objWidth, objHeight);
				center = GraphicsUtils.drawClipImage(image, width, height, k,
						k, objWidth - k, objHeight - k);
				up = GraphicsUtils.drawClipImage(image, width, k, 0, 0,
						objWidth, k);
				down = GraphicsUtils.drawClipImage(image, width, k, 0,
						objHeight - k, objWidth, objHeight);
				g.drawImage(center, 0, 0);
				g.drawImage(left, 0, 0);
				g.drawImage(right, width - k, 0);
				g.drawImage(up, 0, 0);
				g.drawImage(down, 0, height - k);
				g.dispose();
				lazyImages.put(keyName, lazyImage);
			} catch (Exception e) {
				return null;
			} finally {
				left = null;
				right = null;
				center = null;
				up = null;
				down = null;
				image = null;
			}
		}
		return lazyImage;

	}

	public final static LImage getRMXPDialog(LImage rmxpImage, int width,
			int height) {
		String keyName = ("dialog" + width + "|" + height).intern();
		LImage lazyImage = (LImage) lazyImages.get(keyName);
		if (lazyImage == null) {
			int objWidth = 64;
			int objHeight = 64;
			int x1 = 128;
			int x2 = 192;
			int y1 = 0;
			int y2 = 64;
			LImage image = null;
			LImage messageImage = null;
			try {
				image = GraphicsUtils.drawClipImage(rmxpImage, objWidth,
						objHeight, x1, y1, x2, y2);
				messageImage = GraphicsUtils.drawClipImage(rmxpImage, 128, 128,
						0, 0, 128, 128);
			} catch (Exception e) {
				e.printStackTrace();
			}

			MessageDialogSplit mds = new MessageDialogSplit(image,
					rmxp_space_width, rmxp_space_height, rmxp_space_size);
			mds.split();
			int doubleSpace = rmxp_space_size * 2;
			if (width < doubleSpace) {
				width = doubleSpace + 5;
			}
			if (height < doubleSpace) {
				height = doubleSpace + 5;
			}
			lazyImage = LImage.createImage(width - 10, height,true);
			LGraphics graphics = lazyImage.getLGraphics();
			graphics.setAlpha(0.5f);
			messageImage = GraphicsUtils.getResize(messageImage, width
					- rmxp_space_width * 2, height - rmxp_space_height);
			graphics.drawImage(messageImage, 5, 5);
			graphics.setAlpha(1.0f);
			graphics.drawImage(mds.getLeftUpImage(), 0, 0);
			graphics.drawImage(mds.getRightUpImage(),
					(width - rmxp_space_size - rmxp_space_width), 0);
			graphics.drawImage(mds.getLeftDownImage(), 0,
					(height - rmxp_space_size));
			graphics.drawImage(mds.getRightDownImage(), (width
					- rmxp_space_size - rmxp_space_width),
					(height - rmxp_space_size));
			int nWidth = width - doubleSpace;
			int nHeight = height - doubleSpace;
			graphics.drawImage(GraphicsUtils.getResize(mds.getUpImage(),
					nWidth, rmxp_space_size), rmxp_space_size, 0);
			graphics.drawImage(GraphicsUtils.getResize(mds.getDownImage(),
					nWidth, rmxp_space_size), rmxp_space_size,
					(height - rmxp_space_size));
			graphics.drawImage(GraphicsUtils.getResize(mds.getLeftImage(),
					rmxp_space_size, nHeight), 0, rmxp_space_size);
			graphics.drawImage(GraphicsUtils.getResize(mds.getRightImage(),
					rmxp_space_size, nHeight),
					(width - rmxp_space_size - rmxp_space_width),
					rmxp_space_size);
			graphics.dispose();
			lazyImages.put(keyName, lazyImage);
		}
		return lazyImage;
	}

	public static void clear() {
		lazyImages.clear();
	}
}
