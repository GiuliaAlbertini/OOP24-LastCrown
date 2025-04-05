package it.unibo.oop.lastcrown.model.api.card.characters;

/**
 * A character that can be played during a match.
 */
public interface PlayableCharacter extends GenericCharacter {
    /**
     * @return this playable character type (melee or ranged)
     */
    String getType();

    /**
     * @return total copies of this character playable in one round
     */
    int getCopiesPerRound();

    /**
     * @return amount of energy needed to play this character
     */
    int getEnergyToPlay();

    /**
     * @return this character action range (distance form witch this character can spot enemies) 
     */
    int getActionRange();
}
