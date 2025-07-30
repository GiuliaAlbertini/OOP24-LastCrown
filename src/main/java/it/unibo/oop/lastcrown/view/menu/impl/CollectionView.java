package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.menu.api.ModifiableBackScene;
import it.unibo.oop.lastcrown.view.scenes_utilities.BackButton;
import it.unibo.oop.lastcrown.view.scenes_utilities.CardsGridPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.DetailPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.FilterPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.HideableScrollPane;

/**
 * View that shows the complete collection.
 */
public final class CollectionView extends AbstractScene implements ModifiableBackScene {
    private static final long serialVersionUID = 1L;
    private static final double DETAIL_RATIO = 0.30;
    private static final int GRID_HGAP = 10;
    private static final int GRID_VGAP = 10;
    private static final int GRID_COLUMNS = 5;

    private final transient CollectionController collectionController;
    private final transient SceneManager sceneManager;

    private final JPanel mainContainer;
    private final DetailPanel detailPanel;
    private final JPanel rightContainer;
    private final FilterPanel filterPanel;
    private final CardsGridPanel cardsGridPanel;
    private final BackButton backButton;

    private final int detailWidth;
    private transient Optional<CardType> currentFilter = Optional.empty();

    private CollectionView(final SceneManager sceneManager,
                           final CollectionController collectionController,
                           final Set<CardIdentifier> cardsOwned) {
        this.sceneManager = sceneManager;
        this.collectionController = collectionController;
        detailPanel = new DetailPanel();

        filterPanel = new FilterPanel(typeOpt -> loadCards(typeOpt, cardsOwned));

        cardsGridPanel = CardsGridPanel.create(GRID_COLUMNS, GRID_HGAP, GRID_VGAP);
        final var scrollPane = new HideableScrollPane(cardsGridPanel);

        rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.add(filterPanel);
        rightContainer.add(scrollPane);

        mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(detailPanel, BorderLayout.WEST);
        mainContainer.add(rightContainer, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        this.backButton = BackButton.create(SceneName.COLLECTION, SceneName.MENU, this.sceneManager);
        final var south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(backButton);
        add(south, BorderLayout.SOUTH);
        setComponentsOpacity(backButton);

        int initWidth = this.getWidth();
        if (initWidth <= 0) {
            initWidth = SCREEN_WIDTH;
        }
        detailWidth = (int) Math.round(initWidth * DETAIL_RATIO);
        detailPanel.setPreferredSize(new Dimension(detailWidth, this.getHeight()));

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
    public SceneName getSceneName() {
        return SceneName.COLLECTION;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void setBackDestination(final SceneName destination) {
        this.backButton.setBackViewName(destination);
    }

    private void loadCards(final Optional<CardType> type, final Set<CardIdentifier> cardsOwned) {
        if (currentFilter.isPresent() && type.equals(currentFilter)) {
            return;
        }
        currentFilter = type;
        final List<CardIdentifier> list = type.isPresent()
            ? collectionController.getCollectionByType(type.get())
            : collectionController.getCompleteCollection().getCompleteCollectionAsList();
        cardsGridPanel.loadCards(Collections.unmodifiableList(list), 
            card -> detailPanel.showCard(card, detailWidth), cardsOwned);
    }

    private void setComponentsOpacity(final JButton backButton) {
        makeComponentsTransparent(this);
        backButton.setOpaque(true);
    }
}
