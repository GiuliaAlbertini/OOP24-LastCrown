package it.unibo.oop.lastcrown.controller.characters.api;

import javax.swing.JPanel;

/**
 * A controller that handles the behaviour of the hero.
 */
public interface HeroController extends GenericCharacterController {

    /**
     * Set the panel where hero must appear and his new position (centered).
     * @param x horizontal position of the hero animation panel
     * @param y vertical position of the hero animation panel
     * @param width width of the hero animation panel
     * @param height height of the hero animation panel
     * @param panel the new panel where hero must appear
     */
    void setCurrentPanel(int x, int y, int width, int height, JPanel panel);

    /**
     * Dispose the graphic components of this hero to be in the Match Panel.
     */
    void setHeroInGame();

    /**
     * Dispose the graphic components of this hero to be in the Match panel.
     */
    void setHeroInShop();

    /**
     * Make this hero run to left.
     */
    void runLeft();

    /**
     * Make this hero stop to left.
     */
    void stopLeft();
}
