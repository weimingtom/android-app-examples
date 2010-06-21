package org.ssg.android.game.herogame;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.timer.LTimer;

/**
 * @author chenpeng
 * @email ceponline@yahoo.com.cn Loon Framework in Game
 */
public class Hero extends Role {

    private boolean isWalking = false;
    private int destX, destY;
    private int movingX, movingY;
    private int step = MainScreen.CS / 4;
    public int ANIM_OFFSET_X = MainScreen.CS / 2;
    public int ANIM_OFFSET_Y = 0;
    private boolean isAnimating = false;

    public Hero(String filename, int x, int y, int w, int h, Map map) {
        super(filename, x, y, w, h, map, 100, 5, 0);
        dir = DOWN;
    }

    public void move(int direction) {
        if (isWalking)
            return;
        switch (direction) {
            case LEFT:
                // �����ж��¼�
                if (map.isAllow(x - 1, y)) {
                    destX = x - 1;
                    isWalking = true;
                }
                dir = LEFT;
                break;
            case RIGHT:
                if (map.isAllow(x + 1, y)) {
                    destX = x + 1;
                    isWalking = true;
                }
                dir = RIGHT;
                break;
            case UP:
                if (map.isAllow(x, y - 1)) {
                    destY = y - 1;
                    isWalking = true;
                }
                dir = UP;
                break;
            case DOWN:
                if (map.isAllow(x, y + 1)) {
                    destY = y + 1;
                    isWalking = true;
                }
                dir = DOWN;
                break;
            default:
                break;
        }
        movingX = x * MainScreen.CS;
        movingY = y * MainScreen.CS;
        destX *= MainScreen.CS;
        destY *= MainScreen.CS;
    }

    public boolean isCollision(Sprite sprite) {
        RectBox playerRect = new RectBox(x * MainScreen.CS, y * MainScreen.CS,
                width, height);
        RectBox spriteRect = new RectBox(sprite.getXs() * MainScreen.CS, sprite
                .getYs()
                * MainScreen.CS, sprite.getWidth(), sprite.getHeight());
        // 判定指定精灵的矩形边框是否与本精灵重合
        if (playerRect.intersects(spriteRect)) {
            return true;
        }

        return false;
    }

    @Override
    public void draw(LGraphics g, int offsetX, int offsetY) {
        if (!isShow()) {
            return;
        }

        // super.draw(g, offsetX, offsetY);
        int distX = x * MainScreen.CS + offsetX;
        int distY = y * MainScreen.CS + offsetY;
        int distW;
        int distH;

        int srcX = count * width;
        int srcY = dir * height;
        int srcW = srcX + width;
        int srcH = srcY + height;

        if (isWalking) {
            update();
            switch (dir) {
                case LEFT:
                    movingX -= step;
                    if (movingX < destX) {
                        isWalking = false;
                        x--;
                    } else {
                        distX = movingX + offsetX;
                    }
                    break;
                case RIGHT:
                    movingX += step;
                    if (movingX > destX) {
                        isWalking = false;
                        x++;
                    } else {
                        distX = movingX + offsetX;
                    }
                    break;
                case UP:
                    movingY -= step;
                    if (movingY < destY) {
                        isWalking = false;
                        y--;
                    } else {
                        distY = movingY + offsetY;
                    }
                    break;
                case DOWN:
                    movingY += step;
                    if (movingY > destY) {
                        isWalking = false;
                        y++;
                    } else {
                        distY = movingY + offsetY;
                    }
                    break;
                default:
                    break;
            }
        }

        if (!isWalking) {
            distX = x * MainScreen.CS + offsetX;
            distY = y * MainScreen.CS + offsetY;
            if (!isAnimating)
                count = 0;
        }
        distW = distX + width;
        distH = distY + height;

        super.draw(g, distX, distY, distW, distH, srcX, srcY, srcW, srcH);
    }

    @Override
    public void update() {
        if (count == 3) {
            count = 0;
        } else {
            count++;
        }
    }

    public void drawAnimation(LGraphics g, int offsetX, int offsetY, int counts) {
        isAnimating = true;
        this.draw(g, offsetX - ANIM_OFFSET_X, offsetY - ANIM_OFFSET_Y);

        if (counts == this.getCount()) {
            this.setCount(0);
            isAnimating = false;
        } else {
            this.setCount(this.getCount() + 1);
        }
    }
}
