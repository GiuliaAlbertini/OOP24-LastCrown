package it.unibo.oop.lastcrown.view.shop;

import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.menu.api.Scene;

/**
 * An interface that models the shop view.
 */
public interface ShopView extends Scene {
    /**
     * Notifies to the shop JFrame that it is visible to the player.
     */
    void notifyVisible();

    /**
     * Notifies to the shop JFrame that it is hidden to the player.
     */
    void notifyHidden();

    /**
     * Notify that the account has changed.
     * @param amount the amount of coins to add
     * @param bossDefeated flag that is true if a boss has been defeated
     */
    void notifyUpdateAccount(int amount, boolean bossDefeated);

    /**
     * Getter for the account managed by InGameAccountManager.
     * 
     * @return the account
     */
    Account getManagedAccount();
}
