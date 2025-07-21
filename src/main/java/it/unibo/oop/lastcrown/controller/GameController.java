package it.unibo.oop.lastcrown.controller;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * An interface that model the main controller.
 */
public interface GameController {

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
     * The position of the hero in the shop changes according to te actual game phase.
     * @param beginning True if it's the beginning of the game, false otherwise
     */
    void notifyMatchToShop(boolean beginning);

    /**
     * Notifies that the player has given the OK to change view from match to the menu.
     */
    void notifyEndOfTheGame();

    /**
     * Notifies that the player has entered the pause menu.
     */
    void notifyPause();

    /**
     * Notifies that the player has exit the pause menu.
     */
    void notifyPauseEnd();

    /**
     * @return the wall health bar graphic component.
     */
    JComponent getWallHealthBar();

    /**
     * @return the event writer graphic component.
     */
    JComponent getEventWriter();

    /**
     * @return the coins writer bar graphic component.
     */
    JComponent getCoinsWriter();
}
