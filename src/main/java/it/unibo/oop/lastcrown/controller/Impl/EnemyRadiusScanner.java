package it.unibo.oop.lastcrown.controller.impl;

import java.util.*;
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

public class EnemyRadiusScanner {

    private final Map<GenericCharacterController, HitboxController> hitboxControllers;

    public EnemyRadiusScanner(Map<GenericCharacterController, HitboxController> hitboxControllers) {
        this.hitboxControllers = hitboxControllers;
    }

    public List<CollisionEvent> scanForFollowEvents() {
        final List<Hitbox> enemyHitboxes = new ArrayList<>();
        final Map<Hitbox, EnemyController> hitboxToEnemy = new HashMap<>();

        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            GenericCharacterController controller = entry.getKey();
            if (controller.getId().type() == CardType.ENEMY) {
                HitboxController hbc = entry.getValue();
                Hitbox hitbox = hbc.getHitbox();
                enemyHitboxes.add(hitbox);
                hitboxToEnemy.put(hitbox, (EnemyController) controller);
            }
        }

        final List<CollisionEvent> events = new ArrayList<>();

        for (Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            GenericCharacterController controller = entry.getKey();
            if (controller.getId().type() != CardType.ENEMY) {
                PlayableCharacterController player = (PlayableCharacterController) controller;
                HitboxController playerHitboxController = entry.getValue();

                if (playerHitboxController.getRadius() != null && playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {
                    Optional<Hitbox> maybeClosest = playerHitboxController.getRadius().getClosestEnemyInRadius(enemyHitboxes);

                    if (maybeClosest.isPresent()) {
                        Hitbox closestHitbox = maybeClosest.get();
                        EnemyController enemy = hitboxToEnemy.get(closestHitbox);

                        if (enemy != null) {
                            HitboxController enemyHitboxController = hitboxControllers.get(enemy);
                            if (enemyHitboxController != null) {
                                Collidable playerCol = new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
                                Collidable enemyCol = new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());
                                //System.out.println("[DEBUG] Nemico intercettato! Player ID: " + player.getId().number() + " -> Enemy ID: " + enemy.getId().number());
                                events.add(new CollisionEventImpl(EventType.FOLLOW_ENEMY, playerCol, enemyCol));
                            }
                        }
                    }
                }
            }
        }

        return events;
    }
}
