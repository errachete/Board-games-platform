package was;

import mvc.GameTable;
import view.swing.BoardUI;
import view.swing.FrameUI;
import view.swing.PlayerUI;
import view.swing.BoardUI.BoardListener;
import view.swing.ColorTableUI.ColorModel;

/**
 * Implements the necessary methods of PlayerUI to the game Wolf and Sheep
 * @author claudiaggh & errachete
 * @version 1 (03/05/2017)
 */
public class WasPlayerUI extends PlayerUI<WasState, WasAction> {

	public WasPlayerUI(GameTable<WasState, WasAction> game, String name, int id, int position) {
		super(game, name, id, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FrameUI createJFrame(GameTable<WasState, WasAction> game, String name, int position) {
		return new FrameUI("Wolf and Sheep - Player " + id + ": " + name, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoardUI<WasState, WasAction> createBoard(int id, ColorModel cm,
			WasState s, BoardListener<WasState, WasAction> listener) {
		return new WasBoardUI(id, cm, s, listener);
	}
}
