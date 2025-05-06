package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;
import it.unibo.oop.lastcrown.model.spell.impl.SpellFactory;

/**
 * Implementation of a {@link Parser} for the {@link Spell} file.
 */
public class SpellsParser implements Parser<Map<CardIdentifier, Spell>> {

    private static final String REGEX = "\\s*,\\s*";
    private static final String NONE  = "none";
    private static final String TYPE_SPELL = "spell";

    @Override
    public final Map<CardIdentifier, Spell> parse(final List<String> lines) {
        return lines.stream()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .map(this::createSpellEntry)
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Entry<CardIdentifier, Spell> createSpellEntry(final String line) {
        final String[] tokens = line.split(REGEX);
        int index = 0;
        final int id = Integer.parseInt(tokens[index]);
        index++;
        final String name = tokens[index];
        index++;
        final int cost = Integer.parseInt(tokens[index]);
        index++;
        final int copiesPerRound = Integer.parseInt(tokens[index]);
        index++;
        final int energyToPlay = Integer.parseInt(tokens[index]);
        index++;
        final String effectCategory = tokens[index];
        index++;
        final int effectAmount = Integer.parseInt(tokens[index]);
        index++;
        final String effectTarget = tokens[index];
        index++;
        final Optional<Integer> duration = NONE.equalsIgnoreCase(tokens[index])
            ? Optional.empty()
            : Optional.of(Integer.parseInt(tokens[index]));
        final SpellEffect effect = new SpellEffect(
            effectCategory,
            effectAmount,
            effectTarget,
            duration
        );
        index++;
        final Optional<Integer> actionRange = NONE.equalsIgnoreCase(tokens[index])
            ? Optional.empty()
            : Optional.of(Integer.parseInt(tokens[index]));
        final Spell spell = SpellFactory.createSpell(
            name,
            cost,
            copiesPerRound,
            energyToPlay,
            effect,
            actionRange
        );
        return Map.entry(new CardIdentifier(id, TYPE_SPELL), spell);
    }
}
