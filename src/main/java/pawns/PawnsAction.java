package pawns;

import base.model.GameAction;
import base.model.GameState.Coord;

/**
 * An action for Pawns.
 * @author errachete
 * @version 1 (13/03/2017)
 */
public class PawnsAction implements GameAction<PawnsState, PawnsAction> {

	private static final long serialVersionUID = -8491198872908329925L;
	
	private int player;
    private Coord iniPos;
    private Coord endPos;

    
    public PawnsAction(int player, PawnsState.Coord fin, PawnsState.Coord ini) {
        this.player = player;
        this.endPos = fin.copy();
        this.iniPos = ini.copy();
        
    }

    public int getPlayerNumber() {
        return player;
    }

    /**
     * Method which applies the action to the game state given
     * @param state It contains the current game state
     * @return WasState which contains the new game state after applying the action
     */
    public PawnsState applyTo(PawnsState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        int[][] board = state.getBoard();
        board[iniPos.row][iniPos.col] = PawnsState.EMPTY;
        board[endPos.row][endPos.col] = player;

        // update state
        PawnsState next = new PawnsState(state, board, state.isFinished(), -1);
        if (next.isWinner((state.getTurn()))) {
        	next = new PawnsState(state, board, true, state.getTurn());
        } else if (next.validActions(next.getTurn()).isEmpty()){
        	state = new PawnsState(state, board, false, state.getTurn());
        	next = new PawnsState(state, board, false, state.getTurn());
        } else if (next.isDraw()){
            next = new PawnsState(state, board, true, -1);
        } else
        	next = new PawnsState(state, board, false, state.getTurn());
        return next;
    }
    
    public Coord getIniPos(){
    	return iniPos;
    }
    
    public Coord getEndPos(){
    	return endPos;
    }
    
    public int getRow() {
        return endPos.row;
    }
    
    public int getCol() {
        return endPos.col;
    }

    public String toString() {
        String show = "Move ";
        show = show + "from " + iniPos + " to " + endPos;
        return show;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endPos == null) ? 0 : endPos.hashCode());
		result = prime * result + ((iniPos == null) ? 0 : iniPos.hashCode());
		result = prime * result + player;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PawnsAction other = (PawnsAction) obj;
		if (endPos == null) {
			if (other.endPos != null)
				return false;
		} else if (!endPos.equals(other.endPos))
			return false;
		if (iniPos == null) {
			if (other.iniPos != null)
				return false;
		} else if (!iniPos.equals(other.iniPos))
			return false;
		if (player != other.player)
			return false;
		return true;
	}
    
    

}
