package it.unibo.oop.lastcrown.model.collision.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.api.Radius;

/**
 * Implementation of the Radius interface.
 * Represents a detection radius around a hitbox, used to find nearby enemies
 * and perform spatial calculations relative to a semicircle in front of the origin.
 */
public final class RadiusImpl implements Radius {
    private final Hitbox origin;
    private double radius;

    /**
     * Constructs a new RadiusImpl with a specified hitbox as origin
     * and a radius value.
     *
     * @param origin the hitbox which acts as the center of the radius
     * @param radius the size of the radius
     */
    public RadiusImpl(final Hitbox origin, final double radius) {
        this.origin = origin;
        this.radius = radius;
    }

    // lista di nemici nel raggio
    @Override
    public List<Hitbox> getEnemiesInRadius(final List<Hitbox> enemies) {
        final List<Hitbox> result = new ArrayList<>();
        for (final Hitbox h : enemies) {
            if (h.getCenter().getDistance(origin.getCenter()) <= radius && isInRightSemicircle(h.getCenter())) {
                result.add(h);
            }
        }
        return result;
    }


    @Override
    public Optional<Hitbox> getClosestEnemyInRadius(final List<Hitbox> enemies) {
        Hitbox closest = null;
        double minDistance = Double.MAX_VALUE;
        for (final Hitbox h : enemies) {
            final double distance = h.getCenter().getDistance(origin.getCenter());
            if (distance <= radius && distance < minDistance && isInRightSemicircle(h.getCenter())) {
                minDistance = distance;
                closest = h;
            }
        }
        if (closest != null) {
            return Optional.of(closest);
        }
        return Optional.empty();
    }

    @Override
    public boolean hasEnemyInRadius(final List<Hitbox> enemies) {
        for (final Hitbox h : enemies) {
            final double distance = h.getCenter().getDistance(origin.getCenter());
            if (distance <= radius && isInRightSemicircle(h.getCenter())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Point2D getCenter() {
        return this.origin.getCenter();
    }

    @Override
    public double getRadius() {
        return this.radius;
    }

    private boolean isInRightSemicircle(final Point2D target) {
        final Point2D originCenter = origin.getCenter();
        return target.x() >= originCenter.x();
    }

    /**
     * Sets the radius value used by this object.
     *
     * @param radius the new radius to assign
     */
    public void setRadius(final double radius) {
        this.radius = radius;
    }
}
