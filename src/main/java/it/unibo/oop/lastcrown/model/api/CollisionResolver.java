package it.unibo.oop.lastcrown.model.api;

import it.unibo.oop.lastcrown.model.impl.CollisionEventImpl;

/**
 * Interface defining methods for resolving different types of collisions
 * between game entities.
 */
public interface CollisionResolver {

    /**
     * Handles the logic when the player collides with an enemy.
     *
     * @param collision the collision event containing details about the entities involved.
     */
    void resolveEnemyCollision(CollisionEventImpl collision);

    /**
     * Handles the logic when the player collides with a wall or an impassable object.
     *
     * @param collision the collision event containing details about the entities involved.
     */
    void resolveWallCollision(CollisionEventImpl collision);

    /**
     * Handles the logic when the player collides with a shop or a vendor.
     *
     * @param collision the collision event containing details about the entities involved.
     */
    void resolveShopCollision(CollisionEventImpl collision);

    /**
     * Handles the logic when the player collides with a key or other collectible items.
     *
     * @param collision the collision event containing details about the entities involved.
     */
    void resolveKeyCollision(CollisionEventImpl collision);
}
