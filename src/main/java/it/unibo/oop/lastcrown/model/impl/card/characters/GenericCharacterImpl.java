package it.unibo.oop.lastcrown.model.impl.card.characters;

import it.unibo.oop.lastcrown.model.api.card.Requirement;
import it.unibo.oop.lastcrown.model.api.card.characters.GenericCharacter;
import it.unibo.oop.lastcrown.model.impl.card.CardImpl;

/**
 * A standard implementation of Character interface.
 */
public class GenericCharacterImpl extends CardImpl implements GenericCharacter {
    private final int attack;
    private final int health;
    private final double atckRecoveryTime;
    private final double speedMultiplier;

    /**
     * @param name the name of this character
     * @param requirement the requirement to own this character(ex Coins, 200)
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param atckRecoveryTime the attack recovery time of this character(time lap between attacks)
     * @param speedMultiplier the speed multiplier of this character(ex 1.5 -> standard_speed * 1.5)
     */
    public GenericCharacterImpl(final String name, final Requirement requirement,
    final int attack, final int health, final double atckRecoveryTime, final double speedMultiplier) {
        super(name, requirement);
        this.attack = attack;
        this.health = health;
        this.atckRecoveryTime = atckRecoveryTime;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public final int getAttackValue() {
        return this.attack;
    }

    @Override
    public final int getHealthValue() {
        return this.health;
    }

    @Override
    public final double getAtckRecoveryTime() {
        return this.atckRecoveryTime;
    }

    @Override
    public final double getSpeedMultiplier() {
        return this.speedMultiplier;
    }
}
