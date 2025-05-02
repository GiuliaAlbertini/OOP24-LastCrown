package it.unibo.oop.lastcrown.controller.characters.impl;

import java.util.Objects;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.model.api.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.api.card.characters.GenericCharacter;
import it.unibo.oop.lastcrown.model.api.card.characters.InGameCharacter;
import it.unibo.oop.lastcrown.model.impl.card.characters.ingamecharacter.InGameCharacterFactory;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;

/**
 * A standard implementation of CharacterController interface.
 */
public abstract class GenericCharacterControllerImpl implements GenericCharacterController {
    private GenericCharacterGUI view;
    private final InGameCharacter character;
    private CharacterHitObserver opponent;
    private final CharacterDeathObserver deathObserver;
    private final CardIdentifier id;

    /**
     * @param obs the character death observer that communicates with the main controller
     * the death of this linked character
     * @param id the numerical id of this controller
     * @param character the Generic character linked to this controller
     * @param charType the type of the linked character
     */
    public GenericCharacterControllerImpl(final CharacterDeathObserver obs,
     final int id, final GenericCharacter character, final String charType) {
        this.deathObserver = obs;
        this.character = InGameCharacterFactory.createInGameCharacter(charType, character.getName(),
        character.getHealthValue(), character.getAttackValue(), character.getSpeedMultiplier());
        this.id = new CardIdentifier(id, charType);
    }

    @Override
    public final CardIdentifier getId() {
        return this.id;
    }

    @Override
    public final int getObserverId() {
        return this.id.number();
    }

    @Override
    public final void attachCharacterAnimationPanel(final int width, final int height) {
        this.view = createView(width, height);
    }

    /**
     * @param width the width of the new animation panel 
     * @param height the width of the new animation panel 
     * @return new linked character GUI.
     */
    public abstract GenericCharacterGUI createView(int width, int height);

    @Override
    public final void setCharacterPanelPosition(final JPanel matchPanel, final int initialX, final int initialY) {
        this.view.setAnimationPanelPosition(matchPanel, initialX, initialY);
    }

    @Override
    public final void startRunning() {
        this.view.startRunLoop();
    }

    @Override
    public final void stop() {
        this.view.startStopLoop();
    }

    @Override
    public final void startAttacking() {
        this.view.startAttackLoop();
    }

    /**
     * This method is designed to be overridable by the BossController implementation
     * because a boss can have multiple opponents at the same time.
     */
    @Override
    public void setOpponent(final CharacterHitObserver opponent) {
        Objects.requireNonNull(opponent);
        this.opponent = opponent;
    }

    @Override
    public final synchronized void takeHit(final int damage) {
        this.character.takeDamage(damage);
        this.view.setHealthPercentage(this.character.getHealthPercentage());
        if (this.character.isDead()) {
            new Thread(this::startDeath).start();
        }
    }

    @Override
    public final synchronized void heal(final int cure) {
        this.character.restoreHealth(cure);
        this.view.setHealthPercentage(this.character.getHealthPercentage());
    }

    /**
     * Start this character death sequence and notify the main controller of this character death.
     */
    private void startDeath() {
        this.view.startDeathSequence();
        this.deathObserver.notifyDeath(this.id);
    }

    /**
     * This method is designed to be overridable by the BossController implementation
     * because a boss can have multiple opponents at the same time.
     */
    @Override
    public void doAttack() {
        this.opponent.takeHit(this.character.getAttack());
    }
}
