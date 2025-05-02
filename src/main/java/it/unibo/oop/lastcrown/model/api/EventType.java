package it.unibo.oop.lastcrown.model.api;

/**
 * Represents the different types of events that can occur in the game related to collisions or input.
 */
public enum EventType {

    /**
     * Event triggered when two entities collide.
     */
    ENTITY_COLLISION,

    /**
     * Event triggered when an hero collides with a shop.
     */
    SHOP_COLLISION,

    /**
     * Event triggered when an entity collides with a wall.
     */
    WALL_COLLISION,

    /**
     * Event triggered by a key press input.
     */
    KEY_PRESS;
}
