package it.unibo.oop.lastcrown.view.map;

import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Interface for a deck zone.
 */
public interface DeckZone {
    /**
     * Getter for the last card clicked.
     * 
     * @return the CardType of the last clicked button if it's present
    */
    Optional<CardIdentifier> getLastClicked();

    /**
     * Updates the energy bar and notify the InGameController of the card used.
     *
     * @return a boolean indicating if a card has bee used or not
     */
    boolean playCard();

    /**
     * Updates the deck.
     * 
     * @param newDeck the new set rappresenting the deck
     */
    void updateInGameDeck(Set<CardIdentifier> newDeck);

    /**
     * Handle the enabling of the card buttons.
     * It disable them when a boss fight start.
     * 
     * @param start {@code True} if the boss fight is started, {@code False} otherwise
     */
    void handleButtonsEnabling(boolean start);

    /**
     * Moves and resizes this component. 
     * The new location of the top-left corner is specified by x and y, and the new size is specified by width and height.
     * This method changes layout-related information, and therefore, invalidates the component hierarchy
     * 
     * @param x the new x-coordinate of this component
     * @param y the new y-coordinate of this component
     * @param width the new width of this component
     * @param height the new height of this component
     */
    void setBounds(int x, int y, int width, int height);
}
