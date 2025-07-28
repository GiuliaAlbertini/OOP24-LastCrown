package it.unibo.oop.lastcrown.view.map;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;
import it.unibo.oop.lastcrown.view.menu.api.MainView;


/**
 * the JFrame that contains the match map. Provides methods to add further
 * components to the map.
 */
public final class MatchViewImpl extends JPanel implements MatchView, MatchExitObserver {
    private static final long serialVersionUID = 1L;
    private final transient MainView mainView;
    private final MatchPanel mainPanel;
    MatchController matchController;
    private final Map<Integer, JComponent> newComponents;

    /**
     * @param gameContr the main controller linked to the map
     * @param mainView  the main view interface of the application
     * @param width     the width of the map
     * @param height    the height of the map
     * @param deck      the set to use as a deck
     */
    public MatchViewImpl(final MatchController matchContr, final MainView mainView,
            final int width, final int height, final Set<CardIdentifier> deck) {
        this.mainView = mainView;
        this.matchController=matchContr;
        this.newComponents = new ConcurrentHashMap<>();
        this.mainPanel = new MatchPanel(this, matchContr, matchContr.getWallHealthBar(),
                matchContr.getEventWriter(), matchContr.getCoinsWriter(), width, height, deck);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBounds(0, 0, width, height);
    }

    @Override
    public void disposeDefeat() {
        final String title = "Match lost...";
        final String message = "YOU HAVE LOST, you will return to the menu";
        final Dialog defeat = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
            defeat.dispose();
            this.mainView.changePanel(SceneName.MATCH, SceneName.MENU);
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
            this.mainView.changePanel(SceneName.MATCH, SceneName.SHOP);
            // gameContr.notifyMatchToShop(false);
        });
        victory.addButton(ok);
        victory.setLocationRelativeTo(this);
        victory.setVisible(true);
    }

    @Override
    public synchronized HitboxController addGenericGraphics(final int id, final JComponent component, final int x, final int y, String typefolder, String name) {
        this.newComponents.put(id, component);
        final Dimension size = component.getPreferredSize();
        component.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);

        this.mainPanel.add(component);
        this.mainPanel.setComponentZOrder(component, 1);

        final HitboxController hitboxcontroller= matchController.setupCharacter(component, typefolder, name, true, component.getX(), component.getY());

        this.mainPanel.add(hitboxcontroller.getHitboxPanel());
        this.mainPanel.add(hitboxcontroller.getRadiusPanel());
        this.mainPanel.setComponentZOrder(hitboxcontroller.getHitboxPanel(), 1);
        this.mainPanel.setComponentZOrder(hitboxcontroller.getRadiusPanel(), 1);

        this.mainPanel.repaint();
        return hitboxcontroller;
    }


    @Override
    public synchronized void addHeroGraphics(final JComponent heroGraphics) {
        final int cardZoneWidth = (int) (this.getPreferredSize().width * DimensionResolver.DECKZONE.width());
        final int posZoneWidth = (int) (this.getPreferredSize().width * DimensionResolver.POSITIONINGZONE.width());
        final int panelsHeight = this.getPreferredSize().height - (int) (this.getPreferredSize().height * DimensionResolver.UTILITYZONE.height());
        final int cornerWidth = cardZoneWidth + posZoneWidth / 2;
        final int cornerHeight = panelsHeight / 4;
        heroGraphics.setBounds(cornerWidth, cornerHeight, heroGraphics.getPreferredSize().width,
                heroGraphics.getPreferredSize().height);
        this.mainPanel.add(heroGraphics);
        this.mainPanel.setComponentZOrder(heroGraphics, 0);
        this.mainPanel.repaint();
    }


    // @Override
    // public synchronized HitboxController addEnemyGraphics(final int id, final JComponent enemyGraphics, final int x, final int y, String typefolder, String name) {
    //     this.newComponents.put(id, enemyGraphics);
    //     final Dimension size = enemyGraphics.getPreferredSize();
    //     enemyGraphics.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);

    //     final HitboxController hitboxcontroller= matchController.setupCharacter(enemyGraphics, typefolder, name, false, enemyGraphics.getX(), enemyGraphics.getY());
    //     this.mainPanel.add(hitboxPanel.getHitboxPanel());
    //     this.mainPanel.add(enemyGraphics);
    //     this.mainPanel.setComponentZOrder(enemyGraphics, 1);
    //     this.mainPanel.repaint();
    //     return hitboxcontroller;
    // }

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
        for (final var entry : entrySet) {
            this.mainPanel.remove(entry.getValue());
        }
        this.newComponents.clear();
    }

    @Override
    public SceneName getSceneName() {
        return SceneName.MATCH;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public void notifyExitToMenu() {
        this.mainView.changePanel(SceneName.MATCH, SceneName.MENU);
    }

    // zona truppe (confine)
    @Override
    public int getTrupsZoneLimit() {
        return this.mainPanel.getTrupsZoneLimit();
    }

    @Override
    public Dimension getWallSize() {
        return this.mainPanel.getWallSize(); // dimension
    }

    @Override
    public Point getWallCoordinates() {
        return this.mainPanel.getWallCoordinates(); // Point2D
    }

    @Override
    public void updateInGameDeck(final Set<CardIdentifier> newDeck) {
        this.mainPanel.updateInGameDeck(newDeck);
    }
}
