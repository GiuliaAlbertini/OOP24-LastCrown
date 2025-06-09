package it.unibo.oop.lastcrown.controller.impl;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.api.HitboxController;

public record SpawnedCharacter(GenericCharacterController controller, HitboxController hitboxController) {}
