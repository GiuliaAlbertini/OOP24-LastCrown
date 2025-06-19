package it.unibo.oop.lastcrown.view.api;

import javax.swing.JPanel;

/**
 * Interface representing a graphical panel associated with a hitbox.
 * Provides methods to retrieve the panel, update its position and size,
 * and set a new position.
 */
public interface HitboxPanel {

    /**
     * Returns the graphical panel associated with the hitbox.
     *
     * @return the JPanel representing the hitbox
     */
    JPanel getHitboxPanel();

    /**
     * Updates the position and size of the panel based on the hitbox.
     */
    void updatePanel();

    /**
     * Sets a new position for the panel.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    void setPanelPosition(int x, int y);
}
