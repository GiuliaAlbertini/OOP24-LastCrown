package it.unibo.oop.lastcrown.view.scenes_utilities;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * A grid panel that displays card icons with a fixed number of columns and fixed cell size,
 * dynamically adding rows based on the number of cards.
 */
public final class CardsGridPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    static final int FIXED_CELL_SIZE = (int)(Toolkit.getDefaultToolkit().getScreenSize().height * 0.2);

    private final int columns;
    private final int hGap;
    private final int vGap;

    private CardsGridPanel(final int columns, final int hGap, final int vGap) {
        this.columns = columns;
        this.hGap = hGap;
        this.vGap = vGap;
        setLayout(new GridLayout(0, columns, hGap, vGap));
    }

    public static CardsGridPanel create(final int initialColumns, final int hGap, final int vGap) {
        return new CardsGridPanel(initialColumns, hGap, vGap);
    }

    public void loadCards(final List<CardIdentifier> cards,
                        final Consumer<CardIdentifier> cardClicked,
                        final Set<CardIdentifier> cardsOwned) {
        removeAll();
        final int columns = this.columns;
        final int rowsNeeded = (int) Math.ceil((double) cards.size() / columns);
        final int rows = Math.max(rowsNeeded, 3);
        setLayout(new GridLayout(rows, columns, hGap, vGap));
        int count = 0;
        for (final CardIdentifier card : cards) {
            final boolean useGrey = !cardsOwned.contains(card);
            final IconPanel iconPanel = new IconPanel(card, useGrey, true);
            iconPanel.setPreferredSize(new Dimension(FIXED_CELL_SIZE, FIXED_CELL_SIZE));
            iconPanel.setMaximumSize(new Dimension(FIXED_CELL_SIZE, FIXED_CELL_SIZE));
            iconPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(final MouseEvent e) {
                    cardClicked.accept(card);
                }
            });
            add(iconPanel);
            count++;
        }
        for (int i = count; i < rows * columns; i++) {
            JPanel empty = new JPanel();
            empty.setOpaque(false);
            empty.setPreferredSize(new Dimension(FIXED_CELL_SIZE, FIXED_CELL_SIZE));
            add(empty);
        }
        revalidate();
        repaint();
    }

    public static int getFixedCellSize() {
        return FIXED_CELL_SIZE;
    }

    @Override
    public Dimension getPreferredSize() {
        final int totalWidth = columns * FIXED_CELL_SIZE + (columns - 1) * hGap;
        final int rows = (int) Math.ceil((double) getComponentCount() / columns);
        final int totalHeight = rows * FIXED_CELL_SIZE + (rows - 1) * vGap;
        return new Dimension(totalWidth, totalHeight);
    }
}