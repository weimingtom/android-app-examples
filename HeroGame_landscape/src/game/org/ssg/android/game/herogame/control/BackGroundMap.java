package org.ssg.android.game.herogame.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.loon.framework.android.game.core.graphics.LGraphics;
import org.loon.framework.android.game.core.graphics.LImage;
import org.loon.framework.android.game.core.resource.Resources;
import org.loon.framework.android.game.utils.GraphicsUtils;
import org.ssg.android.game.herogame.MainScreen;

/**
 * @author chenpeng
 * @email ceponline@yahoo.com.cn Loon Framework in Game
 */
public class BackGroundMap {

    private int col, row;

    private int width, height;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private char[][] map;

    private LImage floorImage;
    private LImage wallImage;
    private LImage doorImage;

    private static final String FLOOR_IMAGE = "assets/images/floor.gif";
    private static final String WALL_IMAGE = "assets/images/wall.gif";
    private static final String DOOR_IMAGE = "assets/images/door.png";
    
    public BackGroundMap(char[][] map, int col, int row) {
        this.map = map;
        this.col = col;
        this.row = row;
        this.width = col * MainScreen.CS;
        this.height = row * MainScreen.CS;
        
        floorImage = GraphicsUtils.loadImage(FLOOR_IMAGE);
        wallImage = GraphicsUtils.loadImage(WALL_IMAGE);
        doorImage = GraphicsUtils.loadImage(DOOR_IMAGE);
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
        firstTileX = Math.max(firstTileX, 0);
        lastTileX = firstTileX + pixelsToTiles(MainScreen.instance.WIDTH) + 1;

        lastTileX = Math.min(lastTileX, col);

        firstTileY = pixelsToTiles(-offsetY);
        firstTileY = Math.max(firstTileY, 0);
        lastTileY = firstTileY + pixelsToTiles(MainScreen.instance.HEIGHT) + 1;

        lastTileY = Math.min(lastTileY, row);
        for (int i = firstTileY; i < lastTileY; i++) {
            for (int j = firstTileX; j < lastTileX; j++) {
                switch (map[i][j]) {
                    case '0':
                        g.drawImage(floorImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                    case '1':
                        g.drawImage(wallImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                    case '2':
                        g.drawImage(doorImage, tilesToPixels(j) + offsetX,
                                tilesToPixels(i) + offsetY);
                        break;
                }
            }
        }

    }

    public boolean isAllow(int x, int y) {
        if (map[y][x] == '1') {
            return false;
        }
        return true;
    }
    
    public boolean isDoor(int x, int y) {
        if (map[y][x] == '2') {
            return true;
        }
        return false;
    }
}
