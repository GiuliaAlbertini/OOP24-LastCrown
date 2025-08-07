package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.EntityEngagementManager;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;


/**
 * Implementation of the EngagementManager interface.
 * Manages the combat engagement state between player-controlled and enemy characters.
 * Keeps track of which enemies are currently engaged in combat with players and provides
 * methods to update, query, or remove these engagements in a thread-safe manner.
 *
 * Uses synchronization to ensure consistent state in concurrent environments.
 */
public final class EntityEngagementManagerImpl implements EntityEngagementManager {

    private final Set<EnemyEngagement> engagedEnemies = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();
    private final MatchController matchController;

    /**
     * Constructs a new EngagementManagerImpl with a reference to the MatchController.
     *
     * @param matchController the controller managing character states in the current match.
     */
    public EntityEngagementManagerImpl(final MatchController matchController) {
        this.matchController = matchController;
    }

    @Override
    public boolean isEntityEngaged(final int entityId) {
        for (final EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == entityId || engagement.enemyId() == entityId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean engageEnemy(final int enemyId, final int playerId) {
        return updateEnemyState(enemyId, playerId, true);
    }

    @Override
    public boolean releaseEngagementFor(final int characterId) {
        EnemyEngagement toRemove = null;
        synchronized (engagedEnemies) {
            for (final EnemyEngagement e : engagedEnemies) {
                if (e.enemyId() == characterId || e.playerId() == characterId) {
                    toRemove = e;
                    break;
                }
            }
            if (toRemove != null) {
                engagedEnemies.remove(toRemove);
                setEnemyInCombat(toRemove.enemyId(), false);
                enemyLocks.remove(toRemove.enemyId());
                return true;
            }
        }
        return false;
    }

    @Override
    public int getEngagedCounterpart(final int characterId) {
        for (final EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == characterId) {
                return engagement.enemyId();
            } else if (engagement.enemyId() == characterId) {
                return engagement.playerId();
            }
        }
        return -1;
    }

    @Override
    public boolean isEngagedWithDead(final int characterId) {
        if (isPlayerEngaged(characterId)) {
            final int enemy = getEngagedCounterpart(characterId);
            if (enemy != -1) {
                final Optional<GenericCharacterController> enemycontroller = matchController
                        .getCharacterControllerById(enemy);
                return enemycontroller.isPresent()
                    && enemycontroller.get().isDead();
            }
            return false;
        } else {
            if (isEnemyEngaged(characterId)) {
                final int character = getEngagedCounterpart(characterId);
                if (character != -1) {
                    final Optional<GenericCharacterController> charactercontroller = matchController
                            .getCharacterControllerById(character);
                    return charactercontroller.isPresent()
                        && charactercontroller.get().isDead();
                }
            }
            return false;
        }
    }

    @Override
    public Set<EnemyEngagement> getEngagedEnemies() {
        return Collections.unmodifiableSet(engagedEnemies);
    }

    @Override
    public boolean isPlayerEngaged(final int playerId) {
        return isEntityEngaged(playerId);
    }

    @Override
    public boolean isEnemyEngaged(final int enemyId) {
        return isEntityEngaged(enemyId);
    }

    private boolean updateEnemyState(final int enemyId, final int playerId, final boolean engage) {
        final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
        synchronized (lock) {
            if (engage) {
                for (final EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId) {
                        return false;
                    }
                }
                engagedEnemies.add(new EnemyEngagement(enemyId, playerId));
                setEnemyInCombat(enemyId, true);
                return true;

            } else {
                EnemyEngagement toRemove = null;
                for (final EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId && e.playerId() == playerId) {
                        toRemove = e;
                        break;
                    }
                }

                if (toRemove != null) {
                    engagedEnemies.remove(toRemove);
                    setEnemyInCombat(enemyId, false);
                    enemyLocks.remove(enemyId);
                    return true;
                }
                return false;
            }
        }
    }

    private void setEnemyInCombat(final int enemyId, final boolean inCombat) {
        matchController.getCharacterControllerById(enemyId).ifPresent(enemy -> {
            if (enemy instanceof EnemyController) {
                final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
                synchronized (lock) {
                    ((EnemyController) enemy).setInCombat(inCombat);
                }
            }
        });
    }
}
