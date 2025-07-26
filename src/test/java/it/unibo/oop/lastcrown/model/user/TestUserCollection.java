package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.BeforeEach; import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.card.CardIdentifier; import it.unibo.oop.lastcrown.model.card.CardType; import it.unibo.oop.lastcrown.model.user.api.UserCollectionListener; import it.unibo.oop.lastcrown.model.user.impl.UserCollectionImpl;

final class TestUserCollection { 
    private UserCollectionImpl collection;
    @BeforeEach
    void setUp() {
        collection = new UserCollectionImpl();
    }

    @Test
    void testGetCollectionUnmodifiable() {
        var cols = collection.getCollection();
        assertThrows(UnsupportedOperationException.class, () -> cols.add(new CardIdentifier(10, CardType.SPELL)));
    }

    @Test
    void testAddCardAndListenerNotification() {
        AtomicReference<CardIdentifier> notified = new AtomicReference<>();
        collection.addListener(new UserCollectionListener() {
            @Override
            public void onCardAdded(CardIdentifier card) {
                notified.set(card);
            }
        });
        CardIdentifier card = new CardIdentifier(42, CardType.SPELL);
        collection.addCard(card);
        assertTrue(collection.getCollection().contains(card));
        assertEquals(card, notified.get());
    }
}
