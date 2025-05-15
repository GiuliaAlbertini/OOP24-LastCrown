package it.unibo.oop.lastcrown.view.spell.impl;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.view.AnimationPanel;
import it.unibo.oop.lastcrown.view.ReadOnlyAnimationPanel;

/**
 * The panel linked to a specific spell.
 */
final class SpellAnimationPanel extends JComponent implements AnimationPanel, ReadOnlyAnimationPanel {
    private static final long serialVersionUID = 1L;
    private transient Image currentImage;

    private SpellAnimationPanel() { }

    /**
     * Creates a new instance of SpellAnimationPanel.
     * @param width the horizontal size of this animation panel
     * @param height the vertical size of this animation panel
     * @return new SpellAnimationPanel
     */
    public static SpellAnimationPanel create(final int width, final int height) {
        final SpellAnimationPanel instance = new SpellAnimationPanel();
        instance.init(width, height);
        return instance;
    }

    private void init(final int width, final int height) {
        this.setLayout(null);
        this.setOpaque(false);
        this.setSize(width, height);
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    /**
     * Set the next frame to be shown in this panel.
     * @param image the frame to be shown
     */
    public void setSpellImage(final Image image) {
        this.currentImage = image;
        this.repaint();
    }

    @Override
    public void paint(final Graphics g) {
        super.paintComponent(g);
        g.drawImage(this.currentImage, 0, 0, this);
    }
}
