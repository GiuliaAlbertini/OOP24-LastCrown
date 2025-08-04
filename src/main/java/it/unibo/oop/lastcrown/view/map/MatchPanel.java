package it.unibo.oop.lastcrown.view.map;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * A JPanel that represent the main content of the Map.
 */
public final class MatchPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int TRUPS_RED = 235;
    private static final int TRUPS_GREEN = 198;
    private static final int TRUPS_BLUE = 120;
    private static final int ENEMIES_RED = 210;
    private static final int ENEMIES_GREEN = 135;
    private static final int ENEMIES_BLUE = 130;
    private final transient MatchController gameContr;
    private JPanel overLayPanel;
    private PositioningZone posZone;
    private DeckZone cardZone;
    private int panelsHeight;
    private final transient MouseAdapter mouseAdapter;
    private final int frameWidth;
    private final int frameHeight;
    private final int energyBarWidth;
    private final int deckZoneWidth;
    private final int posZoneWidth;
    private final int wallZoneWidth;
    private final int trupsZoneWidth;
    private final int enemiesZoneWidth;
    private final int utilityZoneHeight;

    /**
     * @param obs the match view exit observer
     * @param gameContr mainController interface
     * @param wallHealthBar the wall health bar graphic component
     * @param eventWriter the event writer graphic component
     * @param coinsWriter the coins writer graphic component
     * @param frameWidth the width of the map
     * @param frameHeight the height of the map
     * @param deck the deck to use
     */
    public MatchPanel(final MatchExitObserver obs, final MatchController gameContr,
     final JComponent wallHealthBar, final JComponent eventWriter, final JComponent coinsWriter,
     final int frameWidth, final int frameHeight, final Set<CardIdentifier> deck) {
        this.gameContr = gameContr;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.energyBarWidth = (int) (frameWidth * DimensionResolver.ENERGYBAR.width());
        this.deckZoneWidth = (int) (frameWidth * DimensionResolver.DECKZONE.width());
        this.posZoneWidth = (int) (frameWidth * DimensionResolver.POSITIONINGZONE.width());
        this.wallZoneWidth = (int) (frameWidth * DimensionResolver.WALL.width());
        this.trupsZoneWidth = (int) (frameWidth * DimensionResolver.TRUPSZONE.width());
        this.enemiesZoneWidth = (int) (frameWidth * DimensionResolver.ENEMIESZONE.width());
        this.utilityZoneHeight = (int) (frameHeight * DimensionResolver.UTILITYZONE.height());

        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Point pointInPosZone = SwingUtilities.convertPoint(overLayPanel, e.getPoint(), posZone);
                final CardIdentifier cardId = cardZone.getLastClicked();
                if (cardId == null) {
                    return;
                }
                switch (cardId.type()) {
                    case CardType.MELEE -> {
                        if (posZone.isValidClick(CardType.MELEE, pointInPosZone)) {
                            final int x = deckZoneWidth + posZoneWidth * 3 / 4;
                            cardZone.playCard();
                            gameContr.notifyClicked(x, e.getY());
                        }
                    }
                    case CardType.RANGED -> {
                        if (posZone.isValidClick(CardType.RANGED, pointInPosZone)) {
                            final int x = deckZoneWidth + posZoneWidth * 1 / 4;
                            cardZone.playCard();
                            gameContr.notifyClicked(x, e.getY());
                        }
                    }
                    case CardType.SPELL -> {
                        if (e.getX() > posZoneWidth + wallZoneWidth && e.getY() < panelsHeight) {
                            cardZone.playCard();
                            gameContr.notifyClicked(e.getX() + deckZoneWidth, e.getY());
                        }
                    }
                    default -> { }
                }
            }
        };
        initializePanel();
        setupZone(obs, wallHealthBar, eventWriter, coinsWriter, deck);
    }
    /**
     * Initialize the main panel layout and properties.
    */
    private void initializePanel() {
        this.setFocusable(false);
        this.setLayout(null);
        this.setPreferredSize(new Dimension(frameWidth, frameHeight));
    }

    /**
     * Sets up all the major zones of the map.
     * @param obs the match view exit observer
     * @param wallHealthBar the graphic component of the wall health
     * @param eventWriter the event writer graphic component
     * @param coinsWriter the coins writer graphic component
     * @param deck the deck to use
     */
    private void setupZone(final MatchExitObserver obs, final JComponent wallHealthBar,
     final JComponent eventWriter, final JComponent coinsWriter, final Set<CardIdentifier> deck) {
        this.panelsHeight = this.frameHeight - this.utilityZoneHeight;
        this.setOpaque(false);
        this.posZone = new PositioningZone(this.posZoneWidth, panelsHeight);
        this.cardZone = new DeckZone(gameContr, posZone, deckZoneWidth, panelsHeight, energyBarWidth, deck);
        cardZone.setBounds(0, 0, this.deckZoneWidth, panelsHeight);
        this.add(cardZone);

        posZone.setBounds(deckZoneWidth, 0, this.posZoneWidth, panelsHeight);
        this.add(posZone);

        final JPanel wallZone = new JPanel(null);
        wallZone.setBackground(Color.DARK_GRAY);
        wallZone.setPreferredSize(new Dimension(this.wallZoneWidth, panelsHeight));
        wallZone.setBounds(this.deckZoneWidth + this.posZoneWidth, 0, this.wallZoneWidth, panelsHeight);
        this.add(wallZone);

        final JPanel trupsZone = new JPanel(null);
        trupsZone.setBackground(new Color(TRUPS_RED, TRUPS_GREEN, TRUPS_BLUE));
        trupsZone.setPreferredSize(new Dimension(this.trupsZoneWidth, panelsHeight));
        trupsZone.setBounds(deckZoneWidth + posZoneWidth + wallZoneWidth, 0, this.trupsZoneWidth, panelsHeight);
        this.add(trupsZone);

        final JPanel enemiesZone = new JPanel(null);
        enemiesZone.setBackground(new Color(ENEMIES_RED, ENEMIES_GREEN, ENEMIES_BLUE));
        enemiesZone.setPreferredSize(new Dimension(this.enemiesZoneWidth, panelsHeight));
        enemiesZone.setBounds(deckZoneWidth + posZoneWidth + wallZoneWidth + trupsZoneWidth, 0,
         this.enemiesZoneWidth, panelsHeight);
        this.add(enemiesZone);

        this.overLayPanel = new JPanel(null);
        this.overLayPanel.setPreferredSize(new Dimension(frameWidth - deckZoneWidth, panelsHeight));
        this.overLayPanel.setOpaque(false);
        this.overLayPanel.setBounds(this.deckZoneWidth, 0, frameWidth - deckZoneWidth, panelsHeight);
        this.overLayPanel.addMouseListener(this.mouseAdapter);
        this.add(overLayPanel);

        final UtilityZone utilityZone = new UtilityZone(obs, gameContr, frameWidth,
         utilityZoneHeight, wallHealthBar, eventWriter, coinsWriter);
        utilityZone.setBackground(Color.WHITE);
        utilityZone.setOpaque(true);
        utilityZone.setBounds(0, panelsHeight, this.frameWidth, this.utilityZoneHeight);
        this.add(utilityZone);
        this.setComponentZOrder(utilityZone, 0);
    }

    /**
     * Notifies the MatchView about the bossfight.
     * @param start True if it started, False if it ended
     */
    public void notifyBossFight(final boolean start) {
        this.cardZone.handleButtonsEnabling(start);
    }

    /**
     * @return the trupszone limit of the x coordinate
     */
    public int getTrupsZoneLimit() {
        return this.deckZoneWidth + posZoneWidth + wallZoneWidth + trupsZoneWidth;
    }

    /**
     * @return the wall size
     */
    public Dimension getWallSize() {
        return new Dimension(this.wallZoneWidth, this.panelsHeight);
    }

    /**
     * @return the coordinates of the upper left corner of the wall
     */
    public Point getWallCoordinates() {
        return new Point(this.deckZoneWidth + this.posZoneWidth, 0);
    }
    /**
     * Notify the cardzone about the new deck
     * @param newDeck the new deck to use
     */
    public void updateInGameDeck(final Set<CardIdentifier> newDeck) {
        this.cardZone.updateInGameDeck(newDeck);
    }
}
