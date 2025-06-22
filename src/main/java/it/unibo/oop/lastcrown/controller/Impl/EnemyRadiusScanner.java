package it.unibo.oop.lastcrown.controller.impl;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.model.api.Collidable;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.EventType;
import it.unibo.oop.lastcrown.model.api.Hitbox;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.impl.CollidableImpl;
import it.unibo.oop.lastcrown.model.impl.CollisionEventImpl;

/**
 * Scans the radius of playable characters to detect nearby enemy characters.
 * If an enemy is detected within a player's radius, a FOLLOW_ENEMY collision event is generated.
 */
public final class EnemyRadiusScanner {
    private final Map<GenericCharacterController, HitboxController> hitboxControllers;

    public EnemyRadiusScanner(final Map<GenericCharacterController, HitboxController> hitboxControllers) {
        this.hitboxControllers = hitboxControllers;
    }

    public List<CollisionEvent> scanForFollowEvents() {
        final List<Hitbox> enemyHitboxes = getAllEnemyHitboxes();
        final Map<Hitbox, EnemyController> hitboxToEnemy = mapHitboxesToEnemies();

        final List<CollisionEvent> events = new ArrayList<>();

        for (final var entry : hitboxControllers.entrySet()) {
            final GenericCharacterController controller = entry.getKey();

            if (controller.getId().type() != CardType.ENEMY) {
                final PlayableCharacterController player = (PlayableCharacterController) controller;
                final HitboxController playerHitboxController = entry.getValue();

                addEventIfEnemyInRadius(events, player, playerHitboxController, enemyHitboxes, hitboxToEnemy);
            }
        }

        return events;
    }

    private List<Hitbox> getAllEnemyHitboxes() {
        final List<Hitbox> enemyHitboxes = new ArrayList<>();
        for (final var entry : hitboxControllers.entrySet()) {
            if (entry.getKey().getId().type() == CardType.ENEMY) {
                enemyHitboxes.add(entry.getValue().getHitbox());
            }
        }
        return enemyHitboxes;
    }

    private Map<Hitbox, EnemyController> mapHitboxesToEnemies() {
        final Map<Hitbox, EnemyController> map = new HashMap<>();
        for (final var entry : hitboxControllers.entrySet()) {
            if (entry.getKey().getId().type() == CardType.ENEMY) {
                map.put(entry.getValue().getHitbox(), (EnemyController) entry.getKey());
            }
        }
        return map;
    }

    private void addEventIfEnemyInRadius(
        final List<CollisionEvent> events,
        final PlayableCharacterController player,
        final HitboxController playerHitboxController,
        final List<Hitbox> enemyHitboxes,
        final Map<Hitbox, EnemyController> hitboxToEnemy
    ) {
        if (playerHitboxController.getRadius() != null && playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {
                System.out.println(playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes));
            playerHitboxController.getRadius()
                .getClosestEnemyInRadius(enemyHitboxes)
                .flatMap(hitbox -> Optional.ofNullable(hitboxToEnemy.get(hitbox)))
                .filter(enemy -> !enemy.isInCombat())
                .ifPresent(enemy -> createCollisionEvent(events, player, enemy));
        }
    }

    private void createCollisionEvent(
        final List<CollisionEvent> events,
        final PlayableCharacterController player,
        final EnemyController enemy
    ) {
        final HitboxController playerHitboxController = hitboxControllers.get(player);
        final HitboxController enemyHitboxController = hitboxControllers.get(enemy);

        if (playerHitboxController != null && enemyHitboxController != null) {
            final Collidable playerCol = new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
            final Collidable enemyCol = new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());

            System.out.println("[DEBUG] Nemico intercettato! Player ID: " + player.getId().number()
                    + " -> Enemy ID: " + enemy.getId().number());
            events.add(new CollisionEventImpl(EventType.FOLLOW_ENEMY, playerCol, enemyCol));

        }
    }
}

