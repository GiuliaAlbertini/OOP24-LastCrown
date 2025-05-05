package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

    private static final String REGEX = "\\s*,\\s*";
    private static final String NONE  = "none";
    private static final String TYPE_HERO = "hero";

    /**
     * Parses each string of {@code lines} into an {@link Entry} <{@link CardIdentifier}, {@Hero}>.
     * It returns a {@link Map} of those entries.
     * Assumes that every line is in the correct format.
     * 
     * @param lines the lines to parse
     * @return a map of the entries produced
     */
    @Override
    public Map<CardIdentifier, Hero> parse(final List<String> lines) {
        return lines.stream()
            .map(String::trim)
            .filter(line -> !line.isEmpty())
            .map(this::createHeroEntry)
            .collect(Collectors.toUnmodifiableMap(
                Map.Entry::getKey,
                Map.Entry::getValue
            ));
    }

    private Entry<CardIdentifier, Hero> createHeroEntry(final String line) {
        final String[] tokens = line.split(REGEX);
        int index = 0;
        final int id = Integer.parseInt(tokens[index]);
        index++;
        final String name = tokens[index];
        index++;
        final int attackValue = Integer.parseInt(tokens[index]);
        index++;
        final int healthValue = Integer.parseInt(tokens[index]);
        index++;
        final int reqAmount = Integer.parseInt(tokens[index]);
        index++;
        final String reqType = tokens[index];
        index++;
        final Requirement requirement = new Requirement(reqType, reqAmount);
        final Optional<PassiveEffect> passive = NONE.equalsIgnoreCase(tokens[index])
            ? Optional.empty()
            : Optional.of(new PassiveEffect(tokens[++index], Integer.parseInt(tokens[--index])));
        index = passive.isEmpty() ? index + 1 : index + 2;
        final int meleeCards = Integer.parseInt(tokens[index]);
        index++;
        final int rangedCards = Integer.parseInt(tokens[index]);
        index++;
        final int spellCards = Integer.parseInt(tokens[index]);
        index++;
        final int wallAttack = Integer.parseInt(tokens[index]);
        index++;
        final int wallHealth = Integer.parseInt(tokens[index]);

        final Hero hero = HeroFactory.createHero(
            name,
            requirement,
            attackValue,
            healthValue,
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
}
