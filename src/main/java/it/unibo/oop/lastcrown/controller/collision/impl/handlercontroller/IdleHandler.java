package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.impl.Pair;
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
                //System.out.println("sono il personaggio in idle" + character.getId().type());
        if (character != null) {
            // se sei ranged allora vai in stop
            if (character.getId().type() == CardType.RANGED) {
                System.out.println("sto guardando da idle");
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            } else {
                final var player = character instanceof PlayableCharacterController;
                character.setNextAnimation(player ? Keyword.RUN_RIGHT : Keyword.RUN_LEFT);
                final Movement movementCharacter = new Movement(player ? 2 : -2, 0);
                character.showNextFrameAndMove(movementCharacter);

                matchController.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());
                // Logica per personaggi giocanti
                if (player) {

                    Optional<GenericCharacterController> controllerOpt = matchController
                            .getCharacterControllerById(character.getId().number());
                    if (controllerOpt.isPresent()
                            && controllerOpt.get() instanceof PlayableCharacterController playerCol) {
                        Optional<CollisionEvent> eventOpt = scanner.scanForFollowEventForPlayer(playerCol);
                        eventOpt.ifPresent(event -> {
                            matchController.notifyCollisionObservers(event);
                        });
                    }

                    if (matchController.isPlayerEngaged(character.getId().number())) {
                        queue.enqueue(eventFactory.createEvent(CharacterState.FOLLOWING));
                        return CharacterState.FOLLOWING;

                    } else if (resolver.hasOpponentBossPartner(character.getId().number())) {
                        System.out.println("ssto guardando da idle");
                        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                        return CharacterState.STOPPED;
                    }
                }
                // Logica per nemici
                else {
                    if (matchController.isEnemyDead(character.getId().number())) {
                        // resolver.clearAllOpponentRangedPairs();
                        queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
                        return CharacterState.DEAD;
                    } else {

                        final boolean collision = resolver.wasEnemyCollided(character.getId().number());
                        if (collision) {
                            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                            return CharacterState.STOPPED;
                        } else if (resolver.hasOpponentBossPartner(character.getId().number())) {
                            // System.out.println("nemico in idle che sta per entrare nello stopped");
                            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                            return CharacterState.STOPPED;
                        }
                    }
                }
            }

        }
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }
}
