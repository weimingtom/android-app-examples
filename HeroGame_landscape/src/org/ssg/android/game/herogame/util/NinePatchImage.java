/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/*
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
 * @email ceponline ceponline@yahoo.com.cn
 * @version 0.1
 */


package org.ssg.android.game.herogame.util;

import java.util.ArrayList;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.geom.Rectangle;
import org.loon.framework.android.game.utils.GraphicsUtils;

public class NinePatchImage {
	
    private List<Rectangle> patches;
    private List<Rectangle> horizontalPatches;
    private List<Rectangle> verticalPatches;
    private List<Rectangle> fixed;
    LImage image;
    LImage result;
    
    int [] row = null;
    int [] column = null;
    
    private float horizontalPatchesSum;
    private float verticalPatchesSum;
    
    private int remainderHorizontal = 0;
    private int remainderVertical = 0;
	private boolean verticalStartWithPatch;
	private boolean horizontalStartWithPatch;
	
	private int scaledWidth;
	private int scaledHeight;
    
	public NinePatchImage(String innerFileName) {
		image = GraphicsUtils.loadImage(innerFileName, true);
		findPatches();
	}
	
	public LImage createImage(int scaledWidth , int scaledHeight) {
		this.scaledWidth = scaledWidth;
		this.scaledHeight = scaledHeight;
		computePatches();
		result = new LImage(scaledWidth, scaledHeight, true);
		drawNinePathImage(result.getLGraphics());
		return result;
	}

	private void drawNinePathImage(LGraphics g) {
		int x = 0;
        int y = 0;

        if (patches.size() == 0) {
            g.drawImage(image, 0, 0, scaledWidth, scaledHeight);
            return;
        }

        int fixedIndex = 0;
        int horizontalIndex = 0;
        int verticalIndex = 0;
        int patchIndex = 0;

        boolean hStretch;
        boolean vStretch;

        float vWeightSum = 1.0f;
        float vRemainder = remainderVertical;

        vStretch = verticalStartWithPatch;
        while (y < scaledHeight - 1) {
            hStretch = horizontalStartWithPatch;

            int height = 0;
            float vExtra = 0.0f;

            float hWeightSum = 1.0f;
            float hRemainder = remainderHorizontal;

            while (x < scaledWidth - 1) {
                Rectangle r;
                if (!vStretch) {
                    if (hStretch) {
                        r = horizontalPatches.get(horizontalIndex++);
                        float extra = r.width / horizontalPatchesSum;
                        int width = (int) (extra * hRemainder / hWeightSum);
                        hWeightSum -= extra;
                        hRemainder -= width;
                        g.drawImage(image, x, y, x + width, y + r.height, r.x, r.y,
                                r.x + r.width, r.y + r.height);
                        x += width;
                    } else {
                        r = fixed.get(fixedIndex++);
                        g.drawImage(image, x, y, x + r.width, y + r.height, r.x, r.y,
                                r.x + r.width, r.y + r.height);
                        x += r.width;
                    }
                    height = r.height;
                } else {
                    if (hStretch) {
                        r = patches.get(patchIndex++);
                        vExtra = r.height / verticalPatchesSum;
                        height = (int) (vExtra * vRemainder / vWeightSum);
                        float extra = r.width / horizontalPatchesSum;
                        int width = (int) (extra * hRemainder / hWeightSum);
                        hWeightSum -= extra;
                        hRemainder -= width;
                        g.drawImage(image, x, y, x + width, y + height, r.x, r.y,
                                r.x + r.width, r.y + r.height);
                        x += width;
                    } else {
                        r = verticalPatches.get(verticalIndex++);
                        vExtra = r.height / verticalPatchesSum;
                        height = (int) (vExtra * vRemainder / vWeightSum);
                        g.drawImage(image, x, y, x + r.width, y + height, r.x, r.y,
                                r.x + r.width, r.y + r.height);
                        x += r.width;
                    }
                    
                }
                hStretch = !hStretch;
            }
            x = 0;
            y += height;
            if (vStretch) {
                vWeightSum -= vExtra;
                vRemainder -= height;
            }
            vStretch = !vStretch;
        }
		g.dispose();
	}
    
