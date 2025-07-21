package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A controller that handles the behaviour of a single playable character in game.
 */
public interface PlayableCharacterController extends GenericCharacterController {
    /**
     * @return the action range of this character.
     */
    int getActionRange();
}

