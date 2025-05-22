package it.unibo.oop.lastcrown.view;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
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


    /**
     * Adds a visual component (like a character) to the current game panel.
     *
     * @param comp the component to add
     */
    void setAddCharacterListener(ActionListener listener);
     
    /**
     * Returns the game panel currently in use, which manages rendering and character components.
     *
     * @return the GamePaAnel used in the game view
     */    
    GamePanel getGamePanel();

}

