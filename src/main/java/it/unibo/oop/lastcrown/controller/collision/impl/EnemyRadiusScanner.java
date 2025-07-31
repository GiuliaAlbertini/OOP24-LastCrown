package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.model.collision.impl.CollidableImpl;
import it.unibo.oop.lastcrown.model.collision.impl.CollisionEventImpl;

/**
 * Scans the radius of playable characters to detect nearby enemy characters.
 * If an enemy is detected within a player's radius, a FOLLOW_ENEMY collision
 * event is generated (or BOSS/RANGED specific events).
 */
public final class EnemyRadiusScanner {
    private final Map<GenericCharacterController, HitboxController> hitboxControllers;
    private final MatchController matchController;
    private final CollisionResolver resolver;


    public EnemyRadiusScanner(final Map<GenericCharacterController, HitboxController> hitboxControllers,
            final MatchController matchController, final CollisionResolver resolver) {
        this.hitboxControllers = hitboxControllers;
        this.matchController = matchController;
        this.resolver = resolver;
    }

    /**
     * Scans for a follow event for a specific playable character.
     * This method is designed to find the closest valid enemy target.
     *
     * @param player The playable character controller to scan for.
     * @return An Optional containing a CollisionEvent if a target is found,
     *         otherwise empty.
     */
    public Optional<CollisionEvent> scanForFollowEventForPlayer(final PlayableCharacterController player) {
        final HitboxController playerHitboxController = hitboxControllers.get(player); // hitbox player
        if (playerHitboxController == null || playerHitboxController.getRadius() == null) {
            return Optional.empty();
        }

        final Map<GenericCharacterController, HitboxController> currentHitboxStates = new HashMap<>(hitboxControllers);
        final List<Hitbox> enemyHitboxes = getAllEnemyHitboxes(currentHitboxStates);
        final Map<Hitbox, GenericCharacterController> hitboxToEnemyMap = mapHitboxesToEnemies(currentHitboxStates);
        if (!playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {
            return Optional.empty();
        }

        final Optional<Hitbox> closestEnemyOpt = playerHitboxController.getRadius()
                .getClosestEnemyInRadius(enemyHitboxes);
        if (closestEnemyOpt.isEmpty()) {

            return Optional.empty();
        }

        final Hitbox closestEnemyHitbox = closestEnemyOpt.get();
        final GenericCharacterController enemy = hitboxToEnemyMap.get(closestEnemyHitbox);
        if (enemy == null) {
            return Optional.empty();
        }

        final List<CollisionEvent> events = new ArrayList<>();
        determineAndCreateCollisionEvent(events, player, enemy);

        return events.isEmpty() ? Optional.empty() : Optional.of(events.get(0));
    }

    public Optional<CollisionEvent> scanForWallCollision(final GenericCharacterController enemy) {
        final Optional<HitboxController> hitboxController = matchController.getCharacterHitboxById(enemy.getId().number());
        if (hitboxController.isPresent()) {
            final Hitbox enemyHitbox = hitboxController.get().getHitbox();
            final int enemyX = (int)enemyHitbox.getPosition().x();
            final int wallX = (int)matchController.getMatchView().getWallCoordinates().getX();
            final int wallWidth = matchController.getWall().getHitbox().get().getWidth();
            final int wallBoundary = wallX + wallWidth;
            final int roundedLimit = wallBoundary + (2 - (wallBoundary % 2)) % 2;
            if (enemyX <= roundedLimit) {
                //dato che il muro non è un generic non posso usare createevents perchè usa hitboxControllers
                final Collidable wall = new CollidableImpl(matchController.getWall().getHitbox().get(), matchController.getWall().getCardIdentifier());
                final Collidable enemyCol = new CollidableImpl(enemyHitbox, enemy.getId());
                final CollisionEvent event = new CollisionEventImpl(EventType.WALL, wall, enemyCol);
                final List<CollisionEvent> events = new ArrayList<>();
                events.add(event);
                return Optional.of(event);
            }
        }
        return Optional.empty();
    }

    /**
     * Gathers all enemy and boss hitboxes from the current game state.
     *
     * @param map The map of character controllers to their hitbox controllers.
     * @return A list of hitboxes belonging to enemies and bosses.
     */
    private List<Hitbox> getAllEnemyHitboxes(Map<GenericCharacterController, HitboxController> map) {
        final List<Hitbox> enemyHitboxes = new ArrayList<>();
        for (final var entry : map.entrySet()) {
            final CardType type = entry.getKey().getId().type();
            final var character = entry.getKey();
            if ((type == CardType.ENEMY && character instanceof EnemyController)
                    || (type == CardType.BOSS && character instanceof BossController)) {
                enemyHitboxes.add(entry.getValue().getHitbox());
            }
        }
        return enemyHitboxes;
    }

    /**
     * Creates a map from hitbox to its corresponding generic character controller
     * for enemies and bosses.
     *
     * @param map The map of character controllers to their hitbox controllers.
     * @return A map where keys are hitboxes and values are generic character
     *         controllers of enemies/bosses.
     */
    private Map<Hitbox, GenericCharacterController> mapHitboxesToEnemies(
            Map<GenericCharacterController, HitboxController> map) {
        final Map<Hitbox, GenericCharacterController> result = new HashMap<>();
        for (final var entry : map.entrySet()) {
            final CardType type = entry.getKey().getId().type();
            final var character = entry.getKey();

            if (type == CardType.ENEMY && character instanceof EnemyController enemy) {
                result.put(entry.getValue().getHitbox(), enemy);
            } else if (type == CardType.BOSS && character instanceof BossController boss) {
                result.put(entry.getValue().getHitbox(), boss);
            }
        }
        return result;
    }

    /**
     * Determines the appropriate collision event type and creates it, adding it to
     * the list of events.
     * This method handles the logic for different character types (RANGED, MELEE)
     * encountering enemies (normal, BOSS).
     *
     * @param events The list to which the created CollisionEvent will be added.
     * @param player The playable character (initiator of the scan).
     * @param enemy  The enemy character detected.
     */
    private void determineAndCreateCollisionEvent(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final GenericCharacterController enemy) {
        final int playerId = player.getId().number();
        final int enemyId = enemy.getId().number();

        if (enemy instanceof BossController boss) {
            // Un player (sia MELEE che RANGED) che rileva un Boss.
            // La logica di "boss fight" è gestita dal CollisionResolver,
            // che terrà traccia di tutti i partecipanti.
            synchronized (boss) {
                boolean alreadyInBossFight = resolver.hasOpponentBossPartner(playerId);
                if (!alreadyInBossFight) {
                    createAndAddEvent(events, player, boss, EventType.BOSS);
                }
            }
        } else if (enemy instanceof EnemyController regularEnemy) {
            if (player.getId().type() == CardType.RANGED) {
                boolean isInRangedFight = resolver.hasOpponentRangedPartner(playerId);
                if (!isInRangedFight) {
                    createAndAddEvent(events, player, regularEnemy, EventType.RANGED);
                }
            } else {
                if (!regularEnemy.isInCombat() && matchController.isPlayerIdle(player)
                        && !matchController.isEnemyDead(enemyId)) {
                    synchronized (regularEnemy) {
                        boolean engaged = matchController.engageEnemy(enemyId, playerId);
                        if (engaged) {
                            createAndAddEvent(events, player, regularEnemy, EventType.ENEMY);
                        }
                    }
                } else if (isAtTroopZoneLimit(player)) { // caso borzerzone
                    if (!regularEnemy.isInCombat() && matchController.isPlayerStopped(player)
                            && !matchController.isEnemyDead(enemyId)) {
                        synchronized (regularEnemy) {
                            boolean engaged = matchController.engageEnemy(enemyId, playerId);
                            if (engaged) {
                                createAndAddEvent(events, player, regularEnemy, EventType.ENEMY);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Helper method to create and add a CollisionEvent to the list.
     *
     * @param events    The list of events.
     * @param player    The player collidable.
     * @param enemy     The enemy collidable.
     * @param eventType The type of event.
     */
    private void createAndAddEvent(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final GenericCharacterController enemy,
            final EventType eventType) {

        final HitboxController playerHitboxController = hitboxControllers.get(player);
        final HitboxController enemyHitboxController = hitboxControllers.get(enemy);

        if (playerHitboxController != null && enemyHitboxController != null) {
            final Collidable playerCol = new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
            final Collidable enemyCol = new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());
            events.add(new CollisionEventImpl(eventType, playerCol, enemyCol));
        }
    }

    private boolean isAtTroopZoneLimit(final GenericCharacterController player) {
        Optional<HitboxController> hitboxController = matchController.getCharacterHitboxById(player.getId().number());
        if (hitboxController.isPresent()) {
            int limit = matchController.getMatchView().getTrupsZoneLimit()
                    - hitboxController.get().getHitbox().getWidth();
            int roundedLimit = limit + (5 - (limit % 5)) % 5;
            return hitboxController.get().getHitbox().getPosition().x() >= roundedLimit;
        }
        return false;
    }
}