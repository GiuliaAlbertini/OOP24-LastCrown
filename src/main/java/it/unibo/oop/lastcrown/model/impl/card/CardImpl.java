package it.unibo.oop.lastcrown.model.impl.card;

import it.unibo.oop.lastcrown.model.api.card.Card;
import it.unibo.oop.lastcrown.model.api.card.Requirement;

/**
 * the implementation of Card interface.
 */
public class CardImpl implements Card {
    private final String name;
    private final Requirement requirement;

    /**
     * @param name the name of this specific card
     * @param requirement the requirement of this specific card
     */
    public CardImpl(final String name, final Requirement requirement) {
        this.name = name;
        this.requirement = requirement;
    }

    @Override
    public final String getName() {
       return this.name;
    }

    @Override
    public final Requirement getRequirement() {
        return this.requirement;
    }
}
