package it.unibo.oop.lastcrown.model.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyFactory;

/**
 * A simple test of Enemy implementation.
 */
final class TestEnemy {
    private static final String NAME = "UndeadExecutioner";
    private static final CardType TYPE = CardType.BOSS;
    private static final int RANK = 1;
    private static final int ATTACK = 20;
    private static final int HEALTH = 400;
    private static final double SPEED_MUL = 1.8;
    private final Enemy enemy = EnemyFactory.createEnemy(NAME, RANK, TYPE, ATTACK, HEALTH, SPEED_MUL);

    /**
      * Tests the getters of GenericCharacterImpl class.
      */
    @Test
    void testGetters() {
        assertEquals(NAME, this.enemy.getName());
        assertEquals(RANK, this.enemy.getRank());
        assertEquals(TYPE, this.enemy.getEnemyType());
        assertEquals(ATTACK, this.enemy.getAttackValue());
        assertEquals(HEALTH, this.enemy.getHealthValue());
        assertEquals(SPEED_MUL, this.enemy.getSpeedMultiplier());
    }
}
