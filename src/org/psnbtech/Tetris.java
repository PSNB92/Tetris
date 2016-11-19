package org.psnbtech;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Tetris extends JFrame {
    private static final long FRAME_TIME = 1000L / 50L;

    private BoardPanel board;
    private SidePanel side;
    private boolean isPaused;
    private boolean isNewGame;
    private boolean isGameOver;
    private int level;
    private int score;
    private Clock logicTimer;

    private Tile currentType;
    private Tile nextType;

    private int currentCol;
    private int currentRow;
    private int currentRotation;

    private int dropCooldown;
    private float gameSpeed;

    private Tetris() {

        super("Tetris");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        this.board = new BoardPanel(this);
        this.side = new SidePanel(this);

        add(board, BorderLayout.CENTER);
        add(side, BorderLayout.EAST);

        addKeyListener(new KeyAdapter() {

            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {

				/*
                 * Drop - When pressed, we check to see that the game is not
				 * paused and that there is no drop cooldown, then set the
				 * logic timer to run at a speed of 25 cycles per second.
				 */
                    case KeyEvent.VK_S:
                        if (!isPaused && dropCooldown == 0) {
                            logicTimer.setCyclesPerSecond(25.0f);
                        }
                        break;
					
				/*
				 * Move Left - When pressed, we check to see that the game is
				 * not paused and that the position to the left of the current
				 * position is valid. If so, we decrement the current column by 1.
				 */
                    case KeyEvent.VK_A:
                        if (!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
                            currentCol--;
                        }
                        break;
					
				/*
				 * Move Right - When pressed, we check to see that the game is
				 * not paused and that the position to the right of the current
				 * position is valid. If so, we increment the current column by 1.
				 */
                    case KeyEvent.VK_D:
                        if (!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
                            currentCol++;
                        }
                        break;
					
				/*
				 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece anticlockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to clockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
                    case KeyEvent.VK_Q:
                        if (!isPaused) {
                            rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
                        }
                        break;
				
				/*
			     * Rotate Clockwise - When pressed, check to see that the game is not paused
				 * and then attempt to rotate the piece clockwise. Because of the size and
				 * complexity of the rotation code, as well as it's similarity to anticlockwise
				 * rotation, the code for rotating the piece is handled in another method.
				 */
                    case KeyEvent.VK_E:
                        if (!isPaused) {
                            rotatePiece((currentRotation == 3) ? 0 : currentRotation + 1);
                        }
                        break;
					
				/*
				 * Pause Game - When pressed, check to see that we're currently playing a game.
				 * If so, toggle the pause variable and update the logic timer to reflect this
				 * change, otherwise the game will execute a huge number of updates and essentially
				 * cause an instant game over when we unpause if we stay paused for more than a
				 * minute or so.
				 */
                    case KeyEvent.VK_P:
                        if (!isGameOver && !isNewGame) {
                            isPaused = !isPaused;
                            logicTimer.setPaused(isPaused);
                        }
                        break;
				
				/*
				 * Start Game - When pressed, check to see that we're in either a game over or new
				 * game state. If so, reset the game.
				 */
                    case KeyEvent.VK_ENTER:
                        if (isGameOver || isNewGame) {
                            resetGame();
                        }
                        break;

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

                switch (e.getKeyCode()) {
				
				/*
				 * Drop - When released, we set the speed of the logic timer
				 * back to whatever the current game speed is and clear out
				 * any cycles that might still be elapsed.
				 */
                    case KeyEvent.VK_S:
                        logicTimer.setCyclesPerSecond(gameSpeed);
                        logicTimer.reset();
                        break;
                }

            }

        });

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void startGame() {
        this.isNewGame = true;
        this.gameSpeed = 1.0f;

        this.logicTimer = new Clock(gameSpeed);
        logicTimer.setPaused(true);

        while (true) {
            long start = System.nanoTime();
            logicTimer.update();
            if (logicTimer.hasElapsedCycleAndDecrement()) {
                updateGame();
            }
            if (dropCooldown > 0) {
                dropCooldown--;
            }

            renderGame();

            long delta = (System.nanoTime() - start) / 1000000L;
            if (delta < FRAME_TIME) {
                try {
                    Thread.sleep(FRAME_TIME - delta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void updateGame() {
		/*
		 * Check to see if the piece's position can move down to the next row.
		 */
        if (board.isValidAndEmpty(currentType, currentCol, currentRow + 1, currentRotation)) {
            //Increment the current row if it's safe to do so.
            currentRow++;
        } else {
			/*
			 * We've either reached the bottom of the board, or landed on another piece, so
			 * we need to add the piece to the board.
			 */
            board.setPiece(currentType, currentCol, currentRow, currentRotation);
			
			/*
			 * Check to see if adding the new piece resulted in any clearedLines lines. If so,
			 * increase the player's score. (Up to 4 lines can be clearedLines in a single go;
			 * [1 = 100pts, 2 = 200pts, 3 = 400pts, 4 = 800pts]).
			 */
            int clearedLines = board.checkLinesAndReturnHowManyRemoved();
            score += 100 * clearedLines;

			/*
			 * Increase the speed slightly for the next piece and update the game's timer
			 * to reflect the increase.
			 */
            gameSpeed += 0.025f;
            logicTimer.setCyclesPerSecond(gameSpeed);
            logicTimer.reset();
            dropCooldown = 25;
            updateLevel();
            spawnPiece();
        }
    }

    private void updateLevel() {
        level = (int) (gameSpeed * 1.70f);
    }

    private void renderGame() {
        board.repaint();
        side.repaint();
    }

    private void resetGame() {
        this.level = 1;
        this.score = 0;
        this.gameSpeed = 1.0f;
        this.nextType = Tile.getRandomTile();
        this.isNewGame = false;
        this.isGameOver = false;
        board.clear();
        logicTimer.reset();
        logicTimer.setCyclesPerSecond(gameSpeed);
        spawnPiece();
    }

    private void spawnPiece() {
        this.currentType = nextType;
        this.currentCol = currentType.getSpawnColumn();
        this.currentRow = currentType.getSpawnRow();
        this.currentRotation = 0;
        this.nextType = Tile.getRandomTile();

        if (!board.isValidAndEmpty(currentType, currentCol, currentRow, currentRotation)) {
            this.isGameOver = true;
            logicTimer.setPaused(true);
        }
    }

    private void rotatePiece(int newRotation) {
        int newColumn = currentCol;
        int newRow = currentRow;

        int left = currentType.getLeftInset(newRotation);
        int right = currentType.getRightInset(newRotation);
        int top = currentType.getTopInset(newRotation);
        int bottom = currentType.getBottomInset(newRotation);
		
		/*
		 * If the current piece is too far to the left or right, move the piece away from the edges
		 * so that the piece doesn't clip out of the map and automatically become invalid.
		 */
        if (currentCol < -left) {
            newColumn -= currentCol - left;
        } else if (currentCol + currentType.getDimension() - right >= BoardPanel.COL_COUNT) {
            newColumn -= (currentCol + currentType.getDimension() - right) - BoardPanel.COL_COUNT + 1;
        }
		
		/*
		 * If the current piece is too far to the top or bottom, move the piece away from the edges
		 */
        if (currentRow < -top) {
            newRow -= currentRow - top;
        } else if (currentRow + currentType.getDimension() - bottom >= BoardPanel.ROW_COUNT) {
            newRow -= (currentRow + currentType.getDimension() - bottom) - BoardPanel.ROW_COUNT + 1;
        }
		
		/*
		 * Check to see if the new position is acceptable. If it is, update the rotation and
		 * position of the piece.
		 */
        if (board.isValidAndEmpty(currentType, newColumn, newRow, newRotation)) {
            currentRotation = newRotation;
            currentRow = newRow;
            currentCol = newColumn;
        }
    }

    boolean isPaused() {
        return isPaused;
    }

    boolean isGameOver() {
        return isGameOver;
    }

    boolean isNewGame() {
        return isNewGame;
    }

    int getScore() {
        return score;
    }

    int getLevel() {
        return level;
    }

    Tile getPieceType() {
        return currentType;
    }

    Tile getNextPieceType() {
        return nextType;
    }

    int getPieceCol() {
        return currentCol;
    }

    int getPieceRow() {
        return currentRow;
    }

    int getPieceRotation() {
        return currentRotation;
    }

    public static void main(String[] args) {
        new Tetris().startGame();
    }
}
