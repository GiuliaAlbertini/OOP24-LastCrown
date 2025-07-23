package it.unibo.oop.lastcrown.controller.app_managing.impl;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.api.UserCollection;
import it.unibo.oop.lastcrown.model.user.impl.AccountImpl;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

/**
 * Controller to handle account changes during a match.
 */
public final class InGameAccountManager {
    private static final CompleteCollection COMPLETE_COLLECTION = new CompleteCollectionImpl();

    private final Account account;

    /**
     * Private constructor to initialize the manager with a copy of the given account.
     *
     * @param original the account to copy and manage
     */
    private InGameAccountManager(final Account original) {
        this.account = createAccountCopy(original);
    }

    /**
     * Static factory method to create a new controller instance managing a copy of the given account.
     *
     * @param original the account to manage
     * @return a new InGameAccountManager configured with a copy of the given account
     */
    public static InGameAccountManager create(final Account original) {
        return new InGameAccountManager(original);
    }

    /**
     * Creates a deep copy of the given account.
     *
     * @param source the account to copy
     * @return a new account instance with identical state
     */
    private static Account createAccountCopy(final Account source) {
        final Account copy = new AccountImpl(source.getUsername());
        copy.removeCoins(copy.getCoins());
        copy.addCoins(source.getCoins());
        for (int i = 0; i < source.getBossesDefeated(); i++) {
            copy.increaseBossesDefeated();
        }
        for (int i = 0; i < source.getPlayedGames(); i++) {
            copy.increasePlayedGames();
        }
        copy.addPlaytime(source.getPlaytime());
        final UserCollection col = source.getUserCollection();
        for (final CardIdentifier id : col.getCollection()) {
            copy.addCard(id);
        }
        return copy;
    }

    /**
     * Tells if a card is buyable.
     * 
     * @param id the card to check
     * @return True if the card is buyable, False otherwise
     */
    public Boolean isBuyable(final CardIdentifier id) {
        return substractCoinsAndCost(id) >= 0;
    }

    /**
     * Getter for the account collection.
     * @return the collection
     */
    public Set<CardIdentifier> getUserCollection() {
        return this.account.getUserCollection().getCollection();
    }

    /**
     * Adds a new card to the managed account's collection.
     *
     * @param newCard the identifier of the card to add
     */
    public void addCard(final CardIdentifier newCard) {
        this.account.addCard(newCard);
        this.account.removeCoins(getCardCost(newCard));
    }

    /**
     * Adds coins to the managed account.
     *
     * @param amount the number of coins to add
     */
    public void addCoins(final int amount) {
        this.account.addCoins(amount);
    }

    /**
     * Removes coins from the managed account.
     *
     * @param amount the number of coins to remove
     */
    public void removeCoins(final int amount) {
        this.account.removeCoins(amount);
    }

    /**
     * Records the defeat of a boss for the managed account.
     */
    public void recordBossDefeat() {
        this.account.increaseBossesDefeated();
    }

    /**
     * Records the playing of a game for the managed account.
     */
    public void recordGamePlayed() {
        this.account.increasePlayedGames();
    }

    /**
     * Adds playtime to the managed account.
     *
     * @param hours the amount of playtime to add, in hours
     */
    public void addPlaytime(final double hours) {
        this.account.addPlaytime(hours);
    }

    /**
     * Returns a safe copy of the managed account.
     *
     * @return a copy of the current state of the managed account
     */
    public Account getManagedAccount() {
        return createAccountCopy(this.account);
    }

    private int substractCoinsAndCost(final CardIdentifier id) {
        return this.account.getCoins() - getCardCost(id);
    }

    private Integer getCardCost(final CardIdentifier id) {
        return COMPLETE_COLLECTION.getCost(id);
    }
}
