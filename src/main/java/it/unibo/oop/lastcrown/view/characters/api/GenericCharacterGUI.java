package it.unibo.oop.lastcrown.view.characters.api;

import javax.swing.JPanel;

/**
 * An interface that handles the graphic side of a generic character.
 */
public interface GenericCharacterGUI extends AnimationInterruptor {
    /**
     * Set the size of the animation panel linked to this character GUI.
     * @param newWidth new panel width
     * @param newHeight new panel height
     */
    void setAnimationPanelSize(int newWidth, int newHeight);

    /**
     * Set the position of the animation panel linked to this character GUI (centered on the coordinates given).
     * @param mainPanel the panel where the character animation panel must spawn
     * @param x coordinate x
     * @param y coordinate y
     */
    void setAnimationPanelPosition(JPanel mainPanel, int x, int y);

    /**
     * Set the animation speed multiplier.
     * @param newSpeedMul new speed multiplier
     */
    void setSpeedMultiplier(double newSpeedMul);

    /**
     * Set the new health bar percent linked to this character.
     * @param percentage
     */
    void setHealthPercentage(int percentage);

    /**
     * Make this character start running.
     */
    void startRunLoop();

    /**
     * Make this character stop.
     */
    void startStopLoop();

    /**
     * Make this character start attacking.
     */
    void startAttackLoop();

    /**
     * Make this character die.
     */
    void startDeathSequence();
}
