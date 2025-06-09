package it.unibo.oop.lastcrown.controller.user.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;

/**
 * Controller for the collections.
 */
public interface CollectionController {
    /**
     * Getter for the initial set of cards.
     * 
     * @return a {@link Set} of {@link CardIdentifier} rappresenting the initial set
     */
    Set<CardIdentifier> getInitialSet();

    /**
     * Getter for the complete collection.
     * 
     * @return a {@link List} of {@link CardIdentifier} rappresenting the ordered complete collection
     */
    List<CardIdentifier> getCompleteCollection();

    /**
     * Getter for a collection, given the type of cards.
     * 
     * @param type the type to use
     * @return a {@link List} of {@link CardIdentifier} rappresenting the ordered requested collection
     */
    List<CardIdentifier> getCollectionByType(CardType type);

    /**
     * Getter for the enemies.
     * 
     * @return the {@link List} containing all the enemies
     */
    List<List<Enemy>> getEnemies();

    /**
     * Getter for the name of a card.
     * 
     * @param cardIdentifier the {@link CardIdentifier} of the card to find
     * @return the name of the card
     */
    Optional<String> getCardName(CardIdentifier cardIdentifier);
}

