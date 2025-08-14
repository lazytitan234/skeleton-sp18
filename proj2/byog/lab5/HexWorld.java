package byog.lab5;
import org.junit.Test;
import static org.junit.Assert.*;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);

    //adds a hexagon with side length of length, where the top left block has coordinates xCor, yCor
    //takes in an array of tiles, and populate this array
    private static void addHexagon(TETile[][] world, int length, int xCor, int yCor, TETile tile) {
        //check if this hexagon can be placed at this position in the world
        if (positionAllowed(length, xCor, yCor) && length > 1) {
            drawTopHalf(world, length, length + (length - 1) * 2, xCor, yCor, tile);
            drawBottomHalf(world, length + (length - 1) * 2, length, xCor - (length - 1), yCor - length, tile);
        }
        return;
    }

    private static boolean positionAllowed(int length, int xCor, int yCor) {
        if (yCor >= HEIGHT || yCor + 1 - length * 2 < 0) {
            return false;
        }
        if (xCor - length + 1 < 0 || xCor + length - 1 + length > WIDTH) {
            return false;
        }
        return true;
    }

    private static void drawTopHalf(TETile[][] world, int length, int target, int xCor, int yCor, TETile tile) {
        for (int i = xCor; i < xCor + length; i++) {
            world[i][yCor] = tile;
        }
        if (length == target) {
            return;
        }
        drawTopHalf(world, length + 2, target, xCor - 1, yCor - 1, tile);
    }

    private static void drawBottomHalf(TETile[][] world, int length, int target, int xCor, int yCor, TETile tile) {
        for (int i = xCor; i < xCor + length; i++) {
            world[i][yCor] = tile;
        }
        if (length == target) {
            return;
        }
        drawBottomHalf(world, length - 2, target, xCor + 1, yCor - 1, tile);
    }

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);
        TETile[][] hexTiles = new TETile[WIDTH][HEIGHT];

        for (int x = 0; x < WIDTH; x += 1) {

            for (int y = 0; y < HEIGHT; y += 1) {
                hexTiles[x][y] = Tileset.NOTHING;
            }
        }

        addHexagon(hexTiles, 3, 35, 38, Tileset.FLOWER);
        ter.renderFrame(hexTiles);
    }
}


