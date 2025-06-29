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
            final Map<Hitbox, EnemyController> hitboxToEnemy) {

        // controllo se ha un raggio
        if (playerHitboxController.getRadius() == null) {
            return;
        }

        // controllo se ha nemici nel raggio
        if (!playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {
            return;
        }

        // controllo il nemico più vicino
        Optional<Hitbox> closestEnemyOpt = playerHitboxController.getRadius().getClosestEnemyInRadius(enemyHitboxes);
        if (closestEnemyOpt.isEmpty()) {
            return;
        }

        // mi prendo il nemico più vicino
        Hitbox closestEnemyHitbox = closestEnemyOpt.get();
        EnemyController enemy = hitboxToEnemy.get(closestEnemyHitbox);
        if (enemy == null) {
            return;
        }

        // Controllo esplicito sullo stato del nemico
        if (!enemy.isInCombat()) {
            System.out.println("IL NEMICO PIù VICINO " + enemy.getId() + enemy.isInCombat());
            synchronized (enemy) { // Sincronizzazione sull'oggetto nemico
                // se l'engagement avviene bene
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

        }
    }
}
