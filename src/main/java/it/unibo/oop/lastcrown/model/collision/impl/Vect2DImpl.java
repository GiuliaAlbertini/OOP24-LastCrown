package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.collision.api.Vect2D;

/**
 * Implementation of the Vect2D interface.
 * Represents a 2D vector with basic vector operations such as addition, subtraction,
 * scalar multiplication, normalization, and module (magnitude) calculation.
 */
public final class Vect2DImpl implements Vect2D {
    private final double x;
    private final double y;

    /**
     * Constructs a 2D vector with the specified x and y components.
     *
     * @param x the x component of the vector
     * @param y the y component of the vector
     */
    public Vect2DImpl(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Vect2D sum(final Vect2D v) {
        return new Vect2DImpl(this.x + v.x(), this.y + v.y());
    }

    @Override
    public Vect2D subtract(final Vect2D v) {
       return new Vect2DImpl(this.x - v.x(), this.y - v.y());
    }

    @Override
    public Vect2D mul(final double factor) {
        return new Vect2DImpl(this.x * factor, this.y * factor);
    }

    @Override
    public Vect2D normalized() {
        final double module = Math.sqrt(x * x + y * y);
        return new Vect2DImpl(x / module, y / module);
    }

    @Override
    public double module() {
        return Math.sqrt(x * x + y * y);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public String toString() {
        return "Vect2d(" + x + ", " + y + ")";
    }
}
