package it.unibo.oop.lastcrown.view.menu.api;

import javax.swing.JPanel;

/**
 * Represents a visual scene in the application.
 * <p>
 * Each scene has a name and an associated JPanel that is displayed.
 * </p>
 */
public interface Scene {

    /**
     * Returns the name of the scene.
     *
     * @return the scene name
     */
    String getSceneName();

    /**
     * Returns the JPanel associated with the scene.
     *
     * @return the scene's JPanel
     */
    JPanel getPanel();
}

