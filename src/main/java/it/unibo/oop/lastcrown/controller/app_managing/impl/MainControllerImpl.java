package it.unibo.oop.lastcrown.controller.app_managing.impl;

import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.menu.impl.SceneManagerImpl;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.controller.user.impl.AccountControllerImpl;
import it.unibo.oop.lastcrown.controller.user.impl.CollectionControllerImpl;
import it.unibo.oop.lastcrown.controller.user.impl.DeckControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.menu.api.LoginView;
import it.unibo.oop.lastcrown.view.menu.impl.LoginViewImpl;

/**
 * Implementation of {@link MainController}.
 */
public class MainControllerImpl implements MainController {

    private Optional<SceneManager> sceneManager;
    private Optional<AccountController> accountController = Optional.empty();
    private final LoginView loginView;

    /**
     * Constructor for a new {@link MainControllerImpl}.
     */
    public MainControllerImpl() {
        this.sceneManager = Optional.empty();
        this.loginView = LoginViewImpl.create(this);
        this.loginView.setVisibility(true);
    }

    @Override
    public final void goOverLogin(final String username) {
        this.accountController = Optional.of(
           new AccountControllerImpl(username));
        final CollectionController collectionController = new CollectionControllerImpl();
        final DeckController deckController = new DeckControllerImpl(getUserCollection(accountController));
        this.sceneManager = Optional.of(
            new SceneManagerImpl(
                this,
                accountController.get(),
                collectionController,
                deckController
                ));
        this.closeLoginView();
    }

    @Override
    public final void updateDeckUsers(final Set<CardIdentifier> newSet) {
        this.sceneManager.get().updateDeckController(newSet);
    }

    @Override
    public final void closeAll() {
        this.sceneManager.get().closeApplication();
    }

    @Override
    public final Optional<Account> getAccount() {
        return this.accountController.map(AccountController::getAccount);
    }

    private Set<CardIdentifier> getUserCollection(final Optional<AccountController> accountController) {
        return accountController.get().getAccount().getUserCollection().getCollection();
    }

    private void closeLoginView() {
        this.loginView.setVisibility(false);
    }
}
