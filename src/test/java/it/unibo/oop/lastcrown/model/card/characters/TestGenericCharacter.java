package it.unibo.oop.lastcrown.model.card.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.characters.api.GenericCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.GenericCharacterImpl;

/**
 * A simple test of GenericCharacterImpl class.
 */
final class TestGenericCharacter {
    private static final String NAME = "Mario";
    private static final int ATTACK = 10;
    private static final int HEALTH = 200;
    private static final double SPEED_MUL = 1.8;
    private final GenericCharacter character = new GenericCharacterImpl(NAME, ATTACK,
     HEALTH, SPEED_MUL);

     /**
      * Tests the getters of GenericCharacterImpl class.
      */
    @Test
    void testGetters() {
        assertEquals(NAME, this.character.getName());
        assertEquals(ATTACK, this.character.getAttackValue());
        assertEquals(HEALTH, this.character.getHealthValue());
        assertEquals(SPEED_MUL, this.character.getSpeedMultiplier());
    }
}
