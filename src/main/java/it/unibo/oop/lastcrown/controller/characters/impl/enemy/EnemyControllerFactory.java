package it.unibo.oop.lastcrown.controller.characters.impl.enemy;

import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;

/**
 * Create a enemy controller with the specified parameters.
 */
public final class EnemyControllerFactory {
    private EnemyControllerFactory() { }
    /**
     * @param deathObs the character death observer of this controller
     * @param contrId the numerical id of this controller
     * @param enemy the enemy linked to this controller
     * @return a new enemy Controller
     */
    public static EnemyController createEnemyController(final int contrId, final Enemy enemy) {
        return new EnemyControllerImpl(contrId, enemy);
    }
}
