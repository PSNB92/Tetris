package org.psnbtech;

import javax.swing.*;
import java.awt.*;

class SidePanel extends JPanel {
    private static final int
            TILE_SIZE = BoardPanel.TILE_SIZE >> 1,
            SHADE_WIDTH = BoardPanel.SHADE_WIDTH >> 1,
            TILE_COUNT = 5,
            SQUARE_CENTER_X = 130,
            SQUARE_CENTER_Y = 65,
            SQUARE_SIZE = (TILE_SIZE * TILE_COUNT >> 1);

    private static final int
            SMALL_INSET = 20,
            LARGE_INSET = 40,
            STATS_INSET = 175,
            CONTROLS_INSET = 300,
            TEXT_STRIDE = 25;

    private static final Font
            SMALL_FONT = new Font("Tahoma", Font.BOLD, 11),
            LARGE_FONT = new Font("Tahoma", Font.BOLD, 13);

    private final Color DRAW_COLOR = new Color(128, 192, 128);

    private Tetris tetris;

    SidePanel(Tetris tetris) {
        this.tetris = tetris;

        setPreferredSize(new Dimension(200, BoardPanel.PANEL_HEIGHT));
        setBackground(Color.BLACK);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(DRAW_COLOR);
        drawStats(g);
        drawCategory(g);
        drawPreview(g);
    }

    private void drawPreview(Graphics g) {
        drawPreviewBox(g);
        drawPreviewPiece(g);
    }

    private void drawPreviewBox(Graphics g) {
        g.setFont(LARGE_FONT);
        g.drawString("Next Piece:", SMALL_INSET, 70);
        g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
    }

    private void drawPreviewPiece(Graphics g) {
        Tile type = tetris.getNextPieceType();
        if (!tetris.isGameOver() && type != null) {

            int cols = type.getSideSize();
            int dimension = type.getDimension();

            int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
            int startY = (SQUARE_CENTER_Y - (cols * TILE_SIZE / 2));

            int top = type.getTopInset(0);
            int left = type.getLeftInset(0);

            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    if (type.isTile(col, row, 0)) {
                        drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
                    }
                }
            }
        }
    }

    private void drawStats(Graphics g) {
        int offset = STATS_INSET;
        g.setFont(LARGE_FONT);
        g.drawString("Stats", SMALL_INSET, offset);
        g.setFont(SMALL_FONT);
        g.drawString("Level: " + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("Score: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);
    }

    private void drawCategory(Graphics g) {
        int offset = CONTROLS_INSET;
        g.setFont(LARGE_FONT);
        g.drawString("Controls", SMALL_INSET, offset);
        g.setFont(SMALL_FONT);
        g.drawString("A - Move Left", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("D - Move Right", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("Q - Rotate Anticlockwise", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("E - Rotate Clockwise", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("S - Drop", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("P - Pause Game", LARGE_INSET, offset += TEXT_STRIDE);
    }

    private void drawTile(Tile type, int x, int y, Graphics g) {
        drawBasicTile(type, x, y, g);
        drawLightningShadow(type, x, y, g);
    }

    private void drawBasicTile(Tile type, int x, int y, Graphics g) {
        g.setColor(type.getBaseColor());
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        g.setColor(type.getDarkColor());
        g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
        g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
    }

    private void drawLightningShadow(Tile type, int x, int y, Graphics g) {
        g.setColor(type.getLightColor());
        for (int i = 0; i < SHADE_WIDTH; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
        }
    }
}
