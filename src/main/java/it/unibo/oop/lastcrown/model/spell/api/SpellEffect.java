package it.unibo.oop.lastcrown.model.spell.api;

import java.util.Optional;

/**
 * A specific effect applied by a spell that changes characters statistic during the match.
 * @param category the category of character statistic to which the effect is aimed (health, attack...).
 * @param amount the amount of change to a specific character statistic caused by the effect (+200 health, +3 attack).
 * @param target the target of the effect (friendly characters or enemies).
 * @param duration the duration of the effect expressed in seconds. If it is missing,
 * the effect is activated as soon as the spell is cast.
 */
public record SpellEffect(String category, int amount, String target, Optional<Integer> duration) {
}
