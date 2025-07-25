package it.unibo.oop.lastcrown.controller.menu.impl;

import java.util.Set;

import it.unibo.oop.lastcrown.controller.GameControllerExample;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.controller.user.impl.DeckControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.menu.api.MainView;
import it.unibo.oop.lastcrown.view.menu.impl.MainViewImpl;

/**
 * Implementation of {@link SceneManager}.
 */
public class SceneManagerImpl implements SceneManager {

    private final MainController mainController;
    private final AccountController accountController;
    private final CollectionController collectionController;
    private final MainView mainView;

    /**
     * Constructs a SceneManagerImpl and initializes the main view.
     *
     * @param mainController the {@link MainController} to use
     * @param accountController the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckContr the {@link DeckController} to use
     * @param gameContr the {@link GameControllerExample} to use
     */
    public SceneManagerImpl(
        final MainController mainController,
        final AccountController accountController,
        final CollectionController collectionController,
        final DeckController deckContr,
        final MatchStartObserver gameContr
    ) {
        this.mainController = mainController;
        this.accountController = accountController;
        this.collectionController = collectionController;
        final DeckController deckController = new DeckControllerImpl(Set.copyOf(deckContr.getAvailableCards()));
        this.mainView = MainViewImpl.create(
            this,
            this.mainController,
            this.accountController,
            this.collectionController,
            deckController,
            gameContr
        );
    }

    @Override
    public final void switchScene(final String caller, final String destination) {
        this.mainView.changePanel(caller, destination);
    }

    @Override
    public final void closeApplication() {
        this.mainView.closeApplication();
    }

    @Override
    public final void updateUserCollectionUsers(final Set<CardIdentifier> newSet) {
        this.mainView.updateUserCollectionUsers(newSet);
    }
}
