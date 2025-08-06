package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.impl.CharacterFSM;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;

    /**
     * Manages the state and behavior of all character entities in the game,
     * including their logic controllers, hitboxes, and finite state machines (FSMs).
     */
public interface EntityStateManager {
    /**
     * Adds a new character to the manager.
     *
     * @param id the unique identifier of the character
     * @param controller the character's logic controller
     * @param hitboxController the character's hitbox controller
     * @param fsm the finite state machine associated with the character
     */
    void addCharacter(int id, GenericCharacterController controller, HitboxController hitboxController, CharacterFSM fsm);

    /**
     * Returns the character controller for the given ID, if present.
     *
     * @param id the character's identifier
     * @return an Optional containing the controller, or empty if not found
     */
    Optional<GenericCharacterController> getCharacterControllerById(int id);

    /**
     * Returns the hitbox controller for the given character ID, if present.
     *
     * @param id the character's identifier
     * @return an Optional containing the hitbox controller, or empty if not found
     */
    Optional<HitboxController> getCharacterHitboxById(int id);

    /**
     * Removes the character from the system, along with its FSM and hitbox.
     *
     * @param characterId the identifier of the character to remove
     */
    void removeCharacterById(int characterId);

    /**
     * Updates all character finite state machines.
     * A snapshot is used to avoid concurrent modifications.
     *
     * @param deltaTime the time passed since the last update, in milliseconds
     */
    void updateAll(int deltaTime);

    /**
     * Checks if there is at least one character with the specified card type.
     *
     * @param type the card type to check for
     * @return true if at least one character of the given type exists, false otherwise
     */
    boolean hasEntityWithType(CardType type);

    /**
     * Sets the hitbox radius for all characters, based on their type.
     *
     * @param meleeRadius the radius to apply to melee characters
     * @param rangedRadius the radius to apply to ranged characters
     */
    void setRadiusForAllPlayers(int meleeRadius, int rangedRadius);

    /**
     * Returns the finite state machine associated with the given character ID.
     *
     * @param characterId the identifier of the character
     * @return the FSM, or null if not found
     */
    CharacterFSM getFSM(int characterId);

    /**
     * Returns a list of all characters with the specified card type.
     *
     * @param cardType the type of characters to retrieve
     * @return a list of character controllers
     */
    List<GenericCharacterController> getCharactersByType(CardType cardType);

    /**
     * Retrieves the hitbox controller associated with the given character controller.
     *
     * @param controller the character controller
     * @return an Optional containing the hitbox controller, or empty if not found
     */
    Optional<HitboxController> getHitboxForController(GenericCharacterController controller);

    /**
     * Sets a common state for all character finite state machines.
     *
     * @param newState the new state to apply
     */
    void setAllFSMsToState(CharacterState newState);

    /**
     * Checks whether an enemy has moved beyond the right frame boundary.
     *
     * @param enemyId the identifier of the enemy
     * @param frameWidth the width of the frame
     * @return true if the enemy is beyond the right edge, false otherwise
     */
    boolean isEnemyBeyondFrame(int enemyId, int frameWidth);

    /**
     * Returns a map of character controllers and their associated hitbox controllers.
     *
     * @return the controller-to-hitbox map
     */
    Map<GenericCharacterController, HitboxController> getHitboxControllersMap();
}
