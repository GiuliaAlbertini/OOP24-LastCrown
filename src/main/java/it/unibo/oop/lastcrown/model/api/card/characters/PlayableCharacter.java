package it.unibo.oop.lastcrown.model.api.card.characters;

import it.unibo.oop.lastcrown.model.api.card.PlayableCard;

/**
 * A character that can be played during a match.
 */
public interface PlayableCharacter extends PlayableCard {
    /**
     * @return this playable character type (melee or ranged)
     */
    String getType();

    /**
     * @return this character action range (distance form witch this character can spot enemies) 
     */
    int getActionRange();
}
