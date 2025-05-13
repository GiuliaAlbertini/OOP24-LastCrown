package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A controller that handles the behaviour of a single playable character in game.
 */
public interface PlayableCharacterController extends GenericCharacterController {

    /**
     * Make this character jump up.
     */
    void jumpUp();

    /**
     * Make this character jump down.
     */
    void jumpDown();

    /**
     * Make this character jump forward.
     */
    void jumpForward();

    /**
     * @return the action range of this character.
     */
    int getActionRange();
}

