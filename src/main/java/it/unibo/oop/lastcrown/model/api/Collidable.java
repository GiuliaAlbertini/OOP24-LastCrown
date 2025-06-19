package it.unibo.oop.lastcrown.model.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Represents an entity that can be involved in collisions.
 * Each collidable object must expose its underlying character data,
 * which includes its position, hitbox, and other relevant properties.
 */
public interface Collidable {

    /**
     * Returns the hitbox associated with this collidable entity.
     *
     * @return the hitbox of the entity
     */
    Hitbox getHitbox();

    /**
     * Returns the identifier of the card associated with this entity.
     *
     * @return the card identifier
     */
    CardIdentifier getCardidentifier();
}
