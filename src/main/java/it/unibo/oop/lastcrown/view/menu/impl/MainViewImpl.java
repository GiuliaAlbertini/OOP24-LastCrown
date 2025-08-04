package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.engine.AudioEngine;
import it.unibo.oop.lastcrown.controller.GameControllerExample;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MatchStartObserver;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.controller.user.impl.DeckControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.user.api.Account;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.map.MatchView;
import it.unibo.oop.lastcrown.view.map.MatchViewImpl;
import it.unibo.oop.lastcrown.view.menu.api.ModifiableBackScene;
import it.unibo.oop.lastcrown.view.menu.api.DeckViewInterface;
import it.unibo.oop.lastcrown.view.menu.api.MainView;
import it.unibo.oop.lastcrown.view.menu.api.Scene;
import it.unibo.oop.lastcrown.view.shop.ShopView;
import it.unibo.oop.lastcrown.view.shop.ShopViewImpl;

/**
 * View that uses a {@link CardLayout} to handle the different scenes.
 */
public class MainViewImpl extends JFrame implements MainView {
    private static final long serialVersionUID = 1L;
    private static final double RESIZE_FACTOR = 1.0;
    private static final Dimension SCREENSIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private static final int WIDTH = (int) (SCREENSIZE.getWidth() * RESIZE_FACTOR);
    private static final int HEIGHT = (int) (SCREENSIZE.getHeight() * RESIZE_FACTOR);
    private static final Color BG_COLOR = new Color(15, 35, 65);

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(this.layout);
    private final transient SceneManager sceneManager;
    private final transient MainController mainController;
    private final transient AccountController accountController;
    private transient DeckController deckController;
    private final transient CollectionController collectionController;
    private final transient MatchStartObserver gameController;
    private final Scene menuView;
    private final Scene creditView;
    private Scene statsView;
    private DeckViewInterface deckView;
    private ModifiableBackScene collectionView;
    private final ShopView shopView;
    private MatchView matchView;
    private boolean matchExist;
    private int enemyList = 3;

    /**
     * Constructs the main application window, initializes each scene,
     * and defaults to the menu scene.
     *
     * @param sceneManager         the {@link SceneManager} to use
     * @param mainController       the {@link MainController} to use
     * @param accountController    the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckContr            the {@link DeckController} to use
     * @param gameContr            the {@link GameControllerExample} to use
     */
    public MainViewImpl(final SceneManager sceneManager,
            final MainController mainController,
            final AccountController accountController,
            final CollectionController collectionController,
            final DeckController deckContr,
            final MatchStartObserver gameContr) {
        this.sceneManager = sceneManager;
        this.mainController = mainController;
        this.accountController = mainController.getAccountController();
        this.deckController = new DeckControllerImpl(Set.copyOf(deckContr.getAvailableCards()));
        this.collectionController = collectionController;
        this.gameController = gameContr;
        this.mainPanel.setOpaque(false);

        this.menuView = MenuView.create(this.sceneManager, this.mainController);
        this.creditView = CreditsView.create(this.sceneManager);
        this.statsView = StatsView.create(this.sceneManager, this.accountController);
        this.deckView = DeckView.create(this.sceneManager, deckController);
        this.collectionView = CollectionView.create(this.sceneManager, this.collectionController, getOwnedCards());
        this.shopView = new ShopViewImpl(this.sceneManager, collectionController,
            deckContr.getAvailableCards(), WIDTH, HEIGHT, accountController.getAccount());
        // HERE MISSING SHOP VIEW AND MATCH VIEW TO THE MAIN CONTROLLER
        // gameContr.newShopView(this.shopView);
        // gameContr.newMatchView(this.matchView);

        setUpPanels();
        this.layout.show(this.mainPanel, menuView.getSceneName().get());
    }

    private Set<CardIdentifier> getOwnedCards() {
        return this.accountController.getAccount().getUserCollection().getCollection();
    }

