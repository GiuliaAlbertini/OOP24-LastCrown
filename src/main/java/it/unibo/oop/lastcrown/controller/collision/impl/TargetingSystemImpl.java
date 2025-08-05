package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.api.TargetingSystem;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.*;
import it.unibo.oop.lastcrown.model.collision.impl.CollidableImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionEventImpl;

public final class TargetingSystemImpl implements TargetingSystem {
    private final Map<GenericCharacterController, HitboxController> entityStateManager;
    private final MatchController matchController;
    private final CollisionResolver resolver;

    public TargetingSystemImpl(final Map<GenericCharacterController, HitboxController> entityStateManager,
            final MatchController matchController,
            final CollisionResolver resolver) {
        this.entityStateManager = entityStateManager;
        this.matchController = matchController;
        this.resolver = resolver;
    }

    @Override
    public Optional<CollisionEvent> scanForTarget(final GenericCharacterController scanner) {
        final Map<GenericCharacterController, HitboxController> allEntities = entityStateManager;

        return getCharacterRadius(scanner, allEntities)
                .flatMap(radius -> findClosestEnemy(radius, allEntities))
                .flatMap(enemy -> createInteractionEvent(scanner, enemy, allEntities));
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