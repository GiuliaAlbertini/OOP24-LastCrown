package it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

/**
 * Implementation of the Event interface.
 * Represents a character-related event that triggers a specific behavior
 * based on its associated state and handler.
 */
public final class EventImpl implements Event {
    private final CharacterState state;
    private final StateHandler handler;

    /**
     * @param state   the character state associated with this event
     * @param handler the logic that defines how the event should be handled
     */
    public EventImpl(final CharacterState state, final StateHandler handler) {
        this.state = state;
        this.handler = handler;
    }

    @Override
    public CharacterState execute(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        System.out.println("Processing state: " + state);
        return handler.handle(character, queue, deltaTime);
    }
}
