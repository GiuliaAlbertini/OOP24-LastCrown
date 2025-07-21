package it.unibo.oop.lastcrown.view.shop;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.view.menu.api.Scene;

/**
 * An interface that models the shop view.
 */
public interface ShopView extends Scene {
    /**
     * Adds the hero panel to the shop by specifing if it is the beginning of the game.
     * @param heroComp the JComponent where the hero animation is located
     * @param beginning True if it is the beginning of the game, false otherwise
     */
    void addHeroPanel(JComponent heroComp, boolean beginning);

    /**
     * Notifies to the shop JFrame that it is visible to the player.
     */
    void notifyVisible();

    /**
     * Notifies to the shop JFrame that it is hidden to the player.
     */
    void notifyHidden();
}
