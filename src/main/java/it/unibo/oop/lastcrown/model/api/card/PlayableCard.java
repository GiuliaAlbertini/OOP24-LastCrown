package it.unibo.oop.lastcrown.model.api.card;

/**
 * A card that can be played during the match (Playable Character os Spell).
 */
public interface PlayableCard {
    
    /**
     * @return total coins to spend in order to own this card
     */
    int getCost();

    /**
     * @return total copies of this card that can be played in a single match
     */
    int getCopiesPerMatch();

    /**
     * @return player energy needed to play this card
     */
    int getEnergyToPlay();
}

