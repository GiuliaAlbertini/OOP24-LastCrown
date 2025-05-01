package it.unibo.oop.lastcrown.view.characters.api;

/**
 * An interface that handles the graphic side of the hero.
 */
public interface HeroGUI extends GenericCharacterGUI {

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
