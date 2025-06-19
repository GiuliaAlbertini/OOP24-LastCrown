package it.unibo.oop.lastcrown.view.impl;

import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JComponent;
import javax.swing.JPanel;
import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.GamePanel;

/**
 * Implementation of the GamePanel interface.
 * Represents the main panel used during gameplay to hold character components.
 */
public final class GamePanelImpl implements GamePanel {
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 600;
    private final JPanel panel;

    /**
     * Constructs a new GamePanelImpl and initializes the panel
     * with null layout, background color, and preferred size.
     */
    public GamePanelImpl() {
        panel = new JPanel(null);
        panel.setName(GameState.GAME.toString());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void addCharacterComponent(final JComponent comp) {
        panel.add(comp);
        panel.setComponentZOrder(comp, 0);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void removeCharacterComponent(final JComponent comp) {
        panel.remove(comp);
        panel.revalidate();
        repaintGamePanel();
    }
    @Override
    public void repaintGamePanel() {
        panel.revalidate(); // Ricalcola il layout
        panel.repaint();    // Ridisegna il pannello
    }
}

