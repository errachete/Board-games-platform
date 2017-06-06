package pawns;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.logging.Logger;

import base.model.GameState.Coord;
import view.swing.BoardUI;
import view.swing.ColorTableUI.ColorModel;

/**
 * A board for Pawns.
 * @author errachete
 * @version 2 (03/05/2017)
 */
public class PawnsBoardUI extends BoardUI<PawnsState, PawnsAction> {

	private static final long serialVersionUID = -6806220148484079355L;

	/**
	 * Selected cell on the board
	 */
	private Coord selected;

	/**
	 * {@inheritDoc}
	 * It sets selected to null.
	 */
	public PawnsBoardUI(int id, ColorModel cm, PawnsState state, BoardListener<PawnsState, PawnsAction> listener) {
		super(id, cm, state, listener);
		this.selected = null;
	}

	/**
	 * {@inheritDoc}
	 * In Was, if you are a sheep, you need to select the piece first and the click into a valid cell to move it
	 * and if you are the wolf just click into any correct cell to move.
	 * If the click does not produce a valid action, it doesn't make anything.
	 */
	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {
		Logger.getLogger("log").info("Player " + id + " clicked on cell " + new Coord(row, col));
		Coord click = new Coord(row, col);
		if (!state.isFinished() && id == state.getTurn()){
			if (state.at(click) == state.getTurn()) {
				selected = click;
				repaint();
				listener.sendMessage(
						"Selected " + selected + ". Click cell to move or another piece to change selection.");
			} else if (selected != null && state.at(selected) == state.getTurn()) {
				PawnsAction action = new PawnsAction(id, click, selected);
				if (state.isValid(action)) {
					listener.sendMessage(
							"You have moved from " + action.getIniPos() + " to " + action.getEndPos() + '.');
					listener.sendMessage("Turn of player " + (action.getPlayerNumber() + 1) % 2 + '.');
					listener.makeManualMove(action);
					selected = null;
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Integer getPosition(int row, int col) {
		if (state.at(new Coord(row, col)) != PawnsState.EMPTY)
			return state.at(new Coord(row, col));
		else
			return null;
	}

	@Override
	protected int getNumRows() {
		return PawnsState.dim;
	}

	@Override
	protected int getNumCols() {
		return PawnsState.dim;
	}

	/**
	 * {@inheritDoc}
	 * In pawns we need to set selected to null
	 */
	public void nullSelected() {
		selected = null;
	}

	/**
	 * {@inheritDoc}
	 * In was this method paints a square in the cells round the piece that is about to move to mark
	 * the valid options to move. Furthermore, it paints a circle around the selected piece if you are
	 * the sheep.
	 */
	@Override
	public void paintSelected(Graphics g) {
		if (state.getTurn() == id && selected != null) {
			g.setColor(inverseColor(cm.at(state.getTurn())));
			g.fillOval(selected.col * _CELL_WIDTH + _SEPARATOR + 2, selected.row * _CELL_HEIGHT + _SEPARATOR + 2,
					_CELL_WIDTH - 2 * _SEPARATOR - 4, _CELL_HEIGHT - 2 * _SEPARATOR - 4);
			g.setColor(cm.at(state.getTurn()));
			g.fillOval(selected.col * _CELL_WIDTH + _SEPARATOR + 6, selected.row * _CELL_HEIGHT + _SEPARATOR + 6,
					_CELL_WIDTH - 6 * _SEPARATOR - 4, _CELL_HEIGHT - 6 * _SEPARATOR - 4);

			List<PawnsAction> possible = state.validActions(state.getTurn(), selected);
			g.setColor(cm.at(state.getTurn()));
			for (int j = 0; j < possible.size(); ++j) {
				Coord cell = possible.get(j).getEndPos();
				for (int i = 2; i <= 4; ++i)
					g.drawRect(cell.col * _CELL_WIDTH + _SEPARATOR + i, cell.row * _CELL_HEIGHT + _SEPARATOR + i,
							_CELL_WIDTH - i * _SEPARATOR - 4, _CELL_HEIGHT - i * _SEPARATOR - 4);
			}
		}
	}

	/**
	 * Function that returns the inverse color of a given.
	 * @param c Color to inverse
	 * @return Inverse color
	 */
	private Color inverseColor(Color c) {
		int red = 255 - c.getRed();
		int green = 255 - c.getGreen();
		int blue = 255 - c.getBlue();
		return new Color(red, green, blue);
	}
}
