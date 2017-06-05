package was;

import base.model.GameAction;
import base.model.GameState.Coord;

/**
 * An action for Wolf and Sheeps.
 * @author claudiaggh & errachete
 * @version 1 (13/03/2017)
 */
public class WasAction implements GameAction<WasState, WasAction> {

	private static final long serialVersionUID = -8491198872908329925L;
	
	private int player;
    private WasState.Coord iniPos;
    private WasState.Coord endPos;

    
    public WasAction(int player, WasState.Coord fin, WasState.Coord ini) {
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
    public WasState applyTo(WasState state) {
        if (player != state.getTurn()) {
            throw new IllegalArgumentException("Not the turn of this player");
        }

        // make move
        WasState.Coord[] pieces = state.getPieces();
        for(int i = 0; i < pieces.length; ++i){
        	if(pieces[i].isAt(iniPos.row, iniPos.col))
        		pieces[i] = endPos;
        }

        // update state
        WasState next = new WasState(state, pieces, state.isFinished(), -1);
        if (next.isWinner((state.getTurn()))) {
        	next = new WasState(state, pieces, true, state.getTurn());
        } else if (next.getTurn() == next.SHEEP && next.sheepBlocked()){
        	/* When the sheeps are blocked, we want to skip their turn.
        		In order to do this, we use the property of the constructor
        		change the turn. State is just an auxiliary state.*/
        	state = new WasState(state, pieces, false, -1);
        	next = new WasState(state, pieces, false, -1);
        } else {
            next = new WasState(state, pieces, false, -1);
        }
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
        if(player == 0)
        	show = show + "wolf";
        else
        	show = show + "sheep";
        show = show + " from " + iniPos + " to " + endPos;
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
		WasAction other = (WasAction) obj;
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
