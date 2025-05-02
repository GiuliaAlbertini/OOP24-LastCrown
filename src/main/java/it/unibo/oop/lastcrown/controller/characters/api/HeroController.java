package it.unibo.oop.lastcrown.controller.characters.api;

import javax.swing.JPanel;

/**
 * A controller that handles the behaviour of the hero.
 */
public interface HeroController extends GenericCharacterController {

    /**
     * Dispose the graphic components of this hero to be in the Match View.
     * @param matchPanel the panel where the match takes place
     * @param initialX the initial horizontal coordinate of the animation panel(centered on the given value)
     * @param initialY the initial vertical coordinate of the animation panel(centered on the given value)
     */
    void setHeroInGame(JPanel matchPanel, int initialX, int initialY);

    /**
     * Dispose the graphic components of this hero to be in the Shop View.
     * @param shopPanel the panel corresponding to the shop
     * @param initialX the initial horizontal coordinate of the animation panel(centered on the given value)
     * @param initialY the initial vertical coordinate of the animation panel(centered on the given value)
     */
    void setHeroInShop(JPanel shopPanel, int initialX, int initialY);

    /**
     * Make this hero run to left.
     */
    void runLeft();

    /**
     * Make this hero stop to left.
     */
    void stopLeft();
}
