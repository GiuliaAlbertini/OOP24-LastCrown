package it.unibo.oop.lastcrown.model.collision.impl;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
@SuppressFBWarnings(value = {"EI"}, justification = """
        Event handlers in this project require a direct reference to the original, modifiable Hitbox instance to track real-time state changes.
        This is because characters don't have a direct reference to their hitbox.""")

public final class CollidableImpl implements Collidable {
    private final Hitbox hitbox;
    private final CardIdentifier cardIdentifier;

    /**
     * Creates a new CollidableImpl with the given hitbox and identifier.
     *
     * @param hitbox         the hitbox representing the object's bounds
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
    public CardIdentifier getCardIdentifier() {
        return this.cardIdentifier;
    }
}
