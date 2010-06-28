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
import org.loon.framework.android.game.utils.GraphicsUtils;

import android.graphics.Color;
import android.view.KeyEvent;
import android.view.MotionEvent;

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
    private LImage heroBarBg;
    private StatusBar enemyHPBar;
    private LImage enemyIcon;
    private LImage enemyHPBg;

    private Hero fightingHero;
    private Enemy fightingEnemy;

    public LinkedList<Sprite> sprites; // 精灵

    private ActionKey goLeftKey;
    private ActionKey goRightKey;
    private ActionKey goUpKey;
    private ActionKey goDownKey;

    private LTimer delay;

    private boolean isDead = false;
    private boolean heroFighting = false;
    private boolean enemyFighting = false;
    private int lostHP = -1;
    private int HPx, HPy, step1 = MainScreen.CS / 10;
    private Enemy enemyRef;
    private boolean initDone = false;

    public MainScreen() {
        levelNo = 1;
        init();
    }

    private void init() {
        initDone = false;
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
        
//        heroBarBg = GraphicsUtils.loadImage("assets/images/herobarbg.png");
        heroHPBar = new TitledAndBorderedStatusBar(hero.getHp(), hero.getMaxHP(), 34, 7, 80, 5, "HP");
        heroHPBar.setShowHP(true);
        heroEXPBar = new TitledAndBorderedStatusBar(0, hero.getEXPtoNextLevel(), 34, 22, 80, 4, "EXP");
        heroEXPBar.setShowHP(true);
        heroEXPBar.setColor(LColor.yellow);
        
//        enemyHPBg = GraphicsUtils.loadImage("assets/images/enemybarbg.png");
        
        sprites = level.getSprites();
        initDone = true;
        // delay = new LTimer(50);

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
                    && y > level.firstTileY && y < level.lastTileY)
                sprite.draw(g, offsetX, offsetY);
        }

        g.setColor(Color.WHITE);
        g.setAntiAlias(true);
        
//        g.drawImage(heroBarBg, 0, 0, 137, 32, 0, 0, 150, 35);
        g.drawImage(hero.getImg(), (CS - hero.getWidth()) / 2, (CS - hero
                .getHeight()) / 2,
                (CS - hero.getWidth()) / 2 + hero.getWidth(), (CS - hero
                        .getHeight())
                        / 2 + hero.getHeight(), 0, 0, hero.getWidth(), hero
                        .getHeight());
        heroHPBar.setValue(hero.getHp());
        heroHPBar.setUpdate(hero.getHp());
        heroHPBar.createUI(g);
        heroEXPBar.setValue(hero.getExp());
        heroEXPBar.setUpdate(hero.getExp());
        heroEXPBar.createUI(g);

        if (enemyRef != null) {
//            g.drawString("Enemy HP: " + enemyRef.getHp(), 200, 20);

            g.drawImage(enemyIcon, 150, 0, 150 + enemyIcon.getWidth() / 4, 32,
                    0, 0, enemyIcon.getWidth() / 4, 32);
//            g.drawImage(enemyHPBg, 190, 10, 190 + 111, 17, 0, 0, 111, 7);
            enemyHPBar.setValue(enemyRef.getHp());
            enemyHPBar.setUpdate(enemyRef.getHp());
            enemyHPBar.createUI(g);
        }
        g.drawString("Level: ", 5, HEIGHT - 10);
        g.drawString(Integer.toString(levelNo), 40, HEIGHT - 10);
