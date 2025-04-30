package it.unibo.oop.lastcrown.controller.characters.api;

import it.unibo.oop.lastcrown.model.api.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A controller that handles the behaviour of a single Generic Character in game.
 */
public interface GenericCharacterController extends CharacterAttackObserver {
    /**
     * @return this controller id
     */
    CardIdentifier getId();

    /**
     * Create a new graphic component of this character.
     * @param width the width of the character animation panel
     * @param height the height of the character animation panel
     */
    void attachCharacterView(int width, int height);

    /**
     * Must be called after creating a new character animation panel.
     * Set this character animation panel position (centered on the given coordinates).
     * @param initialX horizontal coordinate
     * @param initialY vertical coordinate
     * @throws NullPointerException if this character animation panel is null
     */
    void setCharacterPanelPosition(int initialX, int initialY);

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
     * @param opponent this character controller new opponent
     * @throws NullPointerException if the given opponent is null
     */
    void setOpponent(GenericCharacterController opponent);

    /**
     * Must be called after setting one opponent.
     * Make this character start attacking his opponent.
     * @throws NullPointerException if the actual opponent is null
     */
    void startAttacking();

    /**
     * The linked in game character will take a hit.
     * The amount of damage is specified by the param.
     * If the health percentage will become 0, this controller
     * will start the death sequence.
     * @param damage the amount of damage this linked character will take.
     */
    void takeHit(int damage);

    /**
     * The linked character health will be restore.
     * The amount of healing is specified by the param.
     * @param cure the amount of healing this linked character will take.
     */
    void heal(int cure);
}
