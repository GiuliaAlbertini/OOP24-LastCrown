package it.unibo.oop.lastcrown.model.spell.impl;

import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;

/**
 * It creates a new spell with the specified params.
 */
public final class SpellFactory {

    private SpellFactory() { }

    /**
     * @param name the name of this spell
     * @param cost the amount of coins to spend to own this spell
     * @param copiesPerRound the maximum number of copies of this spell that can be played in a single match
     * @param energyToPlay the player energy needed to play this card
     * @param spellEffect this spell special effect
     * @return a new Spell
     */
    public static Spell createSpell(final String name, final int cost, final int copiesPerRound,
    final int energyToPlay, final SpellEffect spellEffect) {

        return new SpellImpl(name, cost, copiesPerRound, energyToPlay, spellEffect);
    }
}
