package it.unibo.oop.lastcrown.model.api;

/**
 * Interface for objects that want to observe and react to collision events.
 */
public interface CollisionObserver {

    /**
     * Called when a collision event occurs.
     *
     * @param event the collision event that occurred
     */
    void notify(CollisionEvent event);
}
