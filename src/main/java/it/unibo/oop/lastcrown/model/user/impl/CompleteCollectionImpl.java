package it.unibo.oop.lastcrown.model.user.impl;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.HeroesParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.PlayableCharactersParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.ReadOnlyFileHandlerImpl;
import it.unibo.oop.lastcrown.model.file_handling.impl.SpellsParser;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;

/**
 * Implementation of a {@link CompleteCollection}.
 */
public class CompleteCollectionImpl implements CompleteCollection {

    private static final String HEROES = "heroes";
    private static final String PLAYABLE_CHARACTER = "playableCharacters";
    private static final String SPELLS = "spells";
    private static final String PATH = getPath();

    private final Optional<Map<CardIdentifier, Hero>> heroes;
    private final Optional<Map<CardIdentifier, PlayableCharacter>> playableCharacters;
    private final Optional<Map<CardIdentifier, Spell>> spells;
    private final List<CardIdentifier> completeCollection;
    private final Map<CardIdentifier, Integer> costMap;

    /**
     * Constructor for an object of {@code CompleteCollectionImpl}.
     * It initializes the maps relative to heroes, plyable characters and spells,
     * then creates a List with all the {@link CardIdentifier} in the maps ordered
     * by their ID number.
     */
    public CompleteCollectionImpl() {
        final ReadOnlyFileHandler<Map<CardIdentifier, Hero>> heroesReader =
            new ReadOnlyFileHandlerImpl<>(new HeroesParser(), PATH);
        final ReadOnlyFileHandler<Map<CardIdentifier, PlayableCharacter>> pcReader =
            new ReadOnlyFileHandlerImpl<>(new PlayableCharactersParser(), PATH);
        final ReadOnlyFileHandler<Map<CardIdentifier, Spell>> spellsReader =
            new ReadOnlyFileHandlerImpl<>(new SpellsParser(), PATH);
        this.heroes = heroesReader.readFromFile(HEROES);
        this.playableCharacters = pcReader.readFromFile(PLAYABLE_CHARACTER);
        this.spells = spellsReader.readFromFile(SPELLS);
        this.completeCollection = createCompleteCollection();
        final Map<CardIdentifier, Integer> heroCosts = heroes
            .orElse(Map.of())
            .entrySet().stream()
            .collect(Collectors.toMap(
                Entry::getKey,
                e -> e.getValue().getRequirement().amount()
            ));

        final Map<CardIdentifier, Integer> pcCosts = playableCharacters
            .orElse(Map.of())
            .entrySet().stream()
            .collect(Collectors.toMap(
                Entry::getKey,
                e -> e.getValue().getCost()
            ));

        final Map<CardIdentifier, Integer> spellCosts = spells
            .orElse(Map.of())
            .entrySet().stream()
            .collect(Collectors.toMap(
                Entry::getKey,
                e -> e.getValue().getCost()
            ));

        this.costMap = Stream.of(heroCosts, pcCosts, spellCosts)
            .flatMap(map -> map.entrySet().stream())
            .collect(Collectors.toMap(
                Entry::getKey,
                Entry::getValue
            ));
    }

    @Override
    public final int getCost(final CardIdentifier id) {
        final Integer cost = this.costMap.get(id);
        if (cost == null) {
            throw new NoSuchElementException("Card not in complete collection: " + id);
        }
        return cost;
    }

    @Override
    public final CompleteCollection getCompleteCollection() {
        return this;
    }

    @Override
    public final Set<CardIdentifier> getZeroCostCards() {
        return Stream.of(this.heroes, this.playableCharacters, this.spells)
            .filter(Optional::isPresent)
            .flatMap(optMap -> optMap.get().entrySet().stream())
            .filter(entry -> {
                final Object card = entry.getValue();
                if (card instanceof Hero) {
                    return ((Hero) card).getRequirement().amount() == 0;
                } else if (card instanceof PlayableCharacter) {
                    return ((PlayableCharacter) card).getCost() == 0;
                } else if (card instanceof Spell) {
                    return ((Spell) card).getCost() == 0;
                }
                return false;
            })
            .map(Entry::getKey)
            .collect(Collectors.toSet());
    }

    @Override
    public final Optional<Hero> getHero(final CardIdentifier cardIdentifier) {
        return this.heroes.flatMap(map -> Optional.ofNullable(map.get(cardIdentifier)));
    }

    @Override
    public final Optional<PlayableCharacter> getPlayableCharacter(final CardIdentifier cardIdentifier) {
        return this.playableCharacters.flatMap(map -> Optional.ofNullable(map.get(cardIdentifier)));
    }

    @Override
    public final Optional<Spell> getSpell(final CardIdentifier cardIdentifier) {
        return this.spells.flatMap(map -> Optional.ofNullable(map.get(cardIdentifier)));
    }

    private List<CardIdentifier> sortedByNumber(final Set<CardIdentifier> identifiers) {
        return identifiers.stream()
                   .sorted(Comparator.comparingInt(CardIdentifier::number))
                   .collect(Collectors.toList());
    }

    private List<CardIdentifier> createCompleteCollection() {
        return Stream.of(
                    this.heroes.get().keySet(),
                    this.playableCharacters.get().keySet(),
                    this.spells.get().keySet()
               )
               .map(this::sortedByNumber)
               .flatMap(List::stream)
               .toList();
    }

    @Override
   public final List<CardIdentifier> getCompleteCollectionAsList() {
       return Collections.unmodifiableList(this.completeCollection);
    }

    private static String getPath() {
        return "src"
        + File.separator
        + "main"
        + File.separator
        + "resources"
        + File.separator
        + "entities";
    }
}
