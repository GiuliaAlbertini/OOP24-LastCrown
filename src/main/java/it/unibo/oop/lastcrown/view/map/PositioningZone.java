package it.unibo.oop.lastcrown.view.map;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * A JPanel that builds the positioning zone of the playable characters and hero.
 */
public final class PositioningZone extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int ROWS = 5;
    private static final int COLUMNS = 2;
    private static final int RED = 178;
    private static final int GREEN = 255;
    private static final int BLUE = 242;
    private static final Color LIGHT_BLUE = new Color(RED, GREEN, BLUE);
    private final Set<PositioningCell> meleePanels;
    private final PositioningCell rangedPanel;

    /**
     * @param width the width of the positioning zone
     * @param height the height of the positioning zone
     */
    public PositioningZone(final int width, final int height) {
        this.setPreferredSize(new Dimension(width, height));
        this.setBackground(LIGHT_BLUE);
        this.setLayout(null);
        this.rangedPanel = new PositioningCell();
        this.rangedPanel.setBackground(LIGHT_BLUE);
        this.rangedPanel.setPreferredSize(new Dimension(width / COLUMNS, height));
        this.add(rangedPanel);
        this.rangedPanel.setBounds(0, 0, width / 2, height);

        this.meleePanels = new HashSet<>();
        final PositioningCell upperPanel = new PositioningCell();
        upperPanel.setPreferredSize(new Dimension(width - rangedPanel.getPreferredSize().width, height * COLUMNS / ROWS));
        upperPanel.setBackground(LIGHT_BLUE);
        this.add(upperPanel);
        upperPanel.setBounds(rangedPanel.getPreferredSize().width, 0, 
         width - rangedPanel.getPreferredSize().width, height * COLUMNS / ROWS);
        this.meleePanels.add(upperPanel);

        final PositioningCell lowerPanel = new PositioningCell();
        lowerPanel.setPreferredSize(upperPanel.getPreferredSize());
        lowerPanel.setBackground(LIGHT_BLUE);
        this.add(lowerPanel);
        lowerPanel.setBounds(rangedPanel.getPreferredSize().width, height - height * COLUMNS / ROWS, 
        width - rangedPanel.getPreferredSize().width, height * COLUMNS / ROWS);
        this.meleePanels.add(lowerPanel);
    }

    /**
     * @param cardType the CardType to be considered for the click
     * @param point the coordinates of the click
     * @return True if the click is valid, False otherwise
     */
    public boolean isValidClick(final CardType cardType, final Point point) {
        boolean result = false;
        if (cardType.equals(CardType.MELEE)) {
            for (final var cell : this.meleePanels) {
                final Point pointInPosZone = SwingUtilities.convertPoint(this, point, cell);
                final int xClick = (int) pointInPosZone.getX();
                final int yClick = (int) pointInPosZone.getY();
                if (cell.contains(xClick, yClick)) {
                    result = true;
                }
            }
       } else if (cardType.equals(CardType.RANGED) && this.rangedPanel.contains(point)) {
            result = true;
       }
        return result;
    }

    /**
     * Hightlights the cells of the corresponding CardType.
     * @param cardType the CardType
     */
    public void highlightCells(final CardType cardType) {
        if (cardType.equals(CardType.MELEE)) {
            for (final var cell : this.meleePanels) {
                cell.setHighlighted(true);
                cell.repaint();
            }
        } else if (cardType.equals(CardType.RANGED)) {
                this.rangedPanel.setHighlighted(true);
                this.rangedPanel.repaint();
        }
    }

    /**
     * Stops highlighting the cells of the given CardType.
     * @param cardType the cardType
     */
    public void stopHighLight(final CardType cardType) {
        if (cardType.equals(CardType.MELEE)) {
            for (final var cell : this.meleePanels) {
                cell.setHighlighted(false);
                cell.repaint();
            }
        } else if (cardType.equals(CardType.RANGED)) {
            this.rangedPanel.setHighlighted(false);
            this.rangedPanel.repaint();
        }
    }
}
