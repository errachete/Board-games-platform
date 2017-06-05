package reversi;

import base.model.GameAction;
import was.WasState;
import was.WasState.Coord;

public class ReversiAction implements GameAction<ReversiState, ReversiAction> {

	private static final long serialVersionUID = -1182972360861515180L;

	private int player;
	private WasState.Coord pos;

	public ReversiAction(int player, Coord pos) {
		this.player = player;
		this.pos = pos.copy();
	}

	@Override
	public int getPlayerNumber() {
		return player;
	}

	@Override
	public ReversiState applyTo(ReversiState state) {
		if(player != state.getTurn()) {
			throw new IllegalArgumentException("Not the turn of thsi player");
		}
		
		// make move
		int[][] board = state.getBoard();
		board[pos.row][pos.col] = player;
		for(int i = 0; i < ReversiState.DIR.length; ++i) {
			Coord ini = pos;
			Coord adv = ini.add(ReversiState.DIR[i]);
			while(state.at(adv) == (player + 1) % 2) {
				adv = adv.add(ReversiState.DIR[i]);
			}
			if(state.at(adv) == player) {
				adv = ini.add(ReversiState.DIR[i]);
				while(state.at(adv) == (player + 1) % 2) {
					board[adv.row][adv.col] = player;
					adv = adv.add(ReversiState.DIR[i]);
				}
			}
		}
		
		// update state
		ReversiState next = new ReversiState(state, board, state.isFinished(), -1);
		if(next.validActions(player).isEmpty() && next.validActions((player + 1) % 2).isEmpty()) {
			next = new ReversiState(state, board, true, next.isWinner());
		} else if (next.validActions((player + 1) % 2).isEmpty()) {
			state = new ReversiState(state, board, false, -1);
			next = new ReversiState(state, board, false, -1);
		} else {
			next = new ReversiState(state, board, false, -1);
		}
		return next;
	}
	
	public Coord getPos() {
		return pos;
	}
	
	public String toString() {
        String show = "Put a piece in " + pos;
        return show;
    }

}
