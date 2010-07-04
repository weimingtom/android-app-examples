package org.ssg.android.game.herogame;

import org.loon.framework.android.game.action.map.RectBox;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.timer.LTimer;
import org.ssg.android.game.herogame.control.BackGroundMap;

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
	public static final int ANIM_HIT_FRAME = 9;
	public static final int ANIM_FINAL_FRAME = 18;
	private boolean isAnimating = false;
	private int level, exp;
	private static final int[] EXP_TO_LEVEL = { -1, 50, 60, 70, 80, 90, 100,
			110, 120 };
	private int availablePoints;

	public int getAvailablePoints() {
		return availablePoints;
	}

	public void setAvailablePoints(int availablePoints) {
		this.availablePoints = availablePoints;
	}

	public int getEXPtoNextLevel() {
		return EXP_TO_LEVEL[level];
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void addExp(int exp) {
		if (this.exp + exp >= getEXPtoNextLevel()) {
			this.exp = this.exp + exp - getEXPtoNextLevel();
			level++;
			availablePoints += 4;
		} else {
			this.exp += exp;
		}
	}

	public Hero(String filename, int x, int y, int w, int h, BackGroundMap map) {
		super(filename, x, y, w, h, map, 100, 10, 9);
		dir = DOWN;
		level = 1;
		exp = 0;
		availablePoints = 0;
		resetHPxy();
		
	    hit = 100;
		strength = 20;
		element_set = 100;
		dex = 20;
		agi = 20;
	}

	@Override
    public void resetHPxy() {
		HPx = getXs() * MainScreen.CS + MainScreen.CS / 2 - ANIM_OFFSET_X;
		HPy = getYs() * MainScreen.CS + MainScreen.CS / 2;
		frameNo = 0;
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

	public void drawFightingAnim(LGraphics g, int offsetX, int offsetY) {
		isAnimating = true;
		super.draw(g, offsetX - ANIM_OFFSET_X, offsetY - ANIM_OFFSET_Y);
	}
	
	public boolean updateFightingAnim(long elapsedTime) {
		if (timer.action(elapsedTime)) {
			if (ANIM_FINAL_FRAME == this.getCount()) {
				this.setCount(0);
				isAnimating = false;
			} else {
				this.setCount(this.getCount() + 1);
			}
			return true;
		}
		return false;
	}
}
