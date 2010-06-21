package org.ssg.android.game.herogame;

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

/**
 * @author chenpeng
 * @email ceponline@yahoo.com.cn Loon Framework in Game
 */
public class CreaturesMap {

    private static final int ROW = 15;
    private static final int COL = 10;

    public static final int WIDTH = COL * MainScreen.CS;
    public static final int HEIGHT = ROW * MainScreen.CS;

    private char[][] map;

    private LImage floorImage;
    private LImage wallImage;
    private LImage doorImage;

    public CreaturesMap(char[][] map) {
        this.map = map;
        floorImage = GraphicsUtils.loadImage("assets/images/floor.gif");
        wallImage = GraphicsUtils.loadImage("assets/images/wall.gif");
        doorImage = GraphicsUtils.loadImage("assets/images/door.png");
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
        if (map[y][x] == 1) {
            return false;
        }

        return true;
    }
}
