package it.unibo.oop.lastcrown.view.characters.api;


/**
 * An observer that receives notifications from the animation handler 
 * about the movement of a character.
 */
public interface CharacterMovementObserver {

    /**
     * Notifies the movement observer about the movement of a character.
     * @param deltaX the horizontal movement (positive to the right, negative to the left)
     * @param deltaY the vertical movement(positive down, negative up)
     */
    void notifyMovement(int deltaX, int deltaY);
}
