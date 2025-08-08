package it.unibo.oop.lastcrown.controller.collision.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.EntityTargetingSystem;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;

/**
 * CharacterFSM (Finite State Machine) manages the behavior of a character
 * by processing queued events and transitioning between character states.
 */
@SuppressFBWarnings(
        value = "EI2",
        justification = """
            This class is a Finite State Machine (FSM) that must control the original 'live' character controller.
            A defensive copy would be a different entity, making it impossible for the FSM to perform its function.
            This tight coupling is an intentional and fundamental part of the FSM design pattern.
            """
)
public final class CharacterFSM {
    private final GenericCharacterController character;
    private final EventQueue eventQueue;
    private final EventFactory eventFactory;
    private CharacterState currentState;

    /**
     * Constructs a CharacterFSM with its initial state and dependencies.
     *
     * @param character the controller for the character managed by this FSM
     * @param matchController the match controller used to create the EventFactory
     * @param scanner the enemy radius scanner used by handlers
     * @param resolver the collision resolver for character movement and interaction
     */
    public CharacterFSM(final GenericCharacterController character, final MatchController matchController,
            final EntityTargetingSystem scanner, final CollisionResolver resolver) {
        this.character = character;
        this.currentState = CharacterState.IDLE;
        this.eventQueue = new EventQueue();
        this.eventFactory = new EventFactory(matchController, scanner, resolver); // passaggio controller alla factory
        this.eventQueue.enqueue(this.eventFactory.createEvent(this.currentState));
    }

    /**
     * Updates the FSM logic by processing the next event from the queue
     * and transitioning to a new state if applicable.
     *
     * @param deltaTime time elapsed since last update, used for timing calculations
     */
    public void update(final int deltaTime) {
        // Processa il prossimo evento della coda, se non vuota
        this.currentState = eventQueue.processNext(character, deltaTime);
    }

    /**
     * Returns the current state of the character.
     *
     * @return the current CharacterState
     */
    public CharacterState getCurrentState() {
        return this.currentState;
    }

    /**
     * Setter for the character's state.
     * @param newState the new state to be assigned to the character.
     */
    public void setState(final CharacterState newState) {
        this.currentState = newState;
        this.eventQueue.clear();
        this.eventQueue.enqueue(this.eventFactory.createEvent(this.currentState));
    }
}
