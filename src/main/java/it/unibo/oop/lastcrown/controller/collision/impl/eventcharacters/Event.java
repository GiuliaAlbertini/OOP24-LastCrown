package it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

/**
 * Represents a generic event in the character event system.
 *
 * Each event defines a specific behavior to execute for a character,
 * and may modify the character's CharacterState
 */
public interface Event {
    /**
     * Executes the event logic for the given character.
     *
     * @param character the character controller on which the event operates
     * @param queue the queue of upcoming events
     * @param deltaTime the time elapsed since the last update, in milliseconds
     * @return the resulting CharacterState after executing the event
     */
    CharacterState execute(GenericCharacterController character, EventQueue queue, int deltaTime);
}
