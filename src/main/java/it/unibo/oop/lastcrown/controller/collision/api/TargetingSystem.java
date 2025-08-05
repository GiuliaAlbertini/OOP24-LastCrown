package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;

public interface TargetingSystem {

    Optional<CollisionEvent> scanForWallCollision(GenericCharacterController enemy);

    Optional<CollisionEvent> scanForTarget(GenericCharacterController scanner);

}