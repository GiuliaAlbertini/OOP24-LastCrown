package it.unibo.oop.lastcrown.view.shop;

import java.awt.KeyboardFocusManager;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import it.unibo.oop.lastcrown.controller.HeroInShopObserver;
import it.unibo.oop.lastcrown.view.Dialog;
import it.unibo.oop.lastcrown.view.dimensioning.DimensionResolver;

/**
 * The main Window of the shop.
 */
public final class ShopView extends JFrame implements ContainerObserver {
    private static final long serialVersionUID = 1L;
    private static final int ESCAPE_TAX = 100;
    private static final int DELAY = 5000;
    private static final double FLOOR_HEIGHT = 0.75;
    private static final String TRADER1 = "trader1";
    private static final String TRADER2 = "trader2"; 
    private static final String TRADER3 = "trader3";
    private final int width;
    private final int floor;
    private final int panelSize;
    private final ShopContent shopContent;
    private final TraderPanel panel1;
    private final TraderPanel panel2;
    private final TraderPanel panel3;
    private final transient HeroInShopObserver obs;

    /**
     * @param heroObs the observer of the hero action in the shop
     * @param width the width of this JFrame
     * @param height the height of this JFrame
     */
    public ShopView(final HeroInShopObserver heroObs, final int width, final int height) {
        this.obs = heroObs;
        this.width = width;
        this.floor = (int) (height * FLOOR_HEIGHT);
        this.panelSize = (int) (width * DimensionResolver.TRADER.width());
        this.shopContent = new ShopContent(heroObs, this, width, height);
        this.setSize(width, height);
        this.getContentPane().add(shopContent);
        this.setResizable(false);
        this.setFocusableWindowState(true);
        this.panel1 = TraderPanel.create(TRADER1, panelSize, panelSize);
        this.panel2 = TraderPanel.create(TRADER2, panelSize, panelSize);
        this.panel3 = TraderPanel.create(TRADER3, panelSize, panelSize);
        this.addTraderPanel(panel1, shopContent.getWidth() / 4, shopContent.getHeight() / 2);
        this.addTraderPanel(panel2, shopContent.getWidth() / 2, shopContent.getHeight() / 2);
        this.addTraderPanel(panel3, shopContent.getWidth() / 4 * 3, shopContent.getHeight() / 2);
    }

    private void addTraderPanel(final JComponent component, final int x, final int y) {
        this.shopContent.add(component);
        this.shopContent.setComponentZOrder(component, 0);
        component.setLocation(x - this.panelSize / 2, y - this.panelSize / 2);
        component.repaint();
    }

    /**
     * Adds the hero panel to the shop by specifing if it is the beginning of the game.
     * @param heroComp the JComponent where the hero animation is located
     * @param beginning True if it is the beginning of the game, false otherwise
     */
    public void addHeroPanel(final JComponent heroComp, final boolean beginning) {
        this.shopContent.add(heroComp);
        if (beginning) {
            heroComp.setLocation(0, this.floor - heroComp.getHeight());
        } else {
            heroComp.setLocation(this.width - heroComp.getWidth(), this.floor - heroComp.getHeight());
        }
        this.shopContent.setComponentZOrder(heroComp, 0);
    }

    /**
     * Notifies to the shop JFrame that it is visible to the player.
     */
    public void notifyVisible() {
        SwingUtilities.invokeLater(() -> {
            KeyboardFocusManager.getCurrentKeyboardFocusManager().clearGlobalFocusOwner();
            shopContent.requestFocusInWindow();
        });
        panel1.startStopLoop();
        panel2.startStopLoop();
        panel3.startStopLoop();
    }

    /**
     * Notifies to the shop JFrame that it is hidden to the player.
     */
    public void notifyHidden() {
        panel1.stopAnimations();
        panel2.stopAnimations();
        panel3.stopAnimations();
    }

    @Override
    public void notifyExit() {
        final String title = "To a new Match...";
        final String message = "Do you really want to start a new match?";
        final Dialog dialog = new Dialog(title, message, true);
        final JButton yes = new JButton("YES");
        yes.addActionListener(e -> {
            dialog.dispose();
            this.obs.notifyMatchView();
        });
        dialog.addButton(yes);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    @Override
    public void notifyEscape() {
    final String title = "Quitting...";
    final String message = "Do you really want to escape? You will mantain all the acquirements"
     + " obtained after the last match but you must pay " + ESCAPE_TAX + " coins";
    final Dialog dialog = new Dialog(title, message, true);
    final JButton escape = new JButton("ESCAPE");
    escape.addActionListener(act -> {
        dialog.dispose();
        //System.exit(0);
    });
    dialog.addButton(escape);
    dialog.setLocationRelativeTo(this);
    dialog.setVisible(true);
    }

    @Override
    public void notifyInteraction() {
        this.panel1.startOpenSequence();
        this.panel2.startOpenSequence();
        this.panel3.startOpenSequence();
        SwingUtilities.invokeLater(() -> {
            final Timer t = new Timer(DELAY, ev -> {
                this.panel1.startApprovalSequence();
                this.panel2.startApprovalSequence();
                this.panel3.startApprovalSequence();
                this.shopContent.interactionEnded();
            });
            t.setRepeats(false);
            t.start();
        });
    }

    @Override
    public void notifyCollection() {
        //System.out.println("Opening collection...");
    }
}
