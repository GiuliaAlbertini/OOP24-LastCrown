package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.EntityStateManager;
import it.unibo.oop.lastcrown.controller.collision.api.EntityTargetingSystem;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.api.Radius;
import it.unibo.oop.lastcrown.model.collision.impl.CollidableImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionEventImpl;

/**
 * Implementation of TargetingSystem responsible for scanning characters'
 * surrounding area for targets, handling wall collisions, and creating
 * corresponding
 * CollisionEvent instances.
 *
 * It supports both player-controlled and AI characters, including logic for
 * engaging
 * enemies, bosses, and interacting with the environment.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
            Instances of the entity targeting system, match controller and collision resolver
            must be kept in order to access live info about the characters' state and position.
            This is partly because of the fact that characters' info is not completely accessible from the
            character class itself, requiring some deeper coupling in the controller layer.
            """
)
public final class EntityTargetingSystemImpl implements EntityTargetingSystem {
    private final EntityStateManager entityStateManager;
    private final MatchController matchController;
    private final CollisionResolver resolver;

    /**
     * Constructs a TargetingSystemImpl with access to current entities,
     * match state, and collision resolver.
     *
     * @param entityStateManager the game entities' state manager.
     * @param matchController    the match controller to retrieve game view and wall
     *                           state
     * @param resolver           the collision resolver for checking character
     *                           interactions
     */
    public EntityTargetingSystemImpl(final EntityStateManager entityStateManager,
            final MatchController matchController,
            final CollisionResolver resolver) {
        this.entityStateManager = entityStateManager;
        this.matchController = matchController;
        this.resolver = resolver;
    }

    @Override
    public Optional<CollisionEvent> scanForTarget(final GenericCharacterController scanner) {
        final var entities = this.entityStateManager.getHitboxControllersMap();
        return getCharacterRadius(scanner, entities)
                .flatMap(radius -> findClosestEnemy(radius, entities))
                .flatMap(enemy -> createInteractionEvent(scanner, enemy, entities));
    }

    @Override
    public Optional<CollisionEvent> scanForWallCollision(final GenericCharacterController enemy) {
        return matchController.getCharacterHitboxById(enemy.getId().number())
                .filter(this::isEnemyAtWallBoundary)
                .map(hitboxController -> createWallCollisionEvent(enemy, hitboxController.getHitbox()));
    }

    private Optional<Radius> getCharacterRadius(final GenericCharacterController character,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        return Optional.ofNullable(allEntities.get(character)).flatMap(HitboxController::getRadius);
    }

    private Optional<GenericCharacterController> findClosestEnemy(final Radius radius,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        final List<Hitbox> enemyHitboxes = getEnemyHitboxes(allEntities);
        final Map<Hitbox, GenericCharacterController> hitboxToEnemyMap = createHitboxToEnemyMap(allEntities);
        return radius.getClosestEnemyInRadius(enemyHitboxes)
                .map(hitboxToEnemyMap::get);
    }

    private Optional<CollisionEvent> createInteractionEvent(final GenericCharacterController scanner,
            final GenericCharacterController target,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        if (scanner instanceof PlayableCharacterController p) {
            return createPlayerInteractionEvent(p, target, allEntities);
        } else if (scanner instanceof HeroController) {
            return createHeroInteractionEvent(scanner, target, allEntities);
        }
        return Optional.empty();
    }

    private Optional<CollisionEvent> createPlayerInteractionEvent(final PlayableCharacterController player,
            final GenericCharacterController enemy,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        if (enemy instanceof BossController) {
            return createEventIfAllowed(!resolver.hasOpponentBossPartner(player.getId().number()),
                    player, enemy, EventType.BOSS, allEntities);
        } else if (enemy instanceof EnemyController) {
            if (player.getId().type() == CardType.RANGED) {
                return createEventIfAllowed(!resolver.hasOpponentRangedPartner(player.getId().number()),
                        player, enemy, EventType.RANGED, allEntities);
            } else { // MELEE
                if (canMeleePlayerEngage(player, (EnemyController) enemy)) {
                    synchronized (enemy) {
                        return createEventIfAllowed(
                                matchController.engageEnemy(enemy.getId().number(), player.getId().number()),
                                player, enemy, EventType.ENEMY, allEntities);
                    }
                }
            }
        }
        return Optional.empty();
    }

    private Optional<CollisionEvent> createHeroInteractionEvent(final GenericCharacterController hero,
            final GenericCharacterController enemy,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        if (enemy instanceof BossController) {
            return createEventIfAllowed(!resolver.hasOpponentBossPartner(hero.getId().number()),
                    hero, enemy, EventType.BOSS, allEntities);
        }
        return Optional.empty();
    }

    private Optional<CollisionEvent> createEventIfAllowed(final boolean condition,
            final GenericCharacterController scanner,
            final GenericCharacterController target, final EventType eventType,
            final Map<GenericCharacterController, HitboxController> allEntities) {
        if (condition) {
            final HitboxController scannerHitbox = allEntities.get(scanner);
            final HitboxController targetHitbox = allEntities.get(target);
            if (scannerHitbox != null && targetHitbox != null) {
                final Collidable scannerCol = new CollidableImpl(scannerHitbox.getHitbox(), scanner.getId());
                final Collidable targetCol = new CollidableImpl(targetHitbox.getHitbox(), target.getId());
                return Optional.of(new CollisionEventImpl(eventType, scannerCol, targetCol));
            }
        }
        return Optional.empty();
    }

    private boolean canMeleePlayerEngage(final PlayableCharacterController player, final EnemyController enemy) {
        if (enemy.isInCombat() || matchController.isEnemyDead(enemy.getId().number())) {
            return false;
        }
        return matchController.isPlayerInState(player, CharacterState.IDLE)
                || (matchController.isPlayerInState(player, CharacterState.STOPPED) && isAtTroopZoneLimit(player));
    }

    private List<Hitbox> getEnemyHitboxes(final Map<GenericCharacterController, HitboxController> allEntities) {
        return allEntities.entrySet().stream()
                .filter(entry -> isEnemyOrBoss(entry.getKey()))
                .map(entry -> entry.getValue().getHitbox())
                .collect(Collectors.toList());
    }

    private Map<Hitbox, GenericCharacterController> createHitboxToEnemyMap(
            final Map<GenericCharacterController, HitboxController> allEntities) {
        return allEntities.entrySet().stream()
                .filter(entry -> isEnemyOrBoss(entry.getKey()))
                .collect(Collectors.toMap(entry -> entry.getValue().getHitbox(), Map.Entry::getKey));
    }

    private boolean isEnemyOrBoss(final GenericCharacterController character) {
        return character.getId().type() == CardType.ENEMY || character.getId().type() == CardType.BOSS;
    }

    private boolean isEnemyAtWallBoundary(final HitboxController hitboxController) {
        final int enemyX = (int) hitboxController.getHitbox().getPosition().x();
        final int wallBoundary = (int) (matchController.getMatchView().getWallCoordinates().getX()
                + matchController.getWall().getHitbox().get().getWidth());
        return enemyX <= wallBoundary;
    }

    private CollisionEvent createWallCollisionEvent(final GenericCharacterController enemy, final Hitbox enemyHitbox) {
        final Collidable wall = new CollidableImpl(matchController.getWall().getHitbox().get(),
                matchController.getWall().getCardIdentifier());
        final Collidable enemyCol = new CollidableImpl(enemyHitbox, enemy.getId());
        return new CollisionEventImpl(EventType.WALL, wall, enemyCol);
    }

    private boolean isAtTroopZoneLimit(final GenericCharacterController player) {
        return matchController.getCharacterHitboxById(player.getId().number())
                .map(hitbox -> {
                    final int limit = matchController.getMatchView().getTrupsZoneLimit()
                            - hitbox.getHitbox().getWidth();
                    return hitbox.getHitbox().getPosition().x() >= limit;
                })
                .orElse(false);
    }
}
