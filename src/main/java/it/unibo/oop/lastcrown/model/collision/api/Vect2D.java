package it.unibo.oop.lastcrown.model.collision.api;

/**
 * This interface represents a 2D vector, providing basic operations such as
 * addition, subtraction, multiplication, normalization, and magnitude
 * calculation.
 */
public interface Vect2D {

    /**
     * Adds the given vector to this vector.
     *
     * @param v the vector to be added
     * @return a new vector representing the sum of this vector and the given vector
     */
    Vect2D sum(Vect2D v);

    /**
     * Subtracts the given vector from this vector.
     *
     * @param v the vector to be subtracted
     * @return a new vector representing the difference between this vector and the
     *         given vector
     */
    Vect2D subtract(Vect2D v);

    /**
     * Multiplies this vector by a scalar factor.
     *
     * @param factor the scalar value to multiply the vector by
     * @return a new vector representing this vector scaled by the given factor
     */
    Vect2D mul(double factor);

    /**
     * Normalizes this vector, returning a unit vector (vector with magnitude 1)
     * in the same direction.
     *
     * @return a new normalized vector
     */
    Vect2D normalized();

    /**
     * Computes the magnitude (length) of this vector.
     *
     * @return the magnitude of the vector
     */
    double module();

    /**
     * @return the x component of the vector
     */
    double x();

    /**
     * @return the y component of the vector
     */
    double y();

    /**
     * @return a string representation of the vector
     */
    String toString();
}


