package it.unibo.oop.lastcrown.model.collision.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.impl.handler.HandleFollowEnemy;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Implements CollisionResolver to handle "follow enemy" collision events.
 * Manages active follow movements and tracks completed ones,
 * as well as engagements for ranged characters and boss fights.
 */
public final class CollisionResolverImpl implements CollisionResolver {
    private final Map<Integer, HandleFollowEnemy> activeFollowMovements = new HashMap<>();
    private final Map<Integer, Integer> completedMeleeEngagements = new HashMap<>();

    private final Set<Pair<Integer, Integer>> wallFightPairs = ConcurrentHashMap.newKeySet();
    private final Set<Pair<Integer, Integer>> bossFightPairs = ConcurrentHashMap.newKeySet();
    private final Set<Pair<Integer, Integer>> rangedEngagements = ConcurrentHashMap.newKeySet();

    @Override
    public void notify(final CollisionEvent event) {
        switch (event.getType()) {
            case ENEMY -> handleMeleeEngagement(event);
            case BOSS -> handleBossEngagement(event);
            case RANGED -> handleRangedEngagement(event);
            case WALL -> handleWallEngagement(event);
        }
    }

    /**
     * Handles ENEMY type collision events (Melee player engaging a regular enemy).
     * @param event The collision event.
     */
    private void handleMeleeEngagement(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardIdentifier().number();

        final HandleFollowEnemy movement = new HandleFollowEnemy(event);
        movement.startFollowing();
        activeFollowMovements.put(characterId, movement);
    }

    /**
     * Handles BOSS type collision events (any player engaging a boss).
     * @param event The collision event.
     */
    private void handleBossEngagement(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardIdentifier().number();
        final int bossId = event.getCollidable2().getCardIdentifier().number();
        bossFightPairs.add(new Pair<>(characterId, bossId));
    }


    private void handleWallEngagement(final CollisionEvent event) {
        final int wallId = event.getCollidable1().getCardIdentifier().number();
        final int bossId = event.getCollidable2().getCardIdentifier().number();
        wallFightPairs.add(new Pair<>(wallId, bossId));
    }

    /**
     * Handles RANGED type collision events (Ranged player engaging a regular enemy).
     * @param event The collision event.
     */
    public void handleRangedEngagement(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardIdentifier().number();
        final int enemyId = event.getCollidable2().getCardIdentifier().number();
        rangedEngagements.add(new Pair<>(characterId, enemyId));
    }

    @Override
    public boolean hasOpponentRangedPartner(final int id) {
        for (final Pair<Integer, Integer> pair : rangedEngagements) {
            if (pair.get1() == id || pair.get2() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOpponentRangedPartner(final int id) {
        for (final Pair<Integer, Integer> pair : rangedEngagements) {
            if (pair.get1() == id) {
                return pair.get2();
            } else if (pair.get2() == id) {
                return pair.get1();
            }
        }
        return -1;
    }

    @Override
    public boolean hasOpponentBossPartner(final int id) {
        for (final Pair<Integer, Integer> pair : bossFightPairs) {
            if (pair.get1() == id || pair.get2() == id) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean hasOpponentWallPartner(final int id) {
        for (final Pair<Integer, Integer> pair : wallFightPairs) {
            if (pair.get1() == id || pair.get2() == id) {
                return true;
            }
        }
        return false;
    }


    @Override
    public int getOpponentBossPartner(final int id) {
        for (final Pair<Integer, Integer> pair : bossFightPairs) {
            if (pair.get1() == id) {
                return pair.get2();
            } else if (pair.get2() == id) {
                return pair.get1();
            }
        }
        return -1;
    }


    @Override
    public int getOpponentWallPartner(final int id) {
        for (final Pair<Integer, Integer> pair : wallFightPairs) {
            if (pair.get1() == id) {
                return pair.get2();
            } else if (pair.get2() == id) {
                return pair.get1();
            }
        }
        return -1;
    }

    @Override
    public List<Integer> getAllCharacterIdsInWallFight() {
        final Set<Integer> uniqueEnemyIds = new HashSet<>();
        for (final Pair<Integer, Integer> pair : wallFightPairs) {
            uniqueEnemyIds.add(pair.get2());
        }
        return new ArrayList<>(uniqueEnemyIds);
    }


    @Override
    public List<Integer> getAllCharacterIdsInBossFight() {
        final Set<Integer> uniquePlayerIds = new HashSet<>();
        for (final Pair<Integer, Integer> pair : bossFightPairs) {
            uniquePlayerIds.add(pair.get1());
        }
        return new ArrayList<>(uniquePlayerIds);
    }

    @Override
    public void clearBossFightPairById(final int id) {
        bossFightPairs.removeIf(pair -> pair.get1() == id || pair.get2() == id);
    }

    @Override
    public void clearAllOpponentPairs() {
        completedMeleeEngagements.clear();
    }

    @Override
    public void clearAllOpponentRangedPairs() {
        rangedEngagements.clear();
    }

    @Override
    public void clearAllBossFightPairs() {
        bossFightPairs.clear();
    }

    @Override
    public Optional<MovementResult> updateMovementFor(final int characterId, final long deltaMs) {
        final HandleFollowEnemy movement = activeFollowMovements.get(characterId);

        if (movement != null) {
            final var stillMoving = movement.update(deltaMs);
            final Point2D delta = movement.getDelta();
            final Movement movementDelta = new Movement((int) delta.x(), (int) delta.y());

            if (!stillMoving) {
                activeFollowMovements.remove(characterId);
                completedMeleeEngagements.put(movement.getEnemy().getCardIdentifier().number(), characterId);
            }
            return Optional.of(new MovementResult(
                movement.getCharacter(),
                movement.getCurrentPosition(),
                movementDelta,
                stillMoving));
        }
        return Optional.empty();
    }

    @Override
    public boolean wasEnemyCollided(final int id) {
        return completedMeleeEngagements.containsKey(id) || completedMeleeEngagements.containsValue(id);
    }

    @Override
    public void clearEnemyCollision(final int characterId) {
        if (completedMeleeEngagements.containsKey(characterId)) {
            completedMeleeEngagements.remove(characterId);
        }
        completedMeleeEngagements.entrySet().removeIf(entry -> entry.getValue() == characterId);
    }
}
