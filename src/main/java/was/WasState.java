package was;

import java.util.ArrayList;
import java.util.List;

import base.model.GameState;

/**
 * A state for the game "Wolf and Sheeps" storing all the relevant information
 * needed to keep playing.
 * 
 * @author claudiaggh & errachete
 * @version 1 (13/03/2017)
 */

public class WasState extends GameState<WasState, WasAction> {

	/**
	 * Class representing a coordinate on the game board
	 */
	public static class Coord {
		public int row;
		public int col;

		public Coord(int row, int col) {
			this.row = row;
			this.col = col;
		}

		public Coord add(Coord o) {
			return new Coord(this.row + o.row, this.col + o.col);
		}

		public boolean isAt(int r, int c) {
			return r == row && c == col;
		}

		public String toString() {
			return "(" + row + ", " + col + ")";
		}

		public Coord copy() {
			return new Coord(row, col);
		}

		public boolean equal(Coord other) {
			return col == other.col && row == other.row;
		}
	}

	private static final long serialVersionUID = -6066312347935012935L;

	private final int turn;
	private boolean finished;
	private final int winner;
	private final Coord[] pieces;
	public final static int dim = 8;

	/**
	 * initial positions for wolf (1st) and sheep (rest)
	 */
	private final static Coord[] initialPositions = { new Coord(dim - 1, 0),
			new Coord(0, 1), new Coord(0, 3), new Coord(0, 5), new Coord(0, 7) };

	/**
	 * Different coordinates representing how can a player move on the board. A
	 * sheep can move as the two first ones and the wolf as the four of them.
	 */
	private final static Coord[] moves = { new Coord(1, -1), new Coord(1, 1),
			new Coord(-1, -1), new Coord(-1, 1) };

	public final int WOLF = 0;
	public final int SHEEP = 1;
	public final int EMPTY = -1;
	public final int OUTSIDE = -2;

	public WasState() {
		super(2);
		this.pieces = initialPositions.clone();
		this.turn = 0;
		this.winner = -1;
		this.finished = false;
	}

	public WasState(WasState prev, Coord[] pieces, boolean finished, int winner) {
		super(2);
		this.pieces = pieces.clone();
		this.turn = (prev.turn + 1) % 2;
		this.finished = finished;
		this.winner = winner;
	}

	@Override
	public double evaluate(int playerNumber) {
		double v = super.evaluate(playerNumber);
		if (v == 0) {
			if (playerNumber == WOLF) {
				v = (7 - pieces[WOLF].row) * .1;
			}
		}
		return v;
	}

	/**
	 * Method that checks whether a given action is valid or not according to
	 * the rules and the board
	 * 
	 * @param action
	 *            to check
	 * @return valid
	 */
	public boolean inRange(WasAction action) {
		boolean valid = false;
		if (isFinished()) {
			return false;
		} else if (action.getRow() >= 0 && action.getCol() >= 0
				&& action.getRow() < dim && action.getCol() < dim) {
			valid = at(action.getRow(), action.getCol()) == EMPTY;
		}
		return valid;
	}

	public boolean isValid(WasAction action) {
		List<WasAction> valids = validActions(action.getPlayerNumber(), action.getIniPos());
		boolean found = false;
		for (int i = 0; i < valids.size() && !found; ++i) {
			found = action.getIniPos().equal(valids.get(i).getIniPos()) && 
					action.getEndPos().equal(valids.get(i).getEndPos());
		}
		return found;
	}

	/**
	 * Method that given the integer representing a player makes a list of his
	 * valid actions in the actual moment of the game and returns it
	 * 
	 * @param playerNumber
	 * @return list of valid actions
	 */
	public List<WasAction> validActions(int playerNumber) {
		List<WasAction> valid = new ArrayList<>();
		if (finished) {
			return valid;
		}
		Coord newPos;
		if (playerNumber == WOLF) {
			for (int i = 0; i < 4; ++i) {
				newPos = pieces[0].add(moves[i]);
				WasAction action = new WasAction(playerNumber, newPos,
						pieces[0]);
				if (inRange(action)) {
					valid.add(action);
				}
			}
		} else {
			for (int sheep = 1; sheep < 5; ++sheep) {
				for (int i = 0; i < 2; ++i) {
					newPos = pieces[sheep].add(moves[i]);
					WasAction action = new WasAction(playerNumber, newPos,
							pieces[sheep]);
					if (inRange(action)) {
						valid.add(action);
					}
				}
			}
		}
		return valid;
	}
	
	public List<WasAction> validActions(int playerNumber, Coord piece){
		List<WasAction> actions = new ArrayList<>();
		for(int i = 0; i < 2; ++i) {
			Coord newPos = piece.add(moves[i]);
			WasAction action = new WasAction(playerNumber, newPos, piece);
			if(inRange(action))
				actions.add(action);
		}
		if(playerNumber == WOLF) {
			for(int i = 2; i < 4; ++i) {
				Coord newPos = piece.add(moves[i]);
				WasAction action = new WasAction(playerNumber, newPos, piece);
				if(inRange(action))
					actions.add(action);
			}
		}
		return actions;
	}

	/**
	 * Method that checks whether a given player has already won the game or not
	 * 
	 * @param playerNumber
	 * @return
	 */
	public boolean isWinner(int playerNumber) {
		if (playerNumber == WOLF) {
			return pieces[0].row == 0;
		} else {
			return validActions(WOLF).isEmpty();
		}
	}

	/**
	 * Method that checks whether the sheeps are blocked or not
	 * 
	 * @return
	 */
	public boolean sheepBlocked() {
		return validActions(SHEEP).isEmpty();
	}

	/**
	 * Method that given two integers representing the x and y coordinate of a
	 * position in the board, returns the integer representing what is in the
	 * position; wolf: 0, sheep: 1, nothing: -1, out of board: -2
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public int at(int row, int col) {
		if (row < 0 || col < 0 || row >= dim || col >= dim)
			return OUTSIDE;
		int i = 0;
		for (Coord c : pieces) {
			if (c.isAt(row, col))
				return i;
			i = 1;
		}
		return EMPTY;
	}

	public int getTurn() {
		return turn;
	}

	public Coord[] getPieces() {
		return pieces.clone();
	}

	public boolean isFinished() {
		return finished;
	}

	public int getWinner() {
		return winner;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("\n  ");
		for (int j = 0; j < dim; j++) {
			sb.append("  " + j + " ");
		}
		sb.append("\n");
		sb.append("  ");
		for (int j = 0; j < dim; ++j) {
			sb.append(" ---");
		}
		sb.append("\n");

		for (int i = 0; i < dim; i++) {
			sb.append(i + " |");
			for (int j = 0; j < dim; j++) {
				sb.append(at(i, j) == EMPTY ? "   |" : at(i, j) == 0 ? " W |"
						: " S |");
			}
			sb.append("\n");
			sb.append("  ");
			for (int j = 0; j < dim; ++j) {
				sb.append(" ---");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
}
