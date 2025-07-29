package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.scenes_utilities.CardsGridPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.IconPanel;

/**
 * Classes that handle the deck panel of a DeckView.
 */
public final class DeckRowPanelManager {

    private static final int CARD_CELL_SIZE = CardsGridPanel.getFixedCellSize();

    private final Runnable onDeckUpdated;

    /**
     * Construct a new DeckRowPanelManager.
     * @param onDeckUpdated the action to perform on the deck update
     */
    public DeckRowPanelManager(final Runnable onDeckUpdated) {
        this.onDeckUpdated = onDeckUpdated;
    }

    /**
     * Loads the icons that needs to be put in the deck panel.
     * @param deckPanel the panel to modify
     * @param deckController the deckController to use
     */
    public void loadDeckIcons(final JPanel deckPanel, final DeckController deckController) {
        deckPanel.removeAll();
        for (final CardIdentifier card : deckController.getDeck()) {
            final IconPanel iconPanel = new IconPanel(card, false, false);
            iconPanel.setPreferredSize(new Dimension(CARD_CELL_SIZE, CARD_CELL_SIZE));
            iconPanel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(final MouseEvent e) {
                    deckController.removeCard(card);
                    onDeckUpdated.run();
                }
            });
            deckPanel.add(iconPanel);
        }
        deckPanel.revalidate();
        deckPanel.repaint();
    }
}
