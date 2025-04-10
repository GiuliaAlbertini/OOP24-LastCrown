package it.unibo.oop.lastcrown.model.api.card.spell;

import java.util.Optional;

import it.unibo.oop.lastcrown.model.api.card.PlayableCard;

/**
 * A spell that can be played during the match. It affects the different characters involved 
 * by applying a specific effect.
 */
public interface Spell extends PlayableCard {
    /**
     * @return the name of this spell.
     */
    String getName();

    /**
     * @return this spell effect (ex health, 30, enemies, 10 -> deal 30 health damage to enemies.
     * The effect ends in 10 seconds)
     */
    SpellEffect getSpellEffect();

    /**
     * @return if it's present, this spell action range
     */
    Optional<Integer> getActionRange();
}
