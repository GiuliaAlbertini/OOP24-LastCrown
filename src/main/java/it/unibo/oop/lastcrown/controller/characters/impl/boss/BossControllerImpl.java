package it.unibo.oop.lastcrown.controller.characters.impl.boss;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.characters.api.Enemy;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.EnemyGUIImpl;

/**
 * A standard implementation of BossController interface.
 */
public class BossControllerImpl extends GenericCharacterControllerImpl implements BossController {
    private final Map<Integer, CharacterHitObserver> opponents = new ConcurrentHashMap<>();
    private final CharacterMovementObserver movObs;
    private final String charName;
    private final double speedMultiplier;

    /**
     * @param deathObs the character death observer that communicates with the main controller
     * the death of this linked boss
     * @param movObs the character movement observer that communicates with the main controller
     * when this boss moves
     * @param id the numerical id of this controller
     * @param boss the Generic character linked to this controller
     */
    public BossControllerImpl(final CharacterDeathObserver deathObs,
     final CharacterMovementObserver movObs, final int id, final Enemy boss) {
        super(deathObs, id, boss, CardType.BOSS);
        this.movObs = movObs;
        this.charName = boss.getName();
        this.speedMultiplier = boss.getSpeedMultiplier();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        return new EnemyGUIImpl(this, this.getId(), movObs, CardType.BOSS.get(), this.charName,
        this.speedMultiplier, width, height);
    }

    @Override
    public final void setOpponent(final CharacterHitObserver opponent) {
        this.opponents.put(opponent.getObserverId(), opponent);
    }

    @Override
    public final void setOpponents(final Set<CharacterHitObserver> opponents) {
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
