package it.unibo.oop.lastcrown.controller.shop.api;

import java.util.List;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * Interface that define the methods for a controller for selecting cards to show in the shop.
 */
public interface ShopCardsSelectionController {
    /**
     * Returns up to three random cards of the given type that the user does not yet own.
     * Supports heroes, spells, and friendly characters.
     *
     * @param type the requested CardType (HERO, SPELL, or grouping of MELEE/RANGED)
     * @return a list of up to three CardIdentifier
     */
    List<CardIdentifier> getRandomCardsByType(final CardType type);
}
