package it.unibo.oop.lastcrown.view.shop;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * The observer of the shop frame.
 */
public interface ContainerObserver {

    /**
     * Notifies that the player wants to exit the shop and start a new battle. 
     */
    void notifyExit();

    /**
     * Notifies that the player want to finish this game.
     */
    void notifyEscape();

    /**
     * Notifies thaht the player wants to check their collection.
     */
    void notifyCollection();

    /**
     * Notifues that the player wants to start a new Match.
     */
    void notifyMatch();

    /**
     * Notifies that the player wants to start a shopping session of the specified Card Type.
     * @param id the id of the trader selected by the player
     * @param cardType the cardType that the selected trader can show
     */
    void notifyInteraction(int id, CardType cardType);

    /**
     * Notifies thaht the player has finished a shopping interaction and might have buyed a new card.
     * @param cardIdentifier Optional of CardType if it's a new card, Optional empty otherwise.
     * @param id the numerical id of the trader that notifies the container
     */
    void notifyEndInteraction(Optional<CardIdentifier> cardIdentifier, int id);
}
