package it.unibo.oop.lastcrown.model.characters.impl.playablecharacter;

import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;

/**
 * Creates a Playable Character with the specified params.
 */
public final class PlayableCharacterFactory {

    private PlayableCharacterFactory() { }
    /**
     * @param name the name of this character
     * @param type can be "melee" or "ranged"
     * @param cost total amount of coins to spend to own this playable character
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param copiesPerRound number of times this character can be played during a match
     * @param energyToPlay amount of player energy needed to play this character
     * @param speedMultiplier the speed multiplier of this character(ex 1.5 -> standard_speed * 1.5)
     * @param actionRange this character action range (distance form witch this character can spot enemies)
     * @return new Playable Character
     */
    public static PlayableCharacter createPlayableCharacter(final String name, final String type,
    final int cost, final int attack, final int health,
    final int copiesPerRound, final int energyToPlay, final double speedMultiplier, final int actionRange) {

        return new PlayableCharacterImpl(name, type, cost, attack, health,
        copiesPerRound, energyToPlay, speedMultiplier, actionRange);
    }
}
