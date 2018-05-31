package org.psnbtech;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TetrisKeyAdapterTest {

	@Test
	public void testKeyPressed() {
		//given mock 
		Tetris tetris = Mockito.mock(Tetris.class);
		TetrisKeyAdapter tetrisKeyAdapter = new TetrisKeyAdapter(tetris);
		
		
		//when button pressed
		
		//then method tetris called
	}

}
