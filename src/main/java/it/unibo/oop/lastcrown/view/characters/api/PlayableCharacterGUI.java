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
}
