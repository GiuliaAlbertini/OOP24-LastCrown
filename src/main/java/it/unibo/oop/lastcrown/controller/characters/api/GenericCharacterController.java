package it.unibo.oop.lastcrown.controller.characters.api;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A controller that handles the behaviour of a single Generic Character in game.
 */
public interface GenericCharacterController extends CharacterAttackObserver, CharacterHitObserver {
    /**
     * @return this controller id
     */
    CardIdentifier getId();

    /**
     * Create a new graphic component of this character.
     * @param width the width of the character animation panel
     * @param height the height of the character animation panel
     */
    void attachCharacterAnimationPanel(int width, int height);

    /**
     * Must be called after creating a new character animation panel.
     * Set the new main panel of this character animation panel
     * and its position in it (centered on the given coordinates).
     * @param mainPanel the panel where the character animation panel must spawn
     * @param initialX horizontal coordinate
     * @param initialY vertical coordinate
     * @throws NullPointerException if this character animation panel is null
     */
    void setCharacterPanelPosition(JPanel mainPanel, int initialX, int initialY);

    /**
     * Make the linked character start running.
     */
    void startRunning();

    /**
     * Make the linked character stop.
     */
    void stop();

    /**
     * Set this character controller current opponent.
     * @param opponentObserver the opponent observer
     * @throws NullPointerException if the given opponent observer is null
     */
    void setOpponent(CharacterHitObserver opponentObserver);

    /**
     * Must be called after setting one opponent.
     * Make this character start attacking his opponent.
     * @throws NullPointerException if the actual opponent is null
     */
    void startAttacking();

    /**
     * The linked character health will be restore.
     * The amount of healing is specified by the param.
     * @param cure the amount of healing this linked character will take.
     */
    void heal(int cure);
}
