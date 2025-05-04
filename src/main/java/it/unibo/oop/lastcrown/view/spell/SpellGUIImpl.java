package it.unibo.oop.lastcrown.view.spell;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.view.ImageLoader;

/**
 * A standard implementation of SpellGUI interface.
 */
public class SpellGUIImpl implements SpellGUI {
    private static final Logger LOG = Logger.getLogger(SpellGUIImpl.class.getName());
    private static final int TIME = 100;
    private static final int MILLIS = 1000;
    private final Optional<Integer> duration;
    private final SpellAnimationPanel animationPanel;
    private final List<BufferedImage> spellImages;
    private final int frameSize;
    private int frameCont;

    /**
     * @param spellName the name of the spell
     * @param duration the duration of a spell animation (and effect).
     * can be Optional.of(some duration is seconds) or Optional.empty()
     * @param size the size of the spell animation
     */
    public SpellGUIImpl(final String spellName, final Optional<Integer> duration, final int size) {
        this.duration = duration;
        this.frameSize = size;
        this.animationPanel = SpellAnimationPanel.create(this.frameSize, this.frameSize);
        this.spellImages = ImageLoader.getAnimationFrames(SpellPathLoader.loadSpellPaths(spellName),
         this.frameSize, this.frameSize);
    }

    @Override
    public final void setSpellAnimationPanelPosition(final JPanel matchPanel, final int x, final int y) {
        this.animationPanel.setBounds(x - this.frameSize / 2, y - this.frameSize / 2, this.frameSize, this.frameSize);
        matchPanel.add(animationPanel);
        matchPanel.repaint();
        new Thread(() -> this.startAnimationSequence(matchPanel)).start();
    }

    /**
     * Start this spell animation sequence. At the end remove the spell panel from the superior panel.
     * @param matchPanel the panel where the animation must appear
     */
    private void startAnimationSequence(final JPanel matchPanel) {
        if (this.duration.isEmpty()) {
            for (final BufferedImage bufferedImage : spellImages) {
                this.nextFrame(bufferedImage);
            }
            this.animationPanel.setSpellImage(null);
        } else {
            boolean finished = false;
            final int startTime = (int) System.currentTimeMillis();
            while (!finished) {
                final int elapsedTime = (int) (System.currentTimeMillis() - startTime);
                if (elapsedTime >= this.duration.get() * MILLIS) {
                    finished = true;
                    this.animationPanel.setSpellImage(null);
                } else {
                    frameCont = (frameCont + 1) % this.spellImages.size();
                    this.nextFrame(this.spellImages.get(frameCont));
                }
            }
        }
        matchPanel.remove(this.animationPanel);
        matchPanel.repaint();
    }

    /**
     * Set the next animation frame to be shown.
     * @param frame the next frame to be shown
     */
    private void nextFrame(final BufferedImage frame) {
        this.animationPanel.setSpellImage(frame);
        try {
            Thread.sleep(TIME);
        } catch (final InterruptedException e) {
            LOG.fine("Error occurred during spell animation");
        }
    }
}
