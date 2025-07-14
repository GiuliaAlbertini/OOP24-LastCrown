package it.unibo.oop.lastcrown.controller.api;

import javax.swing.JComponent;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.view.api.RadiusPanel;

/**
 * Controller interface for managing a hitbox and its graphical representation.
 */
public interface HitboxController {

    /**
     * Updates the position of the hitbox to the given coordinates.
     *
     * @param x the new X coordinate
     * @param y the new Y coordinate
     */
    void setnewPosition(int x, int y);

    /**
     * Updates the visual representation of the hitbox.
     */
    void updateView();

    /**
     * Sets the visibility of the hitbox and its graphical components.
     *
     * @param visible true to make it visible, false to hide it
     */
    void setVisibile(boolean visible);

    /**
     * Returns the graphical component representing the hitbox.
     *
     * @return the graphical JComponent of the hitbox
     */
    JComponent getGraphicalComponent();

    /**
     * Returns the hitbox model object.
     *
     * @return the Hitbox object
     */
    Hitbox getHitbox();

    /**
     * Returns the radius associated with the hitbox, if any.
     *
     * @return the Radius object
     */
    Radius getRadius();

    /**
     * Returns the panel used to display the radius.
     *
     * @return the JPanel used to visualize the radius
     */
    JPanel getRadiusPanel();

    /**
     * Sets the radius for the hitbox.
     *
     * @param radius the Radius object to set
     */
    void setRadius(Radius radius);

    /**
     * Sets the panel used to render the radius.
     *
     * @param radiusPanel the RadiusPanel implementation to associate
     */
    void setRadiusPanel(RadiusPanel radiusPanel);

    /**
     * Removes the hitbox and associated graphical elements from the UI.
     */
    void removeFromPanel();
}
