package org.psnbtech;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class TetrisKeyAdapter implements KeyListener {

	private Tetris tetris;

	public TetrisKeyAdapter(Tetris tetris) {
		this.tetris = tetris;
	}

	@Override
	public void keyPressed(KeyEvent e) {

		switch (e.getKeyCode()) {

		/*
		 * Soft Drop - When pressed, we check to see that the game is not paused and
		 * that there is no drop cooldown, then set the logic timer to run at a speed of
		 * 25 cycles per second.
		 */
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			if (!tetris.isPaused() && tetris.getDropCooldown() == 0) {
				tetris.getLogicTimer().setCyclesPerSecond(25.0f);
			}
			break;

		/*
		 * Hard Drop - When pressed, we check to see that the game is not paused and
		 * then check for the lowest available row and drop the piece to there.
		 */
		case KeyEvent.VK_W:
		case KeyEvent.VK_UP:
			if (!tetris.isPaused()) {
				tetris.getBoard();
				for (int lowest = tetris.getCurrentRow(); lowest < BoardPanel.ROW_COUNT; lowest++) {
					// If no collision is detected, try the next row.
					if (tetris.getBoard().isValidAndEmpty(tetris.getCurrentType(), tetris.getCurrentCol(), lowest, tetris.getCurrentRotation())) {
						tetris.setCurrentRow(lowest);
					}

				}
			}
			break;

		/*
		 * Move Left - When pressed, we check to see that the game is not paused and
		 * that the position to the left of the current position is valid. If so, we
		 * decrement the current column by 1.
		 */
		case KeyEvent.VK_A:
		case KeyEvent.VK_LEFT:
			if (!tetris.isPaused() && tetris.getBoard().isValidAndEmpty(tetris.getCurrentType(), tetris.getCurrentCol() - 1, tetris.getCurrentRow(), tetris.getCurrentRotation())) {
				tetris.decrementCurrentCol();
			}
			break;

		/*
		 * Move Right - When pressed, we check to see that the game is not paused and
		 * that the position to the right of the current position is valid. If so, we
		 * increment the current column by 1.
		 */
		case KeyEvent.VK_D:
		case KeyEvent.VK_RIGHT:
			if (!tetris.isPaused() && tetris.getBoard().isValidAndEmpty(tetris.getCurrentType(), tetris.getCurrentCol() + 1, tetris.getCurrentRow(), tetris.getCurrentRotation())) {
				tetris.incrementCurrentCol();
			}
			break;

		/*
		 * Switch Piece - When pressed, we check to see that the game is not paused and
		 * then switch the current piece with the next piece.
		 */
		case KeyEvent.VK_R:
		case KeyEvent.VK_SHIFT:
			if (!tetris.isPaused()) {
				TileType bufferType;
				bufferType = tetris.getCurrentType();
				tetris.setCurrentType(tetris.getNextType());
				tetris.setNextType(bufferType);
			}
			break;

		/*
		 * Rotate Anticlockwise - When pressed, check to see that the game is not paused
		 * and then attempt to rotate the piece anticlockwise. Because of the size and
		 * complexity of the rotation code, as well as it's similarity to clockwise
		 * rotation, the code for rotating the piece is handled in another method.
		 */
		case KeyEvent.VK_Q:
		case KeyEvent.VK_COMMA:
			if (!tetris.isPaused()) {
				tetris.rotatePiece((tetris.getCurrentRotation() == 0) ? 3 : tetris.getCurrentRotation() - 1);
			}
			break;

		/*
		 * Rotate Clockwise - When pressed, check to see that the game is not paused and
		 * then attempt to rotate the piece clockwise. Because of the size and
		 * complexity of the rotation code, as well as it's similarity to anticlockwise
		 * rotation, the code for rotating the piece is handled in another method.
		 */
		case KeyEvent.VK_E:
		case KeyEvent.VK_PERIOD:
			if (!tetris.isPaused()) {
				tetris.rotatePiece((tetris.getCurrentRotation() == 3) ? 0 : tetris.getCurrentRotation() + 1);
			}
			break;

		/*
		 * Pause Game - When pressed, check to see that we're currently playing a game.
		 * If so, toggle the pause variable and update the logic timer to reflect this
		 * change, otherwise the game will execute a huge number of updates and
		 * essentially cause an instant game over when we unpause if we stay paused for
		 * more than a minute or so.
		 */
		case KeyEvent.VK_P:
			if (!tetris.isGameOver() && !tetris.isNewGame()) {
				tetris.setPaused(!tetris.isPaused());
				tetris.getLogicTimer().setPaused(tetris.isPaused());
			}
			break;

		/*
		 * Start Game - When pressed, check to see that we're in either a game over or
		 * new game state. If so, reset the game.
		 */
		case KeyEvent.VK_ENTER:
			if (tetris.isGameOver() || tetris.isNewGame()) {
				tetris.resetGame();
			}
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {

		switch (e.getKeyCode()) {

		/*
		 * Drop - When released, we set the speed of the logic timer back to whatever
		 * the current game speed is and clear out any cycles that might still be
		 * elapsed.
		 */
		case KeyEvent.VK_S:
		case KeyEvent.VK_DOWN:
			tetris.getLogicTimer().setCyclesPerSecond(tetris.getGameSpeed());
			tetris.getLogicTimer().reset();
			break;
		}

	}

	@Override
	public void keyTyped(KeyEvent e) {

	}
}
