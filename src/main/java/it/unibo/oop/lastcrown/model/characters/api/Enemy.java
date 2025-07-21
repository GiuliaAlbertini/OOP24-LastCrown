package it.unibo.oop.lastcrown.model.characters.api;

import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * A generic enemy.
 */
public interface Enemy extends GenericCharacter {

    /**
     * @return the rank of this enemy. It indicates the general strenght of this enemy.
     */
    int getRank();

    /**
     * @return the type of this enemy (standard enemy or boss).
     */
    CardType getEnemyType();
}
