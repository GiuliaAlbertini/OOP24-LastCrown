package it.unibo.oop.lastcrown.controller.app_managing.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.api.Deck;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;
import it.unibo.oop.lastcrown.model.user.impl.DeckImpl;

/**
 * Controller to handle the deck during a match.
 */
public final class InGameDeckController {
    private static final int MAX_CARD_TO_SEND = 3;
    private static final CompleteCollection COMPLETE_COLLECTION = new CompleteCollectionImpl();
    private final Map<CardIdentifier, Integer> copiesRegister;
    private Set<CardIdentifier> availables;
    private final List<CardIdentifier> queue = new ArrayList<>();
    private Deck tempDeck;

    /**
     * Initializes the deck to use, the cards availables and their copies.
     * 
     * @param original the deck to start with
     */
    private InGameDeckController(final Set<CardIdentifier> original) {
        this.tempDeck = new DeckImpl(original);
        this.copiesRegister = resetCopiesRegister(original);
        this.availables = updateAvailables();
        initializeQueue();
    }

    /**
     * Static factory method to create a new controller instance.
     *
     * @param original the initial deck of card identifiers
     * @return a new InGameDeckController configured with the given deck
     */
    public static InGameDeckController create(final Set<CardIdentifier> original) {
        return new InGameDeckController(original);
    }

    /**
     * Construct the copies register.
     * 
     * @param original the cards to put in the register
     * @return the new register
     */
    public Map<CardIdentifier, Integer> resetCopiesRegister(final Set<CardIdentifier> original) {
        return original.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                id -> getCharacterFromCardID(id)
                          .map(PlayableCharacter::getCopiesPerMatch)
                          .orElseThrow(() ->
                              new NoSuchElementException("Card not in collection: " + id)
                          )
            ));
    }

    /**
     * Updates the copies of a card using one.
     * If the card's copies are finished it removes the card from the availables list,
     * otherwise it just decrease the copies and put in the end of the queue.
     * 
     * @param id the card used
     */
    public void useCopy(final CardIdentifier id) {
        if (!copiesRegister.containsKey(id)) {
            throw new NoSuchElementException("Card not in deck: " + id);
        }
        final int remaining = copiesRegister.get(id);
        copiesRegister.put(id, remaining - 1);
        queue.remove(id);
        if (remaining - 1 > 0) {
            queue.add(id);
        } else {
            availables.remove(id);
        }
    }

    /**
     * Getter for the availables cards.
     * 
     * @return the list of cards available
     */
    public List<CardIdentifier> getNextAvailableCards() {
        final int end = Math.min(queue.size(), MAX_CARD_TO_SEND);
        return new ArrayList<>(queue.subList(0, end));
    }

    /**
     * Getter for the energy requested for a card.
     * 
     * @param id the card to check
     * @return the requested energy
     */
    public int getEnergyToPlay(final CardIdentifier id) {
        return getCharacterFromCardID(id)
                   .map(PlayableCharacter::getEnergyToPlay)
                   .orElseThrow(() ->
                       new NoSuchElementException("Card not in collection: " + id)
                   );
    }

    /**
     * Updates the temporary deck.
     * 
     * @param tempDeck the new deck
     */
    public void setTempDeck(final Deck tempDeck) {
        this.tempDeck = new DeckImpl(tempDeck.getDeck());
        this.availables = updateAvailables();
        initializeQueue();
    }

    private Optional<PlayableCharacter> getCharacterFromCardID(final CardIdentifier ci) {
        return COMPLETE_COLLECTION.getPlayableCharacter(ci);
    }

    private Set<CardIdentifier> updateAvailables() {
        return this.tempDeck.getDeck().stream()
            .filter(id -> getCharacterFromCardID(id)
                          .map(pc -> pc.getType() != CardType.HERO)
                          .orElse(false))
            .collect(Collectors.toSet());
    }

    private void initializeQueue() {
        queue.clear();
        queue.addAll(availables);
    }

    /**
     * Method to start the usage of a card.
     * 
     * @param id the card to use
     * @return
     */
    public void playCard(final CardIdentifier id) {
        this.useCopy(id);
    }
}
