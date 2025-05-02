package it.unibo.oop.lastcrown.model.impl.card.characters.enemy;

import it.unibo.oop.lastcrown.model.api.card.characters.Enemy;
import it.unibo.oop.lastcrown.model.impl.card.characters.GenericCharacterImpl;

/**
 * A standard implementation of Enemy interface.
 */
public class EnemyImpl extends GenericCharacterImpl implements Enemy {
    private final int rank;
    private final String enemyType;

    /**
     * @param name the name of this enemy
     * @param rank the rank of this enemy
     * @param enemyType the type of this enemy (standard enemy or boss)
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param speedMultiplier the speed multiplier of this enemy
     */
    public EnemyImpl(final String name, final int rank, final String enemyType,
    final int attack, final int health, final double speedMultiplier) {
        super(name, attack, health, speedMultiplier);
        this.rank = rank;
        this.enemyType = enemyType;
    }

    @Override
    public final int getRank() {
        return this.rank;
    }

    @Override
    public final String getEnemyType() {
        return this.enemyType;
    }
}
