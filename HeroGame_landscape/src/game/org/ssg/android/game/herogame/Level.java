package org.ssg.android.game.herogame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;

import org.loon.framework.android.game.core.resource.Resources;
import org.ssg.android.game.herogame.control.BackGroundMap;
import org.ssg.android.game.herogame.db.DBManager;

public class Level {
    
    private LinkedList<Sprite> sprites;
    private Hero hero;
    public int[] heroPos = new int[2];
    
    public void setHero(Hero hero) {
        this.hero = hero;
    }

    public Hero getHero() {
        return hero;
    }

    public LinkedList<Sprite> getSprites() {
        return sprites;
    }

    private int levelNo;
    private int col, row;
    
    private static final String LEVEL_FILE_NAME = "assets/maps/floor";
    private static final String SKELEON_IMAGE = "assets/images/skeleon.png";
    private static final String MAGE_IMAGE = "assets/images/mage.png";
    private static final String SHOP_IMAGE = "assets/images/npc.png";
    private static final String BOX_IMAGE = "assets/images/box.png";
    
    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    private BackGroundMap backGroundMap;
    public BackGroundMap getBackGroundMap() {
        return backGroundMap;
    }

    private char[][] backGroundArray, creaturesArray;

    public Level(int levelNo) {		//自动存档的Level，需要按照地图初始化
        this.levelNo = levelNo;
        loadLevel(LEVEL_FILE_NAME + levelNo);
        
        backGroundMap = new BackGroundMap(backGroundArray, col, row);
        sprites = new LinkedList<Sprite>();
       
        initSpriteFromMap();//从地图内获取英雄、怪等信息
    }
    
    public Level(String archivingName,int levelNo) {	//肯定不是自动存档的Level
        this.levelNo = levelNo;
        loadLevel(LEVEL_FILE_NAME + levelNo);
        
        backGroundMap = new BackGroundMap(backGroundArray, col, row);
        sprites = new LinkedList<Sprite>();
        
        //从存档内获取英雄、怪等信息
        initSpriteFromArchiving(archivingName);
    }
    
    private void initSpriteFromArchiving(String archivingName) {	//初始化英雄、怪等信息
        calcTileXY(0, 0);
        Hero hero = DBManager.loadHero(archivingName);
        Enemy[] enemys = DBManager.loadEnemys(levelNo, archivingName);
        
        for(int i=0;i<enemys.length;i++){
        	sprites.add(enemys[i]);	
        }
        
        heroPos[0] = hero.x;
        heroPos[1] = hero.y;
    }
    
    private void initSpriteFromMap() {	//初始化英雄、怪等信息
        calcTileXY(0, 0);
        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                
                switch (creaturesArray[i][j]) {
                    case 's':
                    	Enemy enemy_s = new Enemy(SKELEON_IMAGE, j, i, 28, 32, this, 50, 5, 1, "skeleon");
                    	enemy_s.setExp(30);
                        sprites.add(enemy_s);
                        break;
                    case 'm':
                    	Enemy enemy_m = new Enemy(MAGE_IMAGE, j, i, 32, 32, this, 40, 10, 0, "mage");
                    	enemy_m.setExp(40);
                        sprites.add(enemy_m);
                        break;
                    case 'h':
                        heroPos[0] = j;
                        heroPos[1] = i;
                        break;
                    case 'n':
                    	NPC npc = new NPC(SHOP_IMAGE, j, i, 32, 32, this, "shop");
                    	npc.addAction("你好，欢迎来到商店！");
                    	npc.addAction("恩恩，快把东西给我瞧瞧！");
                    	npc.addAction("呵呵，好的。");
                    	npc.addAction("谢啦～");
                        sprites.add(npc);
                        break;
                    case 'b':
                    	NPC box = new NPC(BOX_IMAGE, j, i, 32, 32, this, "box");
                        sprites.add(box);
                        break;
                }
            }
        }
    }
    
    //包括地图、英雄、怪等信息
    private void loadLevel(final String fileName){
        InputStream in = Resources.getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = null;

        try {
            result = reader.readLine();
            if (result != null) {
                String[] mapSize = result.split(" ");
                row = Integer.parseInt(mapSize[0]);
                col = Integer.parseInt(mapSize[1]);
            }
            
            backGroundArray = new char[row][col];
            creaturesArray = new char[row][col];
            int flag = 0;
            int tempRow = 0;
            while ((result = reader.readLine()) != null) {
                if (!"".equals(result)) {
                    if (result.equals("#Terrain")) {
                        flag = 1;
                        tempRow = 0;
                        continue;
                    } else {
                        if (result.equals("#Creature")) {
                            flag = 2;
                            tempRow = 0;
                            continue;
                        }
                    }
                    String[] stringArray = result.split(" ");
                    int size = stringArray.length;
                    char[] charArray = new char[size];
                    for (int i = 0; i < size; i++) {
                        charArray[i] = stringArray[i].charAt(0);
                    }
                    if (flag == 1) {
                        backGroundArray[tempRow++] = charArray;
                    } else {
                        creaturesArray[tempRow++] = charArray;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    reader = null;
                } catch (IOException ex) {
                }
            }
        }
    }

    public static int pixelsToTiles(double pixels) {
        return (int) Math.floor(pixels / MainScreen.CS);
    }

    public static int tilesToPixels(int tiles) {
        return tiles * MainScreen.CS;
    }

    public int firstTileX;
    public int lastTileX;
    public int firstTileY;
    public int lastTileY;
    
    public void calcTileXY(int offsetX, int offsetY) {
        firstTileX = pixelsToTiles(-offsetX);
        lastTileX = firstTileX + pixelsToTiles(MainScreen.instance.WIDTH) + 1;

//        lastTileX = Math.min(lastTileX, col);
        lastTileX = col;

        firstTileY = pixelsToTiles(-offsetY);
        lastTileY = firstTileY + pixelsToTiles(MainScreen.instance.HEIGHT) + 1;

//        lastTileY = Math.min(lastTileY, row);
        lastTileY = row;
    }
   
    public boolean isAllow(int x, int y) {
        if (backGroundArray[y][x] == '1') {
            return false;
        }
        return true;
    }
    
    private static final char[] NPCs = new char[] {
        'n', 'b', 'i'};
    
    public NPC isNPC(int x, int y) {
    	boolean flag = false;
    	for (int i = 0; i < NPCs.length; i++) {
    		if (creaturesArray[y][x] == NPCs[i]) {
    			flag = true;
    			continue;
    		}
    	}
        if (flag) {
        	for (Iterator<Sprite> it = sprites.iterator(); it.hasNext();) {
    			Sprite sprite = it.next();
    			if (x == sprite.x && y == sprite.y)
    				return (NPC) sprite;
        	}
        	return null;
        }
        return null;
    }
}
