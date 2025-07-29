package it.unibo.oop.lastcrown.model.user.impl;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Deck;

/**
 * Factory that creates instances of {@link Deck} using the standard {@link DeckImpl} implementation.
 */
public final class StandardDeckFactory {
    private StandardDeckFactory() { }
    /**
     * Satic factory method to create a Deck.
     * @param userCollection the user collection to use
     * @return the new instance of Deck
     */
    public static Deck createDeck(final Set<CardIdentifier> userCollection) {
        return new DeckImpl(userCollection);
    }
}
