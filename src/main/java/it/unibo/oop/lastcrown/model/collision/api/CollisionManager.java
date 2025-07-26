package it.unibo.oop.lastcrown.model.collision.api;

/**
 * Interface for managing collision observers and notifying them
 * when a collision event occurs.
 */
public interface CollisionManager {

    /**
     * Registers a new observer to be notified of collision events.
     *
     * @param observer the observer to add
     */
    void addObserver(CollisionObserver observer);

    /**
     * Unregisters an observer so it no longer receives collision notifications.
     *
     * @param observer the observer to remove
     */
    void removeObserver(CollisionObserver observer);

    /**
     * Retrieves the current state (e.g., active/inactive) of the given observer.
     *
     * @param observer the observer whose state is queried
     * @return true if the observer is active, false otherwise
     */
    boolean getState(CollisionObserver observer);

    /**
     * Sets the state of the given observer to active.
     *
     * @param observer the observer whose state is to be set
     */
    void setState(CollisionObserver observer);

    /**
     * Notifies all active observers of a new collision event.
     *
     * @param event the collision event to broadcast
     */
    void notify(CollisionEvent event);
}
