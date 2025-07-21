package it.unibo.oop.lastcrown.controller.collision.impl;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;

/**
 * Represents a character that has been spawned in the game, along with its controller and hitbox.
 *
 * @param controller the controller responsible for the character's behavior
 * @param hitboxController the controller managing the character's hitbox and its graphical representation
 */
public record SpawnedCharacter(GenericCharacterController controller, HitboxController hitboxController) { }
