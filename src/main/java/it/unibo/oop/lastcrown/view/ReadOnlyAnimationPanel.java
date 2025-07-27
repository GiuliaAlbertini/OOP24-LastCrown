package it.unibo.oop.lastcrown.view;

import javax.swing.JComponent;

/**
 * An interface that permits to get a read only version of an animation panel.
 */
public interface ReadOnlyAnimationPanel {

    /**
     * @return a read only version of the animation panel.
     */
    JComponent getComponent();
}
