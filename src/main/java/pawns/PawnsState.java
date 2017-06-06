package pawns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import base.model.GameState;

/**
 * A state for the game "Pawns" storing all the relevant information needed to
 * keep playing.
 * 
 * @author errachete
 * @version 1 (13/03/2017)
 */

public class PawnsState extends GameState<PawnsState, PawnsAction> {

	private static final long serialVersionUID = -6066312347935012935L;

	private final int turn;
	private boolean finished;
	private final int winner;
	private final int[][] board;
	public final static int dim = 4;

	private final static Coord[] moves = { new Coord(1, 0), new Coord(1, 1),
			new Coord(1, -1)};

	public final int BLACK = 0;
	public final int WHITE = 1;
	public final static int EMPTY = -1;
	public final int OUTSIDE = -2;

	public PawnsState() {
		super(2);
		this.board = new int[dim][dim];
		for(int i = 0; i < dim; ++i)
			board[0][i] = BLACK;
		for(int i = 1; i < 3; ++i)
			for(int j = 0; j < dim; ++j)
				board[i][j] = EMPTY;
		for(int i = 0; i < dim; ++i)
			board[3][i] = WHITE;
		this.turn = new Random().nextInt() % 2;
		this.winner = -1;
		this.finished = false;
	}

	public PawnsState(PawnsState prev, int[][] board, boolean finished,
			int winner) {
		super(2);
		this.board = board;
		this.turn = (prev.turn + 1) % 2;
		this.finished = finished;
		this.winner = winner;
	}

	public boolean isValid(PawnsAction action) {
		int mult = (action.getPlayerNumber() == BLACK) ? 1 : -1;
		if(action.getEndPos().equal(action.getIniPos().add(moves[0].multiply(mult)))){
			return at(action.getEndPos()) == EMPTY;
		} else if(action.getEndPos().equal(action.getIniPos().add(moves[1].multiply(mult))) ||
				action.getEndPos().equal(action.getIniPos().add(moves[2].multiply(mult)))){
			return at(action.getEndPos()) == (action.getPlayerNumber() + 1) % 2;
		} else
			return false;
	}

	/**
	 * Method that given the integer representing a player makes a list of his
	 * valid actions in the actual moment of the game and returns it
	 * 
	 * @param playerNumber
	 * @return list of valid actions
	 */
	public List<PawnsAction> validActions(int playerNumber) {
		List<PawnsAction> valid = new ArrayList<>();
		if (finished) {
			return valid;
		}
		int mult = (playerNumber == BLACK) ? 1 : -1;
		for(int i = 0; i < dim; ++i){
			for(int j = 0; j < dim; ++j){
				Coord ini = new Coord(i, j);
				if(board[i][j] == playerNumber){
					for(int k = 0; k < moves.length; ++k){
						Coord end = ini.add(moves[k].multiply(mult));
						if(isValid(new PawnsAction(playerNumber, end, ini)))
							valid.add(new PawnsAction(playerNumber, end, ini));
					}
				}
			}
		}
		return valid;
	}
	
	public List<PawnsAction> validActions(int playerNumber, Coord pos){
		List<PawnsAction> valid = validActions(playerNumber);
		List<PawnsAction> result = new ArrayList<>();
		for(PawnsAction a : valid){
			if(a.getIniPos().equal(pos))
				result.add(a);
		}
		return result;
	}

	/**
	 * Method that checks whether a given player has already won the game or not
	 * 
	 * @param playerNumber
	 * @return
	 */
	public boolean isWinner(int playerNumber) {
		int row = (playerNumber == BLACK) ? 3 : 0;
		for(int i = 0; i < dim; ++i){
			if(board[row][i] == playerNumber)
				return true;
		}
		return false;
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
	public int at(Coord pos) {
		if (pos.row < 0 || pos.col < 0 || pos.row >= dim || pos.col >= dim)
			return OUTSIDE;
		return board[pos.row][pos.col];
	}

	public int getTurn() {
		return turn;
	}

	public int[][] getBoard() {
		int[][] copy = new int[dim][];
		for(int i = 0; i < dim; ++i)
			copy[i] = board[i].clone();
		return copy;
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

	public boolean isDraw() {
		return (validActions(BLACK).isEmpty() && validActions(WHITE).isEmpty());
	}
}
