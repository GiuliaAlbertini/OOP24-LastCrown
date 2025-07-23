package it.unibo.oop.lastcrown.model.user.impl;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.model.user.api.UserCollection;

/**
 * Implementation of {@Account}.
 */
public class AccountImpl implements Account {
    private final String username;
    private int coins;
    private int bossesDefeated;
    private int playedGames;
    private double playtime;
    private final UserCollection userCollection;

    /**
     * Constructs an {@code AccountImpl} with the specified username.
     * This constructor initializes the account with default values.
     *
     * @param username the username for the account
     */
    public AccountImpl(final String username) {
        this.username = username;
        this.coins = 300;
        this.bossesDefeated = 0;
        this.playedGames = 0;
        this.playtime = 0.0;
        this.userCollection = new UserCollectionImpl();
    }

    @Override
    public final void addCoins(final int earning) {
        this.coins += earning;
    }

    @Override
    public final void removeCoins(final int earning) {
        this.coins -= earning;
    }

    @Override
    public final void increaseBossesDefeated() {
        this.bossesDefeated++;
    }

    @Override
    public final void increasePlayedGames() {
        this.playedGames++;
    }

    @Override
    public final double computeBossesPerMatch() {
        return (double) this.playedGames / (this.bossesDefeated == 0 ? 1 : this.bossesDefeated);
    }

    @Override
    public final void addPlaytime(final double time) {
        this.playtime += time;
    }

    @Override
    public final String getUsername() {
        return this.username;
    }

    @Override
    public final int getCoins() {
        return this.coins;
    }

    @Override
    public final int getBossesDefeated() {
        return this.bossesDefeated;
    }

    @Override
    public final int getPlayedGames() {
        return this.playedGames;
    }

    @Override
    public final double getPlaytime() {
        return this.playtime;
    }

    @Override
    public final void addCard(final CardIdentifier newCard) {
        this.userCollection.addCard(newCard);
    }

    @Override
    public final UserCollection getUserCollection() {
        final UserCollection orig = this.userCollection;
        final UserCollection copy = new UserCollectionImpl();
        for (final var card : orig.getCollection()) {
            copy.addCard(card);
        }
        return copy;
    }
}
