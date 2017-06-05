package view.swing;

import java.awt.Color;
import java.awt.Graphics;

import base.model.GameAction;
import base.model.GameState;
import extra.jboard.JBoard;

/**
 * Abstract class that implements the common methods and parameters for any board game.
 * @author claudiaggh & errachete
 * @version 1 (03/05/2017)
 * @param <S> A GameState that represents the state od the board in a moment of the game
 * @param <A> A Game Action to apply to the state
 */
public abstract class BoardUI< S extends GameState< S, A >, A extends GameAction< S, A > > extends JBoard{

	private static final long serialVersionUID = -2798902232928717390L;
	
	/**
	 * Interface used by the board to make the actions that the game requires when
	 * interacting with the board
	 * @param <S>
	 * @param <A>
	 */
	public interface BoardListener< S extends GameState<S, A>, A extends GameAction<S, A>>{
		/**
		 * Given an action produced by a manual move, it applies it to the board
		 * @param a Action to apply
		 */
		public void makeManualMove(A a);
		/**
		 * Given a string, it sends it to the status messages window
		 * @param s Message to add
		 */
		public void sendMessage(String s);
	}

	protected ColorTableUI.ColorModel cm;
	protected int id;
	protected S state;
	protected BoardListener<S, A> listener;
	
	/**
	 * Constructor with parameters, initializes the attributes to given values
	 */
	public BoardUI(int id, ColorTableUI.ColorModel cm, S state, BoardListener<S, A> listener) {
		this.cm = cm;
		this.state = state;
		this.listener = listener;
		this.id = id;
	}

	/**
	 * Used to paint a circle around a selected cell on the board in certain games
	 */
	public abstract void paintSelected(Graphics g);
	
	@Override
	protected void keyTyped(int keyCode) {}

	/**
	 * Given the information about a click on the board, it makes the proper action
	 */
	@Override
	protected abstract void mouseClicked(int row, int col, int clickCount, int mouseButton);

	/**
	 * Returns the shape of a given player
	 */
	@Override
	protected Shape getShape(int player) {
		return Shape.CIRCLE;
	}

	/**
	 * Returns the color of a given player
	 */
	@Override
	protected Color getColor(int player) {
		return cm.at(player);
	}

	/**
	 * Given the coordinates of a cell returns an integer representing what's in the cell 
	 * (EMPTY==-1, eoc the player number)
	 */
	@Override
	protected abstract Integer getPosition(int row, int col);

	/**
	 * Given the coordinates of a cell returns the color of the background of the cell 
	 */
	@Override
	protected Color getBackground(int row, int col) {
		return (row+col) % 2 == 0 ? Color.LIGHT_GRAY : Color.BLACK;
	}
	
	public void setState(S s){
		this.state = s;
	}

	/**
	 * Set selected to null, in order not to paint selected when it's not the turn of that player
	 */
	public abstract void nullSelected();
}
