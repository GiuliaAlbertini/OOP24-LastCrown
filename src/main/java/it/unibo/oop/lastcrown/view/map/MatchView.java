package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.controller.MainController;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * the JFrame that contains the match map. Provides methods to add further components to the map.
 */
public final class MatchView extends JFrame {
    private static final long serialVersionUID = 1L;
    private final MatchPanel mainPanel;
    private final transient MainController mainContr;
    private final Map<Integer, JComponent> newComponents;

    /**
     * @param mainController the main controller linked to the map
     * @param frameWidth the width of the map
     * @param frameHeight the height of the map
     */
    public MatchView(final MainController mainController, final int frameWidth, final int frameHeight) {
        this.mainContr = mainController;
        this.newComponents = new ConcurrentHashMap<>();
        this.setFocusableWindowState(false);
        this.mainPanel = new MatchPanel(mainController, frameWidth, frameHeight);
        this.setSize(new Dimension(frameWidth, frameHeight));
        this.setLayout(null);
        this.setContentPane(mainPanel);
        this.setResizable(false);
        this.pack();
    }

    /**
     * Shows to the player a custom modal dialog that closes the match and shows the menu when given the OK.
     */
    public void disposeDefeat() {
        final String title = "Match lost...";
        final String message = "YOU HAVE LOST, you will return to the menu";
        final Dialog defeat = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
            defeat.dispose();
            mainContr.notifyEndOfTheGame();
        });
        defeat.addButton(ok);
        defeat.setLocationRelativeTo(this);
        defeat.setVisible(true);
    }

    /**
     * Shows the player a custom modal dialog that closes the match and shows the shop when given the OK.
     */
    public void disposeVictory() {
        final String title = "Victory achieved...";
        final String message = "YOU HAVE WON!! you will go to the shop";
        final Dialog victory = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
            victory.dispose();
            mainContr.notifyMatchToShop(false);
        });
        victory.addButton(ok);
        victory.setLocationRelativeTo(this);
        victory.setVisible(true);
    }

    /**
     * @return the Trups zone limit of the x coordinate
     */
    public int getTrupsZoneLimit() {
        return this.mainPanel.getTrupsZoneLimit();
    }

    /**
     * Add a generic JComponent to the map, centered in the specified coordinates.
     * @param id the numerical id of the new graphic component
     * @param component the generic JComponent
     * @param x x coordinate
     * @param y y coordinate
     */
    public synchronized void addGenericGraphics(final int id, final JComponent component, final int x, final int y) {
        this.newComponents.put(id, component);
        final Dimension size = component.getPreferredSize();
        component.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);
        this.mainPanel.add(component);
        this.mainPanel.setComponentZOrder(component, 1);
        this.mainPanel.repaint();
    }

    /**
     * Add a generic hero graphic component. The position is already known.
     * @param heroGraphics the generic hero graphic component
     */
    public synchronized void addHeroGraphics(final JComponent heroGraphics) {
        final int cardZoneWidth = (int) (this.getWidth() * DimensionResolver.DECKZONE.width());
        final int posZoneWidth = (int) (this.getWidth() * DimensionResolver.POSITIONINGZONE.width());
        final int panelsHeight = this.getHeight() - (int) (this.getHeight() * DimensionResolver.UTILITYZONE.height());
        final int cornerWidth = cardZoneWidth + posZoneWidth / 2;
        final int cornerHeight = panelsHeight / 4;
        heroGraphics.setBounds(cornerWidth, cornerHeight, 
         heroGraphics.getPreferredSize().width, heroGraphics.getPreferredSize().height);
        this.mainPanel.add(heroGraphics);
        this.mainPanel.setComponentZOrder(heroGraphics, 0);
        this.mainPanel.repaint();
    }

    /**
     * Remove safely a graphic component associated with the specified id from the map.
     * @param id the id linked to the component to eliminate
     */
    public synchronized void removeGraphicComponent(final int id) {
        SwingUtilities.invokeLater(() -> {
            final var component = this.newComponents.get(id);
            if (component != null) {
                this.mainPanel.remove(this.newComponents.get(id));
                this.mainPanel.repaint();
                this.newComponents.remove(id);
            }
        });
    }

    /**
     * Removes all the new graphic components from the map.
     */
    public synchronized void clearNewGraphicsComponent() {
        final var entrySet = this.newComponents.entrySet();
        for (final var entry: entrySet) {
                this.mainPanel.remove(entry.getValue());
        }
        this.newComponents.clear();
    }
}

