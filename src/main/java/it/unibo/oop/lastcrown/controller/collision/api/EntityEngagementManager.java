package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Set;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyEngagement;

/**
 * Manages engagements between players and enemies during a match.
 *
 * Engagements represent combat relationships between entities (players and enemies),
 * ensuring that each enemy can only be engaged by a single player at a time.
 */
public interface EntityEngagementManager {

    /**
     * Checks whether a given entity (player or enemy) is currently engaged in combat.
     *
     * @param entityId the ID of the entity to check
     * @return true if the entity is engaged, false otherwise
     */
    boolean isEntityEngaged(int entityId);

    /**
     * Engages an enemy with a player, if the enemy is not already engaged.
     * Also sets the enemy's combat status to "in combat".
     *
     * @param enemyId the ID of the enemy to engage
     * @param playerId the ID of the player engaging the enemy
     * @return true if the engagement was successful, false if the enemy was already engaged
     */
    boolean engageEnemy(int enemyId, int playerId);

    /**
     * Releases the engagement of a player or enemy.
     * The engagement is removed regardless of whether the ID refers to the player or enemy.
     *
     * @param characterId the ID of the player or enemy to release
     * @return true if an engagement was found and removed, false otherwise
     */
    boolean releaseEngagementFor(int characterId);

    /**
     * Returns the ID of the entity (player or enemy) currently engaged with the specified character.
     *
     * @param characterId the ID of the player or enemy
     * @return the ID of the engaged counterpart, or -1 if not engaged
     */
    int getEngagedCounterpart(int characterId);


    /**
     * Returns an unmodifiable set of all current engagements between players and enemies.
     *
     * @return a set of EnemyEngagement objects
     */
    Set<EnemyEngagement> getEngagedEnemies();

}
