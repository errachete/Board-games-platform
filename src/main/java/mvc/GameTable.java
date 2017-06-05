package mvc;

import java.util.ArrayList;
import java.util.logging.Logger;

import base.model.GameAction;
import base.model.GameError;
import base.model.GameState;
import mvc.GameEvent.EventType;

/**
 * An event-driven game engine.
 * Keeps a list of players and a state, and notifies observers
 * of any changes to the game.
 * @author claudiaggh & errachete
 * @version 1 (03/05/2017)
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

	private Logger log;
    private S initState;
    private S actualState;
    private boolean finished;
    private boolean started;
    private ArrayList< GameObserver< S, A > > obs;

    public GameTable(S initState) {
    	this.log = Logger.getLogger("log");
        this.initState = initState;
        this.actualState = initState;
        this.started = false;
        this.finished = false;
        this.obs = new ArrayList< GameObserver< S, A > >();
    }
    /**
     * Starts the game and notify observers
     */
    public void start() {
        actualState = initState;
        started = true;
        finished = false;
        GameEvent< S, A> start = new GameEvent< S, A >(EventType.Start, null, actualState, null, null);
        notifyObservers(start);
    }
    /**
     * Stops the game and notify observers
     */
    public void stop() {
    	if(!finished) {
    		finished = true;
    		GameEvent< S, A > stop = new GameEvent<S, A>(EventType.Stop, null, actualState, null, null);
    		notifyObservers(stop);
    	} else {
    		GameError error = new GameError("The game is already stopped");
    		GameEvent< S, A > event = 
    				new GameEvent< S, A >(EventType.Error, null, null, error, null);
    		notifyObservers(event);
    		throw error;
    	}
    }
    /**
     * Executes the action given if possible and notify observers. If the game finishes, it stops the game
     * @param action to be executed
     */
    public void execute(A action) {
        if(finished || !started) {
        	GameError error = new GameError("The game is stopped or not started");
        	GameEvent< S, A > event = 
    				new GameEvent< S, A >(EventType.Error, null, null, error, null);
        	notifyObservers(event);
        } else {
        	try {
        		S newState = action.applyTo(actualState);
        		actualState = newState;
        		GameEvent< S, A > movement = new GameEvent< S, A >(EventType.Change, 
        				action, actualState, null, null);
        		notifyObservers(movement);
        		if(actualState.isFinished())
        			stop();
        	} catch (IllegalArgumentException e) {
        		GameError error = new GameError("The game is stopped or not started", e);
            	GameEvent< S, A > event = 
        				new GameEvent< S, A >(EventType.Error, null, null, error, null);
            	notifyObservers(event);
            	throw error;
        	}
        }
    }
    
    public boolean isStopped() {
    	return finished;
    }
   
    public S getState() {
		return actualState;
    }
    public void addObserver(GameObserver<S, A> o) {
        obs.add(o);
    }
    public void removeObserver(GameObserver<S, A> o) {
        obs.remove(o);
    }
    public void notifyObservers(GameEvent<S, A> e) {
    	log.info("Sent event of type " + e.getType());
    	for(GameObserver<S, A> a : obs){
    		a.notifyEvent(e);
    	}
    }
}
