package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.collision.api.Collidable;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import it.unibo.oop.lastcrown.view.characters.api.Movement;


/**
 * Represents the result of a movement operation for a character.
 *
 * @param character   the collidable character involved in the movement
 * @param newPosition the new position of the character after movement
 * @param delta       the time delta of the game loop for this movement
 * @param active      flag indicating whether the movement is currently active
 */
public record MovementResult(Collidable character, Point2D newPosition, Movement delta, boolean active) { }
