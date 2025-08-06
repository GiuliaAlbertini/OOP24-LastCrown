package it.unibo.oop.lastcrown.controller.characters.api;

/**
 * A controller that handles the behaviour of the hero.
 */
public interface HeroController extends GenericCharacterController {

    /**
     * @return the maximum health value of the hero
     */
    int getMaximumHealthValue();
}
