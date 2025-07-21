package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.audio.SoundTrack;
import it.unibo.oop.lastcrown.audio.engine.AudioEngine;
import it.unibo.oop.lastcrown.controller.GameController;
import it.unibo.oop.lastcrown.controller.app_managing.api.MainController;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.controller.user.impl.DeckControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.map.MatchView;
import it.unibo.oop.lastcrown.view.map.MatchViewImpl;
import it.unibo.oop.lastcrown.view.menu.api.CollectionScene;
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
    private static final Color BG_COLOR = new Color(21, 76, 121);

    private final CardLayout layout = new CardLayout();
    private final JPanel mainPanel = new JPanel(this.layout);
    private final transient SceneManager sceneManager;
    private final transient MainController mainController;
    private final transient AccountController accountController;
    private final transient CollectionController collectionController;
    private final transient GameController gameController;
    private final Scene menuView;
    private final Scene creditView;
    private final Scene statsView;
    private Scene deckView;
    private final CollectionScene collectionView;
    private final ShopView shopView;
    private final MatchView matchView;

    /**
     * Constructs the main application window, initializes each scene,
     * and defaults to the menu scene.
     *
     * @param sceneManager the {@link SceneManager} to use
     * @param mainController the {@link MainController} to use
     * @param accountController the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckContr the {@link DeckController} to use
     * @param gameContr the {@link GameController} to use
     */
    public MainViewImpl(final SceneManager sceneManager,
                        final MainController mainController,
                        final AccountController accountController,
                        final CollectionController collectionController,
                        final DeckController deckContr,
                        final GameController gameContr
            ) {
        this.sceneManager = sceneManager;
        this.mainController = mainController;
        this.accountController = accountController;
        this.collectionController = collectionController;
        this.gameController = gameContr;
        final DeckController deckController = new DeckControllerImpl(Set.copyOf(deckContr.getAvailableCards()));
        this.mainPanel.setOpaque(false);

        this.menuView = MenuView.create(this.sceneManager, this.mainController);
        this.creditView = CreditsView.create(this.sceneManager);
        this.statsView = StatsView.create(this.sceneManager, this.accountController);
        this.deckView = DeckView.create(this.sceneManager, deckController);
        this.collectionView = CollectionView.create(this.sceneManager, this.collectionController);
        this.shopView = new ShopViewImpl(this, collectionController,
         deckContr.getAvailableCards(), WIDTH, HEIGHT);
        this.matchView = new MatchViewImpl(gameContr, this, WIDTH, HEIGHT);

        //HERE MISSING SHOP VIEW AND MATCH VIEW TO THE MAIN CONTROLLER
        //gameContr.newShopView(this.shopView);
        //gameContr.newMatchView(this.matchView);

        setUpPanels();
        this.layout.show(this.mainPanel, menuView.getSceneName());
    }

    private void init() {
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setSize(new Dimension(WIDTH, HEIGHT));
        this.setContentPane(this.mainPanel);
        this.setBackground(BG_COLOR);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    /**
     * Factory method to create an istance of {@link MainView}.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param mainController the {@link MainController} to use
     * @param accountController the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckController the {@link DeckController} to use
     * @param gameController the {@link GameController} to use
     * @return an initialized istance of {@link MainViewImpl}
     */
    public static MainView create(final SceneManager sceneManager, 
                                  final MainController mainController, 
                                  final AccountController accountController,
                                  final CollectionController collectionController,
                                  final DeckController deckController,
                                  final GameController gameController) {
        final MainViewImpl view = new MainViewImpl(
            sceneManager,
            mainController,
            accountController,
            collectionController,
            deckController,
            gameController
        );
        view.init();
        return view;
    }

    @Override
    public final void changePanel(final String sceneCaller, final String sceneDestination) {
        switch (sceneDestination) {
            case "SHOP" -> {
                this.shopView.notifyVisible();
                AudioEngine.playSoundTrack(SoundTrack.SHOP);
            }
            case "MATCH" -> {
                this.shopView.notifyHidden();
                this.gameController.notifyShopToMatch();
                AudioEngine.playSoundTrack(SoundTrack.BATTLE);
            }
            case "MENU" -> {
                if (!AudioEngine.getActualSoundTrack().equals(SoundTrack.MENU)) {
                    AudioEngine.playSoundTrack(SoundTrack.MENU);
                }
            }
            case "COLLECTION" -> {
                if ("SHOP".equals(sceneCaller)) {
                    this.collectionView.setBackDestination("SHOP"); 
                } else {
                    this.collectionView.setBackDestination("MENU");
                }
                AudioEngine.playSoundTrack(SoundTrack.COLLECTION);
            }
            default -> { }
        }
        this.layout.show(this.mainPanel, sceneDestination);
    }

    @Override
    public final void closeApplication() {
        this.dispose();
    }

    @Override
    public final void updateDeckView(final Set<CardIdentifier> newSet) {
        this.deckView = DeckView.create(this.sceneManager, new DeckControllerImpl(newSet));
    }

    private void setUpPanels() {
        this.mainPanel.add(this.menuView.getPanel(),   this.menuView.getSceneName());
        this.mainPanel.add(this.creditView.getPanel(), this.creditView.getSceneName());
        this.mainPanel.add(this.statsView.getPanel(), this.statsView.getSceneName());
        this.mainPanel.add(this.deckView.getPanel(), this.deckView.getSceneName());
        this.mainPanel.add(this.collectionView.getPanel(), this.collectionView.getSceneName());
        this.mainPanel.add(this.shopView.getPanel(), this.shopView.getSceneName());
        this.mainPanel.add(this.matchView.getPanel(), this.matchView.getSceneName());
    }
}
