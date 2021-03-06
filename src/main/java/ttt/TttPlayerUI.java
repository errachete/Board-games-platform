package ttt;

import mvc.GameTable;
import view.swing.BoardUI;
import view.swing.FrameUI;
import view.swing.PlayerUI;
import view.swing.BoardUI.BoardListener;
import view.swing.ColorTableUI.ColorModel;

/**
 * Implements the necessary methods of PlayerUI to the game Tic Tac Toe
 * @author claudiaggh & errachete
 * @version 1 (03/05/2017)
 */
public class TttPlayerUI extends PlayerUI<TttState, TttAction> {

	public TttPlayerUI(GameTable<TttState, TttAction> game, String name, int id, int position) {
		super(game, name, id, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FrameUI createJFrame(GameTable<TttState, TttAction> ctrl, String name, int position) {
		return new FrameUI("Tic Tac Toe - Player " + id + ": " + name, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoardUI<TttState, TttAction> createBoard(int id, ColorModel cm, TttState s,
			BoardListener<TttState, TttAction> listener) {
		return new TttBoardUI(id, cm, s, listener);
	}
}
