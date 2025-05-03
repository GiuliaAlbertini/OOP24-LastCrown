package it.unibo.oop.lastcrown.controller.characters.api;

import java.util.Set;

/**
 * A controller that handles the behaviour of a single Boss in game.
 */
public interface BossController extends GenericCharacterController {

    /**
     * Add a set of opponents to this boss.
     * @param opponents the set of opponents to be add
     */
    void setOpponents(Set<CharacterHitObserver> opponents);

    /**
     * Remove one opponent specified by the given id from this boss.
     * It does nothing if the id given does not correspond to any opponent.
     * @param id the id of the opponent to be removed
     */
    void removeOpponent(int id);
}
