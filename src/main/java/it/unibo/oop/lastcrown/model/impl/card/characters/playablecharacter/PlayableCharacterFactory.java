package it.unibo.oop.lastcrown.model.impl.card.characters.playablecharacter;

import it.unibo.oop.lastcrown.model.api.card.Requirement;
import it.unibo.oop.lastcrown.model.api.card.characters.PlayableCharacter;

/**
 * Creates a Playable Character with the specified params.
 */
public class PlayableCharacterFactory {
    /**
     * @param name the name of this character
     * @param type can be "melee" or "ranged"
     * @param requirement the requirement to own this character(ex Coins, 200)
     * @param attack the attack value of this character
     * @param health the health value of this character
     * @param atckRecoveryTime the attack recovery time of this character(time lap between attacks)
     * @param copiesPerRound number of times this character can be played during a match
     * @param energyToPlay amount of player energy needed to play this character
     * @param speedMultiplier the speed multiplier of this character(ex 1.5 -> standard_speed * 1.5)
     * @param actionRange this character action range (distance form witch this character can spot enemies)
     * @return new Playable Character
     */
    public PlayableCharacter createPlayableCharacter(final String name, final String type,
    final Requirement requirement, final int attack, final int health, final double atckRecoveryTime,
    final int copiesPerRound, final int energyToPlay, final double speedMultiplier, final int actionRange) {

        return new PlayableCharacterImpl(name, type, requirement, attack, health,
        atckRecoveryTime, copiesPerRound, energyToPlay, speedMultiplier, actionRange);
    }
}
