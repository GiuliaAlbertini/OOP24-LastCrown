package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.api.UserCollection;
import it.unibo.oop.lastcrown.model.user.impl.UserCollectionImpl;

final class TestUserCollection {

    private UserCollection userCollection;

    @BeforeEach
    void setUp() {
        userCollection = new UserCollectionImpl();
    }

    @Test
    void testInitialCollectionIsNotEmpty() {
        final Set<CardIdentifier> collection = userCollection.getCollection();
        assertNotNull(collection, "La collezione non dovrebbe essere null");
        assertFalse(collection.isEmpty(), "La collezione iniziale dovrebbe contenere carte a costo zero");
    }

    @Test
    void testAddCardToCollection() {
        final CardIdentifier newCard = new CardIdentifier(99, CardType.SPELL);
        assertFalse(userCollection.getCollection().contains(newCard), "La carta non dovrebbe essere già presente");
        
        userCollection.addCard(newCard);
        
        assertTrue(userCollection.getCollection().contains(newCard), "La carta dovrebbe essere stata aggiunta");
    }

    @Test
    void testCollectionIsUnmodifiable() {
        assertThrows(UnsupportedOperationException.class, () -> {
            userCollection.getCollection().add(new CardIdentifier(100, CardType.HERO));
        }, "La collezione restituita dovrebbe essere immutabile");
    }
}
