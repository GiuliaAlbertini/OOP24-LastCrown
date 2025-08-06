package it.unibo.oop.lastcrown.controller.collision.api;

/**
 * Interface representing a system responsible for spawning enemies during a match.
 */
public interface EnemySpawner {

    /**
     * Updates the internal state of the spawner, handling the timed spawning of enemies.
     *
     * @param deltaTime the time elapsed since the last update, in milliseconds.
     */
    void update(int deltaTime);

    /**
     * Immediately spawns a boss enemy onto the battlefield.
     */
    void spawnBoss();

    /**
     * Checks if all enemies for the current round have been spawned.
     *
     * @return true if the round spawning is complete, false otherwise.
     */
    boolean isRoundSpawnComplete();

    /**
     * Gets the current round index.
     *
     * @return the index of the current round.
     */
    int getRoundIndex();
}
