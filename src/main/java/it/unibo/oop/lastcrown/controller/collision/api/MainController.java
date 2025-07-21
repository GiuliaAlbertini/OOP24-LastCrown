package it.unibo.oop.lastcrown.controller.collision.api;

import it.unibo.oop.lastcrown.view.collision.api.MainView;

/**
 * Main controller interface for managing the overall flow of the game.
 * Acts as a central access point for other controllers and the main view.
 */
public interface MainController {

    /**
     * Returns the main view of the application, which manages
     * the different UI panels and scenes.
     *
     * @return the MainView instance used by the game
     */
    MainView getMainView();

    /**
     * Starts a new game session, initializing all necessary components
     * and transitioning to the appropriate starting state.
     */
    void startNewGame();

    /**
     * Returns the game controller, responsible for managing
     * exploration and in-game interactions.
     *
     * @return the GameController used during exploration
     */
    GameController getGameController();

    /**
     * Returns the combat controller, responsible for managing
     * turn-based combat mechanics and interactions during fights.
     *
     * @return the CombatController used during combat
     */
    MatchController getMatchController();
}
