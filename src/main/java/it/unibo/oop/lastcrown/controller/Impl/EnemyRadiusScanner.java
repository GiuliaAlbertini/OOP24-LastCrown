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

    /**
     * Constructs a new EnemyRadiusScanner.
     *
     * @param hitboxControllers a mapping of all character controllers to their associated hitbox controllers
     */
    public EnemyRadiusScanner(final Map<GenericCharacterController, HitboxController> hitboxControllers) {
        this.hitboxControllers = hitboxControllers;
    }


    /**
     * Scans all playable characters' radii for enemies and generates FOLLOW_ENEMY events
     * for each detected interaction between a player and a nearby enemy.
     *
     * @return a list of collision events where enemies are within the radius of playable characters
     */
    public List<CollisionEvent> scanForFollowEvents() {
        final List<Hitbox> enemyHitboxes = new ArrayList<>();
        final Map<Hitbox, EnemyController> hitboxToEnemy = new HashMap<>();

        for (final Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            final GenericCharacterController controller = entry.getKey();
            if (controller.getId().type() == CardType.ENEMY) {
                final HitboxController hbc = entry.getValue();
                final Hitbox hitbox = hbc.getHitbox();
                enemyHitboxes.add(hitbox);
                hitboxToEnemy.put(hitbox, (EnemyController) controller);
            }
        }

        final List<CollisionEvent> events = new ArrayList<>();

        for (final Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            final GenericCharacterController controller = entry.getKey();
            if (controller.getId().type() != CardType.ENEMY) {
                final PlayableCharacterController player = (PlayableCharacterController) controller;
                final HitboxController playerHitboxController = entry.getValue();

                if (playerHitboxController.getRadius() != null
                    && playerHitboxController.getRadius().hasEnemyInRadius(enemyHitboxes)) {

                    final Optional<Hitbox> maybeClosest = 
                        playerHitboxController.getRadius().getClosestEnemyInRadius(enemyHitboxes);

                    if (maybeClosest.isPresent()) {
                        final Hitbox closestHitbox = maybeClosest.get();
                        final EnemyController enemy = hitboxToEnemy.get(closestHitbox);

                        if (enemy != null) {
                            final HitboxController enemyHitboxController = hitboxControllers.get(enemy);
                            if (enemyHitboxController != null) {
                                final Collidable playerCol =
                                    new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
                                final Collidable enemyCol = 
                                    new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());
//System.out.println("[DEBUG] Nemico intercettato! Player ID: " + 
//player.getId().number() + " -> Enemy ID: " + enemy.getId().number());
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
