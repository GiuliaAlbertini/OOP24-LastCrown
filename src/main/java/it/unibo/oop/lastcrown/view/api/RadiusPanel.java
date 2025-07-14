package it.unibo.oop.lastcrown.view.api;

import javax.swing.JPanel;

/**
 * Interface representing a graphical panel that displays a radius
 * around a specific entity or point in the UI.
 */
public interface RadiusPanel {

    /**
     * Returns the Swing panel that visualizes the radius.
     *
     * @return the JPanel that graphically represents the radius
     */
    JPanel getRadiusPanel();

    /**
     * Updates the position of the panel based on the current
     * center position and radius. This method should be called
     * whenever the monitored entity changes its position.
     */
    void updatePosition();
}
