package it.unibo.oop.lastcrown.view.characters.api;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * An interface that handles the graphic side of a generic character.
 */
public interface GenericCharacterGUI {

     /**
     * @return the death animation size (the length of the corresponding list)
     */
    int getDeathAnimationSize();

    /**
     * Must be called before everything else. Creates a new animation panel for this character.
     */
    void createAnimationPanel();

    /**
     * @return the read only version of the graphical component linked to this character.
     */
    JComponent getGraphicalComponent();

    /**
     * Set the new health bar percent linked to this character.
     * @param percentage
     */
    void setHealthPercentage(int percentage);

    /**
     * Set the new animation to be used.
     * @param animation the next animation
     */
    void setNextAnimation(Keyword animation);

    /**
     * Set the next frame of the current animation to be shown on the panel.
     */
    void setNextFrame();

    /**
     * Set the next frame to be shown and the movement of the linked panel.
     * @param x horizontal movement
     * @param y vertical movement
     */
    void setNextFrameAndMovement(int x, int y);
}
