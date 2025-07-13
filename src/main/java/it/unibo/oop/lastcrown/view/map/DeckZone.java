package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;



import it.unibo.oop.lastcrown.controller.MainController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;

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
    private final JButton jb1;
    private final JButton jb2;
    private final JButton jb3;
    private final JButton jb4;
    private JPanel energyBarPanel;
    private final JPanel cardPanel;
    private CardType lastClicked;
    private final int deckZoneWidth;
    private final int deckZoneHeight;

    /**
     * @param mainContr main controller
     * @param pos positioning zone
     * @param deckZoneWidth deck zone width
     * @param deckZoneHeight deck zone height
     * @param energyBarWidth energy bar width
     */
    public DeckZone(final MainController mainContr, final PositioningZone pos, 
     final int deckZoneWidth, final int deckZoneHeight, final int energyBarWidth) {
        this.deckZoneWidth = deckZoneWidth;
        this.deckZoneHeight = deckZoneHeight;
        this.setLayout(null);
        this.setupEnergyBar(this, energyBarWidth);
        this.setBackground(new Color(RED, GREEN, BLUE));
        this.setPreferredSize(new Dimension(this.deckZoneWidth, deckZoneHeight));
        final ActionListener act = e -> {
            final var button = (JButton) e.getSource();
            final CardIdentifier id = (CardIdentifier) button.getClientProperty(KEY_PROPERTY);
            lastClicked = id.type();
            mainContr.notifyButtonPressed(id);
        };

        final MouseListener mouseListener = new MouseAdapter() {
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
        this.cardPanel = new JPanel(new GridLayout(4, 1));
        this.cardPanel.setPreferredSize(new Dimension(deckZoneWidth - energyBarWidth, deckZoneHeight));
        this.cardPanel.setBounds(energyBarWidth, 0, deckZoneWidth - energyBarWidth, deckZoneHeight);
        jb1 = new JButton("Warrior");
        jb1.putClientProperty(KEY_PROPERTY, new CardIdentifier(1, CardType.MELEE));
        jb1.addActionListener(act);
        jb1.addMouseListener(mouseListener);
        jb2 = new JButton("Knight4");
        jb2.putClientProperty(KEY_PROPERTY, new CardIdentifier(2, CardType.MELEE));
        jb2.addActionListener(act);
        jb2.addMouseListener(mouseListener);
        jb3 = new JButton("Archer3");
        jb3.putClientProperty(KEY_PROPERTY, new CardIdentifier(3, CardType.RANGED));
        jb3.addActionListener(act);
        jb3.addMouseListener(mouseListener);
        jb4 = new JButton("Explosion");
        jb4.putClientProperty(KEY_PROPERTY, new CardIdentifier(1, CardType.SPELL));
        jb4.addActionListener(act);
        jb4.addMouseListener(mouseListener);
        this.cardPanel.setLayout(new GridLayout(4, 1));
        this.cardPanel.add(jb1);
        this.cardPanel.add(jb2);
        this.cardPanel.add(jb3);
        this.cardPanel.add(jb4);
        this.add(cardPanel);
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
    public CardType getLastClicked() {
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
        final Color sectionSeparator = Color.DARK_GRAY;
        final int sections = SECTIONS;
        final int filledSections = (int) Math.floor(energyLevel);

        for (int i = 0; i < sections; i++) {
            final JPanel section = new JPanel();
            section.setLayout(new BorderLayout());

            if (i < filledSections) {
                section.setBackground(full);
            } else {
                section.setBackground(empty);
            }
            section.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, sectionSeparator));
            energyBarPanel.add(section);
        }
    }
}
