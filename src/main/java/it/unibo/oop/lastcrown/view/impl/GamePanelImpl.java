package it.unibo.oop.lastcrown.view.impl;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.GamePanel;

public class GamePanelImpl implements GamePanel{
    private final JPanel panel;

    public GamePanelImpl() {
        panel = new JPanel(null);  // layout assoluto
        panel.setName(GameState.GAME.toString());
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setPreferredSize(new Dimension(800, 600));
    }
    
    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public void addCharacterComponent(JComponent comp) {
        panel.add(comp);
        panel.setComponentZOrder(comp, 0);
        panel.revalidate();
        panel.repaint();
    }

    @Override
    public void removeCharacterComponent(JComponent comp) {
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

