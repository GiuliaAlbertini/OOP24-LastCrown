package it.unibo.oop.lastcrown.model.collision.api;

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
     * Sets the position (top-left corner) of this hitbox.
     *
     * @param newPos the new position as a Point2D
     */
    void setPosition(Point2D newPos);

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
     * Sets the width of the hitbox.
     *
     * @param width the width in pixels
     */
    void setWidth(int width);

        /**
     * Sets the height of the hitbox.
     *
     * @param height the height in pixels
     */
    void setHeight(int height);


    /**
     * Calculates and returns the center point of the hitbox.
     * @return the center point of the hitbox as a Point2D object
     */
    Point2D getCenter();

    /**
     * Checks whether this hitbox collides with another hitbox.
     *
     * @param other Hitbox to check collision against
     * @return true if the two hitboxes collide, false otherwise
     */
    boolean checkCollision(Hitbox other);
}
