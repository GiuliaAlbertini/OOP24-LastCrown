package it.unibo.oop.lastcrown.model.user.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.user.api.Deck;
import it.unibo.oop.lastcrown.model.user.api.UserCollectionListener;

/**
 * Implementation of a {@link Deck}.
 */
public class DeckImpl implements Deck, UserCollectionListener {
    private static final Logger LOG = Logger.getLogger(DeckImpl.class.getName());

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

    /**
     * Creates a new instance of {@link Deck}, initializing the first hero of the deck.
     * 
     * @param userCollection the set of {@link CardIdentifier} of the user
     * @return the deck created
     */
    public static Deck createDeck(final Set<CardIdentifier> userCollection) {
        final Deck deck = new DeckImpl(userCollection);
        deck.initHero();
        return deck;
    }

    @Override
    public final void initHero() {
        this.heroId = findFirstHero();
        if (heroId != null) {
            this.deck.add(heroId);
        } else {
            LOG.warning("No hero in user collection; deck starts without a hero");
        }
    }

    @Override
    public final Set<CardIdentifier> getDeck() {
        return Set.copyOf(this.deck);
    }

    @Override
    public final void addCard(final CardIdentifier card) {
        if (!owns(card)) {
            LOG.warning("Cannot add " + card + ": not in user collection");
            return;
        }
        final CardType type;
        try {
            type = CardType.valueOf(card.type());
        } catch (final IllegalArgumentException e) {
            LOG.warning("Unknown card type: " + card.type());
            return;
        }
        if (type == CardType.HERO) {
            switchHero(card);
            return;
        }
        if (heroId == null) {
            LOG.warning("Cannot add non-hero " + card + " before selecting a hero");
            return;
        }
        final Hero hero = completeCollection.getHero(heroId).orElse(null);
        if (hero == null) {
            LOG.severe("Hero details missing for " + heroId);
            return;
        }
        if (!withinLimit(type, hero)) {
            LOG.warning("Cannot add " + type.get() + " " + card + ": limit is " + limitFor(type, hero));
            return;
        }
        if (this.deck.add(card)) {
            LOG.info("Added " + card + " to deck");
        } else {
            LOG.info("Card " + card + " already present in deck");
        }
    }

    @Override
    public final void removeCard(final CardIdentifier card) {
        if (card.equals(heroId)) {
            LOG.warning("Cannot remove the selected hero directly, add another hero to switch");
            return;
        }
        if (this.deck.remove(card)) {
            LOG.info("Removed card " + card + " from deck");
        } else {
            LOG.warning("Cannot remove " + card + ": not in deck");
        }
    }

    @Override
    public final void onCardAdded(final CardIdentifier card) {
        this.userCollection.add(card);
    }

    private CardIdentifier findFirstHero() {
        return this.userCollection.stream()
            .filter(c -> {
                try {
                    return CardType.valueOf(c.type()) == CardType.HERO;
                } catch (final IllegalArgumentException e) {
                    return false;
                }
            })
            .findFirst()
            .orElse(null);
    }

    private boolean owns(final CardIdentifier card) {
        return this.userCollection.contains(card);
    }

    private void switchHero(final CardIdentifier newHero) {
        if (!newHero.equals(heroId)) {
            this.deck.removeIf(c -> {
                try {
                    return CardType.valueOf(c.type()) == CardType.HERO;
                } catch (final IllegalArgumentException e) {
                    return false;
                }
            });
            heroId = newHero;
            this.deck.add(newHero);
            LOG.info("Switched hero to " + newHero);
        }
    }

    private boolean withinLimit(final CardType type, final Hero hero) {
        return countByType(type) < limitFor(type, hero);
    }

    private long countByType(final CardType type) {
        return this.deck.stream()
            .filter(c -> {
                try {
                    return CardType.valueOf(c.type()) == type;
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
