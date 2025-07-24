
package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import it.unibo.oop.lastcrown.controller.GameControllerExample;
import it.unibo.oop.lastcrown.controller.app_managing.impl.InGameDeckController;
import it.unibo.oop.lastcrown.controller.user.api.CollectionController;
import it.unibo.oop.lastcrown.controller.user.impl.CollectionControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A JPanel that contains an energyBar and a panel with 4 card-buttons.
 */
public final class DeckZone extends JPanel {
    private static final int VERTICAL_GAP_BTNS = 5;
    private static final long serialVersionUID = 1L;
    private static final int SECTIONS = 10;
    private static final int RED = 27;
    private static final int GREEN = 120;
    private static final int BLUE = 175;
    private static final String KEY_PROPERTY = "info";

    private static final int MAX_ENERGY = SECTIONS;
    private static final int TIME_RECHARGE_SINGLE_ENERGY = 1000;
    private int currentEnergy;
    private final Timer rechargeTimer;
    private transient InGameDeckController inGameDeckController;
    private final ActionListener buttonListener;
    private final MouseListener mouseListener;

    private JPanel energyBarPanel;
    private final JPanel cardPanel;
    private transient CardIdentifier lastClicked;
    private final int deckZoneWidth;
    private final int deckZoneHeight;
    private final int energyZoneWidth;

    /**
     * @param mainContr main controller
     * @param pos positioning zone
     * @param deckZoneWidth deck zone width
     * @param deckZoneHeight deck zone height
     * @param energyBarWidth energy bar width
     * @param deck the deck
     */
    public DeckZone(final GameControllerExample mainContr, final PositioningZone pos, 
     final int deckZoneWidth, final int deckZoneHeight, final int energyBarWidth, final Set<CardIdentifier> deck) {
        this.deckZoneWidth = deckZoneWidth;
        this.deckZoneHeight = deckZoneHeight;
        this.energyZoneWidth = energyBarWidth;
        this.inGameDeckController = InGameDeckController.create(deck);
        this.setLayout(null);
        this.setupEnergyBar(this, energyBarWidth);
        this.setBackground(new Color(RED, GREEN, BLUE));
        this.setPreferredSize(new Dimension(this.deckZoneWidth, deckZoneHeight));

        this.rechargeTimer = new Timer(TIME_RECHARGE_SINGLE_ENERGY, e -> {
            if (currentEnergy < MAX_ENERGY) {
                currentEnergy++;
                updateEnergyBar(currentEnergy);
            } else {
                ((Timer) e.getSource()).stop();
            }
        });
        this.rechargeTimer.start();

        this.buttonListener = e -> {
            final var button = (JButton) e.getSource();
            final CardIdentifier id = (CardIdentifier) button.getClientProperty(KEY_PROPERTY);
            lastClicked = id;
            mainContr.notifyButtonPressed(id);
        };

        this.mouseListener = new MouseAdapter() {
            @Override
            public void mouseEntered(final MouseEvent e) {
                final var jb = (JButton) e.getSource();
                final CardIdentifier id = (CardIdentifier) jb.getClientProperty(KEY_PROPERTY);
                pos.highlightCells(id.type());
            }
            @Override
            public void mouseExited(final MouseEvent e) {
                final var jb = (JButton) e.getSource();
                final CardIdentifier id = (CardIdentifier) jb.getClientProperty(KEY_PROPERTY);
                pos.stopHighLight(id.type());
            }
        };
        this.cardPanel = new JPanel(new GridLayout(3, 1, 0, VERTICAL_GAP_BTNS));
        this.cardPanel.setPreferredSize(new Dimension(deckZoneWidth - energyBarWidth, deckZoneHeight));
        this.cardPanel.setBounds(energyBarWidth, 0, deckZoneWidth - energyBarWidth, deckZoneHeight);
        this.add(cardPanel);

        updateCardButtons(buttonListener, mouseListener);
    }

    /**
     * Refreshes the card buttons based on next available cards.
     * @param act the ActionListener
     * @param ml the MouseListener
     */
    public void updateCardButtons(final ActionListener act, final MouseListener ml) {
        cardPanel.removeAll();
        final List<CardIdentifier> nextCards = inGameDeckController.getNextAvailableCards();
        for (final CardIdentifier id : nextCards) {
            final JButton jb = new JButton();
            jb.putClientProperty(KEY_PROPERTY, id);
            jb.addActionListener(act);
            jb.addMouseListener(ml);
            cardPanel.add(jb);
            addIconToBtn(id, jb);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
    }

    private void addIconToBtn(final CardIdentifier id, final JButton jb) {
        final int jbWidth = this.deckZoneWidth - this.energyZoneWidth;
        final CollectionController collContr = new CollectionControllerImpl();
        final String name = collContr.getCardName(id)
            .orElseThrow(() -> new IllegalArgumentException(
                "No name found for card " + id
            ));
        final String iconPath = CharacterPathLoader.loadIconPath(id.type().get(), name);
        final BufferedImage img = ImageLoader.getImage(iconPath, jbWidth, jbWidth);
        final ImageIcon icon = new ImageIcon(img);
        jb.setIcon(icon);
    }

    /**
     * Sets up the energy bar.
     * @param container the container of the energy bar
     * @param width the width of the bar
     */
    private void setupEnergyBar(final JPanel container, final int width) {
        energyBarPanel = new JPanel();
        energyBarPanel.setBackground(Color.BLACK);
        energyBarPanel.setPreferredSize(new Dimension(width, deckZoneHeight));
        final int sections = SECTIONS;
        energyBarPanel.setLayout(new GridLayout(sections, 1, 0, 2));
        energyBarPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        updateEnergyBar(SECTIONS);
        energyBarPanel.setBounds(0, 0, width, deckZoneHeight);
        container.add(energyBarPanel);
    }

    /**
     * @return the CardType of the last clicked button
    */
    public CardIdentifier getLastClicked() {
        return this.lastClicked;
    }

    /**
     * Updates the energyBar with a new level.
     * @param energyLevel new energy level
     */
    private void updateEnergyBar(final int energyLevel) {
        energyBarPanel.removeAll();
        final Color full = new Color(255, 105, 180);
        final Color empty = Color.WHITE;
        final Color separator = Color.DARK_GRAY;
        for (int i = 0; i < SECTIONS; i++) {
            final JPanel section = new JPanel(new BorderLayout());
            section.setBackground(i < energyLevel ? full : empty);
            section.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, separator));
            energyBarPanel.add(section);
        }
        energyBarPanel.revalidate();
        energyBarPanel.repaint();
    }

    /**
     * Updates the energy bar and notify the InGameController of the card used.
     * 
     * @return a boolean indicating if a card has bee used or not
     */
    public boolean playCard() {
        final int cost = inGameDeckController.getEnergyToPlay(this.lastClicked);
        if (currentEnergy >= cost) {
            currentEnergy -= cost;
            updateEnergyBar(currentEnergy);
            if (!rechargeTimer.isRunning()) {
                rechargeTimer.start();
            }
            inGameDeckController.playCard(this.lastClicked);
            updateCardButtons(this.buttonListener, this.mouseListener);
            return true;
        }
        return false;
    }

    /**
     * Updates the deck.
     * @param newDeck the new set rappresenting the deck
     */
    public void updateInGameDeck(final Set<CardIdentifier> newDeck) {
        this.inGameDeckController = InGameDeckController.create(newDeck);
        this.updateCardButtons(buttonListener, mouseListener);
    }
}
