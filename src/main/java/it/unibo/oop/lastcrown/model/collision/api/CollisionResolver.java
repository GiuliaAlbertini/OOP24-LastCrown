package it.unibo.oop.lastcrown.model.collision.api;

import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.model.collision.impl.MovementResult;

/**
 * Interface for resolving collisions and managing movements related to characters and enemies.
 * It also defines methods for querying and managing combat engagements (melee, ranged, and boss fights).
 * Extends CollisionObserver to receive collision events.
 */
public interface CollisionResolver extends CollisionObserver {

    /**
     * Updates the movement of the specified character based on the elapsed time.
     * This typically applies to melee characters following an enemy.
     *
     * @param characterId the unique identifier of the character
     * @param deltaMs the time elapsed since the last update, in milliseconds
     * @return an Optional containing the MovementResult if the character is moving, or empty otherwise
     */
    Optional<MovementResult> updateMovementFor(int characterId, long deltaMs);

    /**
     * Checks whether the enemy identified by the given ID has completed a melee collision.
     * This signifies a close-quarters engagement has been established.
     *
     * @param enemyId the unique identifier of the enemy
     * @return true if the enemy has completed a melee collision, false otherwise
     */
    boolean wasEnemyCollided(int enemyId);

    /**
     * Clears the melee collision state for a specific character ID.
     * This removes any record of a completed melee engagement involving that character.
     *
     * @param characterId the unique identifier of the character (can be enemy or player)
     */
    void clearEnemyCollision(int characterId);

    /**
     * Checks if a character (player or boss) is currently a partner in a boss fight.
     *
     * @param id The unique identifier of the character to check.
     * @return true if the character is a partner in any active boss fight, false otherwise.
     */
    boolean hasOpponentBossPartner(int id);

    /**
     * Retrieves the ID of the boss partner for a given character ID.
     * Note: This method is designed for scenarios where a player is paired with a single boss.
     * For a boss managing multiple players, {@link #getAllCharacterIdsInBossFight()} is more appropriate.
     *
     * @param id The unique identifier of the character (player or boss).
     * @return The ID of the boss partner, or -1 if not found.
     */
    int getOpponentBossPartner(int id);

    /**
     * Checks if a character is currently engaged with a wall obstacle.
     *
     * @param id the unique identifier of the character
     * @return true if the character is engaged with a wall, false otherwise
     */
    boolean hasOpponentWallPartner(int id);

    /**
     * Retrieves the ID of the wall entity engaged with the given character.
     *
     * @param id the unique identifier of the character
     * @return the ID of the wall partner, or -1 if none is found
     */
    int getOpponentWallPartner(int id);

    /**
     * Retrieves a list of all character IDs (typically players) currently involved in a boss fight.
     *
     * @return A list of unique character IDs participating in the boss fight.
     */
    List<Integer> getAllCharacterIdsInBossFight();

    /**
     * Clears all boss fight pairs involving the specified character ID.
     * This is useful when a character (player or boss) dies or leaves the boss fight.
     *
     * @param id The unique identifier of the character.
     */
    void clearBossFightPairById(int id);

    /**
     * Clears all recorded melee engagement pairs.
     * This might be used to reset all close-quarters combat states.
     */
    void clearAllOpponentPairs(); // Renamed in impl to clearAllMeleeEngagements, but kept here for now for compatibility

    /**
     * Checks if a character (ranged player or enemy) is currently a partner in a ranged engagement.
     *
     * @param id The unique identifier of the character to check.
     * @return true if the character is a partner in any active ranged engagement, false otherwise.
     */
    boolean hasOpponentRangedPartner(int id);

    /**
     * Retrieves the ID of the ranged partner for a given character ID.
     *
     * @param id The unique identifier of the character.
     * @return The ID of the ranged partner, or -1 if not found.
     */
    int getOpponentRangedPartner(int id);

    /**
     * Clears all recorded ranged engagement pairs.
     * This might be used to reset all ranged combat states.
     */
    void clearAllOpponentRangedPairs();

    /**
     * Clears all boss fight engagement pairs.
     * This is typically called when a boss is defeated or the boss fight concludes.
     */
    void clearAllBossFightPairs();
/**
     * Retrieves a list of all character IDs currently engaged with wall entities.
     *
     * @return A list of unique character IDs involved in wall collisions or interactions.
     */
    List<Integer> getAllCharacterIdsInWallFight();
}
