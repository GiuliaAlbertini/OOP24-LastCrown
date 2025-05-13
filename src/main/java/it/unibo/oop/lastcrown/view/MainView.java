package it.unibo.oop.lastcrown.view;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.controller.impl.GameState;
public interface MainView {

    /**
     * Initializes all the panels required for the different game states.
     * This method should be called once during the application startup.
     */
    void initPanel();

    /**
     * Displays the panel corresponding to the specified game state.
     *
     * @param panel the game state indicating which panel to show
     */
    void showPanel(GameState panel);

    /**
     * Returns the panel associated with the specified game state.
     *
     * @param panel the game state whose panel is to be retrieved
     * @return the JPanel corresponding to the given game state
     */
    JPanel getPanel(GameState panel);
}

