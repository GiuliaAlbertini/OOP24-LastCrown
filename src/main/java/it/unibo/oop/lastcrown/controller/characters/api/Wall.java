package it.unibo.oop.lastcrown.controller.characters.api;

import java.util.List;

/**
 * A defensive Wall that protects the Hero. When the Wall runs out its health, the bossfight will start.
 */
public interface Wall extends CharacterHitObserver {
    /**
     * @return the current attack value
     */
    int getAttack();

    /**
     * @return the current health value
     */
    int getCurrentHealth();

    /**
     * Full the health of the defensive wall.
     */
    void fullWallHealth();

    /**
     * add an Opponent to the defensive wall.
     * @param opponent the opponent observer
     */
    void addOpponent(CharacterHitObserver opponent);

    /**
     * Add a set of opponents to this defensive wall.
     * @param opponents the list of opponents to be added
     */
    void addOpponents(List<CharacterHitObserver> opponents);

    /**
     * Deal an attack to all the opponents of the defensive wall.
     */
    void doAttack();

    /**
     * Remove one opponent specified by the given id from this defensive wall.
     * It does nothing if the id given does not correspond to any opponent.
     * @param id the id of the opponent to be removed
     */
    void removeOpponent(int id);
}

