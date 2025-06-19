package it.unibo.oop.lastcrown.model.impl;

import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Vect2D;

/**
 * Implementation of the Point2D interface representing a point in 2D space.
 * Provides methods for vector addition and distance calculation.
 */
public final class Point2DImpl implements Point2D {
    private final double x;
    private final double y;

    /**
     * Constructs a new point with the specified x and y coordinates.
     *
     * @param x the x coordinate of the point
     * @param y the y coordinate of the point
     */
    public Point2DImpl(final double x, final double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public Point2D sum(final Vect2D v) {
        return new Point2DImpl(x + v.x(), y + v.y());
    }

    @Override
    public double getDistance(final Point2D p) {
        return Math.sqrt(Math.pow(this.x - p.x(), 2) + Math.pow(this.y - p.y(), 2));
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
        return "P2d(" + x + ", " + y + ")";
    }
}

