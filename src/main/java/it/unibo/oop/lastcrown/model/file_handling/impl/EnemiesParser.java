package it.unibo.oop.lastcrown.model.file_handling.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.impl.enemy.EnemyFactory;
import it.unibo.oop.lastcrown.model.file_handling.api.Parser;

/**
 * Implementation of a {@link Parser} for the {@link Enemy} file.
 */
public class EnemiesParser implements Parser<List<List<Enemy>>> {
    private static final String REGEX = "\\s*,\\s*";
    private static final int MAX_RANK = 4;
    private static final int MIN_RANK = 1;
    private static final String TYPE_ENEMY = "enemy";
    private static final String TYPE_BOSS = "boss";

    /**
     * Parses {@link Enemy} from each line of {@code lines}.
     * Returns a list of 4 lists, where each sublist contains enemies of a specific rank (1-4).
     * Assumes that every line is in the correct format.
     * 
     * @param lines the lines to parse
     * @return a list of lists, each containing the enemies of a specific rank
     */
    @Override
    public List<List<Enemy>> parse(final List<String> lines) {
        final Map<Integer, List<Enemy>> grouped = lines.stream()
            .map(String::trim)
            .filter(l -> !l.isEmpty())
            .map(line -> {
                final String[] tokens = line.split(REGEX);
                final int rank = Integer.parseInt(tokens[0]);
                final String name = tokens[1];
                final int attack = Integer.parseInt(tokens[2]);
                final int health = Integer.parseInt(tokens[3]);
                final double speed = Double.parseDouble(tokens[4]);
                final Enemy enemy = EnemyFactory.createEnemy(
                    name,
                    rank,
                    rank == 4 ? TYPE_BOSS : TYPE_ENEMY,
                    attack,
                    health,
                    speed
                );
                return Map.entry(rank, enemy);
            })
            .collect(Collectors.groupingBy(
                Map.Entry::getKey,
                Collectors.mapping(Map.Entry::getValue, Collectors.toUnmodifiableList())
            ));

        return IntStream.rangeClosed(MIN_RANK, MAX_RANK)
                .mapToObj(rank -> grouped.getOrDefault(rank, Collections.emptyList()))
                .collect(Collectors.toUnmodifiableList());
    }
}
