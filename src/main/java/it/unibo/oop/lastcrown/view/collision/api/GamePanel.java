package it.unibo.oop.lastcrown.view.collision.api;

import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JPanel;


/**
 * Represents the main game panel which manages character components and rendering.
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
     * Repaints the game panel, updating the display.
     */
    void repaintGamePanel();
    void setAddCharacterListener(final ActionListener listener);
    JPanel getPanel();
    /*
    SidePanel getSidePanel();
    MapPanelImpl getMapPanel();
    */
}