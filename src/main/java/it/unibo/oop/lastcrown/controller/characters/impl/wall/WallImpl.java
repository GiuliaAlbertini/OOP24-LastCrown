package it.unibo.oop.lastcrown.controller.characters.impl.wall;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;

/**
 * A standard implementation of Wall interface.
 */
public class WallImpl implements Wall {
    private final Map<Integer, CharacterHitObserver> opponents = new ConcurrentHashMap<>();
    private final int maximumHealth;
    private int currentHealth;
    private final int attack;
    private final int id;
    private boolean dead;

    /**
     * @param attack the attack value of the new Wall
     * @param health the health value of the new Wall
     * @param id the id of the new Wall
     */
    public WallImpl(final int attack, final int health, final int id) {
        this.maximumHealth = health;
        this.currentHealth = health;
        this.attack = attack;
        this.id = id;
    }

    @Override
    public final int getObserverId() {
        return this.id;
    }

    @Override
    public final void takeHit(final int damage) {
        this.currentHealth = this.currentHealth - damage;
        if (currentHealth <= 0) {
            this.currentHealth = 0;
            this.dead = true;
        }
    }

    @Override
    public final boolean isDead() {
        return this.dead;
    }

    @Override
    public final int getAttack() {
        return this.attack;
    }

    @Override
    public final int getCurrentHealth() {
        return this.currentHealth;
    }

    @Override
    public final void fullWallHealth() {
        this.currentHealth = this.maximumHealth;
    }

    @Override
    public final void addOpponent(final CharacterHitObserver opponent) {
        this.opponents.put(opponent.getObserverId(), opponent);
    }

    @Override
    public final void addOpponents(final List<CharacterHitObserver> opponents) {
        for (final var opponent : opponents) {
            this.opponents.put(opponent.getObserverId(), opponent);
        }
    }

    @Override
    public final void doAttack() {
         for (final var entry: this.opponents.entrySet()) {
            final CharacterHitObserver obs = entry.getValue();
            if (obs != null && !obs.isDead()) {
                obs.takeHit(this.getAttack());
            }
        }
    }

    @Override
    public final void removeOpponent(final int id) {
        this.opponents.remove(id);
    }
}
