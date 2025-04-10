package it.unibo.oop.lastcrown.model.impl.card.spell;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.api.card.spell.Spell;
import it.unibo.oop.lastcrown.model.api.card.spell.SpellEffect;

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
     * @param spellActionRange this spell action range. If a character is included in this range and 
     * represents the correct target of the spell effect, he will be affected.
     * If the range is missing, the spell's effect will be applied to all characters
     * representing the correct target who are involved in the game at that moment.
     * @return a new Spell
     */
    public static Spell createSpell(final String name, final int cost, final int copiesPerRound,
    final int energyToPlay, final SpellEffect spellEffect, final Optional<Integer> spellActionRange) {

        return new SpellImpl(name, cost, copiesPerRound, energyToPlay, spellEffect, spellActionRange);
    }
}
