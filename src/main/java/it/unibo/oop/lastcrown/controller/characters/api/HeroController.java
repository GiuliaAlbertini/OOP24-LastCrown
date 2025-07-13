package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A controller that handles the behaviour of the hero.
 */
public interface HeroController extends GenericCharacterController {

    /**
     * Dispose the graphic components of this hero to be in the Match View.
     */
    void setHeroInGame();

    /**
     * Dispose the graphic components of this hero to be in the Shop View.
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
