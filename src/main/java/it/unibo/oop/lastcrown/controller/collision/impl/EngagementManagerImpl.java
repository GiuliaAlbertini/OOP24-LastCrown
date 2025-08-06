package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.EngagementManager;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;

public final class EngagementManagerImpl implements EngagementManager {

    private final Set<EnemyEngagement> engagedEnemies = ConcurrentHashMap.newKeySet();
    private final Map<Integer, Object> enemyLocks = new HashMap<>();
    private final MatchController matchController;

    public EngagementManagerImpl(MatchController matchController) {
        this.matchController = matchController;
    }

    @Override
    public boolean isEntityEngaged(final int entityId) {
        for (EnemyEngagement engagement : engagedEnemies) {
            if (engagement.playerId() == entityId || engagement.enemyId() == entityId) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean engageEnemy(int enemyId, int playerId) {
        return updateEnemyState(enemyId, playerId, true);
    }

    @Override
    public boolean releaseEngagementFor(final int characterId) {
        EnemyEngagement toRemove = null;
        synchronized (engagedEnemies) {
            for (EnemyEngagement e : engagedEnemies) {
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
        for (EnemyEngagement engagement : engagedEnemies) {
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
            int enemy = getEngagedCounterpart(characterId);
            if (enemy != -1) {
                Optional<GenericCharacterController> enemycontroller = matchController
                        .getCharacterControllerById(enemy);
                if (enemycontroller.isPresent()) {
                    if (enemycontroller.get().isDead()) {
                        return true;
                    }
                }
                return false;
            }
            return false;
        } else {
            if (isEnemyEngaged(characterId)) {
                int character = getEngagedCounterpart(characterId);
                if (character != -1) {
                    Optional<GenericCharacterController> charactercontroller = matchController
                            .getCharacterControllerById(character);
                    if (charactercontroller.isPresent()) {
                        if (charactercontroller.get().isDead()) {
                            return true;
                        }
                    }
                    return false;
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
    public boolean isPlayerEngaged(int playerId) {
        return isEntityEngaged(playerId);
    }

    @Override
    public boolean isEnemyEngaged(int enemyId) {
        return isEntityEngaged(enemyId);
    }

    private boolean updateEnemyState(int enemyId, Integer playerId, boolean engage) {
        final Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
        synchronized (lock) {
            if (engage) {
                for (EnemyEngagement e : engagedEnemies) {
                    if (e.enemyId() == enemyId) {
                        return false;
                    }
                }
                engagedEnemies.add(new EnemyEngagement(enemyId, playerId));
                setEnemyInCombat(enemyId, true);
                return true;

            } else {
                EnemyEngagement toRemove = null;
                for (EnemyEngagement e : engagedEnemies) {
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

    private void setEnemyInCombat(int enemyId, boolean inCombat) {
        matchController.getCharacterControllerById(enemyId).ifPresent(enemy -> {
            if (enemy instanceof EnemyController) {
                Object lock = enemyLocks.computeIfAbsent(enemyId, k -> new Object());
                synchronized (lock) {
                    ((EnemyController) enemy).setInCombat(inCombat);
                }
            }
		});
	}
}