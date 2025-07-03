package it.unibo.oop.lastcrown.controller.menu.api;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Provides methods to handle the visualization of differents scenes.
 */
public interface SceneManager {
    /**
     * Switch to the scene indicated by the name passed as parameter.
     * 
     * @param nextName the name of the scene to change the current one with
     */
    void switchScene(String nextName);

    /**
     * Closes the whole application.
     */
    void closeApplication();

    /**
     * Update the user's collection used by the DeckController.
     * 
     * @param newSet the new collection to use
     */
    void updateDeckController(Set<CardIdentifier> newSet);
}
