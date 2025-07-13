package it.unibo.oop.lastcrown.view.characters.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * An observer that receives notifications from the animation handler 
 * about the movement of a character.
 */
public interface CharacterMovementObserver {

    /**
     * Notifies the movement observer about the movement of a character.
     * @param id the id of the character controller linked to this movement
     * @param deltaX the horizontal movement (positive to the right, negative to the left)
     * @param deltaY the vertical movement(positive down, negative up)
     */
    void notifyMovement(CardIdentifier id, int deltaX, int deltaY);
}
