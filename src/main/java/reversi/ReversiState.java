package reversi;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import base.model.GameState;

public class ReversiState extends GameState<ReversiState, ReversiAction> {

	private static final long serialVersionUID = 247754110329111286L;

	private final int turn;
	private boolean finished;
	private final int winner;
	private final int[][] board;
	public final static int dim = 8;

	static final Coord[] DIR = { new Coord(1, 0), new Coord(0, -1), new Coord(-1, 0), new Coord(0, 1), new Coord(1, 1),
			new Coord(1, -1), new Coord(-1, -1), new Coord(-1, 1) };

	public final int EMPTY = -1;
	public final int OUTSIDE = -2;

	public ReversiState() {
		super(2);
		this.board = new int[dim][dim];
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				this.board[i][j] = EMPTY;
			}
		}
		board[3][4] = board[4][3] = 1;
		board[3][3] = board[4][4] = 0;
		this.turn = new Random().nextInt() % 2;
		this.winner = -1;
		this.finished = false;
	}

	public ReversiState(ReversiState prev, int[][] board, boolean finished, int winner) {
		super(2);
		this.board = board;
		this.turn = (prev.turn + 1) % 2;
		this.finished = finished;
		this.winner = winner;
	}

	@Override
	public int getTurn() {
		return turn;
	}

	@Override
	public boolean isValid(ReversiAction action) {
		boolean valid = false;
		boolean moved = false;
		for (int i = 0; i < DIR.length && !valid; ++i) {
			moved = false;
			Coord ini = action.getPos();
			Coord adv = ini.add(DIR[i]);
			while (at(adv) == (action.getPlayerNumber() + 1) % 2) {
				moved = true;
				adv = adv.add(DIR[i]);
			}
			valid = (at(adv) == action.getPlayerNumber()) && moved;
		}
		return valid;
	}

	@Override
	public List<ReversiAction> validActions(int playerNumber) {
		List<ReversiAction> valid = new ArrayList<>();
		if (finished) {
			return valid;
		}
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				if (at(new Coord(i, j)) == EMPTY && isValid(new ReversiAction(playerNumber, new Coord(i, j)))) {
					valid.add(new ReversiAction(playerNumber, new Coord(i, j)));
				}
			}
		}
		return valid;
	}

	public int isWinner() {
		int zero = 0, one = 0;
		for (int i = 0; i < dim; ++i) {
			for (int j = 0; j < dim; ++j) {
				if (board[i][j] == 0)
					++zero;
				else if (board[i][j] == 1)
					++one;
			}
		}
		if (zero > one)
			return 0;
		else if (zero < one)
			return 1;
		else
			return -1;
	}

	public int at(Coord pos) {
		if (pos.row < 0 || pos.col < 0 || pos.row >= dim || pos.col >= dim)
			return OUTSIDE;
		else
			return board[pos.row][pos.col];
	}
	
	public int[][] getBoard() {
		int[][] copy = new int[board.length][];
		for (int i = 0; i < board.length; i++)
			copy[i] = board[i].clone();
		return copy;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
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
				sb.append(at(new Coord(i, j)) == EMPTY ? "   |" : at(new Coord(i, j)) == 0 ? " 0 |"
						: " 1 |");
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
