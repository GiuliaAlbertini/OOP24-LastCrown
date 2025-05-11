package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PassiveEffect;
import it.unibo.oop.lastcrown.model.characters.api.Requirement;
import it.unibo.oop.lastcrown.model.characters.impl.hero.HeroFactory;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Implementation of a {@link Parser} for the {@link Hero} file.
 */
public class HeroesParser implements Parser<Map<CardIdentifier, Hero>> {
    private static final String DELIMITER = ",";
    private static final int EXPECTED_FIELDS = 11;
    private static final String NONE = "none";
    private static final String TYPE_HERO = "hero";

    @Override
    public final Map<CardIdentifier, Hero> parse(final List<String> lines) {
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

    private Map.Entry<CardIdentifier, Hero> parseLine(final String line) {
        final String[] tokens = line.split(DELIMITER);
        if (tokens.length < EXPECTED_FIELDS) {
            throw new IllegalArgumentException(
                "Invalid record: expected " + EXPECTED_FIELDS + " fields but got " + tokens.length + " in '" + line + "'"
            );
        }
        final Iterator<String> it = Arrays.asList(tokens).iterator();

        final int id = parseIntField(it.next(), "id", line);
        final String name = it.next().trim();
        final int attack = parseIntField(it.next(), "attack", line);
        final int health = parseIntField(it.next(), "health", line);
        final int reqAmount = parseIntField(it.next(), "requirement amount", line);
        final String reqType = it.next().trim();
        final Requirement requirement = new Requirement(reqType, reqAmount);

        final String passiveToken = it.next().trim();
        final Optional<PassiveEffect> passive = NONE.equalsIgnoreCase(passiveToken)
            ? Optional.empty()
            : parsePassive(passiveToken);

        final int meleeCards = parseIntField(it.next(), "melee cards", line);
        final int rangedCards = parseIntField(it.next(), "ranged cards", line);
        final int spellCards = parseIntField(it.next(), "spell cards", line);
        final int wallAttack = parseIntField(it.next(), "wall attack", line);
        final int wallHealth = parseIntField(it.next(), "wall health", line);

        final Hero hero = HeroFactory.createHero(
            name,
            requirement,
            attack,
            health,
            passive,
            meleeCards,
            rangedCards,
            spellCards,
            wallAttack,
            wallHealth
        );

        final CardIdentifier key = new CardIdentifier(id, TYPE_HERO);
        return Map.entry(key, hero);
    }

    private Optional<PassiveEffect> parsePassive(final String token) {
        final String[] parts = token.split(";");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid passive effect definition: '" + token + "'");
        }
        final String effectName = parts[0].trim();
        final int effectValue = parseIntField(parts[1], "passive effect value", token);
        return Optional.of(new PassiveEffect(effectName, effectValue));
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
}
