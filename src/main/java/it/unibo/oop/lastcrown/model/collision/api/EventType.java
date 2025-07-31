package it.unibo.oop.lastcrown.model.collision.api;

/**
 * Represents the different types of events that can occur in the game
 * related to collisions or user input.
 */
public enum EventType {

    /**
     * Event triggered when the player starts following or engaging an enemy.
     */
    ENEMY,

    /**
     * Event triggered when the player collides with an enemy.
     */
    BOSS,
    /**
     * Event triggered when the player enters combat mode.
     */
    RANGED,

    /**
     * Event triggered when any entity collides with a wall.
     */
    WALL,
}
