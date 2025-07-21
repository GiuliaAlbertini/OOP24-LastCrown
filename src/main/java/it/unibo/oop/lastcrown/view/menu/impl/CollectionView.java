package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.menu.api.CollectionScene;

/**
 * View that shows the complete collection.
 */
public final class CollectionView extends AbstractScene implements CollectionScene {
    private static final int BTN_FONT_SIZE = 20;
    private static final long serialVersionUID = 1L;
    private static final int FILTER_BAR_HGAP = 8;
    private static final String PANEL_NAME = "COLLECTION";
    private static final double DETAIL_RATIO = 0.30;
    private static final int FILTER_BUTTON_HEIGHT = 36;
    private static final int MIN_CELL_SIDE = 200;
    private static final int GRID_HGAP = 10;
    private static final int GRID_VGAP = 10;
    private static final CardType[] TYPES = {
        CardType.HERO, CardType.MELEE, CardType.RANGED, CardType.SPELL,
    };
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME = "SansSerif";
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;

    private final transient CollectionController collectionController;
    private final transient SceneManager sceneManager;

    private final JPanel mainContainer;
    private final JPanel detailPanel;
    private final JPanel rightContainer;
    private final JPanel filterBar;
    private final CardGridPanel cardsGridPanel;
    private final BackButton backButton;

    private int detailWidth;
    private int gridColumns = 1;
    private transient Optional<CardType> currentFilter = Optional.empty();

    private CollectionView(final SceneManager sceneManager,
                           final CollectionController collectionController,
                           final Set<CardIdentifier> cardsOwned) {
        this.sceneManager = sceneManager;
        this.collectionController = collectionController;
        detailPanel = new JPanel(new BorderLayout());

        filterBar = new JPanel(new FlowLayout(FlowLayout.CENTER, FILTER_BAR_HGAP, 0));
        filterBar.setMinimumSize(new Dimension(0, FILTER_BUTTON_HEIGHT));
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, FILTER_BUTTON_HEIGHT));

        addFilterButton("ALL", Optional.empty(), cardsOwned);
        for (final CardType type : TYPES) {
            addFilterButton(type.get(), Optional.of(type), cardsOwned);
        }

        cardsGridPanel = CardGridPanel.create(gridColumns, GRID_HGAP, GRID_VGAP);
        final var scrollPane = new HideableScrollPane(cardsGridPanel);

        rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.add(filterBar);
        rightContainer.add(scrollPane);

        mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(detailPanel, BorderLayout.WEST);
        mainContainer.add(rightContainer, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        this.backButton = BackButton.create(PANEL_NAME, "MENU", this.sceneManager);
        final var south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(backButton);
        add(south, BorderLayout.SOUTH);
        setComponentsOpacity(backButton);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int totalW = getWidth();
                detailWidth = (int) Math.round(totalW * DETAIL_RATIO);
                detailPanel.setPreferredSize(new Dimension(detailWidth, getHeight()));
                detailPanel.revalidate();

                final int availableW = totalW - detailWidth;
                final int columns = Math.max(1, (availableW + GRID_HGAP) / (MIN_CELL_SIDE + GRID_HGAP));
                if (columns != gridColumns) {
                    gridColumns = columns;
                }
                cardsGridPanel.updateGridDimensions(availableW, MIN_CELL_SIDE);
            }
        });

        loadCards(this.currentFilter, cardsOwned);
    }

    /**
     * Factory method to create a new CollectionView.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param collectionController the {@link CollectionController} to use
     * @param cardsOwned the set of cards in the user's collection
     * @return the created CollectionView instance
     */
    public static CollectionView create(
        final SceneManager sceneManager,
        final CollectionController collectionController,
        final Set<CardIdentifier> cardsOwned
    ) {
        return new CollectionView(sceneManager, collectionController, cardsOwned);
    }

    @Override
    public String getSceneName() {
        return PANEL_NAME;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void setBackDestination(final String destination) {
        this.backButton.setBackViewName(destination);
    }

    private void loadCards(final Optional<CardType> type, final Set<CardIdentifier> cardsOwned) {
        if (currentFilter.isPresent() && type.equals(currentFilter)) {
            return;
        }
        currentFilter = type;
        final List<CardIdentifier> list = type.isPresent()
            ? collectionController.getCollectionByType(type.get())
            : collectionController.getCompleteCollection();
        cardsGridPanel.loadCards(Collections.unmodifiableList(list), this::showDetail, cardsOwned);
    }

    private void showDetail(final CardIdentifier card) {
        detailPanel.removeAll();
        final var big = CardPanel.create(card);
        big.setPreferredSize(new Dimension(detailWidth, detailWidth));

        final var wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        final var gbc = new GridBagConstraints();
        gbc.gridx = 0; 
        gbc.gridy = 0;
        wrapper.add(big, gbc);

        detailPanel.add(wrapper, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private void setComponentsOpacity(final JButton backButton) {
        makeComponentsTransparent(this);
        backButton.setOpaque(true);
    }

    private void addFilterButton(final String label,
                                 final Optional<CardType> filterType,
                                 final Set<CardIdentifier> cardsOwned) {
        final var btn = new JButton(label);
        btn.setFont(responsiveFont(Font.BOLD, BTN_FONT_SIZE));
        btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, FILTER_BUTTON_HEIGHT));
        btn.addActionListener(e -> loadCards(filterType, cardsOwned));
        filterBar.add(btn);
    }

    private static Font responsiveFont(final int style, final int size) {
        final double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }
}
