package it.unibo.oop.lastcrown.view.collision.impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.collision.impl.GameState;
import it.unibo.oop.lastcrown.view.collision.api.GamePanel;

/**
 * Implementation of the GamePanel interface.
 * Represents the main panel used during gameplay to hold character components
 * and now also the main game map.
 */
public final class GamePanelImpl implements GamePanel {
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;
    private final JPanel panel;
    private final JButton addCharacterBtn = new JButton("Aggiungi personaggio");

    /**
     * Constructs the main game panel implementation.
     * Initializes the layout, sets up the background and button panel,
     * and prepares the panel to receive character components during gameplay.
     * Also configures the "Add Character" button for interaction.
     */
    public GamePanelImpl() {
        panel = new JPanel(new BorderLayout());
        panel.setName(GameState.GAME.toString());
        panel.setBackground(Color.LIGHT_GRAY);
        resize(panel);
        panel.setFocusable(true);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(addCharacterBtn);
        addCharacterBtn.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        addCharacterBtn.setFocusPainted(false);

        panel.add(buttonPanel, BorderLayout.EAST);
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
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void setAddCharacterListener(final ActionListener listener) {
        addCharacterBtn.addActionListener(listener);
    }

    private void resize(final JPanel panel) {
        var dim = Toolkit.getDefaultToolkit().getScreenSize();
        dim = new Dimension((int) (dim.getWidth()), (int) (dim.getHeight()));
        panel.setSize(dim);
    }
}
