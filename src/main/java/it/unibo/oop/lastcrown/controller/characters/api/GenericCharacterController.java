package it.unibo.oop.lastcrown.controller.characters.api;

import javax.swing.JComponent;

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
     * @return the graphical component linked to this character controller
     */
    JComponent getGraphicalComponent();

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
     * Applies a variation to the actual character attack value.
     * @param variation the amount of attack variation. Can be positive
     * to increase the attack or negative to decrease it
     */
    void setAttackValue(int variation);

    /**
     * Applies a variation to the actual character maximum health value.
     * @param variation the amount of health variation. Can be positive
     * to increase the maximum health or negative to decrease it
     */
    void setMaximumHealthValue(int variation);

    /**
     * Applies a variation to the actual character speed multiplier value.
     * @param variation the amount of speedMultiplier variation. Can be positive
     * to increase the speed multiplier or negative to decrease it
     */
    void setSpeedMultiplierValue(double variation);

    /**
     * The linked character health will be restore.
     * The amount of healing is specified by the param.
     * @param cure the amount of healing this linked character will take.
     */
    void heal(int cure);
}
