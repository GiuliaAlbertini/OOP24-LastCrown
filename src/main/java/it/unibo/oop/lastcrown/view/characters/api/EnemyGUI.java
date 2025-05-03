package it.unibo.oop.lastcrown.view.characters.api;

/**
 * An interface that handles the graphic side of an enemy.
 */
public interface EnemyGUI extends GenericCharacterGUI {

    /**
     * Make this enemy retreat.
     */
    void startRetreatLoop();
}
