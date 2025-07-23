package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyEngagement;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;

/**
 * Interface representing the controller of a match.
 * <p>
 * This controller is responsible for managing all characters in the game,
 * handling collision updates, character positioning, engagements between
 * players and enemies, and notifying collision events to observers.
 * </p>
 */
public interface MatchController {

    /**
     * Adds a character to the match with the given controller and hitbox.
     *
     * @param n the ID to assign to the character
     * @param controller the character's controller logic
     * @param hitbox the character's hitbox for collision detection
     */
    void addCharacter(int n, GenericCharacterController controller, HitboxController hitbox);

    /**
     * Called when the "Add Character" button is pressed in the UI.
     * This method handles character instantiation and insertion into the match.
     */
    void onAddCharacterButtonPressed();

    /**
     * Updates the match model by advancing the internal state of all characters.
     *
     * @param deltaTime the time elapsed since the last update, in milliseconds
     */
    void update(int deltaTime);

    /**
     * Updates the graphical position and hitbox of a character based on movement deltas.
     *
     * @param controller the character controller to update
     * @param dx the delta movement on the X axis
     * @param dy the delta movement on the Y axis
     */
    void updateCharacterPosition(GenericCharacterController controller, int dx, int dy);

    /**
     * Notifies all registered observers of a given collision event.
     *
     * @param event the collision event to notify observers of
     */
    void notifyCollisionObservers(CollisionEvent event);

    /**
     * Retrieves a character controller by its unique identifier.
     *
     * @param id the ID of the character
     * @return an Optional containing the controller if found
     */
    Optional<GenericCharacterController> getCharacterControllerById(int id);

    /**
     * Retrieves the hitbox controller of a character by its ID.
     *
     * @param id the character's ID
     * @return an Optional containing the hitbox controller if found
     */
    Optional<HitboxController> getCharacterHitboxById(int id);

    /**
     * Completely removes a character from the match, including its hitbox,
     * graphical component, and FSM state if applicable.
     *
     * @param characterId the ID of the character to remove
     */
    void removeCharacterCompletelyById(int characterId);

    /**
     * Engages an enemy with a specific player, marking both as in combat.
     *
     * @param enemyId the enemy's ID
     * @param playerId the player's ID
     * @return true if the engagement was successful, false if already engaged
     */
    boolean engageEnemy(int enemyId, int playerId);

    /**
     * Checks if a player is currently engaged with an enemy.
     *
     * @param playerId the player's ID
     * @return true if engaged, false otherwise
     */
    boolean isPlayerEngaged(int playerId);

    /**
     * Checks if an enemy is currently engaged with a player.
     *
     * @param playerId the player's ID
     * @return trueif engaged, false otherwise
     */
    boolean isEnemyEngaged(int playerId);

    /**
     * Releases any current engagement for a character (player or enemy).
     *
     * @param characterId the character's ID
     * @return true if an engagement was removed, false otherwise
     */
    boolean releaseEngagementFor(int characterId);

    /**
     * Returns an unmodifiable view of all currently engaged enemy-player pairs.
     *
     * @return a set of current enemy engagements
     */
    Set<EnemyEngagement> getEngagedEnemies();

    /**
     * Checks if the character is still engaged with a counterpart that has died.
     *
     * @param characterId the character ID (either player or enemy)
     * @return true if the engagement is with a dead character
     */
    boolean isEngagedWithDead(int characterId);

    /**
     * Returns the ID of the counterpart with whom the given character is engaged.
     *
     * @param characterId the ID of the player or enemy
     * @return the ID of the engaged counterpart, or -1 if not engaged
     */
    int getEngagedCounterpart(int characterId);

    /**
     * Checks if a given player is currently in the IDLE state.
     *
     * @param player the player's controller
     * @return true if IDLE, false otherwise
     */
    boolean isPlayerIdle(PlayableCharacterController player);

    /**
     * Checks whether the partner of a boss fight is dead.
     *
     * @param id the character's ID
     * @return true if the partner is dead
     */
    boolean isBossFightPartnerDead(int id);

    /**
     * Checks if a given enemy (non-boss) is dead.
     *
     * @param enemyId the enemy's ID
     * @return true if the enemy is dead
     */
    boolean isEnemyDead(int enemyId);

    /**
     * Checks whether the partner in a ranged fight is dead.
     *
     * @param id the character's ID
     * @return true if the partner is dead
     */
    boolean isRangedFightPartnerDead(int id);
}
