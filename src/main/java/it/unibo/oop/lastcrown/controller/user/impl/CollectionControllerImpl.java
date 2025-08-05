package it.unibo.oop.lastcrown.controller.user.impl;


import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.file_handling.api.ReadOnlyFileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.EnemiesParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.ReadOnlyFileHandlerImpl;
import it.unibo.oop.lastcrown.model.spell.api.Spell;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.impl.CompleteCollectionImpl;

/**
 * Implementation of a {@link CollectionController}.
 */
public class CollectionControllerImpl implements CollectionController {
    private static final String ENEMIES = "enemies";
    private static final String ENEMIES_PATH = getPath();

    private static final Set<CardIdentifier> INITIAL_SET = new HashSet<>();
    private static final CompleteCollectionImpl COMPLETE_COLLECTION = new CompleteCollectionImpl();

    private final List<List<Enemy>> enemies;

    /**
     * Constucts a new {@code CollectionControllerImpl}.
     * It initializes the reader to use for the enemies
     */
    public CollectionControllerImpl() {
        final ReadOnlyFileHandler<List<List<Enemy>>> enemyReader =
            new ReadOnlyFileHandlerImpl<>(new EnemiesParser(), ENEMIES_PATH);
        this.enemies = enemyReader.readFromFile(ENEMIES).get();
    }

    @Override
    public final List<List<Enemy>> getEnemies() {
        return Collections.unmodifiableList(this.enemies);
    }

    @Override
    public final CompleteCollection getCompleteCollection() {
        return COMPLETE_COLLECTION.getCompleteCollection();
    }

    @Override
    public final Set<CardIdentifier> getInitialSet() {
        return Collections.unmodifiableSet(INITIAL_SET);
    }

    @Override
    public final List<CardIdentifier> getCollectionByType(final CardType type) {
        return getCompleteCollection().getCompleteCollectionAsList().stream()
            .filter(card -> card.type().equals(type))
            .collect(Collectors.toList());
    }

    @Override
    public final Optional<String> getCardName(final CardIdentifier card) {
        switch (card.type()) {
            case CardType.HERO:
                return COMPLETE_COLLECTION.getHero(card)
                    .map(Hero::getName);
            case CardType.MELEE:
            case CardType.RANGED:
                return COMPLETE_COLLECTION.getPlayableCharacter(card)
                    .map(PlayableCharacter::getName);
            case CardType.SPELL:
                return COMPLETE_COLLECTION.getSpell(card)
                    .map(Spell::getName);
            default:
                return Optional.empty();
        }
    }

    private static String getPath() {
        return "src"
            + File.separator + "main"
            + File.separator + "resources"
            + File.separator + "entities";
    }
}
