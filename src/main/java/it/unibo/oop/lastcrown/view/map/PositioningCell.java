package it.unibo.oop.lastcrown.view.map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * Utility class that defines a JPanel that can be highlighted when needed.
 */
public final class PositioningCell extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final int BACKGROUND = 230;
    private static final int RED = 0;
    private static final int GREEN = 120;
    private static final int BLUE = 255;
    private final Color backgroundColor = new Color(BACKGROUND, BACKGROUND, BACKGROUND);
    private final Color highLightedColor = new Color(RED, GREEN, BLUE);
    private boolean highLighted;

    /**
     * Creates a new PositioningCell.
     */
    public PositioningCell() {
        this.setOpaque(true);
        this.setLayout(null);
    }

    /**
     * @param highLighted True to be considered highlighted, False otherwise.
     */
    public void setHighlighted(final boolean highLighted) {
        this.highLighted = highLighted;
    }

    @Override
    protected void paintComponent(final Graphics g) {
         super.paintComponent(g);

        final Graphics2D g2 = (Graphics2D) g.create();
        if (highLighted) {
            g2.setColor(backgroundColor);
            g2.fillRect(0, 0, getWidth(), getHeight());
            g2.setColor(highLightedColor);
            g2.setStroke(new BasicStroke(3));
            g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 16, 16);
        }
        g2.dispose();
    }
}
