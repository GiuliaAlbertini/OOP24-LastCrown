package it.unibo.oop.lastcrown.model.user.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.CompleteCollection;
import it.unibo.oop.lastcrown.model.user.api.UserCollection;
import it.unibo.oop.lastcrown.model.user.api.UserCollectionListener;

/**
 * Implementation of a {@link UserCollection}.
 */
public class UserCollectionImpl implements UserCollection {

    private static final CompleteCollection COMPLETE_COLLECTION = new CompleteCollectionImpl();
    private static final Set<CardIdentifier> INITIAL_SET = computeInitialset();

    private final Set<CardIdentifier> userCollection;
    private final List<UserCollectionListener> listeners = new CopyOnWriteArrayList<>();

    /**
     * Constructor for a new {@code UserCollectionImpl}.
     * It initializes the set {@code userCollection} and add the initial set of {@link CardIdentifier} to it.
     */
    public UserCollectionImpl() {
        this.userCollection = new HashSet<>();
        this.userCollection.addAll(INITIAL_SET);
    }

    @Override
    public final Set<CardIdentifier> getCollection() {
        return Collections.unmodifiableSet(this.userCollection);
    }

    @Override
    public final void addCard(final CardIdentifier newCard) {
        this.userCollection.add(newCard);
    }

    @Override
    public final void addListener(final UserCollectionListener listener) {
        this.listeners.add(listener);
    }

    private static Set<CardIdentifier> computeInitialset() {
        return COMPLETE_COLLECTION.getZeroCostCards();
    }

}
