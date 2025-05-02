package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A simple observer that notifies a character Controller 
 * that its linked character is taking damage by an opponent.
 */
public interface CharacterHitObserver {

    /**
     * @return this observer numerical id
     */
    int getObserverId();

    /**
     * Makes the character linked to the controller take a specific amount of damage.
     * @param damage the damage the character is going to take
     */
    void takeHit(int damage);
}
