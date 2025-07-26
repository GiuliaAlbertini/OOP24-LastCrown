package it.unibo.oop.lastcrown.controller.app_managing.api;

import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.map.MatchView;

/**
 * Interface for observing and managing match lifecycle events within the game.
 * <p>
 * Implementations of this interface handle the initialization and termination
 * of the game match, including starting and stopping the game loop,
 * and managing the associated {@link MatchController}.
 */
public interface MatchStartObserver {

    /**
     * Called when the game match is about to start.
     * Responsible for initializing the MatchController and starting the game loop.
     */
    void onMatchStart(int width, int height, CardIdentifier id, CollectionController collectionController);

    /**
     * Called when the game match is about to end.
     * Handles stopping the game loop and cleaning up the match state.
     */
    void onMatchEnd();

    /**
     * Returns the reference to the current MatchController instance,
     * if it has been initialized.
     * This will return null if onMatchStart() has not been called yet.
     *
     * @return the current MatchController instance, or null if not initialized
     */
    MatchController getMatchControllerReference();

    void getMatchView(final MatchView matchView);
}
