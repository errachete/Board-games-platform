package reversi;

import mvc.GameTable;
import view.swing.BoardUI;
import view.swing.FrameUI;
import view.swing.PlayerUI;
import view.swing.BoardUI.BoardListener;
import view.swing.ColorTableUI.ColorModel;

public class ReversiPlayerUI extends PlayerUI<ReversiState, ReversiAction> {

	public ReversiPlayerUI(GameTable<ReversiState, ReversiAction> game, String name, int id, int position) {
		super(game, name, id, position);
	}

	@Override
	public FrameUI createJFrame(GameTable<ReversiState, ReversiAction> ctrl, String name, int position) {
		return new FrameUI("Reversi - Player " + id + ": " + name, position);
	}

	@Override
	public BoardUI<ReversiState, ReversiAction> createBoard(int id, ColorModel cm, ReversiState s,
			BoardListener<ReversiState, ReversiAction> listener) {
		return new ReversiBoardUI(id, cm, s, listener);
	}

}
