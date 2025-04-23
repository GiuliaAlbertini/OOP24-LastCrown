package it.unibo.oop.lastcrown.model.card.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.api.card.characters.PlayableCharacter;
import it.unibo.oop.lastcrown.model.impl.card.characters.playablecharacter.PlayableCharacterFactory;

/**
 * Tests PlayableCharacter implementation and factory.
*/
final class TestPlayableCharacter {
    private static final String NAME = "Mario";
    private static final String TYPE = "melee";
    private static final int COST = 100;
    private static final int ATTACK = 10;
    private static final int HEALTH = 200;
    private static final int COPIES_PER_MATCH = 3;
    private static final int ENERGY_TO_PLAY = 5;
    private static final double SPEED_MUL = 1.8;
    private static final int ACTION_RANGE = 6;

    private final PlayableCharacter character = PlayableCharacterFactory.createPlayableCharacter(NAME, TYPE,
     COST, ATTACK, HEALTH, COPIES_PER_MATCH, ENERGY_TO_PLAY, SPEED_MUL, ACTION_RANGE);

     /**
      * Tests the getters of PlayableCharacter interface.
      */
    @Test
    void testGetters() {
        assertEquals(NAME, this.character.getName());
        assertEquals(TYPE, this.character.getType());
        assertEquals(COST, this.character.getCost());
        assertEquals(ATTACK, this.character.getAttackValue());
        assertEquals(HEALTH, this.character.getHealthValue());
        assertEquals(COPIES_PER_MATCH, this.character.getCopiesPerMatch());
        assertEquals(ENERGY_TO_PLAY, this.character.getEnergyToPlay());
        assertEquals(SPEED_MUL, this.character.getSpeedMultiplier());
        assertEquals(ACTION_RANGE, this.character.getActionRange());
    }
}
