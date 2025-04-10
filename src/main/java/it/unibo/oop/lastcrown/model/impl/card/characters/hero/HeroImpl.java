package it.unibo.oop.lastcrown.model.impl.card.characters.hero;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.api.card.characters.Hero;
import it.unibo.oop.lastcrown.model.api.card.characters.PassiveEffect;
import it.unibo.oop.lastcrown.model.api.card.characters.Requirement;
import it.unibo.oop.lastcrown.model.impl.card.characters.GenericCharacterImpl;

/**
 * A standard implementation of Hero interface.
 */
public class HeroImpl extends GenericCharacterImpl implements Hero {
    private static final double HERO_SPEED_MUL = 2.5;
    private final Requirement requirement;
    private final Optional<PassiveEffect> passiveEffect;
    private final int meleeCards;
    private final int rangedCards;
    private final int spellCards;
    private final int wallAttack;
    private final int wallHealth;

    /**
     * @param name the name of this hero
     * @param requirement the requirement to own this hero(ex Coins, 200)
     * @param attack the attack value of this hero
     * @param health the health value of this hero
     * @param atckRecoveryTime the attack recovery time of this hero(time lap between attacks)
     * @param passiveEffect passive effect of this hero
     * @param meleeCards number of melee cards deck can have with this hero
     * @param rangedCards number of ranged cards deck can have with this hero
     * @param spellCards number of spell cards deck can have with this hero
     * @param wallAttack the attack value of the defensive wall associated with this hero
     * @param wallHealth the health value of the defensive wall associated with this hero
     */
    public HeroImpl(final String name, final Requirement requirement, final int attack, final int health, 
    final double atckRecoveryTime, final Optional<PassiveEffect> passiveEffect, 
    final int meleeCards, final int rangedCards, final int spellCards, final int wallAttack, final int wallHealth) {
        super(name, attack, health, atckRecoveryTime, HERO_SPEED_MUL);
        this.requirement = requirement;
        this.passiveEffect = passiveEffect;
        this.meleeCards = meleeCards;
        this.rangedCards = rangedCards;
        this.spellCards = spellCards;
        this.wallAttack = wallAttack;
        this.wallHealth = wallHealth;
    }

    @Override
    public final Requirement getRequirement() {
        return this.requirement;
    }

    @Override
    public final Optional<PassiveEffect> getPassiveEffect() {
        return this.passiveEffect;
    }

    @Override
    public final int getMeleeCards() {
        return this.meleeCards;
    }

    @Override
    public final int getRangedCards() {
        return this.rangedCards;
    }

    @Override
    public final int getSpellCards() {
        return this.spellCards;
    }

    @Override
    public final int getWallAttack() {
        return this.wallAttack;
    }

    @Override
    public final int getWallHealth() {
        return this.wallHealth;
    }
}
