package it.unibo.oop.lastcrown.model.collision.api;

import java.util.List;
import java.util.Optional;
import it.unibo.oop.lastcrown.model.collision.impl.MovementResult;

/**
 * Interface for resolving collisions and managing engagements between game entities.
 * It provides a unified API to handle melee movements and various types of pair-based
 * engagements (e.g., RANGED, BOSS, WALL).
 * It extends CollisionObserver to receive collision events.
 */
public interface CollisionResolver extends CollisionObserver {

    /**
     * Updates the movement of the specified character based on the elapsed time.
     * This typically applies to melee characters following an enemy.
     *
     * @param characterId the unique identifier of the character.
     * @param deltaMs the time elapsed since the last update, in milliseconds.
     * @return an Optional containing the MovementResult if the character is moving, or empty otherwise.
     */
    Optional<MovementResult> updateMovementFor(int characterId, long deltaMs);

    /**
     * Checks if a specific entity has completed a melee collision, establishing a close-quarters engagement.
     *
     * @param entityId the unique identifier of the entity (player or enemy).
     * @return true if the entity has completed a melee collision, false otherwise.
     */
    boolean wasEnemyCollided(int entityId);

    /**
     * Clears the melee collision state for a specific character ID, removing any completed engagement record.
     *
     * @param characterId the unique identifier of the character.
     */
    void clearEnemyCollision(int characterId);

    /**
     * Checks if an entity is involved in a specific type of pair-based engagement.
     * This generic method replaces hasOpponentRangedPartner, hasOpponentBossPartner, etc.
     *
     * @param id   The unique identifier of the entity to check.
     * @param type The type of engagement (e.g., RANGED, BOSS, WALL).
     * @return true if the entity is a partner in the specified engagement type, false otherwise.
     */
    boolean hasOpponentPartner(int id, EventType type);

    /**
     * Retrieves the ID of the opponent partner for a given entity in a specific engagement type.
     * This generic method replaces getOpponentRangedPartner, getOpponentBossPartner, etc.
     *
     * @param id   The unique identifier of the entity whose partner is sought.
     * @param type The type of engagement (e.g., RANGED, BOSS, WALL).
     * @return The partner's ID, or -1 if no partner is found.
     */
    Optional<Integer> getOpponentPartner(int id, EventType type);

    /**
     * Retrieves a list of all character IDs currently attacking the boss.
     *
     * @return A list of unique character IDs participating in the boss fight.
     */
    List<Integer> getAllCharacterIdsInBossFight();

    /**
     * Retrieves a list of all character IDs currently engaged with wall entities.
     *
     * @return A list of unique character IDs involved in wall collisions.
     */
    List<Integer> getAllCharacterIdsInWallFight();

    /**
     * Clears all engagement pairs of a specific type that involve a given ID.
     * This is useful when a character dies or leaves a specific combat situation.
     *
     * @param id   The unique identifier of the character.
     * @param type The type of engagement to clear the character from.
     */
    void clearEngagementsById(int id, EventType type);

    /**
     * Clears all recorded engagement pairs of a specific type.
     * For example, can be used to end all ranged fights at once.
     *
     * @param type The type of engagements to clear.
     */
    void clearEngagementsByType(EventType type);

    /**
     * Clears all pair-based engagements of any type (RANGED, BOSS, WALL).
     * This is useful for a global state reset.
     */
    void clearAllPairEngagements();
}
