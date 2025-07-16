package it.unibo.oop.lastcrown.controller.api;

import java.util.Optional;
import java.util.Set;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.EnemyEngagement;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;

/**
 * Interface representing the controller of a match.
 * Handles characters, updates, collisions, and interactions.
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
     * This method should handle the logic to initiate character addition.
     */
    void onAddCharacterButtonPressed();

    /**
     * Updates the match model.
     *
     * @param deltaTime the amount of time passed since the last update, in milliseconds
     */
    void update(int deltaTime);

    /**
     * Updates the position of a character based on its controller.
     *
     * @param controller the controller managing the character
     * @param dx the change in X position
     * @param dy the change in Y position
     */
    void updateCharacterPosition(GenericCharacterController controller, int dx, int dy);

    /**
     * Returns the collision resolver used in the match.
     *
     * @return the CollisionResolver instance
     */
    CollisionResolver getCollisionResolver();

    /**
     * Notifies all registered collision observers about a collision event.
     *
     * @param event the collision event to broadcast
     */
    void notifyCollisionObservers(CollisionEvent event);

    /**
     * Retrieves a character controller by its assigned ID.
     *
     * @param id the ID of the character
     * @return an Optional containing the controller if found, or empty if not
     */
    Optional<GenericCharacterController> getCharacterControllerById(int id);

    /**
     * Retrieves a character's hitbox controller by its ID.
     *
     * @param id the ID of the character
     * @return an Optional containing the hitbox controller if found, or empty if not
     */
    Optional<HitboxController> getCharacterHitboxById(int id);

    /**
     * Completely removes a character and its related components from the match using its ID.
     *
     * @param characterId the ID of the character to remove
     */
    void removeCharacterCompletelyById(int characterId);
    boolean engageEnemy(int enemyId, int playerId);
    boolean isPlayerEngaged(final int playerId);
    boolean isEnemyEngaged(final int playerId);
    boolean releaseEngagementFor(final int characterId);
    Set<EnemyEngagement> getEngagedEnemies();
    boolean isEngagedWithDead(final int characterId);
    int getEngagedCounterpart(final int characterId);
    boolean isPlayerIdle(PlayableCharacterController player);

}
