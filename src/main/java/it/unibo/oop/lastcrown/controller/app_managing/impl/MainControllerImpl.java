package it.unibo.oop.lastcrown.controller.app_managing.impl;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.engine.AudioEngine;
import it.unibo.oop.lastcrown.controller.GameControllerExample;
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
import it.unibo.oop.lastcrown.model.file_handling.api.FileHandler;
import it.unibo.oop.lastcrown.model.file_handling.impl.AccountParser;
import it.unibo.oop.lastcrown.model.file_handling.impl.AccountSerializer;
import it.unibo.oop.lastcrown.model.file_handling.impl.FileHandlerImpl;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.menu.api.LoginView;
import it.unibo.oop.lastcrown.view.menu.impl.LoginViewImpl;

/**
 * Implementation of {@link MainController}.
 */
public class MainControllerImpl implements MainController {
    private static final String SEP = File.separator;
    private static final String PATH = getAccountPath();
    private Optional<SceneManager> sceneManager;
    private Optional<AccountController> accountController = Optional.empty();
    private final LoginView loginView;
    private long sessionTimer;

    /**
     * Constructor for a new {@link MainControllerImpl}.
     */
    public MainControllerImpl() {
        this.sceneManager = Optional.empty();
        this.loginView = LoginViewImpl.create(this);
        this.loginView.setVisibility(true);
        AudioEngine.playSoundTrack(SoundTrack.MENU);
        this.sessionTimer = System.currentTimeMillis();
    }

    @Override
    public final void goOverLogin(final String username) {
        this.accountController = Optional.of(
           new AccountControllerImpl(username));
        final CollectionController collectionController = new CollectionControllerImpl();
        final DeckController deckController = new DeckControllerImpl(getUserCollection(accountController));

        //HERE MISSING GAME CONTROLLER INITIALIZATION

        final GameControllerExample gameController = new GameControllerExample() {
            @Override
            public void notifyShopToMatch() {
            }
            @Override
            public void notifyButtonPressed(final CardIdentifier id) {
            }
            @Override
            public void notifyClicked(final int x, final int y) {
            }
            @Override
            public void notifyPause() {
            }
            @Override
            public void notifyPauseEnd() {
            }
            @Override
            public JComponent getWallHealthBar() {
               return new JComponent() {
               };
            }
            @Override
            public JComponent getEventWriter() {
                return new JComponent() {
                };
            }
            @Override
            public JComponent getCoinsWriter() {
                return new JComponent() {
                };
            }
        };
        //= new GameController(hero, boss, playableChars, enemies, spellsMap, 1400, 800);
        this.sceneManager = Optional.of(
            new SceneManagerImpl(
                this,
                accountController.get(),
                collectionController,
                deckController,
                gameController
                ));
        this.closeLoginView();
        this.sessionTimer = System.currentTimeMillis();
    }

    @Override
    public final void updateUserColletionUsers(final Set<CardIdentifier> newSet) {
        this.sceneManager.get().updateUserCollectionUsers(newSet);
    }

    @Override
    public final void closeAll() {
        final double minutes = computeMinutesPassed();
        final Account newAcc = this.getAccount().get();
        newAcc.addPlaytime(minutes);
        writeAccountOnFile(newAcc);
        this.sceneManager.get().closeApplication();
    }

    private double computeMinutesPassed() {
        final long elapsedMillis = System.currentTimeMillis() - this.sessionTimer;
        return elapsedMillis / 60000.0;
    }

    @Override
    public final Optional<Account> getAccount() {
        return this.accountController.map(AccountController::getAccount);
    }

    @Override
    public final void updateAccount(final Account account) {
        account.addPlaytime(computeMinutesPassed());
        this.sessionTimer = System.currentTimeMillis();
        writeAccountOnFile(account);
        this.sceneManager.get().updateAccountUsers(account);
    }

    private void writeAccountOnFile(final Account account) {
        final FileHandler<Account> fileHandler = new FileHandlerImpl<>(new AccountParser(), new AccountSerializer(), PATH);
        fileHandler.writeToFile(account.getUsername(), account);
    }

    private Set<CardIdentifier> getUserCollection(final Optional<AccountController> accountController) {
        return accountController.get().getAccount().getUserCollection().getCollection();
    }

    private void closeLoginView() {
        this.loginView.setVisibility(false);
    }

    private static String getAccountPath() {
        return "OOP24-LastCrown" + SEP
             + "src" + SEP
             + "main" + SEP
             + "resources" + SEP
             + "accounts";
    }
}
