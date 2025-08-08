package it.unibo.oop.lastcrown.view.map;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.menu.api.SceneManager;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.SceneName;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * the JFrame that contains the match map. Provides methods to add further
 * components to the map.
 */
@SuppressFBWarnings(
    value = "EI_EXPOSE_REP2",
    justification = """
        This class is a core View component.
        It must hold a direct reference to the 'live' MatchController to delegate
        user actions and manage child components.
        A defensive copy of a controller cannot be done in this context.
        """
)
public final class MatchViewImpl extends JPanel implements MatchView, MatchExitObserver {
    private static final long serialVersionUID = 1L;
    private static final Font UI_FONT = new Font("Calibri", Font.CENTER_BASELINE, 20);

    private final MatchPanel mainPanel;
    private final transient MatchController matchController;
    private final Map<Integer, JComponent> newComponents;
    private final transient SceneManager sceneManager;
    private final JTextArea eventWriter;
    private final JTextArea coinsWriter;

    /**
     * @param sceneManager the SceneManager of the application
     * @param matchContr   the match controller linked to the map
     * @param width        the width of the map
     * @param height       the height of the map
     * @param deck         the set to use as a deck
     */
    public MatchViewImpl(final SceneManager sceneManager, final MatchController matchContr,
            final int width, final int height, final Set<CardIdentifier> deck) {
        this.sceneManager = sceneManager;
        this.matchController = matchContr;
        this.newComponents = new ConcurrentHashMap<>();
        this.eventWriter = createEventWriter();
        this.coinsWriter = createCoinsWriter();
        this.mainPanel = new MatchPanel(this, matchContr, matchContr.getWallHealthBar(),
                this.eventWriter, this.coinsWriter, width, height, deck);
        this.setPreferredSize(new Dimension(width, height));
        this.setLayout(new BorderLayout());
        this.add(mainPanel, BorderLayout.CENTER);
        mainPanel.setBounds(0, 0, width, height);
    }

    private JTextArea createEventWriter() {
        final JTextArea writer = new JTextArea();
        writer.setEditable(false);
        writer.setFocusable(false);
        writer.setFont(UI_FONT);
        return writer;
    }

    /**
     * Creates the money visualizer component.
     *
     * @return a {@link JTextArea} in which the current amount of money is
     *         displayed.
     */
    private JTextArea createCoinsWriter() {
        final JTextArea writer = new JTextArea();
        writer.setEditable(false);
        writer.setFocusable(false);
        writer.setFont(UI_FONT);

        final int currentCoins = matchController.getCurrentCoins();
        writer.setText(currentCoins + " coins");
        return writer;
    }

    @Override
    public void disposeDefeat() {
        final String title = "Match lost...";
        final String message = "YOU HAVE LOST, you will return to the menu";
        final Dialog defeat = new Dialog(title, message, false);
        final JButton ok = new JButton("OK");
        ok.addActionListener(act -> {
            defeat.dispose();
            this.sceneManager.switchScene(SceneName.MATCH, SceneName.MENU);
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
            this.sceneManager.switchScene(SceneName.MATCH, SceneName.SHOP);
            // gameContr.notifyMatchToShop(false);
        });
        victory.addButton(ok);
        victory.setLocationRelativeTo(this);
        victory.setVisible(true);
    }

    @Override
    public synchronized HitboxController addGenericGraphics(final int id, final JComponent component,
            final int x, final int y, final String typefolder, final String name) {
        this.newComponents.put(id, component);
        final Dimension size = component.getPreferredSize();
        component.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);

        this.mainPanel.add(component);
        this.mainPanel.setComponentZOrder(component, 0);

        final HitboxController hitboxcontroller = matchController.setupCharacter(
                component,
                typefolder,
                name,
                true,
                component.getX(),
                component.getY());
        this.newComponents.put(id + 1, hitboxcontroller.getHitboxPanel());
        this.newComponents.put(id + 2, hitboxcontroller.getRadiusPanel());

        this.mainPanel.add(hitboxcontroller.getHitboxPanel());
        this.mainPanel.add(hitboxcontroller.getRadiusPanel());
        this.mainPanel.setComponentZOrder(hitboxcontroller.getHitboxPanel(), 1);
        this.mainPanel.setComponentZOrder(hitboxcontroller.getRadiusPanel(), 1);

