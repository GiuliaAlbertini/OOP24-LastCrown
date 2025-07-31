package it.unibo.oop.lastcrown.controller.characters.impl.wall;

import java.awt.Color;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;
import it.unibo.oop.lastcrown.view.characters.CharacterHealthBar;

/**
 * A standard implementation of Wall interface.
 */
public final class WallImpl implements Wall {
    private final Map<Integer, CharacterHitObserver> opponents = new ConcurrentHashMap<>();
    private final CharacterHealthBar healthBar;
    private final int maximumHealth;
    private int currentHealth;
    private final int attack;
    private final CardIdentifier id;
    private boolean dead;
    private Optional<Hitbox> hitbox;

    /**
     * @param attack the attack value of the new Wall
     * @param health the health value of the new Wall
     * @param id the id of the new Wall
     * @param healthWidth the width of the health bar
     * @param healthHeight the height of the health bar
     */
    public WallImpl(final int attack, final int health, final int id,
     final int healthWidth, final int healthHeight, final Optional<Hitbox> hitbox) {
        this.healthBar = CharacterHealthBar.create(healthWidth, healthHeight, Color.GREEN);
        this.maximumHealth = health;
        this.currentHealth = health;
        this.attack = attack;
        this.id = new CardIdentifier(id, CardType.WALL);
        this.hitbox= Optional.ofNullable(hitbox).orElse(Optional.empty());
    }

    @Override
    public int getObserverId() {
        return this.id.number();
    }

    @Override
    public void takeHit(final int damage) {
        this.currentHealth = this.currentHealth - damage;
        this.healthBar.setPercentage(this.currentHealth * 100 / this.maximumHealth);
        if (currentHealth <= 0) {
            this.currentHealth = 0;
            this.dead = true;
        }
    }

    @Override
    public Optional<Hitbox> getHitbox() {
        return this.hitbox;
    }

    public void setHitbox(final Hitbox hitbox) {
        this.hitbox = Optional.ofNullable(hitbox);
    }

    @Override
    public boolean isDead() {
        return this.dead;
    }

    @Override
    public int getAttack() {
        return this.attack;
    }

    @Override
    public int getCurrentHealth() {
        return this.currentHealth;
    }

    @Override
    public void fullWallHealth() {
        this.currentHealth = this.maximumHealth;
        this.healthBar.setPercentage(100);
    }

    @Override
    public void addOpponent(final CharacterHitObserver opponent) {
        this.opponents.put(opponent.getObserverId(), opponent);
    }

    @Override
    public void addOpponents(final List<CharacterHitObserver> opponents) {
        for (final var opponent : opponents) {
            this.opponents.put(opponent.getObserverId(), opponent);
        }
    }

    @Override
    public void doAttack() {
         for (final var entry: this.opponents.entrySet()) {
            final CharacterHitObserver obs = entry.getValue();
            if (obs != null && !obs.isDead()) {
                obs.takeHit(this.getAttack());
            }
        }
    }

    @Override
    public void removeOpponent(final int id) {
        this.opponents.remove(id);
    }

    @Override
    public JComponent getHealthBarComponent() {
        return this.healthBar.getComponent();
    }

    @Override
    public CardIdentifier getCardIdentifier() {
        return this.id;
    }

}
