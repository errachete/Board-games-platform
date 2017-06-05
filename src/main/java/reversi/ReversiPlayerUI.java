package reversi;

import mvc.GameTable;
import view.BoardUI;
import view.BoardUI.BoardListener;
import view.ColorTableUI.ColorModel;
import view.FrameUI;
import view.PlayerUI;

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
