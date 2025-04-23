package it.unibo.oop.lastcrown.model.card.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.api.card.characters.InGameCharacter;
import it.unibo.oop.lastcrown.model.impl.card.characters.ingamecharacter.InGameCharacterFactory;

/**
 * A simple test of InGameCharacter implementation.
 */
final class TestInGameCharacter {
    private static final String TYPE = "melee";
    private static final String NAME = "Mario";
    private static final int ATTACK = 10;
    private static final int HEALTH = 100;
    private static final double SPEED_MUL = 1.8;
    private InGameCharacter character;

    @BeforeEach
    void startTests() {
        this.character = InGameCharacterFactory.createInGameCharacter(TYPE, NAME,
        HEALTH, ATTACK, SPEED_MUL);
    }

    /**
     * tests the getters of InGameCharacter implementation.
     */
    @Test
    void testGetters() {
        assertEquals(TYPE, this.character.getType());
        assertEquals(NAME, this.character.getName());
        assertEquals(ATTACK, this.character.getAttack());
        assertEquals(100, this.character.getHealthPercentage());
        assertEquals(SPEED_MUL, this.character.getSpeedMultiplier());
    }

    /**
     * Tests current and maximum health changes and if death is triggered correctly. 
     */
    @Test
    void testHealthChanges() {
        final int cure1 = 10;
        final int damage1 = 50;
        final int cure2 = 50;
        final int variation = 50;
        final int damage2 = 27;
        final int damage3 = 80;
        final int expectation1 = 50;
        final int expectation2 = 73;
        this.character.restoreHealth(cure1);
        assertEquals(100, this.character.getHealthPercentage());
        this.character.takeDamage(damage1);
        assertEquals(expectation1, this.character.getHealthPercentage());
        this.character.restoreHealth(cure2);
        assertEquals(100, this.character.getHealthPercentage());
        this.character.changeMaximumHealth(-variation);
        assertEquals(100, this.character.getHealthPercentage());
        this.character.changeMaximumHealth(variation);
        assertEquals(100, this.character.getHealthPercentage());
        this.character.takeDamage(damage2);
        assertEquals(expectation2, this.character.getHealthPercentage());
        this.character.takeDamage(damage3);
        assertEquals(0, this.character.getHealthPercentage());
        assertTrue(this.character.isDead());
    }

    /**
     * tests attack value changes.
     */
    @Test
    void testAttackChanges() {
        final int lesserVariation = 5;
        final int majorVariation = 10;
        final int expectation1 = ATTACK + majorVariation;
        final int expectation2 = expectation1 - lesserVariation;
        this.character.changeAttack(majorVariation);
        assertEquals(expectation1, this.character.getAttack());
        this.character.changeAttack(-lesserVariation);
        assertEquals(expectation2, this.character.getAttack());
    }

    /**
     * tests speed multiplier changes.
     */
    @Test
    void testSpeedMultiplierChanges() {
        final double variation1 = 0.2;
        final double variation2 = 0.7;
        final double expectation1 = SPEED_MUL + variation1;
        final double expectation2 = expectation1 - variation2;
        this.character.changeSpeedMultiplier(variation1);
        assertEquals(expectation1, this.character.getSpeedMultiplier());
        this.character.changeSpeedMultiplier(-variation2);
        assertEquals(expectation2, this.character.getSpeedMultiplier());
    }

    /**
     * tests boolean param inCombat changes.
     */
    @Test
    void testInCombatChanges() {
        assertFalse(this.character.isInCombat());
        this.character.setInCombat(true);
        assertTrue(this.character.isInCombat());
        this.character.setInCombat(false);
        assertFalse(this.character.isInCombat());
    }
}
