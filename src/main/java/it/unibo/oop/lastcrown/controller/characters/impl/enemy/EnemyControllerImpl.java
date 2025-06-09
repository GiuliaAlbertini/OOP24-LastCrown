package it.unibo.oop.lastcrown.controller.characters.impl.enemy;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.EnemyGUI;

/**
 * A standard implementation of EnemyController interface.
 */
public class EnemyControllerImpl extends GenericCharacterControllerImpl implements EnemyController {
    private final String charName;

    /**
     * @param obs the character death observer that communicates with the main controller
     * the death of this linked enemy
     * @param id the numerical id of this controller
     * @param enemy the enemy linked to this controller
     */
    public EnemyControllerImpl(final CharacterDeathObserver obs,
     final int id, final Enemy enemy) {
        super(obs, id, enemy, CardType.ENEMY);
        this.charName = enemy.getName();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final var view = new EnemyGUI(this, CardType.ENEMY.get(), this.charName, width, height);
        view.setSupportedAnimation();
        return view;
    }
}
