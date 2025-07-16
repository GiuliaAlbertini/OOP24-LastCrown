package it.unibo.oop.lastcrown.model.api;

import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.model.impl.MovementResult;

/**
 * Interface for resolving collisions and managing movements related to characters and enemies.
 * Extends CollisionObserver to receive collision events.
 */
public interface CollisionResolver extends CollisionObserver {

    /**
     * Updates the movement of the specified character based on the elapsed time.
     *
     * @param characterId the unique identifier of the character
     * @param deltaMs the time elapsed since the last update, in milliseconds
     * @return an Optional containing the MovementResult if the character is moving, or empty otherwise
     */
    Optional<MovementResult> updateMovementFor(int characterId, long deltaMs);

    /**
     * Checks whether the enemy identified by the given ID has collided.
     *
     * @param enemyId the unique identifier of the enemy
     * @return true if the enemy has collided, false otherwise
     */
    boolean wasEnemyCollided(int enemyId);

    /**
     * Clears the collision state between the specified enemy and character.
     *
     * @param enemyId the unique identifier of the enemy
     * @param characterId the unique identifier of the character
     */
    void clearEnemyCollision(int characterId);
}
