package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * View to handle the user's deck.
 */
public final class DeckView extends AbstractScene {
    private static final int ADVISE_LABEL_FONT_SIZE = 26;
    private static final long serialVersionUID = 1L;
    private static final int SELECT_BTN_FONT_SIZE = 24;
    private static final int WRAPPER_VERTICAL_STRUT = 10;
    private static final int BTN_FONT_SIZE = 20;
    private static final String PANEL_NAME = "DECK";
    private static final double DETAIL_RATIO = 0.30;
    private static final double DECK_ROW_RATIO = 0.07;
    private static final int BASE_FILTER_BUTTON_HEIGHT = 36;
    private static final int BASE_MIN_CELL_SIDE = 200;
    private static final int BASE_GRID_HGAP = 10;
    private static final int BASE_GRID_VGAP = 10;
    private static final CardType[] TYPES = {
        CardType.HERO, CardType.MELEE, CardType.RANGED, CardType.SPELL,
    };
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME = "SansSerif";
    private static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    private static final int MIN_DECK_BASE = 50;
    private static final int MAX_DECK_BASE = 150;

    private final transient DeckController deckController;
    private final transient SceneManager sceneManager;
    private final JPanel mainContainer;
    private final JPanel detailPanel;
    private final JScrollPane deckScroll;
    private final JPanel deckRowPanel;
    private final JPanel filterBar;
    private final CardGridPanel cardsGridPanel;
    private final JPanel rightContainer;

    private int detailWidth;
    private int deckHeight;
    private int gridColumns = 1;

