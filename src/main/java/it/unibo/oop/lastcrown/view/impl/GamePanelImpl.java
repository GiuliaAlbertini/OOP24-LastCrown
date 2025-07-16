package it.unibo.oop.lastcrown.view.impl;

import java.awt.*; // Importa tutti i tipi di awt
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.impl.GameState;
import it.unibo.oop.lastcrown.view.api.GamePanel;

/**
 * Implementation of the GamePanel interface.
 * Represents the main panel used during gameplay to hold character components
 * and now also the main game map.
 */
public final class GamePanelImpl implements GamePanel {
    private final JPanel panel; // Il JPanel interno che GamePanelImpl wrappa
    private static final int BUTTON_X = 10;
    private static final int BUTTON_Y = 50;
    private static final int BUTTON_WIDTH = 200;
    private static final int BUTTON_HEIGHT = 30;
    private final JButton addCharacterBtn = new JButton("Aggiungi personaggio");
    //private final SidePanel sidePanel;
    //private final MapPanelImpl mapPanel;

    public GamePanelImpl() {

        panel = new JPanel(new BorderLayout());
        panel.setName(GameState.GAME.toString());
        panel.setBackground(Color.LIGHT_GRAY);
        resize(panel);
        panel.setFocusable(true);

        //this.sidePanel = new SidePanelImpl(panel.getWidth(), panel.getHeight(), 200);
        //mapPanel = new MapPanelImpl(panel.getWidth(), panel.getHeight(), 500);

        final JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false); // o true se vuoi sfondo
        buttonPanel.add(addCharacterBtn);
        addCharacterBtn.setPreferredSize(new Dimension(200, 50));
        addCharacterBtn.setFocusPainted(false);

        panel.add(buttonPanel, BorderLayout.EAST);
        //panel.add((Component) sidePanel, BorderLayout.WEST);
        //panel.add((Component) mapPanel, BorderLayout.CENTER);

    }

    /*
    public SidePanel getSidePanel() {
        return sidePanel;
    }

    public MapPanelImpl getMapPanel() {
        return mapPanel;
    }
    */
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