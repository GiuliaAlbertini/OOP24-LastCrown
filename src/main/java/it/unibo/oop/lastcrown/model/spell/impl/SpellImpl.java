package it.unibo.oop.lastcrown.model.spell.impl;

import it.unibo.oop.lastcrown.model.card.PlayableCardImpl;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;

/**
 * A standard implementation of Spell interface.
 */
public class SpellImpl extends PlayableCardImpl implements Spell {
    private final String name;
    private final SpellEffect spellEffect;

    /**
     * @param name the name of this spell
     * @param cost the amount of coins to spend to own this spell
     * @param copiesPerRound the maximum number of copies of this spell that can be played in a single match
     * @param energyToPlay the player energy needed to play this card
     * @param spellEffect this spell special effect
     */
    public SpellImpl(final String name, final int cost, final int copiesPerRound,
    final int energyToPlay, final SpellEffect spellEffect) {
        super(cost, copiesPerRound, energyToPlay);
        this.name = name;
        this.spellEffect = spellEffect;
    }

    @Override
    public final String getName() {
        return this.name;
    }

    @Override
    public final SpellEffect getSpellEffect() {
        return this.spellEffect;
    }
}
