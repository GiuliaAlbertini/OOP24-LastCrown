package it.unibo.oop.lastcrown.model.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Random;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.user.impl.AccountImpl;

final class TestAccount {
    private static final int CARD_ID_BOUND = 100;
    private static final int CARD_ID_ORIGIN = 50;
    private static final int PLAYTIME_BOUND = 100;
    private static final int DEFEATED_BOSSES_BOUND = 20;
    private static final int PLAYED_MATCHES_BOUND = 50;
    private static final int STARTING_COINS = 500;
    private static final double TOLLERANCE = 1e-6;
    private final Random rand = new Random();
    private AccountImpl account;

    @BeforeEach
    void setUp() {
        account = new AccountImpl("testUser");
    }

    @Test
    void testInitialValues() {
        assertEquals("testUser", account.getUsername());
        assertEquals(STARTING_COINS, account.getCoins());
        assertEquals(0, account.getBossesDefeated());
        assertEquals(0, account.getPlayedMatches());
        assertEquals(0.0, account.getPlaytime(), TOLLERANCE);
    }

    @Test
    void testCoinOperations() {
        final int coinsToAdd = rand.nextInt(STARTING_COINS);
        account.addCoins(coinsToAdd);
        final int tot = STARTING_COINS + coinsToAdd;
        assertEquals(tot, account.getCoins());
        final int coinsToRemove = rand.nextInt(tot);
        account.removeCoins(coinsToRemove);
        assertEquals(tot - coinsToRemove, account.getCoins());
    }

    @Test
    void testMatchAndBossStats() {
        final int played = rand.nextInt(PLAYED_MATCHES_BOUND);
        for (int i = 0; i < played; i++) {
            account.increasePlayedMatches();
        }
        final int defeated = played + rand.nextInt(DEFEATED_BOSSES_BOUND);
        for (int i = 0; i < defeated; i++) {
            account.increaseBossesDefeated();
        }
        assertEquals(played, account.getPlayedMatches());
        assertEquals(defeated, account.getBossesDefeated());
        final double res = played == 0 ? 0.0 : (defeated / (double) played);
        assertEquals(res, account.computeBossesPerMatch(), TOLLERANCE);
    }

    @Test
    void testPlaytime() {
        final double time1 = rand.nextDouble(PLAYTIME_BOUND);
        account.addPlaytime(time1);
        final double time2 = rand.nextDouble(PLAYTIME_BOUND);
        account.addPlaytime(time2);
        assertEquals(time1 + time2, account.getPlaytime(), TOLLERANCE);
    }

    @Test
    void testUserCollectionIsolation() {
        final var copy = account.getUserCollection();
        final int num = rand.nextInt(CARD_ID_ORIGIN, CARD_ID_BOUND);
        final CardIdentifier newCard = new CardIdentifier(num, CardType.SPELL);
        account.addCard(newCard);
        assertFalse(copy.getCollection().contains(newCard));
    }
}
