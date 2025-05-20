package it.unibo.oop.lastcrown.view.spell.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.spell.api.SpellGUI;

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
    public final JComponent getGraphicalComponent() {
        return this.animationPanel.getComponent();
    }

    @Override
    public final void startAnimation() {
        new Thread(this::startAnimationSequence).start();
    }

    /**
     * Start this spell animation sequence. At the end remove the spell panel from the superior panel.
     */
    private void startAnimationSequence() {
        if (this.duration.isEmpty()) {
            for (final BufferedImage bufferedImage : spellImages) {
                this.nextFrame(bufferedImage);
            }
        } else {
            boolean finished = false;
            final int startTime = (int) System.currentTimeMillis();
            while (!finished) {
                final int elapsedTime = (int) (System.currentTimeMillis() - startTime);
                if (elapsedTime >= this.duration.get() * MILLIS) {
                    finished = true;
                } else {
                    frameCont = (frameCont + 1) % this.spellImages.size();
                    this.nextFrame(this.spellImages.get(frameCont));
                }
            }
        }
        this.nextFrame(null);
    }

    /**
     * Set the next animation frame to be shown.
     * @param frame the next frame to be shown
     */
    private  void nextFrame(final BufferedImage frame) {
        animationPanel.setSpellImage(frame);
        try {
            Thread.sleep(TIME);
        } catch (final InterruptedException e) {
            LOG.fine("Error occurred during spell animation");
        }
    }
}
