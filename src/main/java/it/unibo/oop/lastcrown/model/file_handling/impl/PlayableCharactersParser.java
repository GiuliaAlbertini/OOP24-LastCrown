package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Implementation of a {@link Parser} for the {@link PlayableCharacter} file.
 */
public class PlayableCharactersParser implements Parser<Map<CardIdentifier, PlayableCharacter>> {

    private static final String REGEX = "\\s*,\\s*";

    /**
     * Parses each string of {@code lines} into an {@link Entry} <{@link CardIdentifier}, {@link PlayableCharacter}>.
     * It returns a {@link Map} of those entries.
     * Assumes that every line is in the correct format.
     * 
     * @param lines the lines to parse
     * @return a map of the entries produced
     */
    @Override
    public Map<CardIdentifier, PlayableCharacter> parse(final List<String> lines) {
        return lines.stream()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .map(this::createPlayableCharacterEntry)
                    .collect(Collectors.toUnmodifiableMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private Entry<CardIdentifier, PlayableCharacterImpl> createPlayableCharacterEntry(final String line) {
        final String[] tokens = line.split(REGEX);
        int index = 0;
        final int id = Integer.parseInt(tokens[index]);
        index++;
        final String name = tokens[index];
        index++;
        final int coinCost = Integer.parseInt(tokens[index]);
        index++;
        final int copiesPerRound = Integer.parseInt(tokens[index]);
        index++;
        final String type = tokens[index];
        index++;
        final int attackValue = Integer.parseInt(tokens[index]);
        index++;
        final int actionRange = Integer.parseInt(tokens[index]);
        index++;
        final int healthValue = Integer.parseInt(tokens[index]);
        index++;
        final int energyToPlay = Integer.parseInt(tokens[index]);
        index++;
        final double speedMultiplier = Double.parseDouble(tokens[index]);

        final PlayableCharacterImpl pc = new PlayableCharacterImpl(
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
}
