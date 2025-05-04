package it.unibo.oop.lastcrown.view.spell;

import javax.swing.JPanel;

/**
 * Handles all the view aspect of a specific spell.
 */
public interface SpellGUI {

    /**
     * Set this spell panel position (Centered in the given coordinates).
     * @param matchPanel the panel where the spell animation must appear 
     * @param x x coordinate of the new spell panel
     * @param y y coordinate of the new spell panel
     */
    void setSpellAnimationPanelPosition(JPanel matchPanel, int x, int y);
}
