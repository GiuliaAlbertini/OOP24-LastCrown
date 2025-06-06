package it.unibo.oop.lastcrown.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Radius;

public class RadiusImpl implements Radius {
    private final Hitbox origin;
    private final double radius;

    public RadiusImpl(final Hitbox origin, final double radius) {
        this.origin = origin;
        this.radius = radius;
    }

    // lista di nemici nel raggio
    @Override
    public List<Hitbox> getEnemiesInRadius(final List<Hitbox> enemies) {
        List<Hitbox> result = new ArrayList<>();
        for (Hitbox h : enemies) {
            if (h.getCenter().getDistance(origin.getCenter()) <= radius) {
                result.add(h);
            }
        }
        return result;
    }

    // trova il nemico più vicino rivedi--
    @Override
    public Optional<Hitbox> getClosestEnemyInRadius(final List<Hitbox> enemies) {
        Hitbox closest = null;
        double minDistance = Double.MAX_VALUE; // valore arbitrario
        for (Hitbox h : enemies) {
            double distance = h.getCenter().getDistance(origin.getCenter());
            if (distance <= radius && distance < minDistance) {
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
        for (Hitbox h : enemies) {
            if (h.getCenter().getDistance(origin.getCenter()) <= radius) {
                return true;
            }
            double dist = h.getCenter().getDistance(origin.getCenter());
            if (dist <= radius) {
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
}
