package it.unibo.oop.lastcrown.view.api;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.api.GamePanel;

/**
 * Represents the main view of the application, responsible for managing
 * and displaying different panels based on the current GameState.
 *
 * Provides methods to initialize panels, switch between them, and interact
 * with the current game view (e.g., adding characters or accessing the game panel).
 */
public interface MainView {

    /**
     * Initializes all panels associated with the different game states.
     * Should be called once at application startup.
     */
    void initPanel();

    /**
     * Displays the panel corresponding to the given GameState.
     *
     * @param panel the game state whose panel should be shown
     */
    void showPanel(GameState panel);

    /**
     * Returns the panel associated with the given GameState.
     *
     * @param panel the game state for which to retrieve the panel
     * @return the JPanel corresponding to the specified state
     */
    JPanel getPanel(GameState panel);

    /**
     * Sets a listener to handle character addition actions within the game panel.
     *
     * @param listener the ActionListener to handle character addition
     */
    void setAddCharacterListener(ActionListener listener);

    /**
     * Returns the current GamePanel used for game rendering and updates.
     *
     * @return the game panel in use
     */
    GamePanel getGamePanel();
}
