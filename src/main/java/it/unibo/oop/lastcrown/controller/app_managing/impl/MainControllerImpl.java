package it.unibo.oop.lastcrown.controller.app_managing.impl;

import java.io.File;
import java.util.Optional;
import java.util.Set;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.engine.AudioEngine;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
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
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;

/**
 * Implementation of {@link MainController}.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP",
    justification = """
            An instance of the match start observer must be kept in order to allow the game start
            to be executed properly.
            """
)
public class MainControllerImpl implements MainController {
    private static final double TO_MINUTES_FACTOR = 60_000.0;
    private static final String SEP = File.separator;
    private static final String ACCOUNT_PATH = getAccountPath();

    private Optional<SceneManager> sceneManager;
    private Optional<AccountController> accountController = Optional.empty();
    private final LoginView loginView;
    private final MatchStartObserver gameController;

    private long sessionTimer;

    /**
     * Constructor for a new {@link MainControllerImpl}.
     */
    public MainControllerImpl() {
        this.sceneManager = Optional.empty();
        this.loginView = LoginViewImpl.create(this);
        this.gameController = new MatchStartObserverImpl(this);
        AudioEngine.playSoundTrack(SoundTrack.MENU);
        this.sessionTimer = System.currentTimeMillis();
    }

    @Override
    public final void goOverLogin(final String username) {
        checkDirExistence();
        this.accountController = Optional.of(
           new AccountControllerImpl(username));
        final CollectionController collectionController = new CollectionControllerImpl();
        final DeckController deckController = new DeckControllerImpl(getUserCollection(accountController));

        //= new GameController(hero, boss, playableChars, enemies, spellsMap, 1400, 800);
        this.sceneManager = Optional.of(
            new SceneManagerImpl(
                this,
                collectionController,
                deckController,
                gameController
                ));
        this.loginView.close();
        this.sessionTimer = System.currentTimeMillis();
    }

    @Override
    public final void updateUserColletionUsers(final Set<CardIdentifier> newSet) {
        this.accountController.ifPresent(mainCtrl -> {
            final Account a = mainCtrl.getAccount();
            newSet.forEach(a::addCard);
            mainCtrl.setAccount(a);
        });
        this.sceneManager.get().updateUserCollectionUsers(newSet);
    }

    @Override
    public final void closeAll() {
        final double minutes = computeMinutesPassed();
        final Account newAcc = this.getAccount().get();
        newAcc.addPlaytime(minutes);
        writeAccountOnFile(newAcc);
        this.sceneManager.ifPresent(sm -> {
            sm.getMainView().close();
        });
    }

    @Override
    public final AccountController getAccountController() {
        return this.accountController.get();
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

    @Override
    public final MatchController getMatchController() {
        return this.gameController.getMatchControllerReference();
    }

    @Override
    public final MatchStartObserver getMatchStartObserver() {
        return this.gameController;
    }

    private static String getAccountPath() {
        return System.getProperty("user.home")
            + SEP + ".lastcrown"
            + SEP + "accounts";
    }

    private void checkDirExistence() {
        final File dir = new File(ACCOUNT_PATH);
        if (!dir.exists() && !dir.mkdirs()) {
            throw new IllegalStateException(
                "Impossible to create the folder for the accounts: " + dir.getAbsolutePath()
            );
        }
    }

    private double computeMinutesPassed() {
        final long elapsedMillis = System.currentTimeMillis() - this.sessionTimer;
        return elapsedMillis / TO_MINUTES_FACTOR;
    }

    private void writeAccountOnFile(final Account account) {
        final FileHandler<Account> fileHandler = new FileHandlerImpl<>(
            new AccountParser(),
            new AccountSerializer(),
            ACCOUNT_PATH
        );
        fileHandler.writeToFile(account.getUsername(), account);
    }

    private Set<CardIdentifier> getUserCollection(final Optional<AccountController> accountController) {
        return accountController.get().getAccount().getUserCollection().getCollection();
    }
}
