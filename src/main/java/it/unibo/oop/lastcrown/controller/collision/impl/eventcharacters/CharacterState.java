package it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters;

/**
 * Represents the different possible states of a character within the game.
 * Each state corresponds to a specific behavior or situation the character may be in.
 */
public enum CharacterState {
    /**
     * The character is idle and not performing any active behavior.
     */
    IDLE,

    /**
     * The character is following a target, typically an enemy or objective.
     */
    FOLLOWING,

    /**
     * The character is engaged in combat.
     */
    COMBAT,

    /**
     * The character has stopped and is not moving or acting.
     */
    STOPPED,

    /**
     * The character is dead and no longer active.
     */
    DEAD
}
