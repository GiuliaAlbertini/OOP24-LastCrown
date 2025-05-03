package it.unibo.oop.lastcrown.model.characters.impl.ingamecharacter;

import it.unibo.oop.lastcrown.model.characters.api.InGameCharacter;

/**
 * A standard implementation of InGameCharacter interface.
 */
public class InGameCharacterImpl implements InGameCharacter {
    private final String name;
    private final String type;
    private int currentHealth;
    private int maximumHealth;
    private int attack;
    private double speedMultiplier;
    private boolean inCombat;
    private boolean dead;

    /**
     * @param type the type of this character (melee, ranged, hero, enemy)
     * @param name the name of this character
     * @param health the health value of this character
     * @param attack the attack value of this character
     * @param speedMultiplier the speed multiplier of this character
     */
    public InGameCharacterImpl(final String type, final String name,
     final int health, final int attack,
     final double speedMultiplier) {
        this.type = type;
        this.name = name;
        this.currentHealth = health;
        this.maximumHealth = health;
        this.attack = attack;
        this.speedMultiplier = speedMultiplier;
        this.dead = false;
    }

    @Override
    public final String getType() {
        return this.type;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final void takeDamage(final int damage) {
        this.currentHealth = this.currentHealth - damage;
        if (this.currentHealth <= 0) {
            this.currentHealth = 0;
            this.dead = true;
        }
    }

    @Override
    public final void restoreHealth(final int cure) {
        this.currentHealth = this.currentHealth + cure;
        if (this.currentHealth > this.maximumHealth) {
            this.currentHealth = this.maximumHealth;
        }
    }

    @Override
    public final void changeMaximumHealth(final int variation) {
        this.maximumHealth = this.maximumHealth + variation;
        if (variation < 0) {
            if (this.currentHealth > this.maximumHealth) {
            this.currentHealth = this.maximumHealth;
            }
        } else {
            this.currentHealth = this.currentHealth + variation;
        }
    }

    @Override
    public final int getHealthPercentage() {
        return 100 * this.currentHealth / this.maximumHealth;
    }

    @Override
    public final int getAttack() {
       return this.attack;
    }

    @Override
    public final void changeAttack(final int variation) {
        this.attack = this.attack + variation;
    }

    @Override
    public final double getSpeedMultiplier() {
        return this.speedMultiplier;
    }

    @Override
    public final void changeSpeedMultiplier(final double variation) {
        this.speedMultiplier = this.speedMultiplier + variation;
    }

    @Override
    public final void setInCombat(final boolean inCombat) {
        this.inCombat = inCombat;
    }

    @Override
    public final boolean isInCombat() {
        return this.inCombat;
    }

    @Override
    public final boolean isDead() {
       return this.dead;
    }
}
