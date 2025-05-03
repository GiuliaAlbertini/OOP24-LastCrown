package it.unibo.oop.lastcrown.model.characters;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PassiveEffect;
import it.unibo.oop.lastcrown.model.characters.api.Requirement;
import it.unibo.oop.lastcrown.model.characters.impl.hero.HeroFactory;

/**
 * A simple test of Hero interface.
 */
final class TestHero {
    private static final String NAME = "Mario";
    private static final Requirement REQUIREMENT = new Requirement("bosses", 10);
    private static final int ATTACK = 10;
    private static final int HEALTH = 200;
    private static final Optional<PassiveEffect> PASSIVE_EFFECT = Optional.of(new PassiveEffect("attack", 10));
    private static final int MELEE_CARDS = 3;
    private static final int RANGED_CARDS = 4;
    private static final int SPELL_CARDS = 2;
    private static final int WALL_ATTACK = 2;
    private static final int WALL_HEALTH = 200;
    private final Hero hero = HeroFactory.createHero(NAME, REQUIREMENT, ATTACK, HEALTH, PASSIVE_EFFECT,
    MELEE_CARDS, RANGED_CARDS, SPELL_CARDS, WALL_ATTACK, WALL_HEALTH);

    /**
     * Tests the getters of Hero interface.
     */
    @Test
    void testGetters() {
        assertEquals(NAME, this.hero.getName());
        assertEquals(REQUIREMENT, this.hero.getRequirement());
        assertEquals(ATTACK, this.hero.getAttackValue());
        assertEquals(HEALTH, this.hero.getHealthValue());
        assertEquals(PASSIVE_EFFECT, this.hero.getPassiveEffect());
        assertEquals(MELEE_CARDS, this.hero.getMeleeCards());
        assertEquals(RANGED_CARDS, this.hero.getRangedCards());
        assertEquals(SPELL_CARDS, this.hero.getSpellCards());
        assertEquals(WALL_ATTACK, this.hero.getWallAttack());
        assertEquals(WALL_HEALTH, this.hero.getWallHealth());
    }
}
