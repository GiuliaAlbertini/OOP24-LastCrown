package it.unibo.oop.lastcrown.controller.shop.impl;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.controller.shop.api.ShopCardsSelectionController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * Controller for selecting cards to show in the shop.
 */
public class ShopCardsSelectionControllerImpl implements ShopCardsSelectionController {
    private static final int MAX_CARDS_TO_SEND = 4;
    private final CollectionController collectionController;
<<<<<<< HEAD
    private final Set<CardIdentifier> userCollection;
=======
    private final List<CardIdentifier> userCollection;
>>>>>>> 72c5403f05b8c33d342c62dbbd63fe4016ae48a2
    private final Random random = new Random();

    /**
     * Constructs the shop selection controller with required controllers.
     *
     * @param collectionController the controller for the complete card collection
     * @param userCollection the user's current card collection
     */
    public ShopCardsSelectionControllerImpl(
            final CollectionController collectionController,
<<<<<<< HEAD
            final Set<CardIdentifier> userCollection) {
=======
            final List<CardIdentifier> userCollection) {
>>>>>>> 72c5403f05b8c33d342c62dbbd63fe4016ae48a2
        this.collectionController = collectionController;
        this.userCollection = Collections.unmodifiableList(userCollection);
    }

    @Override
    public final List<CardIdentifier> getRandomCardsByType(final CardType type) {
        final List<CardIdentifier> candidates = collectionController.getCompleteCollection().stream()
            .filter(card -> matchesTypeGroup(card.type(), type))
            .filter(card -> !userCollection.contains(card))
            .collect(Collectors.toList());

        Collections.shuffle(candidates, random);
        return candidates.stream()
            .limit(MAX_CARDS_TO_SEND)
            .collect(Collectors.toList());
    }

<<<<<<< HEAD
=======
    /**
     * Determines whether a card's actual type matches the requested group.
     * @param actual the given CardType
     * @param requested the requested CardType
     * @return True if it's possible, False otherwise
     */
>>>>>>> 72c5403f05b8c33d342c62dbbd63fe4016ae48a2
    private boolean matchesTypeGroup(final CardType actual, final CardType requested) {
        switch (requested) {
            case HERO:
            case SPELL:
                return actual == requested;
            case FRIENDLY:
                return actual == CardType.MELEE || actual == CardType.RANGED;
            default:
                return false;
        }
    }
}
