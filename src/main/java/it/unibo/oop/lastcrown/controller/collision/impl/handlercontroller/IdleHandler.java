package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;


import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
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

    private static final int PLAYER_SPEED = 2;
    private static final int ENEMY_SPEED = -2;

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
        if (character == null) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        }

        final CardType characterType = character.getId().type();

        if (characterType == CardType.RANGED) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        final boolean isPlayer = character instanceof PlayableCharacterController;
        final Keyword animationKeyword = isPlayer ? Keyword.RUN_RIGHT : Keyword.RUN_LEFT;
        final Movement movementCharacter = new Movement(isPlayer ? PLAYER_SPEED : ENEMY_SPEED, 0);

        character.setNextAnimation(animationKeyword);
        character.showNextFrameAndMove(movementCharacter);
        matchController.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());

        if (isPlayer) {
            return handlePlayerIdleLogic((PlayableCharacterController) character, queue);
        } else {
            return handleEnemyIdleLogic(character, queue);
        }
    }

    /**
     * Handles the specific idle logic for playable characters.
     *
     * @param player The playable character controller.
     * @param queue  The event queue.
     * @return The next character state.
     */
    private CharacterState handlePlayerIdleLogic(final PlayableCharacterController player, final EventQueue queue) {
        scanner.scanForFollowEventForPlayer(player)
                .ifPresent(matchController::notifyCollisionObservers);

        if (matchController.isPlayerEngaged(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.FOLLOWING));
            return CharacterState.FOLLOWING;
        } else if (resolver.hasOpponentBossPartner(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }

    /**
     * Handles the specific idle logic for enemy characters.
     *
     * @param enemy The generic character controller (expected to be an enemy).
     * @param queue The event queue.
     * @return The next character state.
     */
    private CharacterState handleEnemyIdleLogic(final GenericCharacterController enemy, final EventQueue queue) {
        if (matchController.isEnemyDead(enemy.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        } else {
            final boolean collision = resolver.wasEnemyCollided(enemy.getId().number());
            if (collision || resolver.hasOpponentBossPartner(enemy.getId().number())) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            }
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }
}