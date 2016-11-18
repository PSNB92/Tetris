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

        //Set the color for drawing.
        g.setColor(DRAW_COLOR);

		/*
         * This variable stores the current y coordinate of the string.
		 * This way we can re-order, add, or remove new strings if necessary
		 * without needing to change the other strings.
		 */
        int offset;

		/*
         * Draw the "Stats" category.
		 */
        g.setFont(LARGE_FONT);
        g.drawString("Stats", SMALL_INSET, offset = STATS_INSET);
        g.setFont(SMALL_FONT);
        g.drawString("Level: " + tetris.getLevel(), LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("Score: " + tetris.getScore(), LARGE_INSET, offset += TEXT_STRIDE);

		/*
		 * Draw the "Controls" category.
		 */
        g.setFont(LARGE_FONT);
        g.drawString("Controls", SMALL_INSET, offset = CONTROLS_INSET);
        g.setFont(SMALL_FONT);
        g.drawString("A - Move Left", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("D - Move Right", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("Q - Rotate Anticlockwise", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("E - Rotate Clockwise", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("S - Drop", LARGE_INSET, offset += TEXT_STRIDE);
        g.drawString("P - Pause Game", LARGE_INSET, offset += TEXT_STRIDE);
		
		/*
		 * Draw the next piece preview box.
		 */
        g.setFont(LARGE_FONT);
        g.drawString("Next Piece:", SMALL_INSET, 70);
        g.drawRect(SQUARE_CENTER_X - SQUARE_SIZE, SQUARE_CENTER_Y - SQUARE_SIZE, SQUARE_SIZE * 2, SQUARE_SIZE * 2);
		
		/*
		 * Draw a preview of the next piece that will be spawned. The code is pretty much
		 * identical to the drawing code on the board, just smaller and centered, rather
		 * than constrained to a grid.
		 */
        TileType type = tetris.getNextPieceType();
        if (!tetris.isGameOver() && type != null) {
			/*
			 * Get the size properties of the current piece.
			 */
            int cols = type.getColumnsCount();
            int rows = type.getRowsCount();
            int dimension = type.getDimension();
		
			/*
			 * Calculate the top left corner (origin) of the piece.
			 */
            int startX = (SQUARE_CENTER_X - (cols * TILE_SIZE / 2));
            int startY = (SQUARE_CENTER_Y - (rows * TILE_SIZE / 2));
		
			/*
			 * Get the insets for the preview. The default
			 * rotation is used for the preview, so we just use 0.
			 */
            int top = type.getTopInset(0);
            int left = type.getLeftInset(0);
		
			/*
			 * Loop through the piece and draw it's tiles onto the preview.
			 */
            for (int row = 0; row < dimension; row++) {
                for (int col = 0; col < dimension; col++) {
                    if (type.isTile(col, row, 0)) {
                        drawTile(type, startX + ((col - left) * TILE_SIZE), startY + ((row - top) * TILE_SIZE), g);
                    }
                }
            }
        }
    }

    private void drawTile(TileType type, int x, int y, Graphics g) {
		/*
		 * Fill the entire tile with the base color.
		 */
        g.setColor(type.getBaseColor());
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
        g.setColor(type.getDarkColor());
        g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
        g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		
		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
        g.setColor(type.getLightColor());
        for (int i = 0; i < SHADE_WIDTH; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
        }
    }
}
