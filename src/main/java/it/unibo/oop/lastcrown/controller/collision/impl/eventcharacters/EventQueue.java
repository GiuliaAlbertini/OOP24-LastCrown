package it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

/**
 * Manages a thread-safe queue of events for character state changes.
 * Events are processed in FIFO order.
 */
public final class EventQueue {
    private final Queue<Event> events = new LinkedList<>();

    /**
     * Adds a new event to the queue.
     * Events are ignored if null.
     *
     * @param event The event to enqueue.
     */
    public void enqueue(final Event event) {

        Objects.requireNonNull(event, "Event cannot be null.");
        synchronized (events) {
            events.add(event);
        }
    }

    /**
     * Processes the next event in the queue by executing it.
     * If the queue is empty, it returns CharacterState.IDLE, indicating
     * no immediate state change is requested by an event.
     *
     * @param character The character to execute the event on.
     * @param deltaTime The time elapsed since the last update (in milliseconds).
     * @return The resulting CharacterState after event execution, or IDLE if no
     *         event was processed.
     */
    public CharacterState processNext(final GenericCharacterController character, final int deltaTime) {
        synchronized (events) {
            if (!events.isEmpty()) {
                // Poll the event and execute it, then return the resulting state.
                return events.poll().execute(character, this, deltaTime);
            } else {
                // If no events are in the queue, the character remains in a default idle state.
                return CharacterState.IDLE;
            }
        }
    }

    /**
     * Checks if the event queue is empty.
     *
     * @return true if the queue contains no events, false otherwise.
     */
    public boolean isEmpty() {
        synchronized (events) {
            return events.isEmpty();
        }
    }

    /**
     * Clears all events from the queue.
     * This operation is synchronized to ensure thread safety.
     */
    public void clear() {
        synchronized (events) { // Added synchronization for clear()
            events.clear();
        }
    }

    /**
     * Checks if there are any events currently waiting in the queue.
     *
     * @return true if the queue has one or more events, false otherwise.
     */
    public boolean hasPendingEvents() {
        synchronized (events) { // Ensure this method is also thread-safe
            return !events.isEmpty();
        }
    }
}
