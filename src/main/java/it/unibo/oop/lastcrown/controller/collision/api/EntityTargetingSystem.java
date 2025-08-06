package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;

/**
 * Represents the targeting and collision detection system in the game.
 * Responsible for scanning for potential collisions with walls or other targets
 * (like enemies or bosses) based on the position and range of the scanning entity.
 */
public interface EntityTargetingSystem {

    /**
     * Scans if a given enemy has collided with the wall.
     *
     * @param enemy the enemy character to scan
     * @return an Optional containing the collision event if detected
     */
    Optional<CollisionEvent> scanForWallCollision(GenericCharacterController enemy);

    /**
     * Scans for potential targets within the radius of the scanner.
     * Targets may be enemies, bosses, or other entities depending on the scanner type.
     *
     * @param scanner the entity performing the scan
     * @return an Optional containing the collision event if a target is found
     */
    Optional<CollisionEvent> scanForTarget(GenericCharacterController scanner);
}
