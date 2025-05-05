package it.unibo.oop.lastcrown.controller.characters.impl;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.characters.api.EnemyGUI;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.EnemyGUIImpl;

/**
 * A standard implementation of EnemyController interface.
 */
public class EnemyControllerImpl extends GenericCharacterControllerImpl implements EnemyController {
    private EnemyGUI view;
    private final String charName;
    private final double speedMultiplier;

    /**
     * @param obs the character death observer that communicates with the main controller
     * the death of this linked enemy
     * @param id the numerical id of this controller
     * @param enemy the enemy linked to this controller
     */
    public EnemyControllerImpl(final CharacterDeathObserver obs, final int id, final Enemy enemy) {
        super(obs, id, enemy, CardType.ENEMY.get());
        this.charName = enemy.getName();
        this.speedMultiplier = enemy.getSpeedMultiplier();
        this.view = null;
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final EnemyGUI newView = new EnemyGUIImpl(this, CardType.ENEMY.get(), this.charName,
        this.speedMultiplier, width, height);
        this.view = newView;
        return newView;
    }

    @Override
    public final void retreat() {
        this.view.startRetreatLoop();
    }
}
