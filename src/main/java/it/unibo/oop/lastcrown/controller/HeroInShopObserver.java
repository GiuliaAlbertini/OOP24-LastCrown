package it.unibo.oop.lastcrown.controller;

/**
 * The observer of the hero movement in the shop.
 */
public interface HeroInShopObserver {

    /**
     * Notifies the observer that the player wants to make the hero run to the right.
     */
    void notifyRight();

     /**
     * Notifies the observer that the player wants to make the hero stop to the right.
     */
    void notifyStopRight();

     /**
     * Notifies the observer that the player wants to make the hero run to the left.
     */
    void notifyLeft();

     /**
     * Notifies the observer that the player wants to make the hero stop to the left.
     */
    void notifyStopLeft();

    /**
     * Notifies the observer that the player wants to start a new battle.
     */
    void notifyMatchView();
}
