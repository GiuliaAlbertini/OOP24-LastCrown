package it.unibo.oop.lastcrown.controller.impl.eventcharacters;

import java.util.LinkedList;
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
     * @param event the event to enqueue, can be null (ignored)
     */
    public void enqueue(final Event event) {
        synchronized (events) { 
            if (event != null) {
                events.add(event);
            }
        }
    }

    /**
     * Processes the next event in the queue by executing it.
     * If the queue is empty, returns CharacterState.IDLE
     * 
     * @param character the character to execute the event on
     * @param deltaTime the time elapsed since the last update (in milliseconds)
     * @return the resulting CharacterState after event execution
     */
    public CharacterState processNext(final GenericCharacterController character, final int deltaTime) {
        synchronized (events) { 
            if (!events.isEmpty()) {
                return events.poll().execute(character, this, deltaTime);
            } else {
                return CharacterState.IDLE;
            }
        }
    }

    /**
     * Checks if the event queue is empty. 
     * @return true if the queue contains no events, false otherwise
     */
    public boolean isEmpty() {
        synchronized (events) {
            return events.isEmpty();
        }
    }

    /**
     * Clears all events from the queue.
     */
    public void clear() {
        events.clear();
    }
}
