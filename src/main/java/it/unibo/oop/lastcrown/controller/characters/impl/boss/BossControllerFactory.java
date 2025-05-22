package it.unibo.oop.lastcrown.controller.characters.impl.boss;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;

/**
 * Create a new BossController with the specified parameters.
 */
public final class BossControllerFactory {
    private BossControllerFactory() { }
    /**
     * @param deathObs the character death observer of this controller
     * @param movObs the character movement observer of this controller
     * @param contrId the numerical id of this controller
     * @param enemy the enemy linked to this controller
     * @return a new boss Controller
     */
    public static BossController createBossController(final CharacterDeathObserver deathObs,
     final CharacterMovementObserver movObs, final int contrId, final Enemy enemy) {
        return new BossControllerImpl(deathObs, movObs, contrId, enemy);
    }
}
