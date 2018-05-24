package org.psnbtech;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class TetrisKeyAdapter {{

	new KeyAdapter() {
		
		public void keyPressed(KeyEvent e) {
							
			switch(e.getKeyCode()) {
			
			/*
			 * Soft Drop - When pressed, we check to see that the game is not
			 * paused and that there is no drop cooldown, then set the
			 * logic timer to run at a speed of 25 cycles per second.
			 */
			case KeyEvent.VK_DOWN:
				if(!isPaused && dropCooldown == 0) {
					logicTimer.setCyclesPerSecond(25.0f);
				}
				break;
				
			/*
			 * Hard Drop - When pressed, we check to see that the game is not paused and
			 * then check for the lowest available row and drop the piece to there.
			 */
			case KeyEvent.VK_UP:
				if (!isPaused) {
					for (int lowest = currentRow; lowest < board.ROW_COUNT; lowest++) {
						// If no collision is detected, try the next row.
						if (board.isValidAndEmpty(currentType, currentCol, lowest, currentRotation)) {
							currentRow = lowest;
						}

					}
				}
				break;
				
			/*
			 * Move Left - When pressed, we check to see that the game is
			 * not paused and that the position to the left of the current
			 * position is valid. If so, we decrement the current column by 1.
			 */
			case KeyEvent.VK_LEFT:
				if(!isPaused && board.isValidAndEmpty(currentType, currentCol - 1, currentRow, currentRotation)) {
					currentCol--;
				}
				break;
				
			/*
			 * Move Right - When pressed, we check to see that the game is
			 * not paused and that the position to the right of the current
			 * position is valid. If so, we increment the current column by 1.
			 */
			case KeyEvent.VK_RIGHT:
				if(!isPaused && board.isValidAndEmpty(currentType, currentCol + 1, currentRow, currentRotation)) {
					currentCol++;
				}
				break;
				
			/*
			 * Switch Piece - When pressed, we check to see that the game is not paused and
			 * then switch the current piece with the next piece.
			 */

			case KeyEvent.VK_SHIFT:
				if (!isPaused) {
					TileType bufferType;
					bufferType = currentType;
					currentType = nextType;
					nextType = bufferType;
				}
				break;
				
			/*
			 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
			 * and then attempt to rotate the piece anticlockwise. Because of the size and
			 * complexity of the rotation code, as well as it's similarity to clockwise
			 * rotation, the code for rotating the piece is handled in another method.
			 */
			case KeyEvent.VK_COMMA:
				if(!isPaused) {
					rotatePiece((currentRotation == 0) ? 3 : currentRotation - 1);
				}
				break;
			
			/*
		     * Rotate Clockwise - When pressed, check to see that the game is not paused
			 * and then attempt to rotate the piece clockwise. Because of the size and
			 * complexity of the rotation code, as well as it's similarity to anticlockwise
			 * rotation, the code for rotating the piece is handled in another method.
			 */
			case KeyEvent.VK_PERIOD:
				if(!isPaused) {
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
				if(!isGameOver && !isNewGame) {
					isPaused = !isPaused;
					logicTimer.setPaused(isPaused);
				}
				break;
			
			/*
			 * Start Game - When pressed, check to see that we're in either a game over or new
			 * game state. If so, reset the game.
			 */
			case KeyEvent.VK_ENTER:
				if(isGameOver || isNewGame) {
					resetGame();
				}
				break;
			
			}
		}
		
		public void keyReleased(KeyEvent e) {
			
			switch(e.getKeyCode()) {
			
			/*
			 * Drop - When released, we set the speed of the logic timer
			 * back to whatever the current game speed is and clear out
			 * any cycles that might still be elapsed.
			 */
			case KeyEvent.VK_DOWN:
				logicTimer.setCyclesPerSecond(gameSpeed);
				logicTimer.reset();
				break;
			}
			
		}
		
	);}
}
