package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.EventType;

/**
 * Represents a collision between two Collidable objects,
 * associated with a specific EventType.
 */
public final class CollisionEventImpl implements CollisionEvent {
    private final Collidable collidable1;
    private final Collidable collidable2;
    private final EventType event;

    /**
     * @param event the type of collision event
     * @param collidable1 the first collidable involved
     * @param collidable2 the second collidable involved
     */
    public CollisionEventImpl(final EventType event, final Collidable collidable1, final Collidable collidable2) {
        this.collidable1 = collidable1;
        this.event = event;
        this.collidable2 = collidable2;
    }

    @Override
    public Collidable getCollidable1() {
        return collidable1;
    }

    @Override
    public Collidable getCollidable2() {
        return collidable2;
    }

    @Override
    public EventType getType() {
        return this.event;
    }
}

