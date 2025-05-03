package it.unibo.oop.lastcrown.model.characters.impl;

import it.unibo.oop.lastcrown.model.characters.api.GenericCharacter;

/**
 * A standard implementation of GenericCharacter interface.
 */
public class GenericCharacterImpl implements GenericCharacter {
    private final String name;
    private final int attack;
    private final int health;
    private final double speedMultiplier;

    /**
     * @param name the name of this character
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param speedMultiplier the speed multiplier of this character(ex 1.5 -> standard_speed * 1.5)
     */
    public GenericCharacterImpl(final String name, final int attack,
    final int health, final double speedMultiplier) {
        this.name = name;
        this.attack = attack;
        this.health = health;
        this.speedMultiplier = speedMultiplier;
    }

    @Override
    public final String getName() {
        return this.name;
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
    public final double getSpeedMultiplier() {
        return this.speedMultiplier;
    }

}
