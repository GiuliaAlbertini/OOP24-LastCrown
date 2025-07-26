package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.user.api.DeckController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.scenes_utilities.CardsGridPanel;
import it.unibo.oop.lastcrown.view.scenes_utilities.IconPanel;

public final class DeckRowPanelManager {
    
    private final int CARD_CELL_SIZE = CardsGridPanel.getFixedCellSize();

    private final DeckController deckController;
    private final JPanel deckRowPanel;
    private final Runnable onDeckUpdated;

    public DeckRowPanelManager(final DeckController deckController,
                                final JPanel deckRowPanel,
                                final int deckHeight,
                                final Runnable onDeckUpdated) {
        this.deckController = deckController;
        this.deckRowPanel = deckRowPanel;
        this.onDeckUpdated = onDeckUpdated;
    }

    public void loadDeckIcons() {
        deckRowPanel.removeAll();
        for (final CardIdentifier card : deckController.getDeck()) {
            final IconPanel iconPanel = new IconPanel(card, false, false);
            iconPanel.setPreferredSize(new Dimension(CARD_CELL_SIZE,CARD_CELL_SIZE));
            iconPanel.addMouseListener(new MouseAdapter() {
                @Override public void mouseClicked(final MouseEvent e) {
                    deckController.removeCard(card);
                    onDeckUpdated.run();
                }
            });
            deckRowPanel.add(iconPanel);
        }
        deckRowPanel.revalidate();
        deckRowPanel.repaint();
    }
}
