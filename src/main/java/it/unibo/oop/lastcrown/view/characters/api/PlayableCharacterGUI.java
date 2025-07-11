package it.unibo.oop.lastcrown.view.characters.api;

/**
 * An interface that handles the graphic side of a playable character.
 */
public interface PlayableCharacterGUI extends GenericCharacterGUI {

    /**
     * Make this playable character jump up.
     */
    void startJumpUpSequence();

    /**
     * Make this playable character jump down.
     */
    void startJumpDownSequence();

    /**
     * Make this playable character jump forward.
     */
    void startJumpForwardSequence();

    /**
     * Sets the running animation to be manual.
     */
    void startManualRunningAnimation();

    /**
     * Sets the next movement of the animation panel during a manual running animation.
     * @param movement
     */
    void doSelectedMovement(Movement movement);

    /**
     * Stops the manual running animation.
     */
    void stopManualRunningAnimation();
}
