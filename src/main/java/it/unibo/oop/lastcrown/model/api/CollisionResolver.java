package it.unibo.oop.lastcrown.model.api;

import java.util.List;

public interface CollisionResolver extends CollisionObserver {
    Point2D getCharacterPosition(int characterId);
    public List<Collidable> updateAllMovements(long deltaMs); 
}