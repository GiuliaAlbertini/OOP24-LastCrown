package it.unibo.oop.lastcrown.model.collision.api;

import it.unibo.oop.lastcrown.utility.api.Point2D;

/**
 * Represents a movement behavior where a character automatically follows
 * a specific enemy target along a calculated path. The interface defines methods
 * to start the following process, update the movement step-by-step, and query
 * information about the current position, the moving character, and the followed enemy.
 */
public interface FollowEnemy {

    /**
     * Starts the following motion, activating the path calculation.
     * Does nothing if the movement is already active or stopped.
     */
    void startFollowing();

    /**
     * Performs a single algorithm step, advancing the character along the curve.
     *
     * @param deltaMs the amount of time in milliseconds since the last step.
     * @return false if the following movement is complete (target reached or
     *         collision occurred), true otherwise.
     */
    boolean update(long deltaMs);

    /**
     * Gets the character's current position.
     *
     * @return A Point2D representing the current character's position.
     */
    Point2D getCurrentPosition();

    /**
     * Gets the character that is following.
     *
     * @return The Collidable representing the character.
     */
    Collidable getCharacter();

    /**
     * Gets the enemy being followed.
     *
     * @return The Collidable representing the currently followed enemy.
     */
    Collidable getEnemy();


    /**
     * Gets the last calculated position variation (delta).
     *
     * @return The Point2D representing how much the character has moved
     *         since the last step.
     */
    Point2D getDelta();
}
