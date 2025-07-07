package it.unibo.oop.lastcrown.view.shop;

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
     * Notifies that the player is trying to interact with one of the traders.
     */
    void notifyInteraction();
}
