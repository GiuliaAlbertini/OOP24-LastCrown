package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.impl.Pair;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Handler responsible for managing the IDLE state of characters.
 * In the IDLE state, the character performs a default movement (e.g., walking),
 * and transitions to FOLLOWING if an enemy is detected within range (for
 * players),
 * or to STOPPED if a collision is detected (for enemies).
 */
public final class IdleHandler implements StateHandler {
    private final MatchController matchController;
    private final EnemyRadiusScanner scanner;
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    /**
     * Constructs an IdleHandler with the required dependencies.
     *
     * @param matchController the controller for match-level operations such as
     *                        character updates
     * @param scanner         the utility to detect nearby enemies within radius
     * @param eventFactory    the factory used to generate character state
     *                        transition events
     * @param resolver        the collision resolver for managing combat
     *                        interactions
     */
    public IdleHandler(final MatchController matchController,
            final EnemyRadiusScanner scanner,
            final EventFactory eventFactory,
            final CollisionResolver resolver) {
        this.matchController = matchController;
        this.scanner = scanner;
        this.eventFactory = eventFactory;
        this.resolver = resolver;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        if (character != null) {
            final var player = character instanceof PlayableCharacterController;
            character.setNextAnimation(player ? Keyword.RUN_RIGHT : Keyword.RUN_LEFT);
            final Movement movementCharacter = new Movement(player ? 2 : -2, 0);
            character.showNextFrameAndMove(movementCharacter);

            matchController.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());
            // Logica per personaggi giocanti
            if (player) {
                final List<CollisionEvent> events = scanner.scanForFollowEvents();
                if (!events.isEmpty()) {
                    final CollisionEvent event = events.get(0);
                    matchController.notifyCollisionObservers(event);
                }
                if (matchController.isPlayerEngaged(character.getId().number())) {
                    queue.enqueue(eventFactory.createEvent(CharacterState.FOLLOWING));
                    return CharacterState.FOLLOWING;
                }
            }
            // Logica per nemici
            else {
                final boolean collision = resolver.wasEnemyCollided(character.getId().number());
                if (collision) {
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
            }
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }
}
