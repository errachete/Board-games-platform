package reversi;

import java.awt.Graphics;
import java.util.List;
import java.util.logging.Logger;

import base.model.GameState.Coord;
import view.BoardUI;
import view.ColorTableUI.ColorModel;

public class ReversiBoardUI extends BoardUI<ReversiState, ReversiAction> {

	private static final long serialVersionUID = -2113362344001157019L;

	public ReversiBoardUI(int id, ColorModel cm, ReversiState state,
			BoardListener<ReversiState, ReversiAction> listener) {
		super(id, cm, state, listener);
	}

	@Override
	public void paintSelected(Graphics g) {
		if(state.getTurn() == id) {
			List<ReversiAction> possible = state.validActions(state.getTurn());
			g.setColor(cm.at(state.getTurn()));
			for (int j = 0; j < possible.size(); ++j) {
				Coord cell = possible.get(j).getPos();
				for (int i = 2; i <= 4; ++i)
					g.drawRect(cell.col * _CELL_WIDTH + _SEPARATOR + i, cell.row * _CELL_HEIGHT + _SEPARATOR + i,
							_CELL_WIDTH - i * _SEPARATOR - 4, _CELL_HEIGHT - i * _SEPARATOR - 4);
			}
		}
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		Logger.getLogger("log").info("Player " + id + " clicked on cell " + new Coord(row, col));
		Coord click = new Coord(row, col);
		if(id == state.getTurn() && !state.isFinished()) {
			ReversiAction action = new ReversiAction(state.getTurn(), click);
			if (state.isValid(action)) {
				listener.sendMessage("You have put a piece in " + action.getPos() + '.');
				listener.sendMessage("Turn of player " + (action.getPlayerNumber() + 1) % 2 + '.');
				listener.makeManualMove(action);
			}
		}
		
	}

	@Override
	protected Integer getPosition(int row, int col) {
		if (state.at(new Coord(row, col)) != state.EMPTY)
			return state.at(new Coord(row, col));
		else
			return null;
	}

	@Override
	public void nullSelected() {}

	@Override
	protected int getNumRows() {
		return ReversiState.dim;
	}

	@Override
	protected int getNumCols() {
		return ReversiState.dim;
	}

}
