package org.psnbtech;


import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class TetrisKeyAdapterTest {
	
	@Mock
	Tetris tetris;
	@Mock
	Clock clock;
	@Mock
	BoardPanel boardPanel;
	@Mock
	KeyEvent keyEvent;
	
	TetrisKeyAdapter tetrisKeyAdapter;

	@BeforeEach
	public void before() {
		initMocks(this);
		tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		when(tetris.getLogicTimer()).thenReturn(clock);
		doReturn(boardPanel).when(tetris).getBoard();
	}

	@Test
	public void testDownKeyPressed() {
		//given 
		doReturn(false).when(tetris).isPaused();
		doReturn(0).when(tetris).getDropCooldown();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then
		verify(tetris).isPaused();
		verify(tetris).getDropCooldown();
		verify(clock).setCyclesPerSecond(25.0f);
	}

	@Test
	public void testUpKeyPressed() {
		//given 
		doReturn(false).when(tetris).isPaused();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_UP);
		when(boardPanel.isValidAndEmpty(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then
		verify(tetris).isPaused();
		verify(tetris, atLeast(1)).getBoard();
		verify(tetris).getCurrentRow();
		verify(tetris).setCurrentRow(0);
	}
	
	@Test
	public void testLeftKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		when(boardPanel.isValidAndEmpty(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris).isPaused();
		verify(tetris).getBoard();
		verify(tetris).decrementCurrentCol();
	}
	
	@Test
	public void testRightKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		when(boardPanel.isValidAndEmpty(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_RIGHT);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris).isPaused();
		verify(tetris).getBoard();
		verify(tetris).incrementCurrentCol();
	}

	@Test
	public void testShiftKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_SHIFT);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		TileType bufferType = verify(tetris).getCurrentType();
		verify(tetris).getCurrentType();
		verify(tetris).setCurrentType(tetris.getNextType());
		verify(tetris).setNextType(bufferType);
	}	
	
	@Test
	public void testCommaKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_COMMA);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris, atLeast(1)).rotatePiece((tetris.getCurrentRotation() == 0) ? 3 : tetris.getCurrentRotation() - 1);	
	}	
	
	@Test
	public void testPeriodKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_PERIOD);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris, atLeast(1)).rotatePiece((tetris.getCurrentRotation() == 3) ? 0 : tetris.getCurrentRotation() + 1);	
	}	
	
	@Test
	public void testPauseKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		doReturn(false).when(tetris).isGameOver();
		doReturn(false).when(tetris).isNewGame();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_P);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris).setPaused(true);	
		verify(tetris).getLogicTimer();
		verify(clock).setPaused(tetris.isPaused());
	}	
	
	@Test
	public void testEnterKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(true).when(tetris).isGameOver();
		doReturn(true).when(tetris).isNewGame();
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_ENTER);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		verify(tetris).resetGame();
	}	
	
	@Test
	public void testDownKeyReleased() {
		//given 
		when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
		
		//when 
		tetrisKeyAdapter.keyReleased(keyEvent);

		//then
		verify(tetris, atLeast(1)).getLogicTimer();
		verify(clock).setCyclesPerSecond(tetris.getGameSpeed());
		verify(tetris, atLeast(1)).getLogicTimer();
		verify(clock).reset();
	}

}

