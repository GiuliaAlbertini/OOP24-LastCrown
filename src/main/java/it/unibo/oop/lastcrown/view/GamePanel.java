package it.unibo.oop.lastcrown.view;

import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Represents the main game panel which manages character components and rendering.
 */
public interface GamePanel {

    /**
     * Returns the main panel used for displaying the game.
     *
     * @return the game panel as a JPanel
     */
    JPanel getPanel();

    /**
     * Adds a character component to the game panel.
     *
     * @param comp the JComponent representing the character to be added
     */
    void addCharacterComponent(JComponent comp);

    /**
     * Removes a character component from the game panel.
     *
     * @param comp the JComponent representing the character to be removed
     */
    void removeCharacterComponent(JComponent comp);

    /**
     * Repaints the game panel, updating the display.
     */
    void repaintGamePanel();
}
