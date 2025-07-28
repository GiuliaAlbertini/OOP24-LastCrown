package it.unibo.oop.lastcrown.view.scenes_utilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * A panel for showing detailed views of cards.
 */
public class DetailPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public DetailPanel() {
        super(new BorderLayout());
        setOpaque(false);
    }

    /**
     * Display the given card in the detail area, sizing it to a square of the given side.
     * @param card the card to show
     * @param side the width and height of the detail view
     */
    public void showCard(final CardIdentifier card, final int side) {
        removeAll();
        final var big = CardPanel.create(card);
        big.setPreferredSize(new Dimension(side, side));

        final var wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);
        final var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(big, gbc);

        add(wrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    public void showCardWithButton(final CardIdentifier card, final int side, final JButton button) {
        removeAll();
        final var big = CardPanel.create(card);
        big.setPreferredSize(new Dimension(side, side));

        final var wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        final var gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        wrapper.add(big, gbc);

        if (button != null) {
            gbc.gridy = 1;
            gbc.insets.top = 10;
            wrapper.add(button, gbc);
        }

        add(wrapper, BorderLayout.CENTER);
        revalidate();
        repaint();
    }
}