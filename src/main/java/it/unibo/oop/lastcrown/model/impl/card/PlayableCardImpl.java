package it.unibo.oop.lastcrown.model.impl.card;

import it.unibo.oop.lastcrown.model.api.card.PlayableCard;

/**
 * the implementation of Card interface.
 */
public class PlayableCardImpl implements PlayableCard {
    private final int cost;
    private final int copiesPerMatch;
    private final int energyToPlay;

    /**
     * @param cost total coins to spend to own this card
     * @param copiesPerMatch maximum number of copies of this card that can be played in one Match
     * @param energyToPlay player energy to spend to play this card
     */
    public PlayableCardImpl(final int cost, final int copiesPerMatch, final int energyToPlay) {
        this.cost = cost;
        this.copiesPerMatch = copiesPerMatch;
        this.energyToPlay = energyToPlay;
    }

    @Override
    public final int getCost() {
        return this.cost;
    }

    @Override
    public final int getCopiesPerMatch() {
        return this.copiesPerMatch;
    }

    @Override
    public final int getEnergyToPlay() {
        return this.energyToPlay;
    }
}
