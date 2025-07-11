package it.unibo.oop.lastcrown.model.user.api;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Listener for a {@link UserCollection}.
 */
public interface UserCollectionListener {
    /**
     * Defines what to do when a {@link CardIdentifier} is added.
     * 
     * @param card the card added
     */
    void onCardAdded(CardIdentifier card);
}
