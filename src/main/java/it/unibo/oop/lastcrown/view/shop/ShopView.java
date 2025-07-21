package it.unibo.oop.lastcrown.view.shop;

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
}
