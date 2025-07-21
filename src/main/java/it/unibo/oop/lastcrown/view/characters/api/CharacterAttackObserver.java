package it.unibo.oop.lastcrown.view.characters.api;

/**
 * An observer that notifies the character controller when an attack animation is finished.
 */
public interface CharacterAttackObserver {

    /**
     * Communicates to the character controller to deal an attack to its opponent/s.
     */
    void doAttack();
}
