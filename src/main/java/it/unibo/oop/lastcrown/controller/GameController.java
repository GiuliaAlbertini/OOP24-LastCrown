package it.unibo.oop.lastcrown.controller;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * A simple interface that contains some methods that gameController should have.
 */
public interface GameController {

    /**
     * Notifies tha gameController that the player wants to start anew Match from the Shop.
     */
    void notifyShopToMatch();

    /**
     * Notifies that the Player pressed a button card during the match.
     * @param id the id of the linked card
     */
    void notifyButtonPressed(CardIdentifier id);

    /**
     * Notifies that the player has done a valid click in the given coordinates.
     * @param x x coordinate
     * @param y y coordinate
     */
    void notifyClicked(int x, int y);

    /**
     * Notifies that the player wants to move from the match to the shop.
     * @param beginning True if it's the beginning of the game. false otherwise
     */
    void notifyMatchToShop(boolean beginning);
}
