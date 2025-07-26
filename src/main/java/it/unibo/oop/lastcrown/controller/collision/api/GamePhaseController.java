package it.unibo.oop.lastcrown.controller.collision.api;

import it.unibo.oop.lastcrown.controller.collision.impl.GameState;

/**
 * Interface for managing transitions between different game states.
 */
public interface GamePhaseController {

    /**
     * Changes the current game state to the specified new state.
     *
     * @param newState the new GameState to transition to
     */
    void changeState(GameState newState);

    /**
     * Returns the current state of the game.
     *
     * @return the current GameState
     */
    GameState getCurrentState();
}
