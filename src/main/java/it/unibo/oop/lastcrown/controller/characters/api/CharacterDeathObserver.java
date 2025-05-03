package it.unibo.oop.lastcrown.controller.characters.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * A simple observer that notifies the main controller about the death of a character.
 */
public interface CharacterDeathObserver {
    /**
     * Warns the main controller about the death of a character.
     * @param id the Identifier of a specific Character Controller
     */
    void notifyDeath(CardIdentifier id);
}