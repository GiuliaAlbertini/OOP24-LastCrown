package it.unibo.oop.lastcrown.model.api;

/**
 * Represents the hitbox of a game object, used for collision detection.
 */
public interface Hitbox {

    /**
     * Returns the position (top-left corner) of this hitbox.
     *
     * @return the position as a Point2D
     */
    Point2D getPosition();

    /**
     * Returns the width of the hitbox.
     *
     * @return the width in pixels
     */
    int getWidth();

    /**
     * Returns the height of the hitbox.
     *
     * @return the height in pixels
     */
    int getHeight();

    /**
     * Checks whether this hitbox collides with another hitbox.
     *
     * @param other Hitbox to check collision against
     * @return true if the two hitboxes collide, false otherwise
     */
    boolean checkCollision(Hitbox other);
}
