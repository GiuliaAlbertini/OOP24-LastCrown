package it.unibo.oop.lastcrown.model.user.api;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Provides a rappresentation of the deck of the user.
 */
public interface Deck {
    /**
     * Provides the deck as a {@link Set} of {@link CardIdentifier}.
     *
     * @return the set of {@link CardIdentifier} in the deck
     */
    Set<CardIdentifier> getDeck();

    /**
     * Add the given card to the deck.
     *
     * @param card the {@link CardIdentifier} to add
     */
    void addCard(CardIdentifier card);

    /**
     * Remove the given card from the deck.
     *
     * @param card the {@link CardIdentifier} to remove
     */
    void removeCard(CardIdentifier card);

    /**
     * Getter for the current hero.
     * @return the {@link CardIdentifier} of the requested hero
     */
    CardIdentifier getHero();
}
