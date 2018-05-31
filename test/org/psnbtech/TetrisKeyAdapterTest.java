package org.psnbtech;


import java.awt.event.KeyEvent;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TetrisKeyAdapterTest {

	@Test
	public void testDownKeyPressed() {
		//given 
		Tetris tetris = Mockito.mock(Tetris.class);
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		KeyEvent e = Mockito.mock(KeyEvent.class);
		Mockito.when(e.getKeyCode()).thenReturn(KeyEvent.VK_DOWN);
		
		//when button pressed
		tetrisKeyAdapter.keyPressed(e);

		//then method tetris called
		Mockito.	verify(tetris.getLogicTimer());
	}

}