    /**
     * Constuctor for the view.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param deckController the {@link DeckController} to use
     */
    private DeckView(final SceneManager sceneManager,
                     final DeckController deckController) {
        this.sceneManager = Objects.requireNonNull(sceneManager);
        this.deckController = Objects.requireNonNull(deckController);

        detailPanel = new JPanel(new BorderLayout());

        deckRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, scaled(BASE_GRID_HGAP), 0));
        deckScroll = new JScrollPane(
            deckRowPanel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );

        filterBar = new JPanel(new FlowLayout(FlowLayout.CENTER, scaled(BASE_GRID_HGAP), 0));
        final int filterH = scaled(BASE_FILTER_BUTTON_HEIGHT);
        filterBar.setMinimumSize(new Dimension(0, filterH));
        filterBar.setMaximumSize(new Dimension(Integer.MAX_VALUE, filterH));
        final JButton btnAll = new JButton("ALL");
        btnAll.setFont(getResponsiveFont(Font.BOLD, BTN_FONT_SIZE));
        btnAll.setPreferredSize(new Dimension(btnAll.getPreferredSize().width, filterH));
        btnAll.addActionListener(e -> refreshAvailableCardsGrid(Optional.empty()));
        filterBar.add(btnAll);
        for (final CardType type : TYPES) {
            final JButton btn = new JButton(type.get());
            btn.setFont(getResponsiveFont(Font.BOLD, BTN_FONT_SIZE));
            btn.setPreferredSize(new Dimension(btn.getPreferredSize().width, filterH));
            btn.addActionListener(e -> refreshAvailableCardsGrid(Optional.of(type)));
            filterBar.add(btn);
        }

        cardsGridPanel = CardGridPanel.create(gridColumns, 
                                              scaled(BASE_GRID_HGAP), 
                                              scaled(BASE_GRID_VGAP),
                                              new HashSet<>(deckController.getAvailableCards()));
        final JScrollPane gridScroll = new HideableScrollPane(cardsGridPanel);

        rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.add(deckScroll);
        rightContainer.add(filterBar);
        rightContainer.add(gridScroll);

        mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(detailPanel, BorderLayout.WEST);
        mainContainer.add(rightContainer, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        final JButton back = BackButton.create("MENU", this.sceneManager);
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(back);
        add(south, BorderLayout.SOUTH);
        setComponentsOpacity(back);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(final ComponentEvent e) {
                final int totalW = getWidth();
                final int totalH = getHeight();

                detailWidth = (int) Math.round(totalW * DETAIL_RATIO);
                detailPanel.setPreferredSize(new Dimension(detailWidth, totalH));
                detailPanel.revalidate();

                final int proposedDeckH = (int) Math.round(totalH * DECK_ROW_RATIO);
                final int minDeckH = scaled(MIN_DECK_BASE);
                final int maxDeckH = scaled(MAX_DECK_BASE);
                deckHeight = Math.max(minDeckH, Math.min(proposedDeckH, maxDeckH));
                final int rightWidth = totalW - detailWidth;
                deckScroll.setPreferredSize(new Dimension(rightWidth, deckHeight));
                deckRowPanel.revalidate();
                deckRowPanel.repaint();

                loadDeckIcons();

                final int availableWidth = rightWidth;
                final int minCell = scaled(BASE_MIN_CELL_SIDE);
                final int newGridCols = Math.max(
                    1,
                    (availableWidth + scaled(BASE_GRID_HGAP)) / (minCell + scaled(BASE_GRID_HGAP))
                );
                if (newGridCols != gridColumns) {
                    gridColumns = newGridCols;
                }
                cardsGridPanel.updateGridDimensions(availableWidth, scaled(BASE_MIN_CELL_SIDE));
            }
        });

        loadDeckIcons();
        refreshAvailableCardsGrid(Optional.empty());
    }

    /**
     * Factory method to create a DeckView.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param deckController the {@link DeckController} to use
     * @return the created DeckView instance
     */
    public static DeckView create(
        final SceneManager sceneManager,
        final DeckController deckController
    ) {
        return new DeckView(sceneManager, deckController);
    }

    @Override
    public String getSceneName() {
        return PANEL_NAME;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    private void loadDeckIcons() {
        deckRowPanel.removeAll();

        int cardHeight = deckScroll.getViewport().getHeight();
        if (cardHeight <= 0) {
            cardHeight = deckHeight;
        }
        final int cardW = cardHeight / 2;

        final Set<CardIdentifier> deck = deckController.getDeck();
        if (deck.isEmpty()) {
            final JLabel placeholder = new JLabel("Add a hero to start building the deck");
            placeholder.setFont(getResponsiveFont(Font.BOLD, ADVISE_LABEL_FONT_SIZE));
            placeholder.setForeground(Color.WHITE);
            placeholder.setHorizontalAlignment(SwingConstants.CENTER);
            deckRowPanel.add(placeholder);
        } else {
            for (final CardIdentifier card : deck) {
                final IconPanel iconPanel = new IconPanel(card, false);
                final Dimension fixedSize = new Dimension(cardW, cardHeight);
                iconPanel.setPreferredSize(fixedSize);
                iconPanel.setMinimumSize(fixedSize);
                iconPanel.setMaximumSize(fixedSize);
                iconPanel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(final MouseEvent e) {
                        deckController.removeCard(card);
                        loadDeckIcons();
                        refreshAvailableCardsGrid(Optional.empty());
                    }
                });
                deckRowPanel.add(iconPanel);
            }
        }

        deckRowPanel.revalidate();
        deckRowPanel.repaint();
    }

    private void refreshAvailableCardsGrid(final Optional<CardType> type) {
        final List<CardIdentifier> available = type.isPresent()
            ? deckController.getAvailableCardsByType(type.get())
            : deckController.getAvailableCards();
        cardsGridPanel.loadCards(available, this::showDetail);
    }

    private void showDetail(final CardIdentifier card) {
        detailPanel.removeAll();

        final CardPanel big = CardPanel.create(card);
        big.setPreferredSize(new Dimension(detailWidth, detailWidth));

        final JButton select = new JButton("Select");
        select.setFont(getResponsiveFont(Font.BOLD, SELECT_BTN_FONT_SIZE));
        select.addActionListener(e -> {
            deckController.addCard(card);
            loadDeckIcons();
            refreshAvailableCardsGrid(Optional.empty());
        });

        final JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        final GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(big, gbc);

        gbc.gridy = 1;
        gbc.insets.top = WRAPPER_VERTICAL_STRUT;
        wrapper.add(select, gbc);

        detailPanel.add(wrapper, BorderLayout.CENTER);
        detailPanel.revalidate();
        detailPanel.repaint();
    }

    private void setComponentsOpacity(final JButton backButton) {
        makeComponentsTransparent(this);
        backButton.setOpaque(true);
    }

    private static Font getResponsiveFont(final int style, final int size) {
        final double scale = (double) SCREEN_WIDTH / BASE_SCREEN_WIDTH;
        return new Font(FONT_NAME, style, (int) (size * scale));
    }

    private int scaled(final int base) {
        int width = getWidth();
        if (width <= 0) {
            width = SCREEN_WIDTH;
        }
        final double scale = (double) width / BASE_SCREEN_WIDTH;
        return (int) (base * scale);
    }
}
