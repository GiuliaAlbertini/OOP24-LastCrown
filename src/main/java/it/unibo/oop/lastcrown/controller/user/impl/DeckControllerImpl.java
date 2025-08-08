package it.unibo.oop.lastcrown.controller.user.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.api.Deck;
import it.unibo.oop.lastcrown.model.user.impl.StandardDeckFactory;

/**
 * Implementation of {@link DeckController}.
 */
public class DeckControllerImpl implements DeckController {

    private static final CardType[] TYPES = {
        CardType.HERO, CardType.MELEE, CardType.RANGED, CardType.SPELL,
    };

    private final Set<CardIdentifier> userCollection;
    private final Deck deck;

    /**
     * Construct a new {@code DeckContorllerImpl}.
     * It initialize the collection to use and the deck.
     *
     * @param userCollection the collection to use
     */
    public DeckControllerImpl(final Set<CardIdentifier> userCollection) {
        this.userCollection = new HashSet<>(userCollection);
        this.deck = StandardDeckFactory.createDeck(this.userCollection);
    }

    @Override
    public final List<CardIdentifier> getAvailableCards() {
        final List<CardIdentifier> ordered = new ArrayList<>();
        for (final CardType type : TYPES) {
            final List<CardIdentifier> block = this.userCollection.stream()
                .filter(card -> card.type().equals(type))
                .sorted(Comparator.comparing(CardIdentifier::number))
                .toList();
            ordered.addAll(block);
        }
        return Collections.unmodifiableList(ordered);
    }

    @Override
    public final void addCard(final CardIdentifier card) {
        this.deck.addCard(card);
    }

    @Override
    public final Set<CardIdentifier> getDeck() {
        return Collections.unmodifiableSet(this.deck.getDeck());
    }

    @Override
    public final void removeCard(final CardIdentifier card) {
        this.deck.removeCard(card);
    }

    @Override
    public final List<CardIdentifier> getAvailableCardsByType(final CardType type) {
        return this.userCollection.stream()
            .filter(card -> card.type().equals(type))
            .sorted(Comparator.comparing(card -> card.number()))
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public final CardIdentifier getHero() {
        return this.deck.getHero();
    }

    @Override
    public final List<CardIdentifier> getOrderedDeck() {
        final List<CardIdentifier> ordered = new ArrayList<>();
        for (final CardType type : TYPES) {
            final List<CardIdentifier> block = this.deck.getDeck().stream()
                .filter(card -> card.type().equals(type))
                .sorted(Comparator.comparingInt(CardIdentifier::number))
                .toList();
            ordered.addAll(block);
        }
        return Collections.unmodifiableList(ordered);
    }
}
