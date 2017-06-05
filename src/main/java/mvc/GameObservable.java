package mvc;

import base.model.GameAction;
import base.model.GameState;

/**
 * Can send GameEvents to GameObservers
 */
public interface GameObservable<S extends GameState<S, A>, A extends GameAction<S, A>> {
	
	/**
	 * {@inheritDoc}
	 * Adds an observer to an observable's list of observers. We assume the user won't
	 * add the same observer twice.
	 * @param o Observer to add
	 */
    void addObserver(GameObserver<S,A> o);
	/**
	 * {@inheritDoc}
	 * Removes an observer to an observable's list of observers if it's on it.
	 * @param o Observer to remove
	 */
    void removeObserver(GameObserver<S,A> o);
}
