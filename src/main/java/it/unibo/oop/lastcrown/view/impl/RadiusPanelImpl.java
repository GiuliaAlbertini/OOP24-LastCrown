package it.unibo.oop.lastcrown.view.impl;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.api.Point2D;
import it.unibo.oop.lastcrown.model.api.Radius;

public class RadiusPanelImpl implements RadiusPanel{
    private final Radius radius;
    private final JPanel radiusPanel;
    private final HitboxMaskBounds maskBounds;

    public RadiusPanelImpl(Radius radius, HitboxMaskBounds maskBounds) {
        this.radius = radius;
        this.maskBounds = maskBounds;
        this.radiusPanel = createPanel();
        configurePanel();
    }

    private JPanel createPanel() {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawRadius(g);
            }
        };
    }

    private void configurePanel() {
        radiusPanel.setOpaque(false); 
        updatePanelBounds();
    }

    private void drawRadius(Graphics g) {
        g.setColor(new Color(0, 0, 255, 80));
        int diameter = (int)(radius.getRadius() * 2);
        // Disegna centrato nel pannello
        g.fillOval(0, 0, diameter, diameter);
    }

    private void updatePanelBounds() {
        int diameter = (int)(radius.getRadius() * 2);
        Point2D center = maskBounds.getCenter();
        int x = (int)(center.x() + maskBounds.getCharComponent().getX() - radius.getRadius());
        int y = (int)(center.y() + maskBounds.getCharComponent().getY() - radius.getRadius());
        
        radiusPanel.setBounds(x, y, diameter, diameter);
    }

    public JPanel getRadiusPanel() {
        return this.radiusPanel;
    }

    // Aggiungi questo metodo per aggiornare la posizione
    public void updatePosition() {
        updatePanelBounds();
        radiusPanel.repaint();
    }
}