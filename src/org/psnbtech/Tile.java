package org.psnbtech;


import java.awt.*;
import java.util.Random;

enum Tile {
    TypeI(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX), new boolean[][]{
            {false, false, false, false},
            {true, true, true, true},
            {false, false, false, false},
            {false, false, false, false},

    }),
    TypeJ(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX), new boolean[][]{
            {true, false, false},
            {true, true, true},
            {false, false, false},

    }),
    TypeL(new Color(BoardPanel.COLOR_MAX, 127, BoardPanel.COLOR_MIN), new boolean[][]{
            {false, false, true},
            {true, true, true},
            {false, false, false},

    }),
    TypeO(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), new boolean[][]{
            {true, true},
            {true, true},

    }),
    TypeS(new Color(BoardPanel.COLOR_MIN, BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN), new boolean[][]{
            {false, true, true},
            {true, true, false},
            {false, false, false},

    }),
    TypeT(new Color(128, BoardPanel.COLOR_MIN, 128), new boolean[][]{
            {false, true, false},
            {true, true, true},
            {false, false, false},
    }),
    TypeZ(new Color(BoardPanel.COLOR_MAX, BoardPanel.COLOR_MIN, BoardPanel.COLOR_MIN), new boolean[][]{
            {true, true, false},
            {false, true, true},
            {false, false, false},
    });

    private static final Random random = new Random();

    private Color baseColor;
    private Color lightColor;
    private Color darkColor;
    private int spawnColumn;
    private int spawnRow;
    private int dimension;

    private boolean[][] tiles;

    Tile(Color baseColor, boolean[][] tiles) {
        this.baseColor = baseColor;
        this.lightColor = baseColor.brighter();
        this.darkColor = baseColor.darker();
        this.dimension = tiles.length;

        this.tiles = tiles;

        this.spawnColumn = 5 - (dimension >> 1);
        this.spawnRow = getTopInset(0);
    }

    public static Tile getRandomTile() {
        Tile[] values = Tile.values();
        return values[random.nextInt(values.length)];
    }

    public Color getBaseColor() {
        return baseColor;
    }

    public Color getLightColor() {
        return lightColor;
    }

    public Color getDarkColor() {
        return darkColor;
    }

    public int getDimension() {
        return dimension;
    }

    public int getSpawnColumn() {
        return spawnColumn;
    }

    public int getSpawnRow() {
        return spawnRow;
    }

    public boolean isTile(int x, int y, int rotation) {
        return getRotatedTile(rotation)[x][y];
    }

    public boolean[][] getRotatedTile(int rotation) {
        boolean[][] input = tiles;

        for (int i = 0; i < rotation; i++) {
            input = ArrayUtils.rotateArray(input);
        }

        return input;
    }

    public int getLeftInset(int rotation) {
        for (int x = 0; x < dimension; x++) {
            for (int y = 0; y < dimension; y++) {
                if (isTile(x, y, rotation)) {
                    return x;
                }
            }
        }
        return -1;
    }

    public int getRightInset(int rotation) {
        for (int x = dimension - 1; x >= 0; x--) {
            for (int y = 0; y < dimension; y++) {
                if (isTile(x, y, rotation)) {
                    return dimension - x;
                }
            }
        }
        return -1;
    }

    public int getTopInset(int rotation) {
        for (int y = 0; y < dimension; y++) {
            for (int x = 0; x < dimension; x++) {
                if (isTile(x, y, rotation)) {
                    return y;
                }
            }
        }
        return -1;
    }

    public int getBottomInset(int rotation) {
        for (int y = dimension - 1; y >= 0; y--) {
            for (int x = 0; x < dimension; x++) {
                if (isTile(x, y, rotation)) {
                    return dimension - y;
                }
            }
        }
        return -1;
    }
}
