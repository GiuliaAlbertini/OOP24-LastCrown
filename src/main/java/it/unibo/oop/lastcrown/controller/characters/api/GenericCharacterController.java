package it.unibo.oop.lastcrown.controller.characters.api;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

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
     * Set this character controller current opponent.
     * @param opponentObserver the opponent observer
     * @throws NullPointerException if the given opponent observer is null
     */
    void setOpponent(CharacterHitObserver opponentObserver);

    /**
     * Set the next animation of this character. If the animation selected
     * is not supported, this method won't do anything.
     * @param animation the next animation to be shown
     */
    void setNextAnimation(Keyword animation);

    /**
     * Make the linked panel show the next frame of the selected animation.
     */
    void showNextFrame();

    /**
     * Make the linked panel move and show the next frame of the selected animation.
     * @param movement the movement of the linked panel.
     */
    void showNextFrameAndMove(Movement movement);

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

     /**
     * @return the size of the death animation of the linked character.
     */
    int getDeathAnimationSize();

    /**
     * @return True if the linked character is in Combat, false otherwise.
     */
    boolean isInCombat();

    /**
     * @param inCombat True to be considered in combat, False otherwise.
     */
    void setInCombat(boolean inCombat);
}