    private void init() {
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setContentPane(this.mainPanel);
        this.setBackground(BG_COLOR);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved(final ComponentEvent e) {
                SwingUtilities.invokeLater(() -> {
                    revalidate();
                    repaint();
                });
            }
        });
    }

    /**
     * Factory method to create an istance of {@link MainView}.
     *
     * @param sceneManager         the {@link SceneManager} to use
     * @param mainController       the {@link MainController} to use
     * @param accountController    the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckController       the {@link DeckController} to use
     * @param gameController       the {@link GameControllerExample} to use
     * @return an initialized istance of {@link MainViewImpl}
     */
    public static MainView create(final SceneManager sceneManager,
            final MainController mainController,
            final AccountController accountController,
            final CollectionController collectionController,
            final DeckController deckController,
            final MatchStartObserver gameController) {
        final MainViewImpl view = new MainViewImpl(
                sceneManager,
                mainController,
                accountController,
                collectionController,
                deckController,
                gameController);
        view.init();
        return view;
    }

    @Override
    public final void changePanel(final SceneName sceneCaller, final SceneName sceneDestination) {
        this.layout.show(this.mainPanel, sceneDestination.get());
    }

    @Override
    public final void onShop(final SceneName caller) {
        this.shopView.notifyVisible();
        if (SceneName.MATCH.equals(caller)) {
            this.matchView.clearNewGraphicsComponent();
        }
        if (!AudioEngine.getActualSoundTrack().equals(SoundTrack.SHOP)) {
            AudioEngine.playSoundTrack(SoundTrack.SHOP);
        }
    }

    @Override
    public final void onMatch(final SceneName caller) {
        if (this.deckController.getDeck().isEmpty()) {
            final String title = "WAIT!!!";
            final String message = "You didn't create a deck yet!";
            final Dialog dialog = new Dialog(title, message, true);
            dialog.setLocationRelativeTo((JComponent) this.shopView);
            dialog.setVisible(true);
            return;
        }
        this.shopView.notifyHidden();
        this.gameController.onMatchStart(WIDTH, HEIGHT, this.deckController.getHero(), this.collectionController, this, enemyList);
        if (this.matchExist) {
            if (SceneName.SHOP.equals(caller) && this.enemyList > 1) {
                this.enemyList = this.enemyList - 1;
            }
            this.mainPanel.remove(this.matchView.getPanel());
            this.matchExist = false;
        }
        this.matchView = new MatchViewImpl(this.sceneManager, this.gameController.getMatchControllerReference(),
                    WIDTH, HEIGHT, this.deckController.getDeck());
        this.mainPanel.add(this.matchView.getPanel(), this.matchView.getSceneName().get());
        this.matchExist = true;
        this.gameController.getMatchView(matchView);
        this.matchView.updateInGameDeck(this.deckController.getDeck());
        AudioEngine.playSoundTrack(SoundTrack.BATTLE);
    }

    @Override
    public final void onMenu(final SceneName caller) {
        this.enemyList = 3;
        if (SceneName.SHOP.equals(caller) || SceneName.MATCH.equals(caller)) {
            this.mainController.updateAccount(this.shopView.getManagedAccount());
            if (matchExist) {
                this.matchView.clearNewGraphicsComponent();
            }
        }
        if (!AudioEngine.getActualSoundTrack().equals(SoundTrack.MENU)) {
            AudioEngine.playSoundTrack(SoundTrack.MENU);
        }
    }

    @Override
    public final void onCollection(final SceneName caller) {
        if (SceneName.SHOP.equals(caller)) {
            this.collectionView.setBackDestination(SceneName.SHOP);
        } else {
            this.collectionView.setBackDestination(SceneName.MENU);
        }
        AudioEngine.playSoundTrack(SoundTrack.COLLECTION);
    }

    @Override
    public final void onDeck(final SceneName caller) {
        if (SceneName.SHOP.equals(caller)) {
            this.deckView.setBackDestination(SceneName.SHOP);
        } else {
            this.deckView.setBackDestination(SceneName.MENU);
        }
    }

    @Override
    public final void updateAccount(final int amount, final boolean bossDefeated) {
        this.shopView.notifyUpdateAccount(amount, bossDefeated);
    }

    @Override
    public final void updateUserCollectionUsers(final Set<CardIdentifier> newSet) {
        updateDeckController(newSet);
        this.mainPanel.remove(this.deckView.getPanel());
        this.deckView = DeckView.create(this.sceneManager, this.deckController);
        this.mainPanel.add(this.deckView.getPanel(), this.deckView.getSceneName().get());
        this.mainPanel.remove(this.collectionView.getPanel());
        this.collectionView = CollectionView.create(this.sceneManager, this.collectionController, newSet);
        this.mainPanel.add(this.collectionView.getPanel(), this.collectionView.getSceneName().get());
    }

    private void updateDeckController(final Set<CardIdentifier> newSet) {
        final Set<CardIdentifier> currentDeck = this.deckController.getDeck();
        final DeckController newDeckContr = new DeckControllerImpl(newSet);
        if (!currentDeck.isEmpty()) {
            final CardIdentifier hero = this.deckController.getHero();
            newDeckContr.addCard(hero);
            currentDeck.stream()
                .filter(ci -> !ci.equals(hero))
                .forEach(newDeckContr::addCard);
        }
        this.deckController = newDeckContr;
    }

    private void setUpPanels() {
        this.mainPanel.add(this.menuView.getPanel(), this.menuView.getSceneName().get());
        this.mainPanel.add(this.creditView.getPanel(), this.creditView.getSceneName().get());
        this.mainPanel.add(this.statsView.getPanel(), this.statsView.getSceneName().get());
        this.mainPanel.add(this.deckView.getPanel(), this.deckView.getSceneName().get());
        this.mainPanel.add(this.collectionView.getPanel(), this.collectionView.getSceneName().get());
        this.mainPanel.add(this.shopView.getPanel(), this.shopView.getSceneName().get());
    }

    @Override
    public final void updateAccountUsers(final Account account) {
        this.accountController.setAccount(account);
        this.mainPanel.remove(this.statsView.getPanel());
        this.statsView = StatsView.create(this.sceneManager, this.accountController);
        this.mainPanel.add(this.statsView.getPanel(), this.statsView.getSceneName().get());
        updateUserCollectionUsers(account.getUserCollection().getCollection());
    }

    @Override
    public final MainView getFrame() {
        return this;
    }

    @Override
    public final void close() {
        this.dispose();
    }
}
