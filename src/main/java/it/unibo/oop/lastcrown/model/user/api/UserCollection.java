package it.unibo.oop.lastcrown.model.user.api;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Provides a rappresentation of the collection of the user.
 */
public interface UserCollection {
    /**
     * Provides the collection.
     * 
     * @return the collection
     */
    Set<CardIdentifier> getCollection();

    /**
     * Adds a new card to the collection.
     * 
     * @param newCard the card to add
     */
    void addCard(CardIdentifier newCard);
}
