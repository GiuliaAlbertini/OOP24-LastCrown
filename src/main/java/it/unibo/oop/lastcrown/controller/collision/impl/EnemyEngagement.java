package it.unibo.oop.lastcrown.controller.collision.impl;

/**
 * Data carrier class for a player-enemy engagement couple.
 * @param enemyId the enemy's in-game identifier.
 * @param playerId the player's in-game identifier.
 */
public record EnemyEngagement(int enemyId, int playerId) { }
