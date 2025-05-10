package it.unibo.oop.lastcrown.model.user.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Provides methods to manage an account.
 */
public interface Account {

    /**
     * Provides the user's username.
     * 
     * @return the username
     */
    String getUsername();

    /**
     * Increases the amount of coins owned by the user by earning.
     * 
     * @param earning
     */
    void addCoins(int earning);

    /**
     * Decreases the amount of coins owned by the user by earning.
     * 
     * @param earning
     */
    void removeCoins(int earning);

    /**
     * Increases the amount of bosses defeated by the user by one.
     */
    void increaseBossesDefeated();

    /**
     * Increases the amount of games played by the user by one.
     */
    void increasePlayedGames();

    /**
     * Compute the amount of average bosses defeated in a match.
     * 
     * @return the average calculated
     */
    double computeBossesPerMatch();

    /**
     * Increases the time spent in the game by the user of a quantity {@code time}.
     * 
     * @param time the time to add
     */
    void addPlaytime(double time);

    /**
     * Provides the amount of coins owned by the user.
     * 
     * @return the amount of coins
     */
    int getCoins();

    /**
     * Provides the number of bosses defeated by the user.
     * 
     * @return the number of bosses defeated
     */
    int getBossesDefeated();

    /**
     * Provides the number of games played.
     * 
     * @return the number of games
     */
    int getPlayedGames();

    /**
     * Provides the time spent in the game by the user.
     * 
     * @return the time spent
     */
    double getPlaytime();

    /**
     * Add a card to the user's collection.
     * 
     * @param newCard the card to add
     */
    void addCard(CardIdentifier newCard);

    /**
     * Provides the user's collection.
     * 
     * @return the {@link UserCollection} of the user
     */
    UserCollection getUserCollection();
}

