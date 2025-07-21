package it.unibo.oop.lastcrown.controller.characters.impl.wall;

import it.unibo.oop.lastcrown.controller.characters.api.Wall;

/**
 * Creates a Wall with the specified parameters.
 */
public final class WallFactory {
    private WallFactory() { }

    /**
     * @param attack the attack value of the new Wall
     * @param health the health value of the new Wall
     * @param id the id of the new Wall
     * @param healthWidth the width of the health bar
     * @param healthHeight the height of the health bar
     * @return a new Wall
     */
    public static Wall createWall(final int attack, final int health, final int id,
     final int healthWidth, final int healthHeight) {
        return new WallImpl(attack, health, id, healthWidth, healthHeight);
    }
}
