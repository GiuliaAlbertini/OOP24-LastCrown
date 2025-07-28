package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyFactory;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Implementation of a {@link Parser} for the {@link Enemy} file.
 */
public class EnemiesParser implements Parser<List<List<Enemy>>> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELDS = 5;
    private static final int MIN_RANK = 1;
    private static final int MAX_RANK = 4;

    @Override
    public final List<List<Enemy>> parse(final List<String> lines) {
        if (lines == null) {
            throw new IllegalArgumentException("Input lines cannot be null");
        }

        final var nonEmpty = lines.stream()
            .map(String::trim)
            .filter(l -> !l.isEmpty())
            .toList();

        final Map<Integer, List<Enemy>> grouped = nonEmpty.stream()
            .map(this::parseLine)
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toUnmodifiableList())
            ));

        return IntStream.rangeClosed(MIN_RANK, MAX_RANK)
            .mapToObj(rank -> grouped.getOrDefault(rank, Collections.emptyList()))
            .collect(Collectors.toUnmodifiableList());
    }

    private Map.Entry<Integer, Enemy> parseLine(final String line) {
        final String[] tokens = line.split(DELIMITER);
        if (tokens.length < EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                "Invalid record: expected " + EXPECTED_FIELDS + " fields but got " + tokens.length + " in '" + line + "'"
            );
        }

        final Iterator<String> it = Arrays.asList(tokens).iterator();

        final int rank = parseIntField(it.next(), "rank", line);
        validateRank(rank);
        final String name = it.next().trim();
        final int attack = parseIntField(it.next(), "attack", line);
        final int health = parseIntField(it.next(), "health", line);
        final double speed = parseDoubleField(it.next(), "speed", line);

        final CardType type = rank == MAX_RANK
            ? CardType.ENEMY
            : CardType.BOSS;
        final Enemy enemy = EnemyFactory.createEnemy(name, rank, type, attack, health, speed);
        return Map.entry(rank, enemy);
    }

    private int parseIntField(final String token, final String fieldName, final String context) {
        try {
            return Integer.parseInt(token.trim());
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid integer for " + fieldName + " in '" + context + "': '" + token + "'", e
            );
        }
    }

    private double parseDoubleField(final String token, final String fieldName, final String context) {
        try {
            return Double.parseDouble(token.trim());
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                "Invalid double for " + fieldName + " in '" + context + "': '" + token + "'", e
            );
        }
    }

    private void validateRank(final int rank) {
        if (rank < MIN_RANK || rank > MAX_RANK) {
            throw new IllegalArgumentException(
                "Rank must be between " + MIN_RANK + " and " + MAX_RANK + ", got " + rank
            );
        }
    }
}
