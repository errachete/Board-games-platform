package view.console;

import java.util.List;

import base.model.GameAction;
import base.model.GamePlayer;
import base.model.GameState;
import mvc.GameEvent;
import mvc.GameEvent.EventType;
import mvc.GameTable;

/**
 * Controller for the console mode
 * @author claudiaggh & errachete
 * @version 2 (03/05/2017)
 */
public class ConsoleController<S extends GameState<S, A>, A extends GameAction<S, A>>
		implements Runnable{
	
	private List<GamePlayer> players;
	private GameTable<S, A> game;

	/**
	 * Constructor with parameters that initializes the attributes to their given values
	 * @param players
	 * @param game
	 */
	public ConsoleController(List<GamePlayer> players, GameTable<S, A> game) {
		this.players = players;
		this.game = game;
	}

	/**
	 * Method that helds the motor of the game. It initializes the game and keeps it playing until
	 * the game is finished
	 */
	@Override
	public void run() {
		int playerCount = 0;
		game.start();
		GameEvent<S, A> start = new GameEvent<S,A>(EventType.Start, null, null, null, "\nNew game started: ");
		game.notifyObservers(start);		
		for (GamePlayer p : players) {
			GameEvent<S, A> welcome = new GameEvent<S,A>(EventType.Start, null, null, null, "Welcome, " + p.getName() + "!!!" + 
					"\nYou are player number " + playerCount + "\n");
			game.notifyObservers(welcome);
			p.join(playerCount++); // welcome each player, and assign playerNumber
		}
		S currentState = game.getState();
		GameEvent<S, A> initBoard = new GameEvent<S,A>(EventType.Start, null, null, null, toString(currentState));
		game.notifyObservers(initBoard);
		while (!currentState.isFinished()) {
			// request move
			GameEvent<S, A> turn = new GameEvent<S,A>(EventType.Info, null, null, null, "It's " + 
					players.get(currentState.getTurn()).getName() + "'s turn");
			game.notifyObservers(turn);
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// apply move
			game.execute(action);
			currentState = game.getState();
			GameEvent<S, A> afterAct = new GameEvent<S,A>(EventType.Change, action, currentState,
					null, "After action:\n" + currentState.toString());
			game.notifyObservers(afterAct);

			if (currentState.isFinished()) {
				// game over
				GameEvent<S, A> end = new GameEvent<S,A>(EventType.Info, null, null, null, "The game ended: ");
				game.notifyObservers(end);
				int winner = currentState.getWinner();
				if (winner == -1) {
					GameEvent<S, A> draw = new GameEvent<S,A>(EventType.Info, null, null, null, "draw!");
					game.notifyObservers(draw);
				} else {
					GameEvent<S, A> win = new GameEvent<S,A>(EventType.Info, null, null, null,
							"player " + (winner + 1) + " (" + players.get(winner).getName() + ") won!");
					game.notifyObservers(win);
				}
				game.stop();
			}
		}
	}

	private String toString(S currentState) {
		return currentState.toString();
	}

}
