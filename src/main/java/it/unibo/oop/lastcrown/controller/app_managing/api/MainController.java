package it.unibo.oop.lastcrown.controller.app_managing.api;

import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;

/**
 * Provides operations to handle the main phases of the application.
 */
public interface MainController {

    /**
     * Closes the whole application.
     */
    void closeAll();

    /**
     * Notify the classes that uses the user's collection to update it with the new
     * set.
     *
     * @param newSet the new set to use
     */
    void updateUserColletionUsers(Set<CardIdentifier> newSet);

    /**
     * Sets the necessary aspects to go in the Menu section.
     *
     * @param username the username representing the account to use
     */
    void goOverLogin(String username);

    /**
     * Getter for the account in use.
     *
     * @return an optional containing the account
     */
    Optional<Account> getAccount();

    /**
     * Returns the current {@link MatchController} instance.
     *
     * @return the active MatchController
     */
    MatchController getMatchController();

    /*
     * Updates the account in the file.
     *
     * @param account the new version of the account
     */
    void updateAccount(Account account);
}
