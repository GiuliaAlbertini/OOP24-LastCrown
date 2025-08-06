package it.unibo.oop.lastcrown.controller.characters.api;

import java.util.List;
import java.util.Optional;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.collision.api.Hitbox;

/**
 * A defensive Wall that protects the Hero. When the Wall runs out its health,
 * the bossfight will start.
 */
public interface Wall extends CharacterHitObserver {

    /**
     * @return the current attack value
     */
    int getAttack();

    /**
     * Sets the new value of the attack.
     * @param attack the new attack value
     */
    void changeAttackValue(int attack);

    /**
     * @return the current health value
     */
    int getCurrentHealth();

    /**
     * Sets the new value of the maximum health.
     * @param health the new maximum health
     */
    void changeMaximumHealth(int health);

    /**
     * Returns the Hitbox associated with this entity, if present.
     *
     * @return an Optional containing the entity's hitbox, or an empty Optional if none is set
     */
    Optional<Hitbox> getHitbox();

    /**
     * Sets the Hitbox for this entity.
     *
     * @param hitbox the Hitbox to associate with this entity; must not be null
     */
    void setHitbox(Hitbox hitbox);

    /**
     * @return the CardIdentifier liked to this wall.
     */
    CardIdentifier getCardIdentifier();

    /**
     * Full the health of the defensive wall.
     */
    void fullWallHealth();

    /**
     * add an Opponent to the defensive wall.
     *
     * @param opponent the opponent observer
     */
    void addOpponent(CharacterHitObserver opponent);

    /**
     * Add a set of opponents to this defensive wall.
     *
     * @param opponents the list of opponents to be added
     */
    void addOpponents(List<CharacterHitObserver> opponents);

    /**
     * Deal an attack to all the opponents of the defensive wall.
     */
    void doAttack();

    /**
     * Remove one opponent specified by the given id from this defensive wall.
     * It does nothing if the id given does not correspond to any opponent.
     *
     * @param id the id of the opponent to be removed
     */
    void removeOpponent(int id);

    /**
     * @return the graphic component of the wall halth bar in order to be added to
     *         the map
     */
    JComponent getHealthBarComponent();
}
