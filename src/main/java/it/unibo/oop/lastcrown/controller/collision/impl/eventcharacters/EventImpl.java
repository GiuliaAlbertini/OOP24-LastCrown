package it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

/**
 * Implementation of the Event interface.
 * Represents a character-related event that triggers a specific behavior
 * based on its associated state and handler.
 */
public final class EventImpl implements Event {
    private final StateHandler handler;

    /**
     * @param handler the logic that defines how the event should be handled
     */
    public EventImpl(final StateHandler handler) {
        this.handler = handler;
    }

    @Override
    public CharacterState execute(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        return handler.handle(character, queue, deltaTime);
    }
}
