package it.unibo.oop.lastcrown.controller.collision.impl;

/**
 * A simple record that represents an engagement between a player and an enemy.
 * This is used to associate a specific enemy (identified by enemyId)
 * with the player playerId who is currently engaged in combat with them.
 *
 * @param enemyId  the unique identifier of the engaged enemy
 * @param playerId the unique identifier of the engaging player
 */
public record EnemyEngagement(int enemyId, int playerId) { }
