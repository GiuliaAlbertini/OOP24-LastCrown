package it.unibo.oop.lastcrown.model.api;

public interface Point2D {

    /*
     * @return the x coordinate of the point
     */
    double x();

    /*
     * @return the y coordinate of the point
     */
    double y();

   /**
     * This method shifts (sums) the point for a given vector.
     * @param vect the vector to be summed
     * @return the new point, representing the sum of the current point
     *         and the vector
     */
    Point2D sum(Vect2D v);

    
     /**
     * 
     * @param p the point to compute the distance with
     * @return the distance between two points
     */
    double getDistance(Point2D p);

   
}