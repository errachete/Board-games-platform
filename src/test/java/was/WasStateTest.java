package was;

import static org.junit.Assert.*;
import org.junit.Test;

public class WasStateTest {

	/**
	 * Test checking whether given a game state in which the sheeps have won, the program 
	 * recognizes it properly
	 */
	@Test
	public void testSheepsWin() {
		WasState previo = new WasState();
		
		WasState.Coord pieces1[] = {new WasState.Coord(7, 0), 
				new WasState.Coord(6, 1), new WasState.Coord(0, 5),
				new WasState.Coord(0, 7), new WasState.Coord(0, 3)};
		WasState.Coord pieces2[] = {new WasState.Coord(4, 7), 
				new WasState.Coord(3, 6), new WasState.Coord(5, 6),
				new WasState.Coord(5, 0), new WasState.Coord(0, 7)};
		WasState.Coord pieces3[] = {new WasState.Coord(3, 2), 
				new WasState.Coord(4, 1), new WasState.Coord(2, 1),
				new WasState.Coord(4, 3), new WasState.Coord(2, 3)};
		
        WasState prueba = new WasState(previo, pieces1, false, -1);
        assertEquals("Wolf blocked in corner", true, prueba.isWinner(1));
        
        prueba = new WasState(previo, pieces2, false, -1);
        assertEquals("Wolf blocked in wall", true, prueba.isWinner(1));
        
        prueba = new WasState(previo, pieces3, false, -1);
        assertEquals("Wolf blocked in center", true, prueba.isWinner(1));
    } 
	
	/**
	 * Test checking whether given a game state in which the wolf has won, the program 
	 * recognizes it properly
	 */
	@Test
	public void testWolfWins() {
		WasState previo = new WasState();

		for(int i = 1; i < 8; i += 2) {
		WasState.Coord pieces[] = { new WasState.Coord(0, i), 
				new WasState.Coord(6, 1), new WasState.Coord(0, 5),
				new WasState.Coord(0, 7), new WasState.Coord(0, 3) };

		WasState prueba = new WasState(previo, pieces, false, -1);
		assertEquals("Wolf wins in (0, " + i + ")", true, prueba.isWinner(0));
		}
	}
	
	/**
	 * Test checking that the two first wolf movements generated are the correct ones
	 */
	@Test
	public void testWolfMoves() {
		WasState prueba = new WasState();
		
		assertEquals("Initial wolf movement", 1, prueba.validActions(0).size());
		
		WasState.Coord pieces[] = { new WasState.Coord(6, 1), 
				new WasState.Coord(0, 1), new WasState.Coord(0, 5),
				new WasState.Coord(0, 7), new WasState.Coord(0, 3) };

		prueba = new WasState(prueba, pieces, false, -1);
		assertEquals("Second wolf movement", 4, prueba.validActions(0).size());
		
	}

	/**
	 * Test checking that the two first sheep movements generated are the correct ones
	 */
	@Test
	public void testSheepsMoves() {
		WasState prueba = new WasState();
		
		for(int i = 1; i < 7; i += 2) {		
			WasState.Coord pieces[] = { new WasState.Coord(6, 1), 
					new WasState.Coord(7, 0), new WasState.Coord(7, 2),
					new WasState.Coord(7, 4), new WasState.Coord(0, i) };
			
			prueba = new WasState(prueba, pieces, false, -1);
			assertEquals("Sheep at top", 2, prueba.validActions(1).size());
		}
		
		for(int i = 0; i < 8; i += 2) {		
			WasState.Coord pieces[] = { new WasState.Coord(6, 1), 
					new WasState.Coord(7, 0), new WasState.Coord(7, 2),
					new WasState.Coord(7, 4), new WasState.Coord(i, 7) };
			
			prueba = new WasState(prueba, pieces, false, -1);
			assertEquals("Sheep at wall", 1, prueba.validActions(1).size());
		}
	}
}
