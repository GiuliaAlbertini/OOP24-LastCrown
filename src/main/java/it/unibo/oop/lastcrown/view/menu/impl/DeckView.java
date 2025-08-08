package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Optional;
import java.util.Set;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.menu.api.DeckViewInterface;
import it.unibo.oop.lastcrown.view.scenes_utilities.BackButton;
import it.unibo.oop.lastcrown.view.scenes_utilities.CardsGridPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.DetailPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.FilterPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.HideableScrollPane;

/**
 * View to handle the user's deck.
 */
public final class DeckView extends AbstractScene implements DeckViewInterface {
    private static final int RULES_MSG_HEIGHT = SCREEN_HEIGHT / 2;
    private static final int RULES_MSG_WIDTH = SCREEN_WIDTH / 3;
    private static final long serialVersionUID = 1L;
    private static final int SELECT_BTN_FONT_SIZE = 24;
    private static final double DETAIL_RATIO = 0.30;
    private static final int BASE_GRID_HGAP = 10;
    private static final int BASE_GRID_VGAP = 10;
    private static final int BASE_SCREEN_WIDTH = 1920;
    private static final String FONT_NAME = "SansSerif";
    private static final Font RULES_FONT = new Font(FONT_NAME, Font.BOLD, 16);
    private static final int COLUMNS = 5;
    private static final int CARD_CELL_SIZE = CardsGridPanel.getFixedCellSize();

    private final transient DeckController deckController;
    private final transient SceneManager sceneManager;
    private final JPanel mainContainer;
    private final DetailPanel detailPanel;
    private final JScrollPane deckScroll;
    private final JPanel deckRowPanel;
    private final FilterPanel filterPanel;
    private final CardsGridPanel cardsGridPanel;
    private final JPanel rightContainer;
    private final BackButton back;
    private final transient DeckRowPanelManager deckRowManager;
    private transient Optional<CardType> currentFilter = Optional.empty();

    private final int detailWidth;

