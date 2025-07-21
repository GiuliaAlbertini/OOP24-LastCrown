package it.unibo.oop.lastcrown.model.collision.api;

/**
 * Represents a point in a 2D Cartesian coordinate system.
 *
 * Provides methods to access coordinates, compute distance between points,
 * and perform vector-based translations.
 */
public interface Point2D {
    /**
     * @return the x coordinate of the point
     */
    double x();

    /**
     * @return the y coordinate of the point
     */
    double y();

   /**
     * This method shifts (sums) the point for a given vector.
     * @param v the vector to be summed
     * @return the new point, representing the sum of the current point and the vector
     */
    Point2D sum(Vect2D v);
     /**
     *
     * @param p the point to compute the distance with
     * @return the distance between two points
     */
    double getDistance(Point2D p);
}
