package it.unibo.oop.lastcrown.controller.api;

import it.unibo.oop.lastcrown.controller.impl.GameState;

/**
 * Interface for managing transitions between different game states.
 */
public interface GamePhaseController {

    /**
     * Changes the current game state to the specified new state.
     *
     * @param newState the new GameState to transition to
     */
    void changeState(final GameState newState);

    /**
     * Returns the current state of the game.
     *
     * @return the current GameState
     */
    GameState getCurrentState();
}
