package it.unibo.oop.lastcrown.view.collision.impl;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.collision.api.Point2D;
import it.unibo.oop.lastcrown.model.collision.api.Radius;
import it.unibo.oop.lastcrown.view.collision.api.RadiusPanel;

/**
 * Implementation of a panel that graphically represents a character's detection radius
 * using a semi-transparent arc overlay. This is typically used for debugging or visualization.
 */
public final class RadiusPanelImpl implements RadiusPanel {

    private static final int ARC_START_ANGLE = -90;
    private static final int ARC_ANGLE_EXTENT = 180;
    private static final int RADIUS_ALPHA = 80;
    private static final Color RADIUS_COLOR = new Color(0, 0, 255, RADIUS_ALPHA); // Semi-transparent blue

    private final Radius radius;
    private final JPanel radiusPanel;
    private final HitboxMaskBounds maskBounds;

    /**
     * Constructs a RadiusPanelImpl with a specified radius model and mask bounds to align with.
     *
     * @param radius the radius model representing detection range
     * @param maskBounds the component providing position and center offset
     */
    public RadiusPanelImpl(final Radius radius, final HitboxMaskBounds maskBounds) {
        this.radius = radius;
        this.maskBounds = maskBounds;
        this.radiusPanel = createPanel();
        configurePanel();
    }

    private JPanel createPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(final Graphics g) {
                super.paintComponent(g);
                drawRadius(g);
            }
        };
    }

    private void configurePanel() {
        radiusPanel.setOpaque(false);
        updatePanelBounds();
    }


    private void drawRadius(final Graphics g) {
        g.setColor(RADIUS_COLOR);
        final int diameter = (int) (radius.getRadius() * 2);
        g.fillArc(0, 0, diameter, diameter, ARC_START_ANGLE, ARC_ANGLE_EXTENT);
    }

    private void updatePanelBounds() {
        final int diameter = (int) (radius.getRadius() * 2);
        final Point2D center = maskBounds.getCenter();
        final int x = (int) (center.x() + maskBounds.getCharComponent().getX() - radius.getRadius());
        final int y = (int) (center.y() + maskBounds.getCharComponent().getY() - radius.getRadius());
        radiusPanel.setBounds(x, y, diameter, diameter);
    }

    @Override
    public JPanel getRadiusPanel() {
        return this.radiusPanel;
    }

    @Override
    public void updatePosition() {
        updatePanelBounds();
        radiusPanel.repaint();
    }
}
