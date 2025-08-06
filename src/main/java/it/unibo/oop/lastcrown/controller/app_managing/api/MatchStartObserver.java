package it.unibo.oop.lastcrown.controller.app_managing.api;

import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.map.MatchView;
import it.unibo.oop.lastcrown.view.menu.api.MainView;

/**
 * Interface for observing and managing match lifecycle events within the game.
 * Implementations of this interface handle the initialization and termination
 * of the game match, including starting and stopping the game loop,
 * and managing the associated MatchController
 */
public interface MatchStartObserver {

    /**
     * Called when the game match is about to start.
     * Responsible for initializing the MatchController and starting the game loop.
     *
     * @param width the width of the game world
     * @param height the height of the game world
     * @param id the identifier of the player's selected card
     * @param collectionController controller for managing the player's collection
     * @param mainView the main game view to return to after the match
     * @param enemyList identifier of the enemy list for the match
     */
    void onMatchStart(int width, int height, CardIdentifier id,
                      CollectionController collectionController, MainView mainView, int enemyList);

    /**
     * Called when the game match is about to end.
     * Handles stopping the game loop and cleaning up the match state.
     */
    void stopMatchLoop();

    /**
     * Resumes the game loop if it was previously stopped.
     * If the game loop is already running, this method has no effect.
     *
     * @throws IllegalStateException if the MatchController has not been initialized
     */
    void resumeMatchLoop();

    /**
     * Returns the reference to the current MatchController instance,
     * if it has been initialized.
     * This will return null if onMatchStart has not been called yet.
     *
     * @return the current MatchController instance, or null if not initialized
     */
    MatchController getMatchControllerReference();

    /**
     * Sets the view for the current match.
     *
     * @param matchView the view to be used for the ongoing match
     */
    void getMatchView(MatchView matchView);
}
