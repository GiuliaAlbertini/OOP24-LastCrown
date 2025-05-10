package it.unibo.oop.lastcrown.controller.characters.impl.boss;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;

/**
 * Create a new BossController with the specified parameters.
 */
public final class BossControllerFactory {
    private BossControllerFactory() { }
    /**
     * @param obs the character death observer of this controller
     * @param contrId the numerical id of this controller
     * @param enemy the enemy linked to this controller
     * @return a new boss Controller
     */
    public static BossController createBossController(final CharacterDeathObserver obs,
    final int contrId, final Enemy enemy) {
        return new BossControllerImpl(obs, contrId, enemy);
    }
}
