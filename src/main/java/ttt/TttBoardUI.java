package ttt;

import java.awt.Graphics;
import java.util.logging.Logger;

import view.BoardUI;
import view.ColorTableUI.ColorModel;

/**
 * A board for tic tac toe game.
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public class TttBoardUI extends BoardUI<TttState, TttAction> {

	private static final long serialVersionUID = -7980914351488645675L;

	/**
	 * {@inheritDoc}
	 */
	public TttBoardUI(int id, ColorModel cm, TttState state, BoardListener<TttState, TttAction> listener) {
		super(id, cm, state, listener);
	}

	/**
	 * {@inheritDoc}
	 * In Ttt it's not necessary because there are not selected cells.
	 */
	@Override
	public void paintSelected(Graphics g) {}

	/**
	 * {@inheritDoc}
	 * It puts a piece in the clicked cell when it is the turn of the player
	 */
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		Logger.getLogger("log").info("Player " + id + " clicked on cell (" + row + ',' + col + ")");
		if(state.getTurn() == id && !state.isFinished()) {
			TttAction action = new TttAction(state.getTurn(), row, col);
			if(state.isValid(action)) {
				listener.sendMessage("You have chosen the cell (" + row + ',' + col + ").");
				listener.sendMessage("Turn of player " + (action.getPlayerNumber() + 1) % 2 + '.');
				listener.makeManualMove(action);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getPosition(int row, int col) {
		if (state.at(row, col) != TttState.EMPTY)
			return state.at(row, col);
		else
			return null;
	}

	/**
	 * {@inheritDoc}
	 * In Ttt it's not necessary because there are not selected cells.
	 */
	@Override
	public void nullSelected() {}

	@Override
	protected int getNumRows() {
		return state.dim;
	}

	@Override
	protected int getNumCols() {
		return state.dim;
	}

}