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
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

/**
 * Controller to handle the deck during a match.
 */
public final class InGameDeckController {
    private static final int MAX_CARD_TO_SEND = 3;
    private static final CompleteCollection COMPLETE_COLLECTION = new CompleteCollectionImpl();
    private final Map<CardIdentifier, Integer> copiesRegister;
    private Set<CardIdentifier> availables;
    private final List<CardIdentifier> queue = new ArrayList<>();
    private Set<CardIdentifier> deck;

    /**
     * Initializes the deck to use, the cards availables and their copies.
     * 
     * @param original the deck to start with
     */
    private InGameDeckController(final Set<CardIdentifier> original) {
        this.deck = original;
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
     * Construct the copies register mapping each card to its total copies per match.
     * 
     * @param original the cards to put in the register
     * @return the new register
     */
    public Map<CardIdentifier, Integer> resetCopiesRegister(final Set<CardIdentifier> original) {
        return original.stream()
            .collect(Collectors.<CardIdentifier, CardIdentifier, Integer>toMap(
                Function.identity(),
                id -> {
                    int copies = switch (id.type()) {
                        case MELEE, RANGED -> getCharacterFromCardID(id)
                                              .map(PlayableCharacter::getCopiesPerMatch)
                                              .orElseThrow(() ->
                                                  new NoSuchElementException("Character not in collection: " + id)
                                              );
                        case SPELL -> getSpellFromCardID(id)
                                              .map(Spell::getCopiesPerMatch)
                                              .orElseThrow(() ->
                                                  new NoSuchElementException("Spell not in collection: " + id)
                                              );
                        default -> 1;
                    };
                    return Integer.valueOf(copies);
                }
            ));
    }

    /**
     * Updates the copies of a card using one.
     * If the card's copies are finished it removes the card from the availables list,
     * otherwise it just decreases the copies and re-queues the card.
     * 
     * @param id the card used
     */
    private void useCopy(final CardIdentifier id) {
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
     * Getter for the next available cards in queue.
     * 
     * @return the list of cards available to send
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
        switch (id.type()) {
            case MELEE:
            case RANGED:
                return getCharacterFromCardID(id)
                           .map(PlayableCharacter::getEnergyToPlay)
                           .orElseThrow(() ->
                               new NoSuchElementException("Character not in collection: " + id)
                           );
            case SPELL:
                return getSpellFromCardID(id)
                           .map(Spell::getEnergyToPlay)
                           .orElseThrow(() ->
                               new NoSuchElementException("Spell not in collection: " + id)
                           );
            case HERO:
            default:
                return 0;
        }
    }

    /**
     * Updates the temporary deck.
     * 
     * @param newDeck the new deck
     */
    public void setDeck(final Set<CardIdentifier> newDeck) {
        this.deck = newDeck;
        this.availables = updateAvailables();
        initializeQueue();
    }

    private Optional<PlayableCharacter> getCharacterFromCardID(final CardIdentifier ci) {
        return COMPLETE_COLLECTION.getPlayableCharacter(ci);
    }

    private Optional<Spell> getSpellFromCardID(final CardIdentifier ci) {
        return COMPLETE_COLLECTION.getSpell(ci);
    }

    private Set<CardIdentifier> updateAvailables() {
        return this.deck.stream()
            .filter(id -> {
                switch (id.type()) {
                    case MELEE:
                    case RANGED:
                    case SPELL:
                        return true;
                    default:
                        return false;
                }
            })
            .collect(Collectors.toSet());
    }

    private void initializeQueue() {
        queue.clear();
        queue.addAll(availables);
    }

    /**
     * Method to play a card (consume a copy).
     * 
     * @param id the card to use
     */
    public void playCard(final CardIdentifier id) {
        this.useCopy(id);
    }
}
