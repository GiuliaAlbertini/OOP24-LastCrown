package it.unibo.oop.lastcrown.model.user.impl;

import java.io.File;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.characters.impl.playablecharacter.PlayableCharacterImpl;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.HeroesParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.PlayableCharactersParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.ReadOnlyFileHandlerImpl;
import it.unibo.oop.lastcrown.model.file_handling.impl.SpellsParser;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;

public class CompleteCollectionImpl implements CompleteCollection {

    private static final String HEROES = "heroes";
    private static final String PLAYABLE_CHARACTER = "playableCharacters";
    private static final String SPELLS = "spells";
    private static final String HEROES_PATH = getPath(HEROES);
    private static final String PC_PATH = getPath(PLAYABLE_CHARACTER);
    private static final String SPELLS_PATH = getPath(SPELLS);

    private final Optional<Map<CardIdentifier, Hero>> heroes;
    private final Optional<Map<CardIdentifier, PlayableCharacter>> playableCharacters;
    private final Optional<Map<CardIdentifier, Spell>> spells;    
    private final List<CardIdentifier> completeCollection;

    public CompleteCollectionImpl() {
        final ReadOnlyFileHandler<Map<CardIdentifier,Hero>> heroesReader =
            new ReadOnlyFileHandlerImpl<>(new HeroesParser(), HEROES_PATH);
        final ReadOnlyFileHandler<Map<CardIdentifier, PlayableCharacter>> pcReader =
            new ReadOnlyFileHandlerImpl<>(new PlayableCharactersParser(), PC_PATH);
        final ReadOnlyFileHandler<Map<CardIdentifier, Spell>> spellsReader =
            new ReadOnlyFileHandlerImpl<>(new SpellsParser(), SPELLS_PATH);
        this.heroes = heroesReader.readFromFile(HEROES);
        this.playableCharacters = pcReader.readFromFile(PLAYABLE_CHARACTER);
        this.spells = spellsReader.readFromFile(SPELLS);
        this.completeCollection = createCompleteCollection();        
    }

    @Override
    public List<CardIdentifier> getCompleteCollection() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCompleteCollection'");
    }

    @Override
    public Set<CardIdentifier> getZeroCostCards() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getZeroCostCards'");
    }

    @Override
    public Optional<Hero> getHero(final CardIdentifier cardIdentifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHero'");
    }

    @Override
    public Optional<PlayableCharacterImpl> getPlayableCharacter(final CardIdentifier cardIdentifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPlayableCharacter'");
    }

    @Override
    public Optional<Spell> getSpell(final CardIdentifier cardIdentifier) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSpell'");
    }

    private List<CardIdentifier> sortedByNumber(final Set<CardIdentifier> keys) {
        return keys.stream()
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

    private static String getPath(final String fileName) {
        return "OOP24-LastCrown"
        + File.separator
        + "src"
        + File.separator
        + "main"
        + File.separator
        + "resources"
        + File.separator
        + "entities";
    }

}
