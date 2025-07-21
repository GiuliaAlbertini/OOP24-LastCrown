package it.unibo.oop.lastcrown.view.spell.impl;

import java.awt.image.BufferedImage;
import java.util.List;
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
    private final SpellAnimationPanel animationPanel;
    private final List<BufferedImage> spellImages;
    private final int frameSize;

    /**
     * @param spellName the name of the spell
     * @param size the size of the spell animation
     */
    public SpellGUIImpl(final String spellName, final int size) {
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
        for (final BufferedImage bufferedImage : spellImages) {
            this.nextFrame(bufferedImage);
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
