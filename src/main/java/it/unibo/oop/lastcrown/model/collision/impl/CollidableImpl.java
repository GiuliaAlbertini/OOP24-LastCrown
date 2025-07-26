package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;

/**
 * Basic implementation of the Collidable interface.
 *
 * Represents any generic game object that can have a Hitbox,
 * meaning any entity that can participate in collision detection.
 * Each Collidable is identified by a CardIdentifier.
 */
public final class CollidableImpl implements Collidable {
    private Hitbox hitbox;
    private CardIdentifier cardIdentifier;

    /**
     * Creates a new CollidableImpl with the given hitbox and identifier.
     *
     * @param hitbox the hitbox representing the object's bounds
     * @param cardIdentifier the unique identifier of the object
     */
    public CollidableImpl(final Hitbox hitbox, final CardIdentifier cardIdentifier) {
        this.hitbox = hitbox;
        this.cardIdentifier = cardIdentifier;
    }

    @Override
    public Hitbox getHitbox() {
        return this.hitbox;
    }

    @Override
    public CardIdentifier getCardidentifier() {
        return this.cardIdentifier;
    }
}
