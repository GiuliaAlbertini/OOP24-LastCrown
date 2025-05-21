package it.unibo.oop.lastcrown.view;

import java.awt.image.BufferedImage;

import javax.swing.JComponent;

/**
 * An interface that permits to return an animation panel.
 */
public interface ReadOnlyAnimationPanel {

    /**
     * @return the animation panel linked to this view element.
     */
    JComponent getComponent();

    /**
     * @return the current frame shown on the panel.
     */
    BufferedImage getCurrentFrame();
}
