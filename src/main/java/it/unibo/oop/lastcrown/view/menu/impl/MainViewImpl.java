package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.AccountController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.controller.user.impl.DeckControllerImpl;
import it.unibo.oop.lastcrown.view.menu.api.MainView;
import it.unibo.oop.lastcrown.view.menu.api.Scene;

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
    private final Scene menuView;
    private final Scene creditView;
    private final Scene statsView;
    private final Scene deckView;
    private final Scene collectionView;

    /**
     * Constructs the main application window, initializes each scene,
     * and defaults to the menu scene.
     *
     * @param sceneManager the {@link SceneManager} to use
     * @param mainController the {@link MainController} to use
     * @param accountController the {@link AccountController} to use
     * @param collectionController the {@link CollectionController} to use
     * @param deckContr the {@link DeckController} to use
     */
    public MainViewImpl(final SceneManager sceneManager,
                        final MainController mainController,
                        final AccountController accountController,
                        final CollectionController collectionController,
                        final DeckController deckContr
            ) {
        this.sceneManager = sceneManager;
        this.mainController = mainController;
        this.accountController = accountController;
        this.collectionController = collectionController;
        final DeckController deckController = new DeckControllerImpl(Set.copyOf(deckContr.getAvailableCards()));
        this.mainPanel.setOpaque(false);

        this.menuView = MenuView.create(this.sceneManager, this.mainController);
        this.creditView = CreditsView.create(this.sceneManager);
        this.statsView = StatsView.create(this.sceneManager, this.accountController);
        this.deckView = DeckView.create(this.sceneManager, deckController);
        this.collectionView = CollectionView.create(this.sceneManager, this.collectionController);

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
     * @return an initialized istance of {@link MainViewImpl}
     */
    public static MainView create(final SceneManager sceneManager, 
                                  final MainController mainController, 
                                  final AccountController accountController,
                                  final CollectionController collectionController,
                                  final DeckController deckController) {
        final MainViewImpl view = new MainViewImpl(
            sceneManager,
            mainController,
            accountController,
            collectionController,
            deckController
        );
        view.init();
        return view;
    }

    @Override
    public final void changePanel(final String sceneName) {
        this.layout.show(this.mainPanel, sceneName);
    }

    @Override
    public final void closeApplication() {
        this.dispose();
    }

    private void setUpPanels() {
        this.mainPanel.add(this.menuView.getPanel(),   this.menuView.getSceneName());
        this.mainPanel.add(this.creditView.getPanel(), this.creditView.getSceneName());
        this.mainPanel.add(this.statsView.getPanel(), this.statsView.getSceneName());
        this.mainPanel.add(this.deckView.getPanel(), this.deckView.getSceneName());
        this.mainPanel.add(this.collectionView.getPanel(), this.collectionView.getSceneName());
    }
}
