package it.unibo.oop.lastcrown.view.menu.api;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.SceneName;

/**
 * Defines the main view of the application.
 */
public interface MainView {

    /**
     * Changes the currently displayed panel to the one corresponding to the given scene name.
     *
     * @param sceneCaller the name of the calling scene.
     * @param sceneDestination the name of the scene to be displayed,
     */
    void changePanel(SceneName sceneCaller, SceneName sceneDestination);

    /**
     * Calls the close of the application.
     */
    void closeApplication();

    /**
     * Update the DeckView in use.
     * 
     * @param newSet the updated collection to show
     */
    void updateUserCollectionUsers(Set<CardIdentifier> newSet);

    /**
     * Update the users of the given account.
     * 
     * @param account the new version of the account
     */
    void updateAccountUsers(Account account);

    /**
     * Notify the ShopView that the account needs changes.
     * 
     * @param amount the coins to add
     * @param bossDefeated flag that is true if a boss has been defeated
     */
    void updateAccount(int amount, boolean bossDefeated);
}
