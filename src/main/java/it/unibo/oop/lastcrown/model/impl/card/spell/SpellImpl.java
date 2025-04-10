package it.unibo.oop.lastcrown.model.impl.card.spell;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.api.card.spell.Spell;
import it.unibo.oop.lastcrown.model.api.card.spell.SpellEffect;
import it.unibo.oop.lastcrown.model.impl.card.PlayableCardImpl;

/**
 * A standard implementation of Spell interface.
 */
public class SpellImpl extends PlayableCardImpl implements Spell {
    private final String name;
    private final SpellEffect spellEffect;
    private final Optional<Integer> spellActionRange;

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
     */
    public SpellImpl(final String name, final int cost, final int copiesPerRound,
    final int energyToPlay, final SpellEffect spellEffect, final Optional<Integer> spellActionRange) {
        super(cost, copiesPerRound, energyToPlay);
        this.name = name;
        this.spellEffect = spellEffect;
        this.spellActionRange = spellActionRange;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final SpellEffect getSpellEffect() {
        return this.spellEffect;
    }

    @Override
    public final Optional<Integer> getActionRange() {
        return this.spellActionRange;
    }
}
