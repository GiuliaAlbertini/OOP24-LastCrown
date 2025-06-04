package it.unibo.oop.lastcrown.view.menu.api;

/**
 * Defines the main view of the application.
 */
public interface MainView {

    /**
     * Changes the currently displayed panel to the one corresponding to the given scene name.
     *
     * @param sceneName the name of the scene to display.
     */
    void changePanel(String sceneName);

    /**
     * Calls the close of the application.
     */
    void closeApplication();
}
