package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.impl.AccountControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.impl.AccountImpl;

final class TestAccount {
    private AccountImpl account;

    @BeforeEach
    void setUp() {
        AccountController contr = new AccountControllerImpl("m");
        account = new AccountImpl("testUser");
    }

    @Test
    void testInitialValues() {
        assertEquals("testUser", account.getUsername());
        assertEquals(500, account.getCoins());
        assertEquals(0, account.getBossesDefeated());
        assertEquals(0, account.getPlayedMatches());
        assertEquals(0.0, account.getPlaytime(), 1e-6);
    }

    @Test
    void testCoinOperations() {
        account.addCoins(100);
        assertEquals(600, account.getCoins());
        account.removeCoins(200);
        assertEquals(400, account.getCoins());
    }

    @Test
    void testMatchAndBossStats() {
        account.increasePlayedMatches();
        account.increasePlayedMatches();
        account.increaseBossesDefeated();
        assertEquals(2, account.getPlayedMatches());
        assertEquals(1, account.getBossesDefeated());
        assertEquals(2.0, account.computeBossesPerMatch(), 1e-6);
    }

    @Test
    void testPlaytime() {
        account.addPlaytime(1.5);
        account.addPlaytime(2.25);
        assertEquals(3.75, account.getPlaytime(), 1e-6);
    }

    @Test
    void testUserCollectionIsolation() {
        var copy = account.getUserCollection();
        CardIdentifier newCard = new CardIdentifier(99, CardType.SPELL);
        account.addCard(newCard);
        assertFalse(copy.getCollection().contains(newCard));
    }
}
