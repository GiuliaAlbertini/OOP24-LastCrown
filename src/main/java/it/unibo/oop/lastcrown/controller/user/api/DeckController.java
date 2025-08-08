package it.unibo.oop.lastcrown.controller.user.api;

import java.util.List;
import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.api.Deck;

/**
 * Controller for a {@link Deck}.
 */
public interface DeckController {

    /**
     * Getter for the deck in use.
     *
     * @return the {@link Set} of {@link CardIdentifier} containing the cards in the deck
     */
    Set<CardIdentifier> getDeck();

    /**
     * Getter for the deck ordered by type.
     * The order is: HERO->MELEE->Ranged->SPELL
     * @return the ordered deck as a list
     */
    List<CardIdentifier> getOrderedDeck();

    /**
     * Getter for the cards in the user's collection.
     *
     * @return the {@link List} of {@link CardIdentifier} containing the cards in the collection
     */
    List<CardIdentifier> getAvailableCards();

    /**
     * Adds a card to the deck.
     *
     * @param card the card to add
     */
    void addCard(CardIdentifier card);

    /**
     * Removes a card from the deck.
     *
     * @param card the card to remove
     */
    void removeCard(CardIdentifier card);

    /**
     * Getter for the available cards but filtered by type.
     *
     * @param type the type to use as a filter
     * @return the {@link List} of {@link CardIdentifier} containing the cards requested
     */
    List<CardIdentifier> getAvailableCardsByType(CardType type);

    /**
     * Getter for the current hero in the deck.
     * @return the {@link CardIdentifier} of the requested hero
     */
    CardIdentifier getHero();
}
