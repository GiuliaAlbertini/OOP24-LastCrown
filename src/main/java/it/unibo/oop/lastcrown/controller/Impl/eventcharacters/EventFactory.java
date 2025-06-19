package it.unibo.oop.lastcrown.controller.impl.eventcharacters;

import java.util.EnumMap;
import java.util.Map;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.impl.handlercontroller.CombatHandler;
import it.unibo.oop.lastcrown.controller.impl.handlercontroller.DeadHandler;
import it.unibo.oop.lastcrown.controller.impl.handlercontroller.FollowingHandler;
import it.unibo.oop.lastcrown.controller.impl.handlercontroller.IdleHandler;
import it.unibo.oop.lastcrown.controller.impl.handlercontroller.StoppingHandler;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;

/**
 * Creates Event objects for given CharacterStates.
 * Manages handlers for each state and returns corresponding events
 */
public final class EventFactory {
    private final Map<CharacterState, StateHandler> eventHandlers = new EnumMap<>(CharacterState.class);

    /**
     * Constructs an EventFactory and registers the default handlers
     * for character states.
     * @param matchController the match controller managing game logic
     * @param scanner the scanner detecting nearby enemies
     * @param resolver the collision resolver for interactions
     */

    public EventFactory(final MatchController matchController,
                        final EnemyRadiusScanner scanner,
                        final CollisionResolver resolver) {
        registerDefaultHandlers(matchController, scanner, resolver);
    }

    /**
     * Constructs an EventFactory initializing the default state handlers.
     * @param matchController the match controller
     * @param scanner the enemy radius scanner
     * @param resolver the collision resolver
     */
    private void registerDefaultHandlers(final MatchController matchController,
                                         final EnemyRadiusScanner scanner,
                                         final CollisionResolver resolver) {
        registerHandler(CharacterState.IDLE, new IdleHandler(matchController, scanner, this, resolver));
        registerHandler(CharacterState.FOLLOWING, new FollowingHandler(matchController, resolver, this));
        registerHandler(CharacterState.STOPPED, new StoppingHandler(this));
        registerHandler(CharacterState.COMBAT, new CombatHandler(this, resolver, matchController));
        registerHandler(CharacterState.DEAD, new DeadHandler());
    }

    /**
     * Associates a specific state with its corresponding handler.
     * @param state the character state
     * @param handler the handler that manages behavior for that state
     */
    private void registerHandler(final CharacterState state, final StateHandler handler) {
        eventHandlers.put(state, handler);
    }

    /**
     * Creates an Event for a given character state.
     * @param state the state for which to create the event
     * @return the corresponding Event, or null if no handler is registered
     */
    public Event createEvent(final CharacterState state) {
        final StateHandler handler = eventHandlers.get(state);
        return handler != null ? new EventImpl(state, handler) : null;
    }
}
