package it.unibo.oop.lastcrown.model.api;

/**
 * Represents an entity that can be involved in collisions.
 * Each collidable object must have a hitbox, a unique identifier, and a type.
 */
public interface Collidable {

    /**
     * Returns the hitbox associated with this object.
     * The hitbox defines the area used to detect collisions with other objects.
     *
     * @return the Hitbox of the object.
     */
    Hitbox getHitbox();

    /**
     * Returns the unique identifier of the object.
     * This can be used to distinguish between multiple collidable instances.
     *
     * @return an integer representing the object's ID.
     */
    int getId();

    /**
     * Returns the type of the object, e.g., "player", "enemy", "wall".
     * This is used to determine how collisions should be resolved.
     *
     * @return a string representing the object's type.
     */
    String getType();
}
