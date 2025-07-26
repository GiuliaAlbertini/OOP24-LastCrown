package it.unibo.oop.lastcrown.model.collision.api;

/**
 * Represents a collision event between two collidable entities.
 *
 * This interface provides access to the two objects involved in the collision
 * as well as the type of collision event.
 *
 */
public interface CollisionEvent {

    /**
     * Returns the first collidable object involved in this collision.
     *
     * @return the first Collidable entity involved
     */
    Collidable getCollidable1();

    /**
     * Returns the second collidable object involved in this collision.
     *
     * @return the second Collidable entity involved
     */
    Collidable getCollidable2();

    /**
     * Returns the type of this collision event.
     *
     * @return the EventType representing the nature of the collision
     */
    EventType getType();
}
