package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.Optional;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.EntityTargetingSystem;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.utility.Constant;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Handler responsible for managing the IDLE state of characters.
 * In the IDLE state, the character performs a default movement (e.g., walking),
 * and transitions to FOLLOWING if an enemy is detected within range (for
 * players),
 * or to STOPPED if a collision is detected (for enemies).
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = """
        The idle state handler keeps reference to the match controller and collision resolver to access
        all live info about a character's position and collisions.
        This is mainly because the same info wouldn't be otherwise accessible from the character's class.
        """)
public final class IdleHandler implements StateHandler {

    private final MatchController matchController;
    private final EntityTargetingSystem scanner;
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
            final EntityTargetingSystem scanner,
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

        if (characterType == CardType.HERO) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        final boolean isPlayer = character instanceof PlayableCharacterController;
        final Keyword animationKeyword = isPlayer ? Keyword.RUN_RIGHT : Keyword.RUN_LEFT;
        final Movement movementCharacter = new Movement(
                isPlayer ? Constant.PLAYER_SPEED : (Constant.ONENEG * Constant.ENEMY_SPEED), 0);
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

        if (isAtTroopZoneLimit(player) && !matchController.hasEntityTypeInMap(CardType.BOSS)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        scanner.scanForTarget(player)
                .ifPresent(matchController::notifyCollisionObservers);
        if (matchController.isEntityEngaged(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.FOLLOWING));
            return CharacterState.FOLLOWING;
        } else if (resolver.hasOpponentPartner(player.getId().number(), EventType.BOSS)) {
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
        scanner.scanForWallCollision(enemy).ifPresent(matchController::notifyCollisionObservers);

        if (matchController.isEnemyDead(enemy.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        } else {
            final boolean collision = resolver.wasEnemyCollided(enemy.getId().number());
            if (collision || resolver.hasOpponentPartner(enemy.getId().number(), EventType.BOSS)
                    || resolver.hasOpponentPartner(enemy.getId().number(), EventType.WALL)) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            } else if (matchController.isEntityEngaged(enemy.getId().number())) {
                final int idPlayer = matchController.getEngagedCounterpart(enemy.getId().number());
                final GenericCharacterController player = matchController
                        .getCharacterControllerById(idPlayer)
                        .get();
                if (isAtTroopZoneLimit(player)) {
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
            }
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }

    private boolean isAtTroopZoneLimit(final GenericCharacterController player) {
        final Optional<HitboxController> hitboxController = matchController.getCharacterHitboxById(
                player.getId().number());
        if (hitboxController.isPresent()) {
            final int limit = matchController.getMatchView().getTrupsZoneLimit()
                    - hitboxController.get().getHitbox().getWidth();
            final int roundedLimit = limit
                    + (Constant.ROUNDING_AMOUNT - (limit % Constant.ROUNDING_AMOUNT))
                            % (Constant.ONENEG * Constant.ROUNDING_AMOUNT);
            return hitboxController.get().getHitbox().getPosition().x() >= roundedLimit;
        }
        return false;
    }
}
