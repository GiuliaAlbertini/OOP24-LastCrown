package it.unibo.oop.lastcrown.model.api;

import java.util.List;
import java.util.Optional;

/**
 * Represents a circular area defined by a center point and a radius.
 * Provides methods to query enemies within this radius, such as retrieving
 * all enemies inside, finding the closest enemy, and checking for presence of any enemy.
 */
public interface Radius {
   /**
     * Returns a list of enemies whose hitboxes are within the radius.
     *
     * @param enemies the list of enemies to check
     * @return a list of enemies within the radius
     */
    List<Hitbox> getEnemiesInRadius(List<Hitbox> enemies);

    /**
     * Returns the closest enemy within the radius, if any.
     *
     * @param enemies the list of enemies to check
     * @return an Optional containing the closest enemy if one is found, or empty if none are within the radius
     */
    Optional<Hitbox> getClosestEnemyInRadius(List<Hitbox> enemies);

    /**
     * Checks if there is at least one enemy within the radius.
     *
     * @param enemies the list of enemies to check
     * @return true if at least one enemy is within the radius, false otherwise
     */
    boolean hasEnemyInRadius(List<Hitbox> enemies);

    /**
     * Returns the center point of the radius.
     *
     * @return the center Point2D of the radius
     */
    Point2D getCenter();

    /**
     * Returns the size of the radius.
     *
     * @return the radius as a double
     */
    double getRadius();
}
