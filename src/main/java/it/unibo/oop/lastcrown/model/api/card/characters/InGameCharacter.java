package it.unibo.oop.lastcrown.model.api.card.characters;

/**
 * A generic character currently involved in a match.
 */
public interface InGameCharacter {
    /**
     * @return the type of this character
     */
    String getType();

    /**
     * @return the name of this character
     */
    String getName();

    /**
     * Make this character take damage.
     * @param damage the amount of damage this character will take
     */
    void takeDamage(int damage);

    /**
     * Heal this character.
     * @param cure the amount of healing this character will take
     */
    void restoreHealth(int cure);

    /**
     * Change the maximum health of this character
     * (variation can be a negative number to reduce it or
     * a positive number to increase it).
     * @param variation the variation of maximum health
     */
    void changeMaximumHealth(int variation);

    /**
     * @return the actual health percentage of this character.
     */
    int getHealthPercentage();

    /**
     * @return the attack value of this character.
     */
    int getAttack();

    /**
     * Change the attack value of this character.
     * @param variation the variation of attack value
     */
    void changeAttack(int variation);

    /**
     * @return the speed multiplier of this character
     */
    double getSpeedMultiplier();

    /**
     * Change the speed multiplier of this character.
     * @param variation the variation of speed multiplier
     */
    void changeSpeedMultiplier(double variation);

    /**
     * @param inCombat True to be considered in combat, False otherwise.
     */
    void setInCombat(boolean inCombat);

    /**
     * @return True if this character is in combat, False otherwise.
     */
    boolean isInCombat();

    /**
     * @return True if this character is dead, False otherwise.
     */
    boolean isDead();
}
