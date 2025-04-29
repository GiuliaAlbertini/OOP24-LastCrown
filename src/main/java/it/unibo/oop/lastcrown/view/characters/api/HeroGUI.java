package it.unibo.oop.lastcrown.view.characters.api;

import javax.swing.JPanel;

/**
 * An interface that handles the graphic side of the hero.
 */
public interface HeroGUI extends GenericCharacterGUI {

    /**
     * Change the main panel when this hero appears (match or shop panel).
     * @param x x coordinate of the new panel where the hero will appear
     * @param y y coordinate of the new panel where the hero will appear
     * @param width new width of the animation panel linked to this hero
     * @param height new height of the animation panel linked to this hero
     * @param panel new panel where this hero will appear
     */
    void setCurrentPanel(int x, int y, int width, int height, JPanel panel);

    /**
     * Sets the hero animations to be in game animations.
     */
    void useInGameFrames();

    /**
     * Sets the hero animations to be in shop animations.
     */
    void useShopFrames();

    /**
     * Make this hero run to left.
     */
    void startRunLeftLoop();

    /**
     * make this hero stop to left.
     */
    void startStopLeftLoop();
}
