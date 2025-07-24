package it.unibo.oop.lastcrown.controller.menu.api;

import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.SceneName;

/**
 * Provides methods to handle the visualization of differents scenes.
 */
public interface SceneManager {
    /**
     * Switch to the scene indicated by the name passed as parameter.
     * 
     * @param caller the caller of the switch
     * @param destination the name of the scene to change the current one with
     */
    void switchScene(SceneName caller, SceneName destination);

    /**
     * Closes the whole application.
     */
    void closeApplication();

    /**
     * Notify the users who use the user's collection that it changed.
     * 
     * @param newSet the new collection to use
     */
    void updateUserCollectionUsers(Set<CardIdentifier> newSet);
}
