package it.unibo.oop.lastcrown.view.shop;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.CustomLock;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * A class that extends a JPanel and contains the animation of a trader.
 */
public final class TraderPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(TraderPanel.class.getName());
    private static final String TRADER = "trader";
    private static final int TIME = 100;
    private int id;
    private ContainerObserver obs;
    private CardType type;
    private volatile boolean done;
    private int cont;
    private transient CustomLock lock;
    private transient Image currentImage;
    private transient List<BufferedImage> stopImages;
    private transient List<BufferedImage> openImages;
    private transient List<BufferedImage> closeImages;
    private transient List<BufferedImage> approvalImages;
    private boolean mouseEnabled = true;
    private TraderPanel() { }

    /**
     * @param id the numerical id of this trader
     * @param obs the observer of the container
     * @param name the name of the trader
     * @param type the CardType that this trader will show
     * @param width the width of the trader panel
     * @param height the height of the trader panel
     * @return a new TraderPanel
     */
    public static TraderPanel create(final int id, final ContainerObserver obs, final String name,
     final CardType type, final int width, final int height) {
        final TraderPanel instance = new TraderPanel();
        instance.init(id, obs, name, type, width, height);
        return instance;
    }

    private void init(final int id, final ContainerObserver obs, final String name, final CardType type,
     final int width, final int height) {
        this.id = id;
        this.obs = obs;
        this.type = type;
        this.stopImages = ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(
            TRADER, name, Keyword.STOP.get()), width, height);
        this.openImages = ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(
            TRADER, name, Keyword.OPEN.get()), width, height);
        this.closeImages = ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(
            TRADER, name, Keyword.CLOSE.get()), width, height);
        this.approvalImages = ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(
            TRADER, name, Keyword.APPROVAL.get()), width, height);
        this.lock = new CustomLock();
        this.setLayout(null);
        this.setFocusable(false);
        this.setOpaque(false);
        this.setSize(width, height);
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (mouseEnabled) {
                   startAnimation(openImages, Keyword.OPEN);
                   mouseEnabled = false;
                }
            }
        });
    }

    /**
     * Starts the stop loop animation.
     */
    public void startStopLoop() {
        this.done = true;
        new Thread(() -> {
            this.lock.acquireLock();
            this.cont = 0;
            this.done = false;
            while (!done) {
                this.currentImage = this.stopImages.get(cont);
                this.repaint();
                this.cont = (this.cont + 1) % this.stopImages.size();
                try {
                    Thread.sleep(TIME);
                } catch (final InterruptedException e) {
                    LOG.fine("Error occurred during trader stop animation");
                }
            }
            this.lock.releaseLock();
        }).start();
    }

    /**
     * Starts the Close animation sequence.
     */
    public void startCloseSequence() {
        this.startAnimation(this.closeImages, Keyword.CLOSE);
        this.mouseEnabled = true;
    }

    /**
     * Starts the Approval animation sequence.
     */
    public void startApprovalSequence() {
        this.startAnimation(this.approvalImages, Keyword.APPROVAL);
        this.mouseEnabled = false;
    }

    private void startAnimation(final List<BufferedImage> frames, final Keyword keyword) {
        this.done = true;
        new Thread(() -> {
            this.lock.acquireLock();
            for (final var frame : frames) {
                this.currentImage = frame;
                this.repaint();
                try {
                    Thread.sleep(TIME);
                } catch (final InterruptedException e) {
                    LOG.fine("Error occurred during trader open animation");
                }
            }
            this.lock.releaseLock();
            if (keyword.equals(Keyword.OPEN)) {
                this.obs.notifyInteraction(this.id, this.type);
            } else if (keyword.equals(Keyword.CLOSE)) {
                this.startStopLoop();
            } else if (keyword.equals(Keyword.APPROVAL)) {
                this.startAnimation(this.closeImages, Keyword.CLOSE);
            }
        }).start();
    }

    /**
     * Must be called when the shop JFrame is not visible. Stops the animation of this trader.
     */
    public void stopAnimations() {
        this.done = true;
        this.mouseEnabled = true;
    }

    @Override
    public void paint(final Graphics g) {
        super.paint(g);
        g.drawImage(currentImage, 0, 0, this);
    }
}
