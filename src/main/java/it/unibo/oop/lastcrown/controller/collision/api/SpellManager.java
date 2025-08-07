package it.unibo.oop.lastcrown.controller.collision.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/** Spell manager interface. */
public interface SpellManager {

    /**
     * Handles the selection of a spell from the user interface.
     * This method prepares the system to cast the selected spell.
     *
     * @param id The {@link CardIdentifier} of the selected spell.
     */
    void handleSpellSelection(CardIdentifier id);

    /**
     * Handles a user click, casting the prepared spell at the specified
     * coordinates.
     *
     * @param x The x-coordinate of the cast location.
     * @param y The y-coordinate of the cast location.
     */
    void castSpell(int x, int y);

    /**
     * Updates the state of active spells, checking for animations and applying
     * effects.
     *
     * @param deltaTime The time elapsed since the last update in milliseconds.
     */
    void update(int deltaTime);
}
