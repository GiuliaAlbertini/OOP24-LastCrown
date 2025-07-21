package it.unibo.oop.lastcrown.view.map;

import java.awt.Dimension;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import it.unibo.oop.lastcrown.controller.GameController;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.menu.api.MainView;

/**
 * the JFrame that contains the match map. Provides methods to add further components to the map.
 */
public final class MatchViewImpl extends JPanel implements MatchView, MatchExitObserver {
    private static final long serialVersionUID = 1L;
    private static final String NAME = "MATCH";
    private final transient MainView mainView;
    private final MatchPanel mainPanel;
    private final Map<Integer, JComponent> newComponents;

    /**
     * @param gameContr the main controller linked to the map
     * @param mainView the main view interface of the application
     * @param width the width of the map
     * @param height the height of the map
     */
    public MatchViewImpl(final GameController gameContr, final MainView mainView,
     final int width, final int height) {
        this.mainView = mainView;
        this.newComponents = new ConcurrentHashMap<>();
        this.mainPanel = new MatchPanel(this, gameContr, gameContr.getWallHealthBar(),
        gameContr.getEventWriter(), gameContr.getCoinsWriter(), width, height);
        this.add(mainPanel);
        mainPanel.setBounds(0, 0, width, height);
        this.setSize(new Dimension(width, height));
        this.setLayout(null);
    }

    @Override
    public void disposeDefeat() {
        final String title = "Match lost...";
        final String message = "YOU HAVE LOST, you will return to the menu";
        final Dialog defeat = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
           defeat.dispose();
           this.mainView.changePanel(NAME, "MENU");
        }); 
        defeat.addButton(ok);
        defeat.setLocationRelativeTo(this);
        defeat.setVisible(true);
    }

    @Override
    public void disposeVictory() {
        final String title = "Victory achieved...";
        final String message = "YOU HAVE WON!! you will go to the shop";
        final Dialog victory = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
            victory.dispose();
            this.mainView.changePanel(NAME, "SHOP");
            //gameContr.notifyMatchToShop(false);
        });
        victory.addButton(ok);
        victory.setLocationRelativeTo(this);
        victory.setVisible(true);
    }

    @Override
    public synchronized void addGenericGraphics(final int id, final JComponent component, final int x, final int y) {
        this.newComponents.put(id, component);
        final Dimension size = component.getPreferredSize();
        component.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);
        this.mainPanel.add(component);
        this.mainPanel.setComponentZOrder(component, 1);
        this.mainPanel.repaint();
    }

    @Override
    public synchronized void addHeroGraphics(final JComponent heroGraphics) {
        final int cardZoneWidth = (int) (this.getWidth() * DimensionResolver.DECKZONE.width());
        final int posZoneWidth = (int) (this.getWidth() * DimensionResolver.POSITIONINGZONE.width());
        final int panelsHeight = this.getHeight() - (int) (this.getHeight() * DimensionResolver.UTILITYZONE.height());
        final int cornerWidth = cardZoneWidth + posZoneWidth / 2;
        final int cornerHeight = panelsHeight / 4;
        heroGraphics.setBounds(cornerWidth, cornerHeight, 
         heroGraphics.getPreferredSize().width, heroGraphics.getPreferredSize().height);
        this.mainPanel.add(heroGraphics);
        this.mainPanel.setComponentZOrder(heroGraphics, 1);
        this.mainPanel.repaint();
    }

    @Override
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

   @Override
    public synchronized void clearNewGraphicsComponent() {
        final var entrySet = this.newComponents.entrySet();
        for (final var entry: entrySet) {
                this.mainPanel.remove(entry.getValue());
        }
        this.newComponents.clear();
    }

    @Override
    public String getSceneName() {
        return NAME;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void notifyExitToMenu() {
        this.mainView.changePanel(NAME, "MENU");
    }
}
