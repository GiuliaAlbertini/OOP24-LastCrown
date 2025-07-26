package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.card.CardIdentifier; import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.api.Deck;
import it.unibo.oop.lastcrown.model.user.impl.DeckImpl;

final class DeckTest { 
    private Deck deck; 
    private CardIdentifier hero; 
    private CardIdentifier meleeCard;
    @BeforeEach
    void setUp() {
        hero = new CardIdentifier(1, CardType.HERO);
        meleeCard = new CardIdentifier(2, CardType.MELEE);
        final Set<CardIdentifier> collection = Set.of(hero, meleeCard);
        deck = DeckImpl.createDeck(collection);
    }

    @Test
    void testInitHero() {
        assertEquals(hero, deck.getHero());
        assertTrue(deck.getDeck().contains(hero));
    }

    @Test
    void testAddAndRemoveCard() {
        deck.addCard(meleeCard);
        assertTrue(deck.getDeck().contains(meleeCard));
        deck.removeCard(meleeCard);
        assertFalse(deck.getDeck().contains(meleeCard));
    }

    @Test
    void testCannotAddUnknownCard() {
        final CardIdentifier unknown = new CardIdentifier(5, CardType.RANGED);
        deck.addCard(unknown);
        assertFalse(deck.getDeck().contains(unknown));
    }

    @Test
    void testSwitchHero() {
        final CardIdentifier newHero = new CardIdentifier(3, CardType.HERO);
        final Set<CardIdentifier> col = Set.of(newHero);
        final DeckImpl newDeck = (DeckImpl) DeckImpl.createDeck(col);
        assertEquals(newHero, newDeck.getHero());
    }
}