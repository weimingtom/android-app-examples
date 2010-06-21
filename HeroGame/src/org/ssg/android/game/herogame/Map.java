package org.ssg.android.game.herogame;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.utils.GraphicsUtils;

/**
 * 
 * @author chenpeng
 * @email  ceponline@yahoo.com.cn
 * 
 * Loon Framework in Game 
 *
 */
public class Map{

    private static final int ROW = 15;
    private static final int COL = 10;
    
    public static final int WIDTH = COL * MainScreen.CS;
    public static final int HEIGHT = ROW * MainScreen.CS;

    private int[][] map = {
            {1,1,1,1,1,1,1,1,1,1},
            {1,0,0,1,1,1,1,0,0,1},
            {1,0,0,0,0,0,0,0,0,1},
            {1,0,0,1,1,1,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,0,1},
            {1,0,0,0,0,0,1,0,2,1},
            {1,1,1,1,1,1,1,1,1,1}};

    private LImage floorImage;
    private LImage wallImage;
    private LImage doorImage;

    public Map() {
        floorImage = GraphicsUtils.loadImage("assets/floor.gif");
        wallImage = GraphicsUtils.loadImage("assets/wall.gif");
        doorImage = GraphicsUtils.loadImage("assets/door.png");
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

    public void draw(LGraphics g, int offsetX, int offsetY) {

        firstTileX = pixelsToTiles(-offsetX);
        lastTileX = firstTileX + pixelsToTiles(MainScreen.WIDTH) + 1;

        lastTileX = Math.min(lastTileX, COL);

        firstTileY = pixelsToTiles(-offsetY);
        lastTileY = firstTileY + pixelsToTiles(MainScreen.HEIGHT) + 1;

        lastTileY = Math.min(lastTileY, ROW);
        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                switch (map[i][j]) {
                    case 0:
                        g.drawImage(floorImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                    case 1:
                        g.drawImage(wallImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                    case 2:
                        g.drawImage(doorImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                }
            }
        }

    }

    public boolean isAllow(int x, int y) {
        if (map[y][x] == 1) {
            return false;
        }

        return true;
    }
}
