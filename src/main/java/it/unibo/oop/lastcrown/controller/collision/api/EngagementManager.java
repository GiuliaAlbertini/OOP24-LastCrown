package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Set;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyEngagement;

public interface EngagementManager {

    boolean isEntityEngaged(int entityId);

    boolean engageEnemy(int enemyId, int playerId);

    boolean releaseEngagementFor(int characterId);

    int getEngagedCounterpart(int characterId);

    boolean isEngagedWithDead(int characterId);

    Set<EnemyEngagement> getEngagedEnemies();

    boolean isPlayerEngaged(int playerId);

    boolean isEnemyEngaged(int enemyId);
}