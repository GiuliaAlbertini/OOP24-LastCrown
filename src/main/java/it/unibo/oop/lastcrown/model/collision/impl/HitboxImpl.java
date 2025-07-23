package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;

/**
 * Implementation of the Hitbox interface.
 * Represents a rectangular hitbox defined by a position (top-left corner),
 * a width, and a height. It includes methods for collision detection,
 * center point calculation, and dimension updates.
 */
public final class HitboxImpl implements Hitbox {
    private Point2D position; // coordinate x,y angolo in alto a sinistra
    private int width;
    private int height;

    /**
     * Constructs a new Hitbox with the specified width, height, and position.
     *
     * @param width    the width of the hitbox
     * @param height   the height of the hitbox
     * @param position the top-left corner of the hitbox represented as a Point2D
     */
    public HitboxImpl(final int width, final int height, final Point2D position) {
        this.height = height;
        this.width = width;
        this.position = position;
    }

    @Override
    public Point2D getPosition() {
        return this.position;
    }

    @Override
    public void setPosition(final Point2D newPos) {
        this.position = newPos;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public Point2D getCenter() {
        final double centerX = this.position.x() + this.width / 2.0;
        final double centerY = this.position.y() + this.height / 2.0;
        return new Point2DImpl(centerX, centerY);
    }

    @Override
    public void setWidth(final int width) {
        this.width = width;
    }

    @Override
    public void setHeight(final int height) {
        this.height = height;
    }

    @Override
    public boolean checkCollision(final Hitbox other) {
        //final double threshold = 10.0; // margine extra per rendere più morbido il contatto

        final double ax1 = this.getPosition().x() /*- threshold*/;
        final double ay1 = this.getPosition().y() /*- threshold*/;
        final double ax2 = ax1 + this.width + 2 /* * threshold*/;
        final double ay2 = ay1 + this.height + 2 /* * /*threshold*/;

        final double bx1 = other.getPosition().x();
        final double by1 = other.getPosition().y();
        final double bx2 = bx1 + other.getWidth();
        final double by2 = by1 + other.getHeight();

        return !(ax2 < bx1 || bx2 < ax1 || ay2 < by1 || by2 < ay1);
    }

}
