package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.collision.api.EntityEngagementManager;

/**
 * Manages the combat engagement state between player and enemy characters.
 * This version is a pure data manager and has no dependency on MatchController.
 */
public final class EntityEngagementManagerImpl implements EntityEngagementManager {

    private final Set<EnemyEngagement> engagedEnemies = new HashSet<>();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();

    @Override
    public boolean isEntityEngaged(final int entityId) {
        synchronized (engagedEnemies) {
            return engagedEnemies.stream()
                .anyMatch(e -> e.playerId() == entityId || e.enemyId() == entityId);
        }
    }

    @Override
    public boolean engageEnemy(final int enemyId, final int playerId) {
        final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
        synchronized (lock) {
            final boolean alreadyEngaged = engagedEnemies.stream().anyMatch(e -> e.enemyId() == enemyId);
            if (alreadyEngaged) {
                return false;
            }
            engagedEnemies.add(new EnemyEngagement(enemyId, playerId));
            return true;
        }
    }

    @Override
    public boolean releaseEngagementFor(final int characterId) {
        synchronized (engagedEnemies) {
            return engagedEnemies.removeIf(e -> e.enemyId() == characterId || e.playerId() == characterId);
        }
    }

    @Override
    public int getEngagedCounterpart(final int characterId) {
        synchronized (engagedEnemies) {
            final var engagementOpt = engagedEnemies.stream()
                .filter(e -> e.playerId() == characterId || e.enemyId() == characterId)
                .findFirst();

            if (engagementOpt.isPresent()) {
                final EnemyEngagement engagement = engagementOpt.get();
                return engagement.playerId() == characterId ? engagement.enemyId() : engagement.playerId();
            }
            return -1;
        }
    }

    @Override
    public Set<EnemyEngagement> getEngagedEnemies() {
        return Collections.unmodifiableSet(new HashSet<>(engagedEnemies));
    }
}
