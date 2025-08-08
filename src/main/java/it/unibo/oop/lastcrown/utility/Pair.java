package it.unibo.oop.lastcrown.utility;

import java.util.Objects;

/**
 * A generic immutable pair of two elements.
 * Provides proper implementations of equals, hashCode, and toString .
 *
 * @param <X> the type of the first element
 * @param <Y> the type of the second element
 */
public final class Pair<X, Y> {

    private final X e1;
    private final Y e2;

    /**
     * Constructs a new pair with the given elements.
     *
     * @param x the first element
     * @param y the second element
     */
    public Pair(final X x, final Y y) {
        this.e1 = x;
        this.e2 = y;
    }

    /**
     * Returns the first element of the pair.
     *
     * @return the first element
     */
    public X get1() {
        return e1;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return the second element
     */
    public Y get2() {
        return e2;
    }

    /**
     * Computes the hash code for this pair using both elements.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }

    /**
     * Checks if this pair is equal to another object.
     * Two pairs are equal if both their first and second elements are equal.
     *
     * @param obj the object to compare with
     * @return true if the pairs are equal, false otherwise
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Pair<?, ?> other = (Pair<?, ?>) obj;
        return Objects.equals(e1, other.e1) && Objects.equals(e2, other.e2);
    }

    /**
     * Returns a string representation of the pair.
     *
     * @return a string in the format "Pair [e1=..., e2=...]"
     */
    @Override
    public String toString() {
        return "Pair [e1=" + e1 + ", e2=" + e2 + "]";
    }
}
