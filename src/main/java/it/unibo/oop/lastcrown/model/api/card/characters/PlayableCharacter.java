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
     * @return this character action range (distance form witch this character can spot enemies) 
     */
    int getActionRange();
}
