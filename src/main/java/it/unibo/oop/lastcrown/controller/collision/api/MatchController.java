package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.Optional;
import java.util.Set;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.Wall;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyEngagement;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.map.MatchView;

/**
 * Interface representing the controller of a match.
 * Handles characters, updates, collisions, and interactions.
 */
public interface MatchController {

    /**
     * Adds a character to the match with the given controller and hitbox.
     *
     * @param n          the ID to assign to the character
     * @param controller the character's controller logic
     * @param hitbox     the character's hitbox for collision detection
     */
    void addCharacter(int n, GenericCharacterController controller, HitboxController hitbox);

    HitboxController setupCharacter(final JComponent charComp, final String typeFolder, final String name,
            final boolean isPlayable, int x, int y);

    /**
     * Updates the match model.
     *
     * @param deltaTime the amount of time passed since the last update, in
     *                  milliseconds
     */
    void update(int deltaTime);

    /**
     * Updates the position of a character based on its controller.
     *
     * @param controller the controller managing the character
     * @param dx         the change in X position
     * @param dy         the change in Y position
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
     * @return an Optional containing the hitbox controller if found, or empty if
     *         not
     */
    Optional<HitboxController> getCharacterHitboxById(int id);

    /**
     * Completely removes a character and its related components from the match
     * using its ID.
     *
     * @param characterId the ID of the character to remove
     */
    void removeCharacterCompletelyById(int characterId);

    boolean engageEnemy(int enemyId, int playerId);

    boolean isPlayerEngaged(final int playerId);

    boolean isEnemyEngaged(final int enemyId);

    boolean releaseEngagementFor(final int characterId);

    Set<EnemyEngagement> getEngagedEnemies();

    boolean isEngagedWithDead(final int characterId);

    int getEngagedCounterpart(final int characterId);

    boolean isPlayerIdle(PlayableCharacterController player);

    boolean isBossFightPartnerDead(final int id);

    boolean isEnemyDead(int enemyId);

    boolean isRangedFightPartnerDead(final int id);

    boolean isPlayerStopped(final PlayableCharacterController player);

    void notifyClicked(int x, int y);

    void notifyButtonPressed(CardIdentifier id);

    void notifyPauseEnd();

    JComponent getWallHealthBar();

    JComponent getCoinsWriter();

    JComponent getEventWriter();

    void newMatchView(final MatchView matchView);
    // void notifyShopToMatch();

    CharacterState getCharacterState(final GenericCharacterController character);

    MatchView getMatchView();

    Wall getWall();

    HitboxController getWallHitboxController();
    void setAllFSMsToState(final CharacterState newState);
    boolean isEnemyBeyondFrame(final int enemyId);
    int getHitboxControllersCount();
    void getRandomBossFromFirstList();
    boolean hasAnyEnemiesInMap();
    boolean hasBossInMap();
    void printEngagedEnemies();
    void setRadiusPlayerInMap();
    boolean hasAnyPlayerInMap();
    boolean hasAnyRangedInMap();
    void printHitboxControllers();
    public void matchResult();
    void rewardCoinsForRound(boolean bossFight);
    void setBossActive();
    boolean isRoundSpawnComplete();
    boolean retreat();
    void notifyPauseStart();
}
