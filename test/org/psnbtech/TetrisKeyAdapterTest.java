package org.psnbtech;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TetrisKeyAdapterTest {

	@Test
	public void testDownKeyPressed() {
		//given 
		Tetris tetris = Mockito.mock(Tetris.class);
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		
		//when button pressed
		@SuppressWarnings("deprecation")
		KeyEvent e = new KeyEvent(tetris, 0, 0, KeyEvent.VK_DOWN, 0);
		tetrisKeyAdapter.keyPressed(e);

		//then method tetris called
		Mockito.	verify(tetris.getLogicTimer());
	}

}
