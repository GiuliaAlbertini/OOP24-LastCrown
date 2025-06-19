package it.unibo.oop.lastcrown.controller.impl.eventcharacters;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

/**
 * Represents a handler responsible for managing the behavior of a character
 * during a specific state in the game (e.g., stopping, walking, attacking).
 * Each implementation of this interface defines the transition logic and
 * possible side effects associated with that state.
 */
public interface StateHandler {
    /**
     * @param character the character controller on which the state is applied
     * @param queue the event queue used to enqueue the next events
     * @param deltaTime the time elapsed since the last update (in milliseconds)
     * @return the next CharacterState the character should transition to
     */
    CharacterState handle(GenericCharacterController character, EventQueue queue, int deltaTime);
}
