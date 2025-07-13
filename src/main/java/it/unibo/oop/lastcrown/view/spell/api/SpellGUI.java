package it.unibo.oop.lastcrown.view.spell.api;

import javax.swing.JComponent;

/**
 * Handles all the view aspect of a specific spell.
 */
public interface SpellGUI {

    /**
     * @return the graphical linked to this spell
     */
    JComponent getGraphicalComponent();

    /**
     * Starts the animatio sequence of this spell.
     */
    void startAnimation();
}
