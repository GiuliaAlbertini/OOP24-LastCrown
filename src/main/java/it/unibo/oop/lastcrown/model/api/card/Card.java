package it.unibo.oop.lastcrown.model.api.card;

/**
 * A generic Card of the Collection.
 */
public interface Card {
    /**
     * @return the card name
     */
    String getName();

    /**
     * @return the requirement to own this card (ex. Coins, 200 -> 200 Coins needed)
     */
    Requirement getRequirement();
}

