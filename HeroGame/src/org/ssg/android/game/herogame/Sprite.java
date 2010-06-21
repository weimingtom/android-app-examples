package org.ssg.android.game.herogame;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.util.Log;

/**
 * Copyright 2008 - 2009 Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 * 
 * @project loonframework
 * @author chenpeng
 * @email：ceponline@yahoo.com.cn
 * @version 0.1
 */
public abstract class Sprite extends
        org.loon.framework.android.game.action.sprite.Sprite {

    public static final int LEFT = 1;
    public static final int RIGHT = 2;
    public static final int UP = 3;
    public static final int DOWN = 0;

    protected int dir;

    protected int x, y;

    protected int width, height;

    protected LImage image;

    protected int count;

    private boolean isShow = true; // 是否显示

    protected Map map;

    public LTimer timer;

    private int fixX, fixY;

    public Sprite(String fileName, int x, int y, int w, int h, Map map) {
        this.x = x;
        this.y = y;
        this.map = map;
        this.timer = new LTimer(300);
        width = w;
        height = h;
        image = GraphicsUtils.loadImage(fileName);
        count = 0;

        fixX = (MainScreen.CS - width) / 2;
        fixY = (MainScreen.CS - height) / 2;
        if (fixX < 0)
            fixX = 0;
        if (fixY < 0)
            fixY = 0;
        Log.e("aaa", "" + fixX + "/" + fixY);
    }

    /**
     * 更新精灵状态
     * 
     * @param elapsedTime
     */
    public void updateStatus(long elapsedTime) {
        if (timer.action(elapsedTime)) {
            // if (count == 0) {
            // count = 1;
            // } else if (count == 1) {
            // count = 0;
            // }
            if (count == 4) {
                count = 0;
            } else {
                count++;
            }
        }
        update();
    }

    public abstract void update();

    /**
     * 描绘精灵
     * 
     * @param g
     * @param offsetX
     * @param offsetY
     */
    public void draw(LGraphics g, int offsetX, int offsetY) {
        if (!isShow()) {
            return;
        }

        // 如果超过帧数，从第0帧再开始显示
        if (image.getWidth() <= count * width) {
            count = 0;
        }

        this.draw(g, x * MainScreen.CS + offsetX, y * MainScreen.CS + offsetY,
                x * MainScreen.CS + offsetX + width, y * MainScreen.CS
                        + offsetY + height, count * width, dir * height, count
                        * width + width, dir * height + height);
    }

    public void draw(LGraphics g, int x, int y, int w, int h, int x1, int y1,
            int w1, int h1) {
        g.drawImage(image, x + fixX, y + fixY, w + fixX, h + fixY, x1, y1, w1,
                h1);
    }

    /**
     * 对精灵进行矩形碰撞测试
     * 
     * @param sprite
     * @return
     */
    public boolean isCollision(Sprite sprite) {
        RectBox playerRect = new RectBox((int) x, (int) y, width, height);
        RectBox spriteRect = new RectBox((int) sprite.getX(), (int) sprite
                .getY(), sprite.getWidth(), sprite.getHeight());
        // 判定指定精灵的矩形边框是否与本精灵重合
        if (playerRect.intersects(spriteRect)) {
            return true;
        }

        return false;
    }

    public int getXs() {
        return x;
    }

    public int getYs() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean isShow) {
        this.isShow = isShow;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
