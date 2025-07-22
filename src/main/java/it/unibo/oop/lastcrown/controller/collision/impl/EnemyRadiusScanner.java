
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
 * event is generated.
 */

// scanner di tutta la mappa da ogni giocatore. mi dice anche da un player che
// non è in collisione se un'altro lo è nel caso
public final class EnemyRadiusScanner {
    private final Map<GenericCharacterController, HitboxController> hitboxControllers;
    private final MatchController matchController;
    private final CollisionResolver resolver;

    public EnemyRadiusScanner(final Map<GenericCharacterController, HitboxController> hitboxControllers,
            MatchController matchController, CollisionResolver resolver) {
        this.hitboxControllers = hitboxControllers;
        this.matchController = matchController;
        this.resolver = resolver;
    }

    public Optional<CollisionEvent> scanForFollowEventForPlayer(final PlayableCharacterController player) {
    final HitboxController playerHitboxController = hitboxControllers.get(player);
    if (playerHitboxController == null) {
        return Optional.empty();
    }

    final Map<GenericCharacterController, HitboxController> localCopy = new HashMap<>(hitboxControllers);
    final List<Hitbox> enemyHitboxes = getAllEnemyHitboxes(localCopy);
    final Map<Hitbox, GenericCharacterController> hitboxToEnemy = mapHitboxesToEnemies(localCopy);

    final List<CollisionEvent> events = new ArrayList<>();
    addEventIfEnemyInRadius(events, player, playerHitboxController, enemyHitboxes, hitboxToEnemy);

    if (events.isEmpty()) {
        return Optional.empty();
    }

    return Optional.of(events.get(0)); // di solito è uno solo
}


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

    // metodo chiamato solo dal player
    private void addEventIfEnemyInRadius(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final HitboxController playerHitboxController,
            final List<Hitbox> enemyHitboxes,
            final Map<Hitbox, GenericCharacterController> hitboxToEnemy) {

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
        GenericCharacterController enemy = hitboxToEnemy.get(closestEnemyHitbox);
        if (enemy == null) {
            return;
        }

        // se il mio player incontra il boss
        if (enemy instanceof BossController) {
            synchronized (enemy) {
                System.out.println("ma entro almeno in radius per verificare la boss");
                boolean inBossFight = resolver.hasOpponentBossPartner(player.getId().number());
                System.out.println(inBossFight);
                if (!inBossFight) {
                    System.out.println("entro per cacciare l'evento boss");
                    createCollisionEvent(events, player, enemy);
                    System.out.println(events);

                }
            }

            // createCollisionEvent(events, player, enemy);
        } else if (enemy instanceof EnemyController) {
            boolean isInRangedFight = resolver.hasOpponentRangedPartner(player.getId().number());
            if (player.getId().type() == CardType.RANGED && !isInRangedFight) {
                createCollisionEvent(events, player, enemy);
            } else if (!enemy.isInCombat() && matchController.isPlayerIdle(player) && !matchController.isEnemyDead(enemy.getId().number())) {
                synchronized (enemy) {
                    boolean engaged = matchController.engageEnemy(enemy.getId().number(), player.getId().number());
                    if (engaged) {
                        createCollisionEvent(events, player, enemy);
                    }
                }
            }
        }

    }

    private void createCollisionEvent(
            final List<CollisionEvent> events,
            final PlayableCharacterController player,
            final GenericCharacterController enemy) {
        final HitboxController playerHitboxController = hitboxControllers.get(player);
        final HitboxController enemyHitboxController = hitboxControllers.get(enemy);

        if (playerHitboxController != null && enemyHitboxController != null) {
            final Collidable playerCol = new CollidableImpl(playerHitboxController.getHitbox(), player.getId());
            final Collidable enemyCol = new CollidableImpl(enemyHitboxController.getHitbox(), enemy.getId());

            // se il player è ranged
            if (playerCol.getCardidentifier().type() == CardType.RANGED) {


                final EventType eventType = (enemy instanceof BossController)
                        ? EventType.BOSS
                        : EventType.RANGED;

                        System.out.println("[DEBUG] Intercettato da ranged " + (enemy instanceof BossController ? "Boss" : "Nemico") +
                        "! Player ID: " + player.getId().number() +
                        " -> Target ID: " + enemy.getId().number());
                events.add(new CollisionEventImpl(eventType, playerCol, enemyCol));
            } else {
                final EventType eventType = (enemy instanceof BossController)
                        ? EventType.BOSS
                        : EventType.ENEMY;

                System.out.println("[DEBUG] Intercettato " + (enemy instanceof BossController ? "Boss" : "Nemico") +
                        "! Player ID: " + player.getId().number() +
                        " -> Target ID: " + enemy.getId().number());

                events.add(new CollisionEventImpl(eventType, playerCol, enemyCol));
            }

        }
    }


}
