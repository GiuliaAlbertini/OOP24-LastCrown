package it.unibo.oop.lastcrown.controller.characters.impl.hero;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.HeroController;
import it.unibo.oop.lastcrown.model.characters.api.Hero;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;

/**
 * Create a HeroController with the specified parameters.
 */
public final class HeroControllerFactory {
    private HeroControllerFactory() { }

     /**
     * @param deathObs the character death observer of this controller
     * @param movObs the character movement observer of this controller
     * @param contrId the numerical id of this controller
     * @param hero the hero linked to this controller
     * @return a new hero Controller
     */
    public static HeroController createHeroController(final CharacterDeathObserver deathObs,
     final CharacterMovementObserver movObs, final int contrId, final Hero hero) {
        return new HeroControllerImpl(deathObs, movObs, contrId, hero);
    }
}
