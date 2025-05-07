package it.unibo.oop.lastcrown.model.api;

import java.util.List;
import java.util.Optional;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;

public interface Radius {
   /**
     * Returns a list of enemies whose hitboxes are within the radius.
     *
     * @param enemies the list of enemies to check
     * @return a list of enemies within the radius
     */
    List<Enemy> getEnemiesInRadius(final List<Enemy> enemies);

    /**
     * Returns the closest enemy within the radius, if any.
     *
     * @param enemies the list of enemies to check
     * @return an Optional containing the closest enemy if one is found, or empty if none are within the radius
     */
    Optional<Enemy> getClosestEnemyInRadius(final List<Enemy> enemies);

    /**
     * Checks if there is at least one enemy within the radius.
     *
     * @param enemies the list of enemies to check
     * @return true if at least one enemy is within the radius, false otherwise
     */
    boolean hasEnemyInRadius(final List<Enemy> enemies);

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
