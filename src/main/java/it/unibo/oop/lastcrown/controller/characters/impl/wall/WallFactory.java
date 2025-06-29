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
     * @return a new Wall
     */
    public static Wall createWall(final int attack, final int health, final int id) {
        return new WallImpl(attack, health, id);
    }
}
