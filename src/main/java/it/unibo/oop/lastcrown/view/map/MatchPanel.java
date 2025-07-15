package it.unibo.oop.lastcrown.view.map;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import it.unibo.oop.lastcrown.controller.GameController;
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
    private final transient GameController gameContr;
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
    private final JButton jb3;

    /**
     * @param gameContr mainController interface
     * @param frameWidth the width of the map
     * @param frameHeight the height of the map
     */
    public MatchPanel(final GameController gameContr, final int frameWidth, final int frameHeight) {
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

        final ActionListener act = e -> {
            final var button = (JButton) e.getSource();
            this.gameContr.notifyButtonPressed((CardIdentifier) button.getClientProperty("info"));
        };

        jb3 = new JButton("ENEMY");
        jb3.putClientProperty("info", new CardIdentifier(1, CardType.ENEMY));
        jb3.addActionListener(act);
        this.mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                final Point pointInPosZone = SwingUtilities.convertPoint(overLayPanel, e.getPoint(), posZone);
                final CardType cardType = cardZone.getLastClicked();
                if (cardType == null) {
                    return;
                }
                switch (cardType) {
                    case CardType.MELEE -> {
                        if (posZone.isValidClick(CardType.MELEE, pointInPosZone)) {
                            final int x = deckZoneWidth + posZoneWidth * 3 / 4;
                            gameContr.notifyClicked(x, e.getY());
                        }
                    }
                    case CardType.RANGED -> {
                        if (posZone.isValidClick(CardType.RANGED, pointInPosZone)) {
                            final int x = deckZoneWidth + posZoneWidth * 1 / 4;
                            gameContr.notifyClicked(x, e.getY());
                        }
                    }
                    case CardType.SPELL -> {
                        if (e.getX() > posZoneWidth + wallZoneWidth && e.getY() < panelsHeight) {
                            gameContr.notifyClicked(e.getX() + deckZoneWidth, e.getY());
                        }
                    }
                    default -> { }
                }
            }
        };
        initializePanel();
        setupZone();
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
     */
    private void setupZone() {
        this.panelsHeight = this.frameHeight - this.utilityZoneHeight;
        this.setOpaque(false);

        this.posZone = new PositioningZone(this.posZoneWidth, panelsHeight);
        this.cardZone = new DeckZone(gameContr, posZone, deckZoneWidth, panelsHeight, energyBarWidth);
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

        final JPanel utilityZone = new JPanel();
        utilityZone.setLayout(new BoxLayout(utilityZone, BoxLayout.X_AXIS));
        utilityZone.setBackground(Color.WHITE);
        utilityZone.setOpaque(true);
        utilityZone.setPreferredSize(new Dimension(this.frameWidth, this.utilityZoneHeight));
        utilityZone.setBounds(0, panelsHeight, this.frameWidth, this.utilityZoneHeight);
        utilityZone.add(jb3);
        this.add(utilityZone);
        this.setComponentZOrder(utilityZone, 0);
    }
}
