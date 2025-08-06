package it.unibo.oop.lastcrown.controller.characters.impl.boss;

import java.util.Map;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.EnemyGUI;

/**
 * A standard implementation of BossController interface.
 */
public class BossControllerImpl extends GenericCharacterControllerImpl implements BossController {
    private final Map<Integer, CharacterHitObserver> opponents = new ConcurrentHashMap<>();
    private final String charName;

    /**
     * @param id the numerical id of this controller
     * @param boss the Generic character linked to this controller
     */
    public BossControllerImpl(final int id, final Enemy boss) {
        super(id, boss, CardType.BOSS);
        this.charName = boss.getName();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final var view = new EnemyGUI(this, CardType.ENEMY.get(), this.charName, width, height);
        view.setSupportedAnimation();
        return view;
    }

    @Override
    public final void setOpponent(final CharacterHitObserver opponent) {
        this.opponents.put(opponent.getObserverId(), opponent);
    }

    @Override
    public final void setOpponents(final List<CharacterHitObserver> opponents) {
        for (final var opponent : opponents) {
            this.opponents.put(opponent.getObserverId(), opponent);
        }
    }

    @Override
    public final void removeOpponent(final int id) {
        this.opponents.remove(id);
    }

    @Override
    public final void doAttack() {
        for (final var entry: this.opponents.entrySet()) {
            final CharacterHitObserver obs = entry.getValue();
            if (obs != null && !obs.isDead()) {
                obs.takeHit(this.getAttackValue());
            }
        }
    }
}
