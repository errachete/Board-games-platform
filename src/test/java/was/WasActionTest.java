package was;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WasActionTest {
	
	/**
	 * Test checking whether given a game state in which the sheeps are blocked, the program 
	 * recognizes it properly
	 */
	@Test
	public void testBlockedSheep() {
		// Previo empieza con turno lobo
		WasState previo = new WasState();
		
		WasState.Coord pieces1[] = {new WasState.Coord(3, 0), 
				new WasState.Coord(1, 0), new WasState.Coord(7, 2),
				new WasState.Coord(7, 4), new WasState.Coord(7, 6)};
		WasState.Coord pieces2[] = {new WasState.Coord(3, 0), 
				new WasState.Coord(7, 0), new WasState.Coord(7, 2),
				new WasState.Coord(7, 4), new WasState.Coord(7, 6)};
		WasState.Coord pieces3[] = {new WasState.Coord(3, 0), 
				new WasState.Coord(6, 3), new WasState.Coord(7, 2),
				new WasState.Coord(7, 4), new WasState.Coord(7, 6)};
		
		// Al construir prueba, le tocaria a la ovejas
		// Al utilizar ..., deberia saltar turno y ser lobo otra vez
		
		previo = new WasState(previo, pieces1, false, -1);
		
        WasAction action = new WasAction(0, new WasState.Coord(3, 0),
        		new WasState.Coord(2, 1));
		
        WasState prueba = new WasState(previo, pieces1, false, -1);
        action.applyTo(prueba);
        assertEquals("Sheeps blocked by wolf", 0, prueba.getTurn());
        
        prueba = new WasState(previo, pieces2, false, -1);
        action.applyTo(prueba);
        assertEquals("Sheeps at bottom", 0, prueba.getTurn());
        
        prueba = new WasState(previo, pieces3, false, -1);
        action.applyTo(prueba);
        assertEquals("Sheeps blocked by sheeps", 0, prueba.getTurn());
	}
}
