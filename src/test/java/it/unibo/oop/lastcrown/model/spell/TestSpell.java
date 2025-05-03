package it.unibo.oop.lastcrown.model.spell;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.impl.SpellFactory;
import it.unibo.oop.lastcrown.model.spell.impl.SpellImpl;

/**
 * A simple test of Spell standard implementation and factory.
 */
final class TestSpell {
    private static final String NAME = "Explosion";
    private static final int COST = 5;
    private static final int COPIES_PER_ROUND = 3;
    private static final int ENERGY_TO_PLAY = 4;
    private static final String CATEGORY = "health";
    private static final int AMOUNT = 40;
    private static final String TARGET = "enemies";
    private static final int ACTION_RANGE = 450;

    private final Spell spell1 = new SpellImpl(NAME, COST, COPIES_PER_ROUND,
    ENERGY_TO_PLAY, new SpellEffect(CATEGORY, AMOUNT, TARGET, Optional.empty()),
    Optional.of(ACTION_RANGE));

    private final Spell spell2 = SpellFactory.createSpell(NAME, COST, COPIES_PER_ROUND,
    ENERGY_TO_PLAY, new SpellEffect(CATEGORY, AMOUNT, TARGET, Optional.empty()),
    Optional.of(ACTION_RANGE));

    /**
     * Test the getters of SpellImpl.
     */
    @Test
    void testSpellStandardImplementation() {
        assertEquals(NAME, spell1.getName());
        assertEquals(COST, spell1.getCost());
        assertEquals(COPIES_PER_ROUND, spell1.getCopiesPerMatch());
        assertEquals(ENERGY_TO_PLAY, spell1.getEnergyToPlay());
        assertEquals(new SpellEffect(CATEGORY, AMOUNT, TARGET, Optional.empty()), spell1.getSpellEffect());
        assertEquals(Optional.of(ACTION_RANGE), spell1.getActionRange());
    }

    /**
     * Test the getters of Spell implementation obtained with SpellFactory.
     */
    @Test
    void testSpellFactory() {
        assertEquals(NAME, spell2.getName());
        assertEquals(COST, spell2.getCost());
        assertEquals(COPIES_PER_ROUND, spell2.getCopiesPerMatch());
        assertEquals(ENERGY_TO_PLAY, spell2.getEnergyToPlay());
        assertEquals(new SpellEffect(CATEGORY, AMOUNT, TARGET, Optional.empty()), spell2.getSpellEffect());
        assertEquals(Optional.of(ACTION_RANGE), spell2.getActionRange());
    }
}
