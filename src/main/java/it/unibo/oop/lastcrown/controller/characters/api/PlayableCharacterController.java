package it.unibo.oop.lastcrown.controller.characters.api;

import it.unibo.oop.lastcrown.view.characters.api.Movement;

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

     /**
     * Set the running animation to be manual: the character panel will move
     * itself following the given instruction.
     */
    void setManualRunningAnimation();

    /**
     * the running animation will be set to the default one.
     */
    void stopManualRunningAnimation();

    /**
     * Must be called after setting manual running animation.
     * The panel will move itself following the given movement
     * and it will set the next running animation frame.
     * @param movement the movement of the panel
     */
    void movePanel(Movement movement);
}

