package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A controller that handles the behaviour of a single enemy in game.
 */
public interface EnemyController extends GenericCharacterController {

    /**
     * Make this enemy retreat.
     */
    void retreat();
}
