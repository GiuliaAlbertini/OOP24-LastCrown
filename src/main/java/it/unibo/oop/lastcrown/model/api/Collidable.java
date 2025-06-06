package it.unibo.oop.lastcrown.model.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Represents an entity that can be involved in collisions.
 * Each collidable object must expose its underlying character data,
 * which includes its position, hitbox, and other relevant properties.
 */
public interface Collidable {
    public Hitbox getHitbox();
    public CardIdentifier getCardidentifier();
}

