package was;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;
import java.util.logging.Logger;

import view.BoardUI;
import view.ColorTableUI.ColorModel;
import was.WasState.Coord;

/**
 * A board for Wolf and Sheep.
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public class WasBoardUI extends BoardUI<WasState, WasAction> {

	private static final long serialVersionUID = -6806220148484079355L;

	/**
	 * Selected cell on the board
	 */
	private Coord selected;

	/**
	 * {@inheritDoc}
	 * It sets selected to null.
	 */
	public WasBoardUI(int id, ColorModel cm, WasState state, BoardListener<WasState, WasAction> listener) {
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
		if (state.getTurn() == state.WOLF && id == state.getTurn() && !state.isFinished()) {
			WasAction action = new WasAction(state.WOLF, click, state.getPieces()[state.WOLF]);
			if (state.isValid(action)) {
				listener.sendMessage("You have moved from " + action.getIniPos() + " to " + action.getEndPos() + '.');
				listener.sendMessage("Turn of player " + (action.getPlayerNumber() + 1) % 2 + '.');
				listener.makeManualMove(action);
				selected = null;
			}
		} else if (!state.isFinished() && id == state.getTurn()){
			if (state.at(row, col) == state.SHEEP) {
				selected = click;
				repaint();
				listener.sendMessage(
						"Selected " + selected + ". Click cell to move or another piece to change selection.");
			} else if (selected != null && state.at(selected.row, selected.col) == state.SHEEP) {
				WasAction action = new WasAction(state.SHEEP, click, selected);
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
		if (state.at(row, col) != state.EMPTY)
			return state.at(row, col);
		else
			return null;
	}

	@Override
	protected int getNumRows() {
		return WasState.dim;
	}

	@Override
	protected int getNumCols() {
		return WasState.dim;
	}

	/**
	 * {@inheritDoc}
	 * In was we need to set selected to null
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
		if (state.getTurn() == state.SHEEP && id == state.SHEEP && selected != null) {
			g.setColor(inverseColor(cm.at(state.getTurn())));
			g.fillOval(selected.col * _CELL_WIDTH + _SEPARATOR + 2, selected.row * _CELL_HEIGHT + _SEPARATOR + 2,
					_CELL_WIDTH - 2 * _SEPARATOR - 4, _CELL_HEIGHT - 2 * _SEPARATOR - 4);
			g.setColor(cm.at(state.getTurn()));
			g.fillOval(selected.col * _CELL_WIDTH + _SEPARATOR + 6, selected.row * _CELL_HEIGHT + _SEPARATOR + 6,
					_CELL_WIDTH - 6 * _SEPARATOR - 4, _CELL_HEIGHT - 6 * _SEPARATOR - 4);

			List<WasAction> possible = state.validActions(state.getTurn(), selected);
			g.setColor(cm.at(state.getTurn()));
			for (int j = 0; j < possible.size(); ++j) {
				Coord cell = possible.get(j).getEndPos();
				for (int i = 2; i <= 4; ++i)
					g.drawRect(cell.col * _CELL_WIDTH + _SEPARATOR + i, cell.row * _CELL_HEIGHT + _SEPARATOR + i,
							_CELL_WIDTH - i * _SEPARATOR - 4, _CELL_HEIGHT - i * _SEPARATOR - 4);
			}
		}
		else if(state.getTurn() == state.WOLF && id == state.WOLF) {
			List<WasAction> possible = state.validActions(state.getTurn(), state.getPieces()[state.WOLF]);
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
