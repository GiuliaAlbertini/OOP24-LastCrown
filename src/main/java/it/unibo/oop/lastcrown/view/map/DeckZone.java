
package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.util.List;
import java.util.Set;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import it.unibo.oop.lastcrown.controller.GameControllerExample;
import it.unibo.oop.lastcrown.controller.app_managing.impl.InGameDeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * A JPanel that contains an energyBar and a panel with 4 card-buttons.
 */
public final class DeckZone extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int SECTIONS = 10;
    private static final int RED = 27;
    private static final int GREEN = 120;
    private static final int BLUE = 175;
    private static final String KEY_PROPERTY = "info";

    private static final int MAX_ENERGY = SECTIONS;
    private static final int TIME_RECHARGE_SINGLE_ENERGY = 1000;
    private int currentEnergy = 0;
    private final Timer rechargeTimer;
    private InGameDeckController inGameDeckController;
    private final ActionListener buttonListener;
    private final MouseListener mouseListener;

    private JPanel energyBarPanel;
    private final JPanel cardPanel;
    private CardIdentifier lastClicked;
    private final int deckZoneWidth;
    private final int deckZoneHeight;

    /**
     * @param mainContr main controller
     * @param pos positioning zone
     * @param deckZoneWidth deck zone width
     * @param deckZoneHeight deck zone height
     * @param energyBarWidth energy bar width
     */
    public DeckZone(final GameControllerExample mainContr, final PositioningZone pos, 
     final int deckZoneWidth, final int deckZoneHeight, final int energyBarWidth, final Set<CardIdentifier> deck) {
        this.deckZoneWidth = deckZoneWidth;
        this.deckZoneHeight = deckZoneHeight;
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
        this.cardPanel = new JPanel(new GridLayout(3, 1, 0, 5));
        this.cardPanel.setPreferredSize(new Dimension(deckZoneWidth - energyBarWidth, deckZoneHeight));
        this.cardPanel.setBounds(energyBarWidth, 0, deckZoneWidth - energyBarWidth, deckZoneHeight);
        this.add(cardPanel);

        updateCardButtons(this.buttonListener, this.mouseListener);
    }

    /**
     * Refreshes the card buttons based on next available cards.
     */
    public void updateCardButtons(final ActionListener act, final MouseListener ml) {
        cardPanel.removeAll();
        List<CardIdentifier> nextCards = inGameDeckController.getNextAvailableCards();
        for (CardIdentifier id : nextCards) {
            JButton jb = new JButton(id.toString());
            jb.putClientProperty(KEY_PROPERTY, id);
            jb.addActionListener(act);
            jb.addMouseListener(ml);
            cardPanel.add(jb);
        }
        cardPanel.revalidate();
        cardPanel.repaint();
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
     * @param id the id of the used card
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

    public void updateInGameDeck(Set<CardIdentifier> newDeck) {
        this.inGameDeckController = InGameDeckController.create(newDeck);
        this.updateCardButtons(buttonListener, mouseListener);      
    }
}
