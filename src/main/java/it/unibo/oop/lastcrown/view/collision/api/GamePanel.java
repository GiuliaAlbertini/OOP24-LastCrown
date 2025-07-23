package it.unibo.oop.lastcrown.view.collision.api;

import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Represents the main game panel which manages character components and rendering.
 * It allows adding or removing character elements, repainting the display,
 * and retrieving or interacting with the panel itself.
 */
public interface GamePanel {

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
     * Repaints the game panel, updating the visual display of components.
     */
    void repaintGamePanel();

    /**
     * Sets an ActionListener that handles the addition of new characters to the panel.
     *
     * @param listener the ActionListener to be triggered when a character is added
     */
    void setAddCharacterListener(ActionListener listener);

    /**
     * Returns the underlying JPanel associated with the game panel.
     *
     * @return the JPanel used to display and manage game elements
     */
    JPanel getPanel();
}
