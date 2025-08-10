package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyEngagement;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.view.map.MatchView;

/**
 * Controls the main logic of a match, coordinating model, view, and character controllers,
 * handling collisions, engagements, events, and match progression.
 */
public interface MatchController {

    /**
     * Adds a character to the match with its controller and hitbox.
     *
     * @param n          the numeric ID of the character
     * @param controller the character's logic controller
     * @param hitbox     the hitbox controller for collision management
     */
    void addCharacter(int n, GenericCharacterController controller, HitboxController hitbox);

    /**
     * Initializes a character by setting its graphical component, hitbox, and,
     * if applicable, interaction radius.
     *
     * @param charComp   the Swing graphical component of the character
     * @param typeFolder the folder for the character type (e.g., "melee", "ranged", "boss")
     * @param name       the character's name
     * @param isPlayable true if the character is player-controlled
     * @param x          initial X position
     * @param y          initial Y position
     * @return the created hitbox controller
     */
    HitboxController setupCharacter(JComponent charComp, String typeFolder, String name,
                                    boolean isPlayable, int x, int y);

    /**
     * Updates the match state.
     *
     * @param deltaTime time elapsed since the last update, in milliseconds
     */
    void update(int deltaTime);

    /**
     * Updates the position of a character according to its controller.
     *
     * @param controller the character's controller
     * @param dx         change in X position
     * @param dy         change in Y position
     */
    void updateCharacterPosition(GenericCharacterController controller, int dx, int dy);

    /**
     * Notifies all registered observers of a collision event.
     *
     * @param event the collision event to broadcast
     */
    void notifyCollisionObservers(CollisionEvent event);

    /**
     * Retrieves the character controller by ID.
     *
     * @param id the character ID
     * @return an Optional containing the controller, if found
     */
    Optional<GenericCharacterController> getCharacterControllerById(int id);

    /**
     * Retrieves the hitbox controller of a character by ID.
     *
     * @param id the character ID
     * @return an Optional containing the hitbox controller, if found
     */
    Optional<HitboxController> getCharacterHitboxById(int id);

    /**
     * Completely removes a character and its components from the match.
     *
     * @param characterId the ID of the character to remove
     */
    void removeCharacterCompletelyById(int characterId);

    /**
     * Attempts to engage an enemy with a player character.
     *
     * @param enemyId  the enemy ID
     * @param playerId the player character ID
     * @return true if the engagement was successful
     */
    boolean engageEnemy(int enemyId, int playerId);

    /**
     * Releases a character from its current engagement.
     *
     * @param characterId the ID of the character to release
     * @return true if the release was successful
     */
    boolean releaseEngagementFor(int characterId);

    /**
     * Returns the set of currently engaged enemies.
     *
     * @return a set of {@link EnemyEngagement}
     */
    Set<EnemyEngagement> getEngagedEnemies();

    /**
     * Checks if a character is engaged with an opponent that is already dead.
     *
     * @param characterId the character ID
     * @return true if the opponent is dead
     */
    boolean isEngagedWithDead(int characterId);

    /**
     * Returns the ID of the character engaged with the given one.
     *
     * @param characterId the character ID
     * @return the opponent's ID, or -1 if none
     */
    int getEngagedCounterpart(int characterId);

    /**
     * Checks if the boss fight partner of the given entity is dead.
     *
     * @param id the entity ID
     * @return true if the partner is dead
     */
    boolean isBossFightPartnerDead(int id);

    /**
     * Checks if an enemy (non-boss) is dead.
     *
     * @param enemyId the enemy ID
     * @return true if the enemy is dead
     */
    boolean isEnemyDead(int enemyId);

    /**
     * Checks if the ranged fight partner of the given entity is dead.
     *
     * @param id the entity ID
     * @return true if the partner is dead
     */
    boolean isRangedFightPartnerDead(int id);

    /**
     * Handles a click on the map at the specified coordinates.
     *
     * @param x the X coordinate
     * @param y the Y coordinate
     */
    void notifyClicked(int x, int y);

    /**
     * Handles a button press associated with a card.
     *
     * @param id the card identifier
     */
    void notifyButtonPressed(CardIdentifier id);

    /**
     * Notifies that the pause has ended.
     */
    void notifyPauseEnd();

    /**
     * Returns the health bar component of the wall.
     *
     * @return the Swing component representing the wall's health bar
     */
    JComponent getWallHealthBar();

    /**
     * Associates a new match view with this controller.
     *
     * @param matchView the match view
     */
    void newMatchView(MatchView matchView);

    /**
     * Returns the current match view.
     *
     * @return the MatchView instance
     */
    MatchView getMatchView();

    /**
     * Returns the wall in the match.
     *
     * @return the Wall instance
     */
    Wall getWall();

    /**
     * Sets all characters to a given FSM (finite state machine) state.
     *
     * @param newState the new state to apply
     */
    void setAllFSMsToState(CharacterState newState);

    /**
     * Checks if an enemy is beyond the boundaries of the game frame.
     *
     * @param enemyId the enemy ID
     * @return true if the enemy is outside the frame
     */
    boolean isEnemyBeyondFrame(int enemyId);

    /**
     * Spawns a random boss from the first available list.
     */
    void spawnRandomBossFromFirstList();

    /**
     * Updates the action radius of all player characters on the map.
     */
    void setRadiusPlayerInMap();

    /**
     * Determines the result of the match (win or loss) and updates the view.
     */
    void matchResult();

    /**
     * Awards coins for completing a round.
     *
     * @param bossFight true if the reward is for defeating a boss
     */
    void rewardCoinsForRound(boolean bossFight);

    /**
     * Marks a boss as active in the match.
     */
    void setBossActive();

    /**
     * Checks if the current round's spawn is complete.
     *
     * @return true if spawn is complete
     */
    boolean isRoundSpawnComplete();

    /**
     * Checks whether the player must retreat (wall destroyed and no boss present).
     *
     * @return true if retreat conditions are met
     */
    boolean retreat();

    /**
     * Notifies that the pause has started.
     */
    void notifyPauseStart();

    /**
     * Halves the maximum health of the hero.
     */
    void halveHeroMaxHealth();

    /**
     * Checks if there is at least one entity of the given type on the map.
     *
     * @param type the card type to check
     * @return true if at least one entity of this type is present
     */
    boolean hasEntityTypeInMap(CardType type);

    /**
     * Checks if a player character is in the specified state.
     *
     * @param player         the player character controller
     * @param stateToCompare the state to compare against
     * @return true if the player is in that state
     */
    boolean isPlayerInState(PlayableCharacterController player, CharacterState stateToCompare);

    /**
     * Generates a unique ID for a new character.
     *
     * @return the generated ID
     */
    int generateUniqueCharacterId();

    /**
     * Handles the playback of boss fight music.
     */
    void handleBossMusic();

    /**
     * Checks if a given entity is currently engaged in combat.
     *
     * @param entityId the entity ID
     * @return true if engaged
     */
    boolean isEntityEngaged(int entityId);

    /**
     * Returns all characters currently on the map of the specified type.
     *
     * @param cardType the card type
     * @return a list of character controllers
     */
    List<GenericCharacterController> getCharactersByType(CardType cardType);

    /**
     * Returns the current number of coins.
     *
     * @return the coin count
     */
    int getCurrentCoins();

    /**
     * Updates the event text displayed in the view.
     *
     * @param text the text to display
     */
    void updateEventText(String text);
}
