package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.spell.api.SpellEffect;
import it.unibo.oop.lastcrown.model.spell.impl.SpellFactory;

/**
 * Implementation of a {@link Parser} for the {@link Spell} file.
 */
public class SpellsParser implements Parser<Map<CardIdentifier, Spell>> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELDS = 8;

    @Override
    public final Map<CardIdentifier, Spell> parse(final List<String> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("Input lines cannot be null");
        }
        final var nonEmpty = lines.stream()
            .map(String::trim)
            .filter(l -> !l.isEmpty())
            .toList();

        return nonEmpty.stream()
            .map(this::parseLine)
            .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Map.Entry<CardIdentifier, Spell> parseLine(final String line) {
        final String[] tokens = line.split(DELIMITER);
        if (tokens.length < EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                "Invalid record: expected " + EXPECTED_FIELDS + " fields but got " + tokens.length + " in '" + line + "'"
            );
        }
        final Iterator<String> it = Arrays.asList(tokens).iterator();

        final int id = parseInt(it.next(), "id", line);
        final String name = it.next().trim();
        final int cost = parseInt(it.next(), "cost", line);
        final int copiesPerRound = parseInt(it.next(), "copies per round", line);
        final int energyToPlay = parseInt(it.next(), "energy to play", line);

        final String category = it.next().trim();
        final int amount = parseInt(it.next(), "effect amount", line);
        final String targetName = it.next().trim();
        final CardType target = "friendly".equalsIgnoreCase(targetName)
                                    ? CardType.FRIENDLY
                                    : CardType.ENEMY;
        final SpellEffect effect = new SpellEffect(category, amount, target);

        final Spell spell = SpellFactory.createSpell(
            name,
            cost,
            copiesPerRound,
            energyToPlay,
            effect
        );
        return Map.entry(new CardIdentifier(id, CardType.SPELL), spell);
    }

    private int parseInt(final String token, final String fieldName, final String context) {
        try {
            return Integer.parseInt(token.trim());
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid integer for " + fieldName + " in '" + context + "': '" + token + "'", e
            );
        }
    }
}
