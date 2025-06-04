package it.unibo.oop.lastcrown.view;

import javax.swing.JComponent;

/**
 * An interface that permits to return an animation panel.
 */
public interface ReadOnlyAnimationPanel {

    /**
     * @return the animation panel linked to this view element.
     */
    JComponent getComponent();
}
