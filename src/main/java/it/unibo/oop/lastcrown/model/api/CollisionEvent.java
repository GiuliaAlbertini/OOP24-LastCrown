package it.unibo.oop.lastcrown.model.api;

/**
 * Represents a collision event between two collidable entities.
 */
public interface CollisionEvent {

    /**
     * @return the first collidable object involved in the collision
     */
    Collidable getCollidable1();

    /**
     * @return the second collidable object involved in the collision
     */
    Collidable getCollidable2();
    
    EventType getType();
}
