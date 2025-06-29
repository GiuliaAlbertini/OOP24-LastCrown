package it.unibo.oop.lastcrown.model.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.impl.handler.HandleFollowEnemy;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Implements CollisionResolver to handle "follow enemy" collision events.
 * Manages active follow movements and tracks completed ones.
 */
public final class CollisionResolverImpl implements CollisionResolver {
    private final Map<Integer, HandleFollowEnemy> activeFollowMovements = new HashMap<>();
    private final Map<Integer, Integer> completedFollows = new HashMap<>(); //nemico-personaggio
    MatchController matchController;
    /**
     * Costruttore vuoto di default per CollisionResolverImpl.
     */
    public CollisionResolverImpl(MatchController controller) {
        this.matchController=controller;
    }

    @Override
    public void notify(final CollisionEvent event) {
        switch (event.getType()) {
            case FOLLOW_ENEMY -> handleFollowEnemy(event);
            //default -> System.out.println("[WARN] Evento collisione non gestito: " + event.getType());
        }
    }

    private void handleFollowEnemy(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardidentifier().number();
        final HandleFollowEnemy movement = new HandleFollowEnemy(event);
        movement.startFollowing();
        activeFollowMovements.put(characterId, movement);
        //System.out.println(activeFollowMovements);
    }

    @Override
    public Optional<MovementResult> updateMovementFor(final int characterId, final long deltaMs) {

        final HandleFollowEnemy movement = activeFollowMovements.get(characterId);
        final int enemyId = movement.getEnemy().getCardidentifier().number();

        if (movement != null) {
            final var stillMoving = movement.update(deltaMs);
            // non si sta più muovendo
            if (!stillMoving) {
                activeFollowMovements.remove(characterId);
                //character ha raggiunto il nemico

                completedFollows.put(enemyId, characterId);


            }
            final Point2D delta = movement.getDelta();
            final Movement movementDelta = new Movement((int) delta.x(), (int) delta.y());
            return Optional.of(new MovementResult(
                    movement.getCharacter(),
                    movement.getCurrentPosition(),
                    movementDelta,
                    stillMoving));
        }
        return Optional.empty();
    }

    @Override
    public Point2D getCharacterPosition(final int characterId) {
        final HandleFollowEnemy movement = activeFollowMovements.get(characterId);
        if (movement != null) {
            return movement.getCurrentPosition();
        } else {
            return null;
        }
    }

    @Override
    public boolean wasEnemyCollided(final int enemyId) {
        return completedFollows.containsKey(enemyId);
    }

    @Override
    public void clearEnemyCollision(final int enemyId, final int characterId) {
        completedFollows.remove(enemyId, characterId);
        //System.out.println(completedFollows);
    }

    @Override
    public Optional<Integer> getEnemyId(final int characterId) {
        int enemyId = 0;
        for (final Map.Entry<Integer, Integer> entry : completedFollows.entrySet()) {
            if (entry.getValue().equals(characterId)) {
                enemyId = entry.getKey();
                return Optional.of(enemyId);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Integer> getCharacterId(final int enemyId) {
        int characterId = 0;
        for (final Map.Entry<Integer, Integer> entry : completedFollows.entrySet()) {
            if (entry.getKey().equals(enemyId)) {
                characterId = entry.getValue();
                return Optional.of(characterId);
            }
        }
        return Optional.empty();
    }

}
