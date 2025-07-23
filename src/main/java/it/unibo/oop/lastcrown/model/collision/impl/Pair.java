package it.unibo.oop.lastcrown.model.collision.impl;

import java.util.Objects;

/**
 * A standard generic immutable pair class representing a tuple of two elements.
 *
 * @param <E1> the type of the first element
 * @param <E2> the type of the second element
 */
public final class Pair<T, U> {

    private final T e1;
    private final U e2;

    /**
     * Constructs a new Pair with the specified elements.
     *
     * @param x the first element
     * @param y the second element
     */
    public Pair(final T x, final U y) {
        super();
        this.e1 = x;
        this.e2 = y;
    }

    /**
     * Returns the first element of the pair.
     *
     * @return the first element
     */
    public T get1() {
        return e1;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return the second element
     */
    public U get2() {
        return e2;
    }

    /**
     * Returns the hash code of this pair based on its elements.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(e1, e2);
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * Two pairs are equal if both elements are equal.
     *
     * @param obj the reference object with which to compare
     * @return true if this object is the same as the obj argument, false otherwise
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
