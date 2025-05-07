package it.unibo.oop.lastcrown.model.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Radius;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;

public class RadiusImpl implements Radius {
    private final Point2D center;
    private final double radius;

    public RadiusImpl(final Hitbox hitbox, final double radius){
        this.center= hitbox.getCenter();
        this.radius=radius;
    }

    //lista di nemici nel raggio
    @Override
    public List<Enemy> getEnemiesInRadius(final List<Enemy> enemies){
        List<Enemy> result = new ArrayList<>();
        for (Enemy e : enemies){
            if (e.getHitbox().getCenter().getDistance(center) <= radius){
                result.add(e);
            }
        }
        return result;
    }

    //trova il nemico più vicino rivedi--
    @Override
    public Optional<Enemy> getClosestEnemyInRadius(final List<Enemy> enemies){
        Enemy closest= null;
        double minDistance= 20; //valore arbitrario
        for (Enemy e : enemies){

            double distance= e.getHitbox().getCenter().getDistance(center);
            if (distance<=radius && distance< minDistance){
                minDistance=distance;
                closest=e;
            }
        }
        if (closest!= null){
            return Optional.of(closest);
        }
        return Optional.empty(); 
    }

    
    @Override
    public boolean hasEnemyInRadius(final List<Enemy> enemies) {
        for (Enemy e : enemies) {
            if (e.getHitbox().getCenter().getDistance(center) <= radius) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Point2D getCenter() {
        return this.center;
    }

    @Override
    public double getRadius() {
        return this.radius;
    }
}
