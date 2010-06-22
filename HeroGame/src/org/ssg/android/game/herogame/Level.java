package org.ssg.android.game.herogame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.loon.framework.android.game.core.resource.Resources;

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
    
    public Level(int levelNo) {
        this.levelNo = levelNo;
        loadLevel(LEVEL_FILE_NAME + levelNo);
        
        backGroundMap = new BackGroundMap(backGroundArray, col, row);
        sprites = new LinkedList<Sprite>();
        init();
    }
    
    public void init() {
        calcTileXY(0, 0);
        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                
                switch (creaturesArray[i][j]) {
                    case 's':
                        sprites.add(new Enemy(SKELEON_IMAGE, j, i, 28, 32, backGroundMap, 50, 5, 1));
                        break;
                    case 'm':
                        sprites.add(new Enemy(MAGE_IMAGE, j, i, 32, 32, backGroundMap, 40, 10, 0));
                        break;
                    case 'h':
                        heroPos[0] = j;
                        heroPos[1] = i;
                        break;
                }
            }
        }
    }
    
    public void loadLevel(final String fileName){
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
    
    private void calcTileXY(int offsetX, int offsetY) {
        firstTileX = pixelsToTiles(-offsetX);
        lastTileX = firstTileX + pixelsToTiles(MainScreen.WIDTH) + 1;

        lastTileX = Math.min(lastTileX, col);

        firstTileY = pixelsToTiles(-offsetY);
        lastTileY = firstTileY + pixelsToTiles(MainScreen.HEIGHT) + 1;

        lastTileY = Math.min(lastTileY, row);
    }
   
}
