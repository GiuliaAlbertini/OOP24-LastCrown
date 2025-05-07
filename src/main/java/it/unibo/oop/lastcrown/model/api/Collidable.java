package it.unibo.oop.lastcrown.model.api;

import it.unibo.oop.lastcrown.model.characters.api.InGameCharacter;

/**
 * Represents an entity that can be involved in collisions.
 * Each collidable object must expose its underlying character data,
 * which includes its position, hitbox, and other relevant properties.
 */
public interface Collidable {

    /**
     * Returns the underlying data of the in-game character associated
     * with this collidable object. This data includes the character's
     * hitbox, position, and potentially other attributes useful for
     * collision resolution and game logic.
     *
     * @return InGameCharacter associated with this collidable entity
     */
    InGameCharacter getData();
}

