package it.unibo.oop.lastcrown.model.user.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.user.api.Deck;

/**
 * Implementation of a {@link Deck}.
 */
public class DeckImpl implements Deck {
    private final Set<CardIdentifier> userCollection;
    private final CompleteCollectionImpl completeCollection = new CompleteCollectionImpl();
    private final Set<CardIdentifier> deck = new HashSet<>();

    private CardIdentifier heroId;

    /**
     * Constuct a new {@code DeckImpl}.
     *
     * @param userCollection the collection of the user
     */
    public DeckImpl(final Set<CardIdentifier> userCollection) {
        this.userCollection = Collections.unmodifiableSet(userCollection);
    }

    @Override
    public final Set<CardIdentifier> getDeck() {
        return Set.copyOf(this.deck);
    }

    @Override
    public final void addCard(final CardIdentifier card) {
        if (!owns(card)) {
            return;
        }
        final CardType type;
        try {
            type = card.type();
        } catch (final IllegalArgumentException e) {
            return;
        }
        if (type == CardType.HERO) {
            switchHero(card);
            return;
        }
        if (heroId == null) {
            return;
        }
        final Hero hero = getHeroInstance();
        if (hero == null) {
            return;
        }
        if (!withinLimit(type, hero)) {
            return;
        }
        this.deck.add(card);
    }

    @Override
    public final void removeCard(final CardIdentifier card) {
        if (card.equals(heroId)) {
            return;
        }
        this.deck.remove(card);
    }

    @Override
    public final CardIdentifier getHero() {
        return this.heroId;
    }

    private boolean owns(final CardIdentifier card) {
        return this.userCollection.contains(card);
    }

    private void switchHero(final CardIdentifier newHero) {
        if (!newHero.equals(heroId)) {
            this.deck.removeIf(c -> {
                try {
                    return c.type() == CardType.HERO;
                } catch (final IllegalArgumentException e) {
                    return false;
                }
            });
            this.heroId = newHero;
            this.deck.add(newHero);
            enforceLimits();
        }
    }

    private void enforceLimits() {
        final Map<CardType, List<CardIdentifier>> byType = this.deck.stream()
            .filter(c -> {
                try {
                    return c.type() != CardType.HERO;
                } catch (final IllegalArgumentException e) {
                    return false;
                }
            })
            .collect(Collectors.groupingBy(CardIdentifier::type));
        final Hero h = getHeroInstance();
        byType.forEach((type, cards) -> {
            final int limit = limitFor(type, h);
            final int excess = cards.size() - limit;
            if (excess > 0) {
                cards.stream()
                    .limit(excess)
                    .forEach(card -> {
                        this.deck.remove(card);
                    });
            }
        });
    }

    private Hero getHeroInstance() {
        return completeCollection.getHero(heroId).orElse(null);
    }

    private boolean withinLimit(final CardType type, final Hero hero) {
        return countByType(type) < limitFor(type, hero);
    }

    private long countByType(final CardType type) {
        return this.deck.stream()
            .filter(c -> {
                try {
                    return c.type() == type;
                } catch (final IllegalArgumentException e) {
                    return false;
                }
            })
            .count();
    }

    private int limitFor(final CardType type, final Hero hero) {
        switch (type) {
            case MELEE:
                return hero.getMeleeCards();
            case RANGED:
                return hero.getRangedCards();
            case SPELL:
                return hero.getSpellCards();
            default:
                return 0;
        }
    }
}