	void computePatches() {
        boolean measuredWidth = false;
        boolean endRow = true;

        remainderHorizontal = 0;
        remainderVertical = 0;

        if (fixed.size() > 0) {
            int start = fixed.get(0).y;
            for (Rectangle rect : fixed) {
                if (rect.y > start) {
                    endRow = true;
                    measuredWidth = true;
                }
                if (!measuredWidth) {
                    remainderHorizontal += rect.width;
                }
                if (endRow) {
                    remainderVertical += rect.height;
                    endRow = false;
                    start = rect.y;
                }
            }
        }

        remainderHorizontal = scaledWidth - remainderHorizontal;
        remainderVertical = scaledHeight - remainderVertical;

        horizontalPatchesSum = 0;
        if (horizontalPatches.size() > 0) {
            int start = -1;
            for (Rectangle rect : horizontalPatches) {
                if (rect.x > start) {
                    horizontalPatchesSum += rect.width;
                    start = rect.x;
                }
            }
        } else {
            int start = -1;
            for (Rectangle rect : patches) {
                if (rect.x > start) {
                    horizontalPatchesSum += rect.width;
                    start = rect.x;
                }
            }
        }

        verticalPatchesSum = 0;
        if (verticalPatches.size() > 0) {
            int start = -1;
            for (Rectangle rect : verticalPatches) {
                if (rect.y > start) {
                    verticalPatchesSum += rect.height;
                    start = rect.y;
                }
            }
        } else {
            int start = -1;
            for (Rectangle rect : patches) {
                if (rect.y > start) {
                    verticalPatchesSum += rect.height;
                    start = rect.y;
                }
            }
        }
        //setSize(size);
    }
	
    private void findPatches() {
        int width = image.getWidth();
        int height = image.getHeight();
        
        row = image.getPixels(0, 0, width, 1);
        column = image.getPixels(0, 0, 1, height);

        boolean[] result = new boolean[1];
        Pair<List<Pair<Integer>>> left = getPatches(column, result);
        verticalStartWithPatch = result[0];

        result = new boolean[1];
        Pair<List<Pair<Integer>>> top = getPatches(row, result);
        horizontalStartWithPatch = result[0];

        fixed = getRectangles(left.first, top.first);
        patches = getRectangles(left.second, top.second);

        if (fixed.size() > 0) {
            horizontalPatches = getRectangles(left.first, top.second);
            verticalPatches = getRectangles(left.second, top.first);
        } else {
            if (top.first.size() > 0) {
                horizontalPatches = new ArrayList<Rectangle>(0);
                verticalPatches = getVerticalRectangles(top.first);
            } else if (left.first.size() > 0) {
                horizontalPatches = getHorizontalRectangles(left.first);
                verticalPatches = new ArrayList<Rectangle>(0);
            } else {
                horizontalPatches = verticalPatches = new ArrayList<Rectangle>(0);
            }
        }
    }
    
    private Pair<List<Pair<Integer>>> getPatches(int[] pixels, boolean[] startWithPatch) {
        int lastIndex = 1;
        int lastPixel = pixels[1];
        boolean first = true;

        List<Pair<Integer>> fixed = new ArrayList<Pair<Integer>>();
        List<Pair<Integer>> patches = new ArrayList<Pair<Integer>>();

        for (int i = 1; i < pixels.length - 1; i++) {
            int pixel = pixels[i];
            if (pixel != lastPixel) {
                if (lastPixel == 0xFF000000) {
                    if (first) startWithPatch[0] = true;
                    patches.add(new Pair<Integer>(lastIndex, i));
                } else {
                    fixed.add(new Pair<Integer>(lastIndex, i));
                }
                first = false;

                lastIndex = i;
                lastPixel = pixel;
            }
        }
        if (lastPixel == 0xFF000000) {
            if (first) startWithPatch[0] = true;
            patches.add(new Pair<Integer>(lastIndex, pixels.length - 1));
        } else {
            fixed.add(new Pair<Integer>(lastIndex, pixels.length - 1));
        }

        if (patches.size() == 0) {
            patches.add(new Pair<Integer>(1, pixels.length - 1));
            startWithPatch[0] = true;
            fixed.clear();
        }

        return new Pair<List<Pair<Integer>>>(fixed, patches);
    }
    
    private List<Rectangle> getRectangles(List<Pair<Integer>> leftPairs,
            List<Pair<Integer>> topPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> left : leftPairs) {
            int y = left.first;
            int height = left.second - left.first;
            for (Pair<Integer> top : topPairs) {
                int x = top.first;
                int width = top.second - top.first;

                rectangles.add(new Rectangle(x, y, width, height));
            }
        }
        return rectangles;
    }
    
    private List<Rectangle> getVerticalRectangles(List<Pair<Integer>> topPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> top : topPairs) {
            int x = top.first;
            int width = top.second - top.first;

            rectangles.add(new Rectangle(x, 1, width, image.getHeight() - 2));
        }
        return rectangles;
    }

    private List<Rectangle> getHorizontalRectangles(List<Pair<Integer>> leftPairs) {
        List<Rectangle> rectangles = new ArrayList<Rectangle>();
        for (Pair<Integer> left : leftPairs) {
            int y = left.first;
            int height = left.second - left.first;

            rectangles.add(new Rectangle(1, y, image.getWidth() - 2, height));
        }
        return rectangles;
    }

    static class Pair<E> {
        E first;
        E second;

        Pair(E first, E second) {
            this.first = first;
            this.second = second;
        }

        @Override
        public String toString() {
            return "Pair[" + first + ", " + second + "]";
        }
    }
    
    
    
}
