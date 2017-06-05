package base.player;

import base.model.GameAction;
import base.model.GamePlayer;
import base.model.GameState;

/**
 * A player that can play any game.
 */
public class AiPlayer implements GamePlayer {

    protected String name;

    protected int playerNumber;
    protected AiAlgorithm algorithm;

    public AiPlayer(String name, AiAlgorithm algorithm) {
        this.name = name;
        this.algorithm = algorithm;
    }

    @Override
    public void join(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    @Override
    public int getPlayerNumber() {
    	return playerNumber;
    }

    @Override
    public String getName() {
        return name;
    }

	public <S extends GameState<S,A>, A extends GameAction<S,A>> A requestAction(S state) {
        return algorithm.chooseAction(playerNumber, state);
    }
}
