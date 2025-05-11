package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterFactory;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Implementation of a {@link Parser} for the {@link PlayableCharacter} file.
 */
public class PlayableCharactersParser implements Parser<Map<CardIdentifier, PlayableCharacter>> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELDS = 8;

    @Override
    public final Map<CardIdentifier, PlayableCharacter> parse(final List<String> lines) {
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

    private Map.Entry<CardIdentifier, PlayableCharacter> parseLine(final String line) {
        final String[] tokens = line.split(DELIMITER);
        if (tokens.length < EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                "Invalid record: expected " + EXPECTED_FIELDS + " fields but got " + tokens.length + " in '" + line + "'"
            );
        }
        final Iterator<String> it = Arrays.asList(tokens).iterator();

        final int id = parseIntField(it.next(), "id", line);
        final String name = it.next().trim();
        final int coinCost = parseIntField(it.next(), "coin cost", line);
        final int copiesPerRound = parseIntField(it.next(), "copies per round", line);
        final String type = it.next().trim();
        final int attackValue = parseIntField(it.next(), "attack value", line);
        final int actionRange = parseIntField(it.next(), "action range", line);
        final int healthValue = parseIntField(it.next(), "health value", line);
        final int energyToPlay = parseIntField(it.next(), "energy to play", line);
        final double speedMultiplier = parseDoubleField(it.next(), "speed multiplier", line);

        final PlayableCharacter pc = PlayableCharacterFactory.createPlayableCharacter(
            name,
            type,
            coinCost,
            attackValue,
            healthValue,
            copiesPerRound,
            energyToPlay,
            speedMultiplier,
            actionRange
        );
        return Map.entry(new CardIdentifier(id, type), pc);
    }

    private int parseIntField(final String token, final String fieldName, final String context) {
        try {
            return Integer.parseInt(token.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid integer for " + fieldName + " in '" + context + "': '" + token + "'", e
            );
        }
    }

    private double parseDoubleField(final String token, final String fieldName, final String context) {
        try {
            return Double.parseDouble(token.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid double for " + fieldName + " in '" + context + "': '" + token + "'", e
            );
        }
    }
}