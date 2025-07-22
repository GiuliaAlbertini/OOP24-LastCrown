package it.unibo.oop.lastcrown.view.menu.impl;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Shared component for displaying a grid of card icons that adapts to available width.
 */
public final class CardGridPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    private int gridColumns;
    private int gridCellSide;
    private final int heightGap;
    private final int verticalGap;

    /**
     * Constructs a CardGridPanel with initial spacing.
     *
     * @param initialColumns the initial number of columns
     * @param hgap horizontal gap between cells (in pixels)
     * @param vgap vertical gap between cells (in pixels)
     */
    private CardGridPanel(final int initialColumns, final int hgap, final int vgap) {
        this.gridColumns = initialColumns;
        this.heightGap = hgap;
        this.verticalGap = vgap;
        this.gridCellSide = 0;
    }

    /**
     * Factory method to create a CardGridPanel.
     * 
     * @param initialColumns the initial number of columns
     * @param hgap horizontal gap between cells (in pixels)
     * @param vgap vertical gap between cells (in pixels)
     * @return the created CardGridPanel instance
     */
    public static CardGridPanel create(final int initialColumns, 
                                       final int hgap, 
                                       final int vgap) {
        final CardGridPanel cgp = new CardGridPanel(initialColumns, hgap, vgap);
        cgp.setLayout(new GridLayout(0, cgp.gridColumns, hgap, cgp.verticalGap));
        return cgp;
    }

    /**
     * Updates the number of columns and cell size based on the available width.
     *
     * @param availableWidth the width in pixels available for the grid
     * @param minCellSide    the minimum side length of a cell (in pixels)
     */
    public void updateGridDimensions(final int availableWidth, final int minCellSide) {
        final int newColumns = Math.max(1, (availableWidth + heightGap) / (minCellSide + heightGap));
        if (newColumns != gridColumns) {
            gridColumns = newColumns;
            ((GridLayout) getLayout()).setColumns(gridColumns);
        }
        gridCellSide = (availableWidth - (gridColumns - 1) * heightGap) / gridColumns;
        updateEachCellSize();
    }

    private void updateEachCellSize() {
        for (final var component : getComponents()) {
            if (component instanceof IconPanel) {
                ((IconPanel) component).setPreferredSize(new Dimension(gridCellSide, gridCellSide));
            }
        }
        revalidate();
        repaint();
    }

    /**
     * Populates the grid with IconPanels for the given cards, each wired to the provided click handler.
     *
     * @param cards the set of CardIdentifier to display
     * @param cardClicked callback invoked when a card is clicked
     * @param cardsOwned the set of owned cards
     */
    public void loadCards(final List<CardIdentifier> cards,
                          final Consumer<CardIdentifier> cardClicked,
                          final Set<CardIdentifier> cardsOwned) {
        removeAll();
        for (final CardIdentifier card : cards) {
            final boolean useGrey = !cardsOwned.contains(card);
            final IconPanel iconPanel = new IconPanel(card, useGrey);
            iconPanel.setPreferredSize(new Dimension(gridCellSide, gridCellSide));
            iconPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    cardClicked.accept(card);
                }
            });
            add(iconPanel);
        }
        revalidate();
        repaint();
    }
}
