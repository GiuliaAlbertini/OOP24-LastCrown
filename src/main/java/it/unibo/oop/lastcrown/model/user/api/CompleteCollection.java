package it.unibo.oop.lastcrown.model.user.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.model.spell.api.Spell;

/**
 * Provides method to work  with the entire collection of cards available in the game.
 */
public interface CompleteCollection {
    /**
     * Provides all the {@link CardIdentifier} in the collection.
     * 
     * @return a list with all the {@link CardIdentifier}
     */
    List<CardIdentifier> getCompleteCollection();

    /**
     * Provides the {@link CardIdentifier} of all the cards that have a cost equals to zero.
     * 
     * @return a set with all the requested {@link CardIdentifier}
     */
    Set<CardIdentifier> getZeroCostCards();

    /**
     * Provides a {@link Hero} relative to a given {@link CardIdentifier}.
     * 
     * @param cardIdentifier the {@link CardIdentifier} to use
     * @return an {@link Optional} of the requested {@link Hero} if it is found, an empty Optional otherwise
     */
    Optional<Hero> getHero(CardIdentifier cardIdentifier);

    /**
     * Provides a {@link PlayableCharacter} relative to a given {@link CardIdentifier}.
     * 
     * @param cardIdentifier the {@link CardIdentifier} to use
     * @return an {@link Optional} of the requested {@link PlayableCharacter} if it is found, an empty Optional otherwise
     */
    Optional<PlayableCharacter> getPlayableCharacter(CardIdentifier cardIdentifier);

    /**
     * Provides a {@link Spell} relative to a given {@link CardIdentifier}.
     * 
     * @param cardIdentifier the {@link CardIdentifier} to use
     * @return an {@link Optional} of the requested {@link Spell} if it is found, an empty Optional otherwise
     */
    Optional<Spell> getSpell(CardIdentifier cardIdentifier);

    /**
     * Return the cost of a card.
     * @param id the card to check
     * @return the cost
     */
    int getCost(CardIdentifier id);
}
