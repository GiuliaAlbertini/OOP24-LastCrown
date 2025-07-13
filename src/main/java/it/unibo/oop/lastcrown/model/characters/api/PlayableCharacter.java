package it.unibo.oop.lastcrown.model.characters.api;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.card.PlayableCard;

/**
 * A character that can be played during a match.
 */
public interface PlayableCharacter extends PlayableCard, GenericCharacter {
    /**
     * @return this playable character type (melee or ranged)
     */
    CardType getType();

    /**
     * @return this character action range (distance form witch this character can spot enemies) 
     */
    int getActionRange();
}
