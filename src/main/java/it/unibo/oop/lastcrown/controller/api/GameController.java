package it.unibo.oop.lastcrown.controller.api;

/**
 * Interface for the controller that manages the game loop.
 */
public interface GameController {

    /**
     * Starts the game loop.
     *
     * @param exploration true if the game should run in exploration mode, false otherwise
     */
    void run(boolean exploration);
}
