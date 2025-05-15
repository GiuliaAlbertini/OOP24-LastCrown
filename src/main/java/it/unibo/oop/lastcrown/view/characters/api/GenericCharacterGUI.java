package it.unibo.oop.lastcrown.view.characters.api;

import javax.swing.JComponent;

/**
 * An interface that handles the graphic side of a generic character.
 */
public interface GenericCharacterGUI extends AnimationInterruptor {

    /**
     * Must be called before everything else. Creates a new animation panel for this character.
     */
    void createAnimationPanel();

    /**
     * @return the graphical component linked to this character.
     */
    JComponent getGraphicalComponent();

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
