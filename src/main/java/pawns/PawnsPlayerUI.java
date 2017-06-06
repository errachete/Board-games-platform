package pawns;

import mvc.GameTable;
import view.swing.BoardUI;
import view.swing.FrameUI;
import view.swing.PlayerUI;
import view.swing.BoardUI.BoardListener;
import view.swing.ColorTableUI.ColorModel;

/**
 * Implements the necessary methods of PlayerUI to the game Pawns
 * @author errachete
 * @version 1 (03/05/2017)
 */
public class PawnsPlayerUI extends PlayerUI<PawnsState, PawnsAction> {

	public PawnsPlayerUI(GameTable<PawnsState, PawnsAction> game, String name, int id, int position) {
		super(game, name, id, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public FrameUI createJFrame(GameTable<PawnsState, PawnsAction> game, String name, int position) {
		return new FrameUI("Pawns - Player " + id + ": " + name, position);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public BoardUI<PawnsState, PawnsAction> createBoard(int id, ColorModel cm,
			PawnsState s, BoardListener<PawnsState, PawnsAction> listener) {
		return new PawnsBoardUI(id, cm, s, listener);
	}
}
