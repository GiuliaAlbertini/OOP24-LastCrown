package it.unibo.oop.lastcrown.view.collision.api;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.collision.api.Point2D;

/** Interface for the characters' hitbox mask. */
public interface HitboxMask {

    /**
     * Calculates the hitbox's center and dimensions by analyzing the alpha channel of the given image.
     * Updates the hitbox's size and position accordingly.
     *
     * @param image the image used to determine the opaque area of the hitbox
     */
    void calculateHitboxCenter(BufferedImage image);

    /**
     * Returns the center point of the hitbox relative to the character component.
     *
     * @return the center of the hitbox as a Point2D object
     */
    Point2D getCenter();

    /**
     * Returns the character's graphical component.
     *
     * @return the Swing component representing the character
     */
    JComponent getCharComponent();

    /**
     * Updates the hitbox's position based on the graphical component's current coordinates.
     *
     * @param componentX the X coordinate of the character's component
     * @param componentY the Y coordinate of the character's component
     */
    void updateHitboxPosition(int componentX, int componentY);

}