//        g.drawRect(100 - 1,HEIGHT - 50, 150 + 2, HEIGHT - 20);
        g.setAntiAlias(false);

        if (fightingHero != null && heroFighting) {
            fightingHero.drawAnimation(g, offsetX, offsetY, 19);
            fightingEnemy.setCount(0);
            fightingEnemy.drawAnimation(g, offsetX, offsetY, 0);
        }
        if (fightingEnemy != null && enemyFighting) {
            fightingEnemy.drawAnimation(g, offsetX, offsetY, 19);
            fightingHero.setCount(0);
            fightingHero.drawAnimation(g, offsetX, offsetY, 0);
        }

        if (lostHP != -1) {
            drawLostHP(g);
        }

        if (isDead) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 160, 320, 160);
            g.setColor(Color.WHITE);
            g.drawString("死了！！！", 150, 240);
        }
    }

    private void drawLostHP(LGraphics g) {
        g.setColor(Color.WHITE);
        g.setAntiAlias(true);
        g.drawString(lostHP + "", HPx, HPy);
        g.setAntiAlias(false);
        HPy -= step1;
    }

    public void alter(LTimerContext timer) {
        // if (delay.action(timer.getTimeSinceLastUpdate())) {
        if (isDead)
            return;

        // role.update(timer.getTimeSinceLastUpdate());

        if (fightingHero != null) {
            fight(hero, enemyRef);
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
                init();
            }
        }

        Iterator<Sprite> it = sprites.iterator();
        while (it.hasNext()) {
            Sprite sprite = it.next();
            // 更新所有精灵状态
            sprite.updateStatus(timer.getTimeSinceLastUpdate());

            if ((fightingHero == null) && hero.isCollision(sprite)
                    && (sprite instanceof Enemy) && !isDead) {
                Enemy enemy = (Enemy) sprite;
                enemyRef = enemy;

                if (fightingHero == null) {
                    fightingHero = new Hero("assets/images/anim_herofight.png",
                            hero.getXs(), hero.getYs(), 30, 32, map);
                    fightingHero.setDir(0);
                    heroFighting = true;
                    enemyFighting = false;
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
                    fightingEnemy.setDir(0);
                }

                
                enemyIcon = enemy.getImg();
                enemyHPBar = new TitledAndBorderedStatusBar(enemy.getHp(), enemy.getMaxHP(), 180, 13, 80, 5, "HP");
                enemyHPBar.setShowHP(true);
                
                hero.setShow(false);
                enemy.setShow(false);

                fight(hero, enemy);

                goRightKey.reset();
                goLeftKey.reset();
                goUpKey.reset();
                goDownKey.reset();
                continue;
            }
        }

    }

    private void fight(Hero hero, Enemy enemy) {
        if (heroFighting) {
            if (fightingHero.getCount() == 9) {
                lostHP = hero.getAttack() - enemy.getDefence();
                if (lostHP < 0)
                	lostHP = 0;
                enemy.setHp(enemy.getHp() - lostHP);
                HPx = fightingEnemy.getXs() * CS + enemy.getWidth() / 2
                        - fightingEnemy.ANIM_OFFSET_X;
                HPy = fightingEnemy.getYs() * CS + CS / 2;
            }
            if (fightingHero.getCount() == 19) {
                lostHP = -1;
                if (enemy.getHp() <= 0) {
                    hero.setShow(true);
                    hero.addExp(enemy.getExp());
                    sprites.remove(enemy);
                    fightingHero = null;
                    fightingEnemy = null;
                    enemyRef = null;
                    heroFighting = false;
                    enemyFighting = false;
                } else {
                    heroFighting = false;
                    enemyFighting = true;
                }
            }
        } else {
            if (enemyFighting) {
                if (fightingEnemy.getCount() == 9) {
                    lostHP = enemy.getAttack() - hero.getDefence();
                    if (lostHP < 0)
                    	lostHP = 0;
                    hero.setHp(hero.getHp() - lostHP);
                    HPx = fightingHero.getXs() * CS + CS
                            / 2 - fightingHero.ANIM_OFFSET_X;
                    HPy = fightingHero.getYs() * CS + CS
                            / 2;
                }
                if (fightingEnemy.getCount() == 19) {
                    lostHP = -1;
                    if (hero.getHp() <= 0) {
                        hero.setShow(false);
                        enemy.setShow(true);
                        isDead = true;
                        fightingHero = null;
                        fightingEnemy = null;
                        enemyRef = null;
                        heroFighting = false;
                        enemyFighting = false;
                    } else {
                        heroFighting = true;
                        enemyFighting = false;
                    }
                }
            }
        }
    }

    public boolean onKeyDown(int arg0, KeyEvent arg1) {
        if (arg0 == KeyEvent.KEYCODE_ENTER && isDead) {
            init();
            return false;
        }

        if (fightingHero != null)
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
        if (fightingHero != null)
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
        return false;
    }

    @Override
    public boolean onTouchMove(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onTouchUp(MotionEvent arg0) {
        return false;
    }

    class ActionKey {

        static final int PRESS_ONLY = 1, STATE_RELEASED = 0, STATE_PRESSED = 1,
                STATE_WAITING_FOR_RELEASE = 2;

        int mode, amount, state;

        public ActionKey() {
            this(0);
        }

        public ActionKey(int mode) {
            this.mode = mode;
            reset();
        }

        public void reset() {
            state = STATE_RELEASED;
            amount = 0;
        }

        public void press() {
            if (state != STATE_WAITING_FOR_RELEASE) {
                amount++;
                state = STATE_PRESSED;
            }
        }

        public void release() {
            state = STATE_RELEASED;
        }

        public boolean isPressed() {
            if (amount != 0) {
                amount--;
                if (state == STATE_RELEASED) {
                    amount = 0;
                } else if (mode == PRESS_ONLY) {
                    state = STATE_WAITING_FOR_RELEASE;
                    amount = 0;
                }
                return true;
            }
            return false;
        }
    }

}