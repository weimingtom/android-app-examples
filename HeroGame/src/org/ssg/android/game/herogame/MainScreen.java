package org.ssg.android.game.herogame;

import java.util.Iterator;
import java.util.LinkedList;

import org.loon.framework.android.game.action.sprite.StatusBar;
import org.loon.framework.android.game.core.graphics.LColor;
import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.graphics.Screen;
import org.loon.framework.android.game.core.timer.LTimer;
import org.loon.framework.android.game.core.timer.LTimerContext;
import org.ssg.android.game.herogame.control.ActionKey;
import org.ssg.android.game.herogame.control.BackGroundMap;
import org.ssg.android.game.herogame.control.Dialog;
import org.ssg.android.game.herogame.control.DirectionGameController;
import org.ssg.android.game.herogame.control.HeroStatusDialog;
import org.ssg.android.game.herogame.control.InfoBox;
import org.ssg.android.game.herogame.control.OnTouchListener;
import org.ssg.android.game.herogame.control.TitledAndBorderedStatusBar;
import org.ssg.android.game.herogame.control.ToolBar;
import org.ssg.android.game.herogame.control.Touchable;
import org.ssg.android.game.herogame.db.ConstantUtil;
import org.ssg.android.game.herogame.db.DBManager;
import org.ssg.android.game.herogame.util.BattleUtil;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class MainScreen extends Screen {
	private static final long serialVersionUID = 1L;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 480;
	public static final int CS = 32;
	private Level level;
	private int levelNo;
	private BackGroundMap map;
	private Hero hero;
	private StatusBar heroHPBar;
	private StatusBar heroEXPBar;
	private StatusBar enemyHPBar;
	private LImage enemyIcon;

	private Hero fightingHero;
	private Enemy fightingEnemy;

	public LinkedList<Sprite> sprites; // 精灵

	public ActionKey goLeftKey;
	public ActionKey goRightKey;
	public ActionKey goUpKey;
	public ActionKey goDownKey;

	private boolean isDead = false;
	public boolean heroFighting = false;
	public boolean enemyFighting = false;
	private int step1 = MainScreen.CS / 10;
	private Enemy enemyRef;
	private boolean initDone = false;

	private InfoBox infoBox;

	private Dialog dialog;

	private ToolBar toolbar;

	private DirectionGameController gameController;

	private LinkedList<Touchable> touchables;

	public Dialog topDialog, defaultTopDialog;

	public static MainScreen instance;
	
	private static Object _lock = new Object();
	private static volatile boolean _drawing;

	// private String archivingId; // 存档ID
	private String archivingName;// 存档名称

	// public String getArchivingId() {
	// return archivingId;
	// }
	//
	// public void setArchivingId(String archivingId) {
	// this.archivingId = archivingId;
	// }

	public String getArchivingName() {
		return archivingName;
	}

	public void setArchivingName(String archivingName) {
		this.archivingName = archivingName;
	}

	public MainScreen(String archivingName) {
		this.archivingName = archivingName;
		levelNo = 1;
		instance = this;
		init();
	}

	private void init() {
		initDone = false;

		levelInit();

		fightingHero = null;
		fightingEnemy = null;
		if (isDead)
			levelNo = 1;
		if (goLeftKey == null)
			goLeftKey = new ActionKey();
		else
			goLeftKey.reset();
		if (goRightKey == null)
			goRightKey = new ActionKey();
		else
			goRightKey.reset();
		if (goUpKey == null)
			goUpKey = new ActionKey();
		else
			goUpKey.reset();
		if (goDownKey == null)
			goDownKey = new ActionKey();
		else
			goDownKey.reset();

		heroHPBar = new TitledAndBorderedStatusBar(hero.getHp(), hero
				.getMaxHP(), 34, 7, 80, 5, "HP");
		heroHPBar.setShowHP(true);
		heroEXPBar = new TitledAndBorderedStatusBar(0,
				hero.getEXPtoNextLevel(), 34, 22, 80, 4, "EXP");
		heroEXPBar.setShowHP(true);
		heroEXPBar.setColor(LColor.yellow);

		enemyHPBar = new TitledAndBorderedStatusBar(1, 1, 180, 13, 80, 5, "HP");
		enemyHPBar.setShowHP(true);

		infoBox = new InfoBox();

		dialog = new HeroStatusDialog(hero);
		addTouchable(dialog);

		toolbar = new ToolBar(0, 450, 320, 30);
		addTouchable(toolbar);

		gameController = new DirectionGameController(0, 48);
		addTouchable(gameController);
		topDialog = null;
		defaultTopDialog = null;

		initDone = true;
	}

	private void levelInit() {

		// 自动存档，需要按照地图初始化，并将信息存入自动存档内
		if (archivingName.equals(ConstantUtil.autoSaveArchivingName)) {
			level = new Level(levelNo);
			map = level.getBackGroundMap();

			if (hero == null || isDead) {
				hero = new Hero("assets/images/hero.png", 1, 1, 20, 32, map);
				isDead = false;
			} else {
				hero.setMap(map);
			}
			hero.setXs(level.heroPos[0]);
			hero.setYs(level.heroPos[1]);

			sprites = level.getSprites();

			// 存入存档信息：自动存档，不需要单独调用设置“存档信息”的方法

			// 存入英雄信息
			DBManager.saveHero(hero);

			// 存入怪的信息
			for (int i = 0; i < sprites.size(); i++) {
				if (sprites.get(i) instanceof Enemy) {
					Enemy[] enemys = new Enemy[1];
					enemys[0] = (Enemy) (sprites.get(i));
					DBManager.saveEnemys(levelNo, enemys);
				}
			}

		} else {// 按照存档初始化
			level = new Level(levelNo);
			map = level.getBackGroundMap();

			if (hero == null || isDead) {
				hero = new Hero("assets/images/hero.png", 1, 1, 20, 32, map);
				isDead = false;
			} else {
				hero.setMap(map);
			}
			hero.setXs(level.heroPos[0]);
			hero.setYs(level.heroPos[1]);

			sprites = level.getSprites();

			// 存入存档信息：自动存档，不需要单独调用设置“存档信息”的方法

			// 存入英雄信息
			DBManager.saveHero(hero);

			// 存入怪的信息
			for (int i = 0; i < sprites.size(); i++) {
				if (sprites.get(i) instanceof Enemy) {
					Enemy[] enemys = new Enemy[1];
					enemys[0] = (Enemy) (sprites.get(i));
					DBManager.saveEnemys(levelNo, enemys);
				}
			}
		}
	}

    public static void checkLock(){
        synchronized (_lock) {
            while (_drawing) {
                try {
					_lock.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            }
        }
    }
    
    public static void beginDrawing() {
    	_drawing = true;
    }
    
    public static void endDrawing() {
        synchronized (_lock) {
            _drawing = false;
            _lock.notifyAll();
        }
    }
    
	@Override
	public void draw(LGraphics g) {
		if (!initDone)
			return;
		
		int offsetX = WIDTH / 2 - hero.getXs() * CS;
		offsetX = Math.min(offsetX, 0);
		offsetX = Math.max(offsetX, WIDTH - map.getWidth());

		int offsetY = HEIGHT / 2 - hero.getYs() * CS;
		offsetY = Math.min(offsetY, 0);
		offsetY = Math.max(offsetY, HEIGHT - map.getHeight());
		map.draw(g, offsetX, offsetY);

		hero.draw(g, offsetX, offsetY);

		for (Iterator<Sprite> it = sprites.iterator(); it.hasNext();) {
			Sprite sprite = it.next();
			int x = sprite.getXs();
			int y = sprite.getYs();
			if (x > level.firstTileX && x < level.lastTileX
					&& y > level.firstTileY && y < level.lastTileY) {
				sprite.draw(g, offsetX, offsetY);

				if (fightingHero == null && x == infoBox.getCurX()
						&& y == infoBox.getCurY() && sprite instanceof Enemy
						&& infoBox.isVisible()) {
					g.drawImage(sprite.getImg(), 150, 0, 150 + sprite.getImg()
							.getWidth() / 4, 32, 0, 0, sprite.getImg()
							.getWidth() / 4, 32);

					enemyHPBar.setMaxValue(((Enemy) sprite).getMaxHP());
					enemyHPBar.setValue(((Enemy) sprite).getHp());
					enemyHPBar.setUpdate(((Enemy) sprite).getHp());
					enemyHPBar.createUI(g);
					
					infoBox.draw(g);
				}
			}
		}

		g.setColor(Color.WHITE);
		g.setAntiAlias(true);
		g.drawImage(hero.getImg(), (CS - hero.getWidth()) / 2, (CS - hero
				.getHeight()) / 2,
				(CS - hero.getWidth()) / 2 + hero.getWidth(), (CS - hero
						.getHeight())
						/ 2 + hero.getHeight(), 0, 0, hero.getWidth(), hero
						.getHeight());
		heroEXPBar.setMaxValue(hero.getMaxHP());
		heroHPBar.setValue(hero.getHp());
		heroHPBar.setUpdate(hero.getHp());
		heroHPBar.createUI(g);
		heroEXPBar.setMaxValue(hero.getEXPtoNextLevel());
		heroEXPBar.setValue(hero.getExp());
		heroEXPBar.setUpdate(hero.getExp());
		heroEXPBar.createUI(g);

		if (enemyRef != null) {
			g.drawImage(enemyIcon, 150, 0, 150 + enemyIcon.getWidth() / 4, 32,
					0, 0, enemyIcon.getWidth() / 4, 32);
			enemyHPBar.setValue(enemyRef.getHp());
			enemyHPBar.setUpdate(enemyRef.getHp());
			enemyHPBar.createUI(g);
		}
		g.drawString("Level: ", 5, HEIGHT - 10);
		g.drawString(Integer.toString(levelNo), 40, HEIGHT - 10);
		g.setAntiAlias(false);

		if (fightingHero != null && heroFighting) {
			fightingHero.drawFightingAnim(g, offsetX, offsetY);
			drawLostHP(g, fightingHero);
		}
		if (fightingEnemy != null && enemyFighting) {
			fightingEnemy.drawFightingAnim(g, offsetX, offsetY);
			drawLostHP(g, fightingEnemy);
		}

		gameController.draw(g);

		beginDrawing();
		if (topDialog != null) {
			g.setColor(LColor.black);
			g.setAlpha(0.2f);
			g.fillRect(0, 0, WIDTH, HEIGHT);
			g.setColor(LColor.white);
			g.setAlpha(1.0f);
			topDialog.draw(g);
		}
		endDrawing();
		
		toolbar.draw(g);
		
		if (isDead) {
			g.setColor(Color.BLACK);
			g.fillRect(0, 160, 320, 160);
			g.setColor(Color.WHITE);
			g.drawString("死了！！！", 150, 240);
		}

	}

	private void drawLostHP(LGraphics g, Role role) {
		if (role == null || role.damage == -1 || role.frameNo == 10)
			return;

		g.setColor(Color.WHITE);
		g.setAntiAlias(true);
		g.drawString(role.damage + "", role.HPx, role.HPy);
		g.setAntiAlias(false);
		role.HPy -= step1;
		role.frameNo++;
	}

	public void alter(LTimerContext timer) {
		if (isDead || topDialog != null)
			return;

		if (heroFighting || enemyFighting) {
			fight(hero, enemyRef, timer.getTimeSinceLastUpdate());
		} else {
			if (goRightKey.isPressed()) {
				hero.move(Hero.RIGHT);
			} else if (goLeftKey.isPressed()) {
				hero.move(Hero.LEFT);
			} else if (goUpKey.isPressed()) {
				hero.move(Hero.UP);
			} else if (goDownKey.isPressed()) {
				hero.move(Hero.DOWN);
			}

			if (map.isDoor(hero.getXs(), hero.getYs())) {
				levelNo++;
				levelInit();
			}
		}

		Iterator<Sprite> it = sprites.iterator();
		while (it.hasNext()) {
			Sprite sprite = it.next();
			sprite.updateStatus(timer.getTimeSinceLastUpdate());

			if ((fightingHero == null) && hero.isCollision(sprite)
					&& (sprite instanceof Enemy) && !isDead) {
				Enemy enemy = (Enemy) sprite;
				enemyRef = enemy;

				if (fightingHero == null) {
					fightingHero = new Hero("assets/images/anim_herofight.png",
							hero.getXs(), hero.getYs(), 30, 32, map);
					fightingHero.timer = new LTimer(50);
					fightingHero.setDir(0);
					heroFighting = true;
					enemyFighting = true;
				}
				if (fightingEnemy == null) {
					if (enemy.getFileName().endsWith("assets/images/mage.png")) {
						fightingEnemy = new Enemy(
								"assets/images/anim_magefight.png", hero
										.getXs() + 1, hero.getYs(), 41, 32, map);
						fightingEnemy.ANIM_OFFSET_X = CS;
					} else {
						fightingEnemy = new Enemy(
								"assets/images/anim_skeleonfight.png", hero
										.getXs() + 1, hero.getYs(), 32, 32, map);
					}
					fightingEnemy.timer = new LTimer(100);
					fightingEnemy.setDir(0);
				}

				enemyIcon = enemy.getImg();
				enemyHPBar.setMaxValue(enemy.getMaxHP());
				enemyHPBar.setValue(enemy.getHp());

				hero.setShow(false);
				enemy.setShow(false);

				goRightKey.reset();
				goLeftKey.reset();
				goUpKey.reset();
				goDownKey.reset();
				continue;
			}
		}

	}

	private void fight(Hero hero, Enemy enemy, long elapsedTime) {
		if (fightingHero.updateFightingAnim(elapsedTime)) {
			if (fightingHero.getCount() == Hero.ANIM_HIT_FRAME) {
				fightingEnemy.damage = BattleUtil.attack(hero, enemy);
				if (fightingEnemy.damage < 0)
					fightingEnemy.damage = 0;
				enemy.setHp(enemy.getHp() - fightingEnemy.damage);
			}
			if (fightingHero.getCount() == Hero.ANIM_FINAL_FRAME) {
				fightingEnemy.damage = -1;
				fightingEnemy.resetHPxy();
				if (enemy.getHp() <= 0) {
					hero.setShow(true);
					hero.addExp(enemy.getExp());
					sprites.remove(enemy);
					fightingHero = null;
					fightingEnemy = null;
					enemyRef = null;
					heroFighting = false;
					enemyFighting = false;
					return;
				}
			}
		}
		if (fightingEnemy.updateFightingAnim(elapsedTime)) {
			if (fightingEnemy.getCount() == Enemy.ANIM_HIT_FRAME) {
				fightingHero.damage = BattleUtil.attack(enemy, hero);
				if (fightingHero.damage < 0)
					fightingHero.damage = 0;
				hero.setHp(hero.getHp() - fightingHero.damage);
			}
			if (fightingEnemy.getCount() == Enemy.ANIM_FINAL_FRAME) {
				fightingHero.damage = -1;
				fightingHero.resetHPxy();
				if (hero.getHp() <= 0) {
					hero.setShow(false);
					enemy.setShow(true);
					isDead = true;
					fightingHero = null;
					fightingEnemy = null;
					enemyRef = null;
					heroFighting = false;
					enemyFighting = false;
					return;
				}
			}
		}
	}

	public boolean onKeyDown(int arg0, KeyEvent arg1) {
		if (arg0 == KeyEvent.KEYCODE_ENTER && isDead) {
			init();
			return false;
		}

		if (heroFighting || topDialog != null)
			return false;

		switch (arg0) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			goLeftKey.press();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			goRightKey.press();
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			goUpKey.press();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			goDownKey.press();
			break;
		case KeyEvent.KEYCODE_A:
			goLeftKey.press();
			break;
		case KeyEvent.KEYCODE_D:
			goRightKey.press();
			break;
		case KeyEvent.KEYCODE_W:
			goUpKey.press();
			break;
		case KeyEvent.KEYCODE_S:
			goDownKey.press();
			break;
		}
		return false;
	}

	public boolean onKeyUp(int keyCode, KeyEvent e) {
		if (heroFighting || topDialog != null)
			return false;
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			goLeftKey.release();
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			goRightKey.release();
			break;
		case KeyEvent.KEYCODE_DPAD_UP:
			goUpKey.release();
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			goDownKey.release();
			break;
		case KeyEvent.KEYCODE_A:
			goLeftKey.release();
			break;
		case KeyEvent.KEYCODE_D:
			goRightKey.release();
			break;
		case KeyEvent.KEYCODE_W:
			goUpKey.release();
			break;
		case KeyEvent.KEYCODE_S:
			goDownKey.release();
			break;
		}
		return false;
	}

	@Override
	public boolean onTouchDown(MotionEvent arg0) {
		if (toolbar.isTouched()) {
			for (OnTouchListener listener : toolbar.getOnTouchListener()) {
				listener.onTouchDown(arg0);
			}
			return false;
		}
		
		if (topDialog != null) {
			if (topDialog.isTouched()) {
				for (OnTouchListener listener : topDialog.getOnTouchListener()) {
					listener.onTouchDown(arg0);
				}
				return false;
			}
		} else {
//			if (gameController.isTouched()) {
				for (OnTouchListener listener : gameController
						.getOnTouchListener()) {
					listener.onTouchDown(arg0);
				}
//				return false;
//			}
			setInfoBox();
		}
		return false;
	}

	public void setInfoBox() {
		if (heroFighting || enemyFighting)
			return;
		int curX, curY;
		curX = getTouchX() / 32;
		curY = getTouchY() / 32;
		if (infoBox.getCurX() == curX && infoBox.getCurY() == curY
				&& infoBox.isVisible()) {
			infoBox.setVisible(false);
		} else {
//			for (Iterator<Sprite> it = sprites.iterator(); it.hasNext();) {
//				Sprite sprite = it.next();
//				if (sprite.getXs() == curX && sprite.getYs() == curY) {
					infoBox.setCurX(curX);
					infoBox.setCurY(curY);
					infoBox.setVisible(true);
					return;
//				}
//			}
		}
	}
	
	@Override
	public boolean onTouchMove(MotionEvent arg0) {
		if (toolbar.isTouched()) {
			for (OnTouchListener listener : toolbar.getOnTouchListener()) {
				listener.onTouchMove(arg0);
			}
			return false;
		}
		
		if (topDialog != null) {
			if (topDialog.isTouched()) {
				for (OnTouchListener listener : topDialog.getOnTouchListener()) {
					listener.onTouchMove(arg0);
				}
				return false;
			}
		} else {
//			if (gameController.isTouched()) {
				for (OnTouchListener listener : gameController
						.getOnTouchListener()) {
					listener.onTouchMove(arg0);
				}
				return false;
//			}
		}

		return false;
	}

	@Override
	public boolean onTouchUp(MotionEvent arg0) {
		if (toolbar.isTouched()) {
			for (OnTouchListener listener : toolbar.getOnTouchListener()) {
				listener.onTouchUp(arg0);
			}
			return false;
		}
		
		if (topDialog != null) {
			if (topDialog.isTouched()) {
				for (OnTouchListener listener : topDialog.getOnTouchListener()) {
					listener.onTouchUp(arg0);
				}
				return false;
			}
		} else {
//			if (gameController.isTouched()) {
				for (OnTouchListener listener : gameController
						.getOnTouchListener()) {
					listener.onTouchUp(arg0);
				}
				return false;
//			}
		}
		return false;
	}

	public void addTouchable(Touchable touchable) {
		if (touchables == null) {
			touchables = new LinkedList<Touchable>();
		}
		touchables.add(touchable);
	}

	public void removeTouchable(Touchable touchable) {
		if (touchables != null) {
			touchables.remove(touchable);
		}
	}

	public static final int MESSAGE_UP_KEY_PRESSED = 0;
	public static final int MESSAGE_RIGHT_KEY_PRESSED = 1;
	public static final int MESSAGE_DOWN_KEY_PRESSED = 2;
	public static final int MESSAGE_LEFT_KEY_PRESSED = 3;

	private Handler _handler = new Handler() {
		@Override
		public void handleMessage(Message message) {
			switch (message.what) {
			case MESSAGE_UP_KEY_PRESSED:
				goUpKey.press();
				break;
			case MESSAGE_RIGHT_KEY_PRESSED:
				goRightKey.press();
				break;
			case MESSAGE_DOWN_KEY_PRESSED:
				goDownKey.press();
				break;
			case MESSAGE_LEFT_KEY_PRESSED:
				goLeftKey.press();
				break;
			default:
				break;
			}
		}
	};

	public void sendMessage(int code, String info) {
		_handler.sendMessage(_handler.obtainMessage(code, info));
	}

	public void sendMessageDelay(int code, int delayMillis) {
		_handler.sendMessageDelayed(_handler.obtainMessage(code), delayMillis);
	}
}