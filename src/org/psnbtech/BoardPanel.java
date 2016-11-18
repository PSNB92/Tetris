package org.psnbtech;

import javax.swing.*;
import java.awt.*;

/**
 * The {@code BoardPanel} class is responsible for displaying the game grid and
 * handling things related to the game board.
 *
 * @author Brendan Jones
 */
public class BoardPanel extends JPanel {

    public static final int COLOR_MIN = 35;
    public static final int COLOR_MAX = 255 - COLOR_MIN;
    private static final int BORDER_WIDTH = 5;
    public static final int COL_COUNT = 10;
    private static final int VISIBLE_ROW_COUNT = 20;
    private static final int HIDDEN_ROW_COUNT = 2;
    public static final int ROW_COUNT = VISIBLE_ROW_COUNT + HIDDEN_ROW_COUNT;
    public static final int TILE_SIZE = 24;
    public static final int SHADE_WIDTH = 4;
    private static final int CENTER_X = COL_COUNT * TILE_SIZE / 2;
    private static final int CENTER_Y = VISIBLE_ROW_COUNT * TILE_SIZE / 2;
    public static final int PANEL_WIDTH = COL_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    public static final int PANEL_HEIGHT = VISIBLE_ROW_COUNT * TILE_SIZE + BORDER_WIDTH * 2;
    private static final Font LARGE_FONT = new Font("Tahoma", Font.BOLD, 16);
    private static final Font SMALL_FONT = new Font("Tahoma", Font.BOLD, 12);


    private Tetris tetris;
    private TileType[][] tiles;

    public BoardPanel(Tetris tetris) {
        this.tetris = tetris;
        this.tiles = new TileType[ROW_COUNT][COL_COUNT];

        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK);
    }

    public void clear() {
        for (int i = 0; i < ROW_COUNT; i++) {
            for (int j = 0; j < COL_COUNT; j++) {
                tiles[i][j] = null;
            }
        }
    }

    public boolean isValidAndEmpty(TileType type, int x, int y, int rotation) {

        //Ensure the piece is in a valid column.
        if (x < -type.getLeftInset(rotation) || x + type.getDimension() - type.getRightInset(rotation) >= COL_COUNT) {
            return false;
        }

        //Ensure the piece is in a valid row.
        if (y < -type.getTopInset(rotation) || y + type.getDimension() - type.getBottomInset(rotation) >= ROW_COUNT) {
            return false;
        }

		/*
		 * Loop through every tile in the piece and see if it conflicts with an existing tile.
		 * 
		 * Note: It's fine to do this even though it allows for wrapping because we've already
		 * checked to make sure the piece is in a valid location.
		 */
        for (int col = 0; col < type.getDimension(); col++) {
            for (int row = 0; row < type.getDimension(); row++) {
                if (type.isTile(col, row, rotation) && isOccupied(x + col, y + row)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addPiece(TileType type, int x, int y, int rotation) {
        // TODO rename to "setPiece"
			/*
		 * Loop through every tile within the piece and add it
		 * to the board only if the boolean that represents that
		 * tile is set to true.
		 */
        for (int col = 0; col < type.getDimension(); col++) {
            for (int row = 0; row < type.getDimension(); row++) {
                if (type.isTile(col, row, rotation)) {
                    setTile(col + x, row + y, type);
                }
            }
        }
    }

    public int checkLines() {

        int completedLines = 0;
		
		/*
		 * Here we loop through every line and check it to see if
		 * it's been cleared or not. If it has, we increment the
		 * number of completed lines and check the next row.
		 * 
		 * The isRowFullAndRemoveIt function handles clearing the line and
		 * shifting the rest of the board down for us.
		 */
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

    private void setTile(int x, int y, TileType type) {
        tiles[y][x] = type;
    }

    private TileType getTile(int x, int y) {
        return tiles[y][x];
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //This helps simplify the positioning of things.
        g.translate(BORDER_WIDTH, BORDER_WIDTH);
		
		/*
		 * Draw the board differently depending on the current game state.
		 */
        if (tetris.isPaused()) {
            g.setFont(LARGE_FONT);
            g.setColor(Color.WHITE);
            String msg = "PAUSED";
            g.drawString(msg, CENTER_X - g.getFontMetrics().stringWidth(msg) / 2, CENTER_Y);
        } else if (tetris.isNewGame() || tetris.isGameOver()) {
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
        } else {
			
			/*
			 * Draw the tiles onto the board.
			 */
            for (int x = 0; x < COL_COUNT; x++) {
                for (int y = HIDDEN_ROW_COUNT; y < ROW_COUNT; y++) {
                    TileType tile = getTile(x, y);
                    if (tile != null) {
                        drawTile(tile, x * TILE_SIZE, (y - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                    }
                }
            }
			
			/*
			 * Draw the current piece. This cannot be drawn like the rest of the
			 * pieces because it's still not part of the game board. If it were
			 * part of the board, it would need to be removed every frame which
			 * would just be slow and confusing.
			 */
            TileType type = tetris.getPieceType();
            int pieceCol = tetris.getPieceCol();
            int pieceRow = tetris.getPieceRow();
            int rotation = tetris.getPieceRotation();

            //Draw the piece onto the board.
            for (int col = 0; col < type.getDimension(); col++) {
                for (int row = 0; row < type.getDimension(); row++) {
                    if (pieceRow + row >= 2 && type.isTile(col, row, rotation)) {
                        drawTile(type, (pieceCol + col) * TILE_SIZE, (pieceRow + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                    }
                }
            }
			
			/*
			 * Draw the ghost (semi-transparent piece that shows where the current piece will land). I couldn't think of
			 * a better way to implement this so it'll have to do for now. We simply take the current position and move
			 * down until we hit a row that would cause a collision.
			 */
            Color base = type.getBaseColor();
            base = new Color(base.getRed(), base.getGreen(), base.getBlue(), 20);
            for (int lowest = pieceRow; lowest < ROW_COUNT; lowest++) {
                //If no collision is detected, try the next row.
                if (isValidAndEmpty(type, pieceCol, lowest, rotation)) {
                    continue;
                }

                //Draw the ghost one row higher than the one the collision took place at.
                lowest--;

                //Draw the ghost piece.
                for (int col = 0; col < type.getDimension(); col++) {
                    for (int row = 0; row < type.getDimension(); row++) {
                        if (lowest + row >= 2 && type.isTile(col, row, rotation)) {
                            drawTile(base, base.brighter(), base.darker(), (pieceCol + col) * TILE_SIZE, (lowest + row - HIDDEN_ROW_COUNT) * TILE_SIZE, g);
                        }
                    }
                }

                break;
            }
			
			/*
			 * Draw the background grid above the pieces (serves as a useful visual
			 * for players, and makes the pieces look nicer by breaking them up.
			 */
            g.setColor(Color.DARK_GRAY);
            for (int x = 0; x < COL_COUNT; x++) {
                for (int y = 0; y < VISIBLE_ROW_COUNT; y++) {
                    g.drawLine(0, y * TILE_SIZE, COL_COUNT * TILE_SIZE, y * TILE_SIZE);
                    g.drawLine(x * TILE_SIZE, 0, x * TILE_SIZE, VISIBLE_ROW_COUNT * TILE_SIZE);
                }
            }
        }
		
		/*
		 * Draw the outline.
		 */
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, TILE_SIZE * COL_COUNT, TILE_SIZE * VISIBLE_ROW_COUNT);
    }

    private void drawTile(TileType type, int x, int y, Graphics g) {
        drawTile(type.getBaseColor(), type.getLightColor(), type.getDarkColor(), x, y, g);
    }

    private void drawTile(Color base, Color light, Color dark, int x, int y, Graphics g) {
		
		/*
		 * Fill the entire tile with the base color.
		 */
        g.setColor(base);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
		
		/*
		 * Fill the bottom and right edges of the tile with the dark shading color.
		 */
        g.setColor(dark);
        g.fillRect(x, y + TILE_SIZE - SHADE_WIDTH, TILE_SIZE, SHADE_WIDTH);
        g.fillRect(x + TILE_SIZE - SHADE_WIDTH, y, SHADE_WIDTH, TILE_SIZE);
		
		/*
		 * Fill the top and left edges with the light shading. We draw a single line
		 * for each row or column rather than a rectangle so that we can draw a nice
		 * looking diagonal where the light and dark shading meet.
		 */
        g.setColor(light);
        for (int i = 0; i < SHADE_WIDTH; i++) {
            g.drawLine(x, y + i, x + TILE_SIZE - i - 1, y + i);
            g.drawLine(x + i, y, x + i, y + TILE_SIZE - i - 1);
        }
    }

}
