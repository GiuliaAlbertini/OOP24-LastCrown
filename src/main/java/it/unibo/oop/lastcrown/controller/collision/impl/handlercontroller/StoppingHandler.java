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
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * A StateHandler that handles the character's stopping state.
 * Sets the STOP animation frame and schedules a transition to the COMBAT state
 * or IDLE based on engagement and combat status.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            The stopping state handler keeps reference to the match controller and collision resolver
            to access live info about the characters' position and collisions.
            This mainly because the same data is not made accessible in the characters' model classes,
            requiring some more controller coupling.
            """
)
public final class StoppingHandler implements StateHandler {

    private static final int ENEMY_SPEED = 2;
    private static final int ROUNDING_AMOUNT = 5;

    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private final EntityTargetingSystem scanner;
    private final MatchController match;
    private boolean wait;
    private boolean retreat;

    /**
     * @param eventFactory    the factory used to create events for the character's
     *                        state transitions
     * @param matchController the match controller for game-wide interactions
     * @param resolver        the collision resolver for combat logic
     * @param scanner         the enemy radius scanner for detecting targets
     */
    public StoppingHandler(final EventFactory eventFactory, final MatchController matchController,
            final CollisionResolver resolver, final EntityTargetingSystem scanner) {
        this.eventFactory = eventFactory;
        this.match = matchController;
        this.resolver = resolver;
        this.scanner = scanner;
        this.wait = false;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {

        if (character == null) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        }

        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isBosshandle = resolver.hasOpponentBossPartner(character.getId().number());
        final int charId = character.getId().number();
        final CardType characterType = character.getId().type();

        match.matchResult();

        if (characterType == CardType.HERO && !match.hasEntityTypeInMap(CardType.BOSS) && match.isRoundSpawnComplete()
                && !match.hasEntityTypeInMap(CardType.MELEE) && !match.hasEntityTypeInMap(CardType.ENEMY)) {
            match.setRadiusPlayerInMap();
            match.spawnRandomBossFromFirstList();
        }

        if (characterType == CardType.HERO) {
            handleHeroCharacter(character, queue, charId);
            return CharacterState.STOPPED;
        }

        if (characterType == CardType.RANGED) {
            if (!retreat) {
                return handleRangedCharacter(character, queue, charId);
            } else {
                return handleRetreatRanged(character, queue, charId);
            }
        }

        if (!match.hasEntityTypeInMap(CardType.BOSS)) {
            if (match.getWall().getCurrentHealth() <= 0) {
                if (character.getId().type() == CardType.ENEMY) {
                    final Movement movementCharacter = new Movement(ENEMY_SPEED, 0);
                    character.setNextAnimation(Keyword.RETREAT);
                    character.showNextFrameAndMove(movementCharacter);
                    match.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());
                    retreat = true;
                    if (match.isEnemyBeyondFrame(character.getId().number())) {
                        queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
                        return CharacterState.DEAD;
                    }
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                } else {
                    character.setNextAnimation(Keyword.STOP);
                    character.showNextFrame();
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
            } else if (match.isRoundSpawnComplete() && !match.hasEntityTypeInMap(CardType.ENEMY)) {
                match.spawnRandomBossFromFirstList();
                match.setRadiusPlayerInMap();
                character.setNextAnimation(Keyword.STOP);
                character.showNextFrame();
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            }
        } else if (!isBosshandle) {
            retreat = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        }

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        if (isPlayer && isAtTroopZoneLimit(character) && !match.hasEntityTypeInMap(CardType.BOSS)) {
            handleCharacterTrupzone((PlayableCharacterController) character, queue, charId);
            return CharacterState.STOPPED;
        }

        final boolean isEngaged = match.isEntityEngaged(charId);
        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId);
        final boolean isWallFight = resolver.hasOpponentWallPartner(charId);

        if (!isEngaged && wait && !isBossFight && !isWallFight) {
            this.wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        } else if (isEngagedWithDead) {
            this.wait = true;
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        } else {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }

    }

    private CharacterState handleRangedCharacter(final GenericCharacterController character, final EventQueue queue,
            final int charId) {

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        match.getCharacterControllerById(charId)
                .filter(PlayableCharacterController.class::isInstance)
                .map(PlayableCharacterController.class::cast)
                .flatMap(scanner::scanForTarget)
                .ifPresent(event -> {
                    match.notifyCollisionObservers(event);
                });

        if (match.isRangedFightPartnerDead(charId)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        } else if (resolver.hasOpponentRangedPartner(charId) || isBossFight) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }

    private CharacterState handleRetreatRanged(final GenericCharacterController character, final EventQueue queue,
            final int charId) {

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        match.getCharacterControllerById(charId)
                .filter(PlayableCharacterController.class::isInstance)
                .map(PlayableCharacterController.class::cast)
                .flatMap(scanner::scanForTarget)
                .ifPresent(event -> {
                    match.notifyCollisionObservers(event);
                });

        if (isBossFight) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }

    private CharacterState handleHeroCharacter(final GenericCharacterController character, final EventQueue queue,
            final int charId) {
        final boolean isEngagedWithDead = match.isBossFightPartnerDead(charId);

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        if (match.hasEntityTypeInMap(CardType.BOSS)) {
            scanner.scanForTarget(character)
                    .ifPresent(match::notifyCollisionObservers);

            if (isEngagedWithDead) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            } else if (resolver.hasOpponentBossPartner(character.getId().number())) {
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;

    }

    private CharacterState handleCharacterTrupzone(final PlayableCharacterController player, final EventQueue queue,
            final int charId) {

        scanner.scanForTarget(player)
                .ifPresent(match::notifyCollisionObservers);
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId);

        if (isEngagedWithDead) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        } else if (match.isEntityEngaged(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        } else if (resolver.hasOpponentBossPartner(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }

    private boolean isAtTroopZoneLimit(final GenericCharacterController player) {
        final Optional<HitboxController> hitboxController = match.getCharacterHitboxById(player.getId().number());
        if (hitboxController.isPresent()) {
            final int limit = match.getMatchView().getTrupsZoneLimit() - hitboxController.get().getHitbox().getWidth();
            final int roundedLimit = limit + (ROUNDING_AMOUNT - (limit % ROUNDING_AMOUNT)) % ROUNDING_AMOUNT;
            return hitboxController.get().getHitbox().getPosition().x() >= roundedLimit;
        }
        return false;
    }
}
