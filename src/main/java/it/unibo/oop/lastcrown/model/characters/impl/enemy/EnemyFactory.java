package it.unibo.oop.lastcrown.model.characters.impl.enemy;

import it.unibo.oop.lastcrown.model.characters.api.Enemy;

/**
 * Creates a Enemy with the specified parameters.
 */
public final class EnemyFactory {

    private EnemyFactory() { }

    /**
     * @param name the name of this enemy
     * @param rank the rank of this enemy
     * @param enemyType the type of this enemy (standard enemy or boss)
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param speedMultiplier the speed multiplier of this enemy
     * @return a new Enemy
     */
    public static Enemy createEnemy(final String name, final int rank, final String enemyType,
    final int attack, final int health, final double speedMultiplier) {
        return new EnemyImpl(name, rank, enemyType, attack, health, speedMultiplier);
    }
}
