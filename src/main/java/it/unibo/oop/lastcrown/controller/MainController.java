package it.unibo.oop.lastcrown.controller;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * An interface that model the main controller.
 */
public interface MainController {

    /**
     * Notifies that the player during the match has pressed a button with a specific CardIdentifier.
     * @param id the CardIdentifier of the specific card associated with the button
     */
    void notifyButtonPressed(CardIdentifier id);

    /**
     * Notifies that the player is doing a valid click in the specified coordinates.
     * @param x the x corrdinate
     * @param y the y coordinate
     */
    void notifyClicked(int x, int y);

    /**
     * Notifies that the player has given the OK to change view from match to the shop.
     * @param beginning True if 
     */
    void notifyMatchToShop(boolean beginning);

    /**
     * Notifies that the player has given the OK to change view from match to the menu.
     */
    void notifyEndOfTheGame();
}