        this.mainPanel.repaint();
        return hitboxcontroller;
    }

    @Override
    public synchronized void addSpellGraphics(final int id, final JComponent component, final int x, final int y) {
        this.newComponents.put(id, component);
        final Dimension size = component.getPreferredSize();
        component.setBounds(x - size.width / 2, y - size.height / 2, size.width, size.height);
        this.mainPanel.add(component);
        this.mainPanel.setComponentZOrder(component, 0);
        this.mainPanel.repaint();
    }

    @Override
    public synchronized HitboxController addHeroGraphics(final int id, final JComponent heroGraphics,
            final String typefolder, final String name) {
        final int cardZoneWidth = (int) (this.getPreferredSize().width * DimensionResolver.DECKZONE.width());
        final int posZoneWidth = (int) (this.getPreferredSize().width * DimensionResolver.POSITIONINGZONE.width());
        final int panelsHeight = this.getPreferredSize().height
                - (int) (this.getPreferredSize().height * DimensionResolver.UTILITYZONE.height());
        final int cornerWidth = cardZoneWidth + posZoneWidth / 2;
        final int cornerHeight = panelsHeight / 4;
        heroGraphics.setBounds(
                cornerWidth,
                cornerHeight,
                heroGraphics.getPreferredSize().width,
                heroGraphics.getPreferredSize().height);
        this.mainPanel.add(heroGraphics);
        this.newComponents.put(id, heroGraphics);
        this.mainPanel.setComponentZOrder(heroGraphics, 1);

        final HitboxController hitboxcontroller = matchController.setupCharacter(
                heroGraphics,
                typefolder,
                name,
                true,
                heroGraphics.getX(),
                heroGraphics.getY());
        this.mainPanel.add(hitboxcontroller.getHitboxPanel());
        this.mainPanel.add(hitboxcontroller.getRadiusPanel());
        this.mainPanel.setComponentZOrder(hitboxcontroller.getHitboxPanel(), 1);
        this.mainPanel.setComponentZOrder(hitboxcontroller.getRadiusPanel(), 1);
        this.mainPanel.repaint();
        return hitboxcontroller;
    }

    @Override
    public synchronized HitboxController addEnemyGraphics(final int id, final JComponent enemyGraphics,
            final int x, final int y, final String typefolder, final String name) {
        this.newComponents.put(id, enemyGraphics);
        final Dimension size = enemyGraphics.getPreferredSize();
        enemyGraphics.setBounds(x, y, size.width, size.height);

        this.mainPanel.add(enemyGraphics);
        this.mainPanel.setComponentZOrder(enemyGraphics, 1);

        final HitboxController hitboxcontroller = matchController.setupCharacter(
                enemyGraphics,
                typefolder,
                name,
                false,
                enemyGraphics.getX(),
                enemyGraphics.getY());

        this.mainPanel.add(hitboxcontroller.getHitboxPanel());
        this.mainPanel.setComponentZOrder(hitboxcontroller.getHitboxPanel(), 1);

        this.mainPanel.repaint();
        return hitboxcontroller;
    }

    @Override
    public void addWallPanel(final HitboxController hitboxController) {
        this.mainPanel.add(hitboxController.getHitboxPanel());
        this.mainPanel.setComponentZOrder(hitboxController.getHitboxPanel(), 1);
        this.mainPanel.repaint();
    }

    @Override
    public void notifyBossFight(final boolean start) {
        this.mainPanel.notifyBossFight(start);
    }

    @Override
    public synchronized void removeGraphicComponent(final int id) {
        final HitboxController hitboxcontroller = matchController.getCharacterHitboxById(id).get();
        this.mainPanel.remove(hitboxcontroller.getHitboxPanel());

        final var radiusPanel = hitboxcontroller.getRadiusPanel();
        if (radiusPanel != null) {
            this.mainPanel.remove(hitboxcontroller.getRadiusPanel());
        }

        final var component = this.newComponents.get(id);
        if (component != null) {
            this.mainPanel.remove(this.newComponents.get(id));
            this.newComponents.remove(id);
        }
        this.mainPanel.repaint();
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
        this.sceneManager.switchScene(SceneName.MATCH, SceneName.MENU);
    }

    @Override
    public int getTrupsZoneLimit() {
        return this.mainPanel.getTrupsZoneLimit();
    }

    @Override
    public Dimension getWallSize() {
        return this.mainPanel.getWallSize();
    }

    @Override
    public Point getWallCoordinates() {
        return this.mainPanel.getWallCoordinates();
    }

    @Override
    public void updateInGameDeck(final Set<CardIdentifier> newDeck) {
        this.mainPanel.updateInGameDeck(newDeck);
    }

    @Override
    public void setEventText(final String text) {
        this.eventWriter.setText(text);
    }

    @Override
    public void setCoins(final int coins) {
        this.coinsWriter.setText(coins + " coins");
    }

    @Override
    public void notifyPause(final boolean pause) {
        this.mainPanel.notifyPause(pause);
    }
}
