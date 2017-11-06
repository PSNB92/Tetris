package org.psnbtech;

import javax.swing.*;
import java.awt.*;

class BoardPanel extends JPanel {

    static final int COLOR_MIN = 35;
    static final int COLOR_MAX = 255 - COLOR_MIN;
    static final int COL_COUNT = 10;

    private static final int VISIBLE_ROW_COUNT = 20;
    private static final int HIDDEN_ROW_COUNT = 2;

    static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
    static final int SHADE_WIDTH = 4;
    static final int TILE_SIZE = 24;

    private static final int BORDER_WIDTH = 5;
    private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
    private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
    private static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);

    static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;

    private Tetris tetris;
    private Tile[][] tiles;

    BoardPanel(Tetris tetris) {
        this.tetris = tetris;
        this.tiles = new Tile[ROW_COUNT][COL_COUNT];

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
    }

    void clear() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                tiles[i][j] = null;
            }
        }
    }

    boolean isValidAndEmpty(Tile tile, int x, int y, int rotation) {
        if (notInValidColumn(tile, x, rotation)) {
            return false;
        }

        if (notInValidRow(tile, y, rotation)) {
            return false;
        }

        return doesNotConflictWithOtherTiles(tile, x, y, rotation);
    }

    private boolean doesNotConflictWithOtherTiles(Tile tile, int x, int y, int rotation) {
        for (int col = 0; col < tile.getDimension(); col++) {
            for (int row = 0; row < tile.getDimension(); row++) {
                if (tile.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean notInValidRow(Tile tile, int y, int rotation) {
        return y < -tile.getTopInset(rotation) || y + tile.getDimension() - tile.getBottomInset(rotation) >= ROW_COUNT;
    }

    private boolean notInValidColumn(Tile tile, int x, int rotation) {
        return x < -tile.getLeftInset(rotation) || x + tile.getDimension() - tile.getRightInset(rotation) >= COL_COUNT;
    }

    void setPiece(Tile tile, int x, int y, int rotation) {
        for (int col = 0; col < tile.getDimension(); col++) {
            for (int row = 0; row < tile.getDimension(); row++) {
                if (tile.isTile(col, row, rotation)) {
                    setTile(col + x, row + y, tile);
                }
            }
        }
    }

    int checkLinesAndReturnHowManyRemoved() {
        int completedLines = 0;

        for (int row = 0; row < ROW_COUNT; row++) {
            if (isRowFullAndRemoveIt(row)) {
                completedLines++;
            }
        }
        return completedLines;
    }

    private boolean isRowFullAndRemoveIt(int line) {
        for (int col = 0; col < COL_COUNT; col++) {
            if (!isOccupied(col, line)) {
                return false;
            }
        }

        for (int row = line - 1; row >= 0; row--) {
            for (int col = 0; col < COL_COUNT; col++) {
                setTile(col, row + 1, getTile(col, row));
            }
        }
        return true;
    }

    private boolean isOccupied(int x, int y) {
        return tiles[y][x] != null;
    }

    private void setTile(int x, int y, Tile tile) {
        tiles[y][x] = tile;
    }

    private Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.translate(BORDER_WIDTH, BORDER_WIDTH);

        if (tetris.isPaused()) {
            drawPausedScreen(g);
        } else if (tetris.isNewGame() || tetris.isGameOver()) {
            drawStartGameScreen(g);
        } else {
            drawGameScreen(g);
        }

        drawOutline(g);
    }

    private void drawGameScreen(Graphics g) {
        drawAllTiles(g);
        drawCurrentPiece(g);
        drawBackgroundGridAbovePieces(g);
    }

    private void drawStartGameScreen(Graphics g) {
        g.setFont(LARGE_FONT);
        g.setColor(Color.WHITE);

			/*
             * Because both the game over and new game screens are nearly identical,
			 * we can handle them together and just use a ternary operator to change
			 * the messages that are displayed.
			 */
        String msg = tetris.isNewGame() ? "TETRIS" : "GAME OVER";
        g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 150);
        g.setFont(SMALL_FONT);
        msg = "Press Enter to Play" + (tetris.isNewGame() ? "" : " Again");
        g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, 300);
    }

    private void drawPausedScreen(Graphics g) {
        g.setFont(LARGE_FONT);
        g.setColor(Color.WHITE);
        String msg = "PAUSED";
        g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
    }

    private void drawAllTiles(Graphics g) {
        for (int x = 0; x < COL_COUNT; x++) {
            for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
                Tile tile = getTile(x, y);
                if (tile != null) {
                    drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                }
            }
        }
    }

    private void drawCurrentPiece(Graphics g) {
        Tile tile = tetris.getPieceType();
        int pieceCol = tetris.getPieceCol();
        int pieceRow = tetris.getPieceRow();
        int rotation = tetris.getPieceRotation();

        //Draw the piece onto the board.
        for (int col = 0; col < tile.getDimension(); col++) {
            for (int row = 0; row < tile.getDimension(); row++) {
                if (pieceRow + row >= 2 && tile.isTile(col, row, rotation)) {
                    drawTile(tile, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                }
            }
        }

        drawHelpingGhost(g, tile, pieceCol, pieceRow, rotation);
    }

    private void drawHelpingGhost(Graphics g, Tile tile, int pieceCol, int pieceRow, int rotation) {
        Color base = tile.getBaseColor();
        Color transparentBase = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
        int lowest = getLowestTilePoint(tile, pieceCol, pieceRow, rotation);
        drawGhostPieceAtPoint(g, tile, pieceCol, rotation, transparentBase, lowest);
    }

    private void drawGhostPieceAtPoint(Graphics g, Tile tile, int pieceCol, int rotation, Color transparentBase, int lowest) {
        for (int col = 0; col < tile.getDimension(); col++) {
            for (int row = 0; row < tile.getDimension(); row++) {
                if (lowest + row >= 2 && tile.isTile(col, row, rotation)) {
                    drawTile(transparentBase,
                            transparentBase.brighter(),
                            transparentBase.darker(),
                            (pieceCol + col) * TILE_SIZE,
                            (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE,
                            g
                    );
                }
            }
        }
    }

    private int getLowestTilePoint(Tile tile, int pieceCol, int pieceRow, int rotation) {
        for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
            if (isValidAndEmpty(tile, pieceCol, lowest, rotation)) {
                continue;
            }
            return lowest - 1;
        }
        throw new RuntimeException();
    }

    private void drawBackgroundGridAbovePieces(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        for (int x = 0; x < COL_COUNT; x++) {
            for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
                g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
                g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
            }
        }
    }

    private void drawOutline(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
    }

    private void drawTile(Tile tile, int x, int y, Graphics g) {
        drawTile(tile.getBaseColor(), tile.getLightColor(), tile.getDarkColor(), x, y, g);
    }

    private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
        g.setColor(base);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        bottomRightDarkShadow(dark, x, y, g);
        topLeftLightningShadow(light, x, y, g);
    }

    private void bottomRightDarkShadow(Color dark, int x, int y, Graphics g) {
        g.setColor(dark);
        g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
        g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
    }

    private void topLeftLightningShadow(Color light, int x, int y, Graphics g) {
        g.setColor(light);
        for (int i = 0; i < SHADE_WIDTH; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
        }
    }
}
