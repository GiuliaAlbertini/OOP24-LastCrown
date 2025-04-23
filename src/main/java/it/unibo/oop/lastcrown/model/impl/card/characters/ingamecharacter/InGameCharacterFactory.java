package it.unibo.oop.lastcrown.model.impl.card.characters.ingamecharacter;

import it.unibo.oop.lastcrown.model.api.card.characters.InGameCharacter;

/**
 * Create a InGameCharacter with the specified parameters.
 */
public final class InGameCharacterFactory {
    private InGameCharacterFactory() { }

    /**
     * @param type the type of this character (melee, ranged, hero, enemy)
     * @param name the name of this character
     * @param health the health value of this character
     * @param attack the attack value of this character
     * @param speedMultiplier the speed multiplier of this character
     * @return a new InGameCharacter
     */
    public static InGameCharacter createInGameCharacter(final String type,
     final String name, final int health, final int attack, final double speedMultiplier) {
        return new InGameCharacterImpl(type, name, health, attack, speedMultiplier);
     }
}
