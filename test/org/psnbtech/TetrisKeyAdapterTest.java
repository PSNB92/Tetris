package org.psnbtech;


import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import java.awt.event.KeyEvent;

import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
	public void testLeftKeyPressed() {
		//given 
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		doReturn(false).when(tetris).isPaused();
		Mockito.when(boardPanel.isValidAndEmpty(Mockito.any(), Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt())).thenReturn(true);
		Mockito.when(keyEvent.getKeyCode()).thenReturn(KeyEvent.VK_LEFT);
		
		//when 
		tetrisKeyAdapter.keyPressed(keyEvent);

		//then 
		Mockito.verify(tetris).isPaused();
		Mockito.verify(tetris).getBoard();
		Mockito.verify(tetris).decrementCurrentCol();
	}

}