    /**
     * Constuctor for the view.
     * 
     * @param sceneManager the {@link SceneManager} to use
     * @param deckController the {@link DeckController} to use
     */
    private DeckView(final SceneManager sceneManager,
                     final DeckController deckController) {
        this.sceneManager = sceneManager;
        this.deckController =  deckController;

        final int initWidth = this.getWidth() > 0 ? this.getWidth() : SCREEN_WIDTH;
        final int initHeight = this.getHeight() > 0 ? this.getHeight() : SCREEN_HEIGHT;

        detailPanel = DetailPanel.create();
        this.detailWidth = (int) Math.round(initWidth * DETAIL_RATIO);
        this.detailPanel.setPreferredSize(new Dimension(this.detailWidth, initHeight));
        final int rightWidth = initWidth - this.detailWidth;

        deckRowPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, scaled(BASE_GRID_HGAP), 0));
        deckScroll = new JScrollPane(
            deckRowPanel,
            JScrollPane.VERTICAL_SCROLLBAR_NEVER,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED
        );
        deckScroll.setPreferredSize(new Dimension(rightWidth, CARD_CELL_SIZE));
        deckScroll.setMaximumSize(new Dimension(rightWidth, CARD_CELL_SIZE));

        filterPanel = new FilterPanel(typeOpt -> {
            currentFilter = typeOpt;
            refreshAvailableCardsGrid(typeOpt);
        });

        cardsGridPanel = CardsGridPanel.create(COLUMNS, scaled(BASE_GRID_HGAP), scaled(BASE_GRID_VGAP));
        final JScrollPane gridScroll = new HideableScrollPane(cardsGridPanel);
        final int rowsVisible = 3;
        final int cellSize = CardsGridPanel.getFixedCellSize();
        final int vGapScaled = scaled(BASE_GRID_VGAP);
        final int totalHeight = rowsVisible * cellSize + (rowsVisible - 1) * vGapScaled;
        gridScroll.setPreferredSize(new Dimension(rightWidth, totalHeight));

        rightContainer = new JPanel();
        rightContainer.setLayout(new BoxLayout(rightContainer, BoxLayout.Y_AXIS));
        rightContainer.add(deckScroll);
        rightContainer.add(filterPanel);
        rightContainer.add(gridScroll);

        mainContainer = new JPanel(new BorderLayout());
        mainContainer.add(detailPanel, BorderLayout.WEST);
        mainContainer.add(rightContainer, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(mainContainer, BorderLayout.CENTER);

        this.back = BackButton.create(SceneName.DECK, SceneName.MENU, this.sceneManager);
        final JButton rulesBtn = createRulesBtn();
        final JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        south.add(rulesBtn);
        south.add(back);
        add(south, BorderLayout.SOUTH);
        makeComponentsTransparent(this);
        back.setOpaque(true);
        rulesBtn.setOpaque(true);

        deckRowPanel.revalidate();
        deckRowPanel.repaint();

        deckRowManager = new DeckRowPanelManager(this::onDeckUpdated);
        this.deckRowManager.loadDeckIcons(this.deckRowPanel, this.deckController);
        refreshAvailableCardsGrid(Optional.empty());
    }

    private void onDeckUpdated() {
        this.deckRowManager.loadDeckIcons(deckRowPanel, this.deckController);
        refreshAvailableCardsGrid(currentFilter);
    }

    private JButton createRulesBtn() {
        final JButton rulesBtn = new JButton("RULES");
        rulesBtn.setPreferredSize(back.getPreferredSize());
        rulesBtn.setFont(back.getFont());
        rulesBtn.setBackground(back.getBackground());
        rulesBtn.setForeground(back.getForeground());
        rulesBtn.setBorder(back.getBorder());
        rulesBtn.addActionListener(e -> showRules());
        return rulesBtn;
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
    public SceneName getSceneName() {
        return SceneName.DECK;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void showRules() {
        final String title = "HOW TO BUILD A DECK";
        final String message = """
            To start building a deck you need to choose a hero first.\n
            Once you choose a hero you can add other cards by clicking them in the panel in the bottom right.\n
            Then just click the select button that appears on the detail panel located on the left to add them.\n
            The limit of cards per type is determined by the hero.\n
            To remove a card from the deck you can just click the card from the deck panel in the upper right.\n
            An hero can't be removed.\n
            In order to change the hero you need to select the new hero you want and it will be automatically switched.\n
            The deck is automatically correct to respect the new hero.
            """;
        final JTextArea textArea = new JTextArea(message);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setFont(RULES_FONT);
        final JScrollPane scroll = new JScrollPane(textArea);
        scroll.setPreferredSize(new Dimension(RULES_MSG_WIDTH, RULES_MSG_HEIGHT));

        JOptionPane.showMessageDialog(
            SwingUtilities.getWindowAncestor(this),
            scroll,
            title,
            JOptionPane.INFORMATION_MESSAGE
    );
    }

    private void refreshAvailableCardsGrid(final Optional<CardType> type) {
        final List<CardIdentifier> available = type.isPresent()
            ? deckController.getAvailableCardsByType(type.get())
            : deckController.getAvailableCards();
        cardsGridPanel.loadCards(
            available,
            this::setUpSelectableDetailPanel,
            Set.copyOf(available)
        );
    }

    private void setUpSelectableDetailPanel(final CardIdentifier card) {
        final JButton select = new JButton("Select");
        select.setFont(getResponsiveFont(Font.BOLD, SELECT_BTN_FONT_SIZE));
        select.addActionListener(e -> {
            deckController.addCard(card);
            deckRowManager.loadDeckIcons(this.deckRowPanel, this.deckController);
            refreshAvailableCardsGrid(currentFilter);
        });

        detailPanel.showCardWithButton(card, detailWidth, select);
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

    @Override
    public void setBackDestination(final SceneName destination) {
        this.back.setBackViewName(destination);
    }
}
