package it.unibo.oop.lastcrown.controller.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.api.HitboxController;
import it.unibo.oop.lastcrown.controller.api.MatchController;
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
 * If an enemy is detected within a player's radius, a FOLLOW_ENEMY collision
 * event is generated.
 */
public final class EnemyRadiusScanner {
    private final Map<GenericCharacterController, HitboxController> hitboxControllers;
    private final MatchController matchController;

    public EnemyRadiusScanner(final Map<GenericCharacterController, HitboxController> hitboxControllers,
                              MatchController matchController) {
        this.hitboxControllers = hitboxControllers;
        this.matchController = matchController;
    }

    public List<CollisionEvent> scanForFollowEvents() {
        // Copia thread-safe della mappa
        final Map<GenericCharacterController, HitboxController> localCopy = new HashMap<>(hitboxControllers);

        final List<Hitbox> enemyHitboxes = getAllEnemyHitboxes(localCopy);
        final Map<Hitbox, EnemyController> hitboxToEnemy = mapHitboxesToEnemies(localCopy);
        final List<CollisionEvent> events = new ArrayList<>();

        for (final var entry : localCopy.entrySet()) {
            final GenericCharacterController controller = entry.getKey();

            if (controller.getId().type() != CardType.ENEMY) {
                final PlayableCharacterController player = (PlayableCharacterController) controller;
                final HitboxController playerHitboxController = entry.getValue();

                addEventIfEnemyInRadius(events, player, playerHitboxController, enemyHitboxes, hitboxToEnemy);
            }
        }

        return events;
    }

    private List<Hitbox> getAllEnemyHitboxes(Map<GenericCharacterController, HitboxController> map) {
        final List<Hitbox> enemyHitboxes = new ArrayList<>();
        for (final var entry : map.entrySet()) {
            if (entry.getKey().getId().type() == CardType.ENEMY) {
                enemyHitboxes.add(entry.getValue().getHitbox());
            }
        }
        return enemyHitboxes;
    }

    private Map<Hitbox, EnemyController> mapHitboxesToEnemies(Map<GenericCharacterController, HitboxController> map) {
        final Map<Hitbox, EnemyController> result = new HashMap<>();
        for (final var entry : map.entrySet()) {
            if (entry.getKey().getId().type() == CardType.ENEMY) {
                result.put(entry.getValue().getHitbox(), (EnemyController) entry.getKey());
            }
        }
        return result;
    }

    private void addEventIfEnemyInRadius(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final HitboxController playerHitboxController,
            final List<Hitbox> enemyHitboxes,
            final Map<Hitbox, EnemyController> hitboxToEnemy) {

        if (playerHitboxController.getRadius() == null) {
            return;
        }

        if (!playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {
            return;
        }

        Optional<Hitbox> closestEnemyOpt = playerHitboxController.getRadius().getClosestEnemyInRadius(enemyHitboxes);
        if (closestEnemyOpt.isEmpty()) {
            return;
        }

        Hitbox closestEnemyHitbox = closestEnemyOpt.get();
        EnemyController enemy = hitboxToEnemy.get(closestEnemyHitbox);
        if (enemy == null) {
            return;
        }

        if (!enemy.isInCombat()) {
            System.out.println("IL NEMICO PIÙ VICINO " + enemy.getId() + enemy.isInCombat());
            synchronized (enemy) {
                boolean engaged = matchController.engageEnemy(enemy.getId().number(), player.getId().number());
                if (engaged) {
                    createCollisionEvent(events, player, enemy);
                }
            }
        }
    }

    private void createCollisionEvent(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final EnemyController enemy) {
        final HitboxController playerHitboxController = hitboxControllers.get(player);
        final HitboxController enemyHitboxController = hitboxControllers.get(enemy);

        if (playerHitboxController != null && enemyHitboxController != null) {
            final Collidable playerCol = new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
            final Collidable enemyCol = new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());

            System.out.println("sono in createcollisionevent " + enemy.isInCombat());
            System.out.println("[DEBUG] Nemico intercettato! Player ID: " + player.getId().number()
                    + " -> Enemy ID: " + enemy.getId().number());
            events.add(new CollisionEventImpl(EventType.FOLLOW_ENEMY, playerCol, enemyCol));
            System.out.println(playerCol.getCardidentifier().number());
            System.out.println(enemyCol.getCardidentifier().number());
        }
    }
}
