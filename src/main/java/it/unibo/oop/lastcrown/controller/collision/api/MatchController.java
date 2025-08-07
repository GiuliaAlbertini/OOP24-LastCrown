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

        /**
         * TODO - complete JavaDoc.
         * @param charComp
         * @param typeFolder
         * @param name
         * @param isPlayable
         * @param x
         * @param y
         * @return TODO - complete JavaDoc.
         */
        HitboxController setupCharacter(JComponent charComp, String typeFolder, String name,
                        boolean isPlayable, int x, int y);

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

        /**
         * TODO - complete JavaDoc.
         * @param enemyId
         * @param playerId
         * @return TODO - complete JavaDoc.
         */
        boolean engageEnemy(int enemyId, int playerId);

        /**
         * TODO - complete JavaDoc.
         * @param characterId
         * @return TODO - complete JavaDoc.
         */
        boolean releaseEngagementFor(int characterId);

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        Set<EnemyEngagement> getEngagedEnemies();

        /**
         * TODO - complete JavaDoc.
         * @param characterId
         * @return TODO - complete JavaDoc.
         */
        boolean isEngagedWithDead(int characterId);

        /**
         * TODO - complete JavaDoc.
         * @param characterId
         * @return TODO - complete JavaDoc.
         */
        int getEngagedCounterpart(int characterId);

        /**
         * TODO - complete JavaDoc.
         * @param id
         * @return TODO - complete JavaDoc.
         */
        boolean isBossFightPartnerDead(int id);

        /**
         * TODO - complete JavaDoc.
         * @param enemyId
         * @return TODO - complete JavaDoc.
         */
        boolean isEnemyDead(int enemyId);

        /**
         * TODO - complete JavaDoc.
         * @param id
         * @return TODO - complete JavaDoc.
         */
        boolean isRangedFightPartnerDead(int id);

        /**
         * TODO - complete JavaDoc.
         * @param x
         * @param y
         */
        void notifyClicked(int x, int y);

        /**
         * TODO - complete JavaDoc.
         * @param id
         */
        void notifyButtonPressed(CardIdentifier id);

        /**
         * TODO - complete JavaDoc.
         */
        void notifyPauseEnd();

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        JComponent getWallHealthBar();

        /**
         * TODO - complete JavaDoc.
         * @param matchView
         */
        void newMatchView(MatchView matchView);

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        MatchView getMatchView();

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        Wall getWall();

        /**
         * TODO - complete JavaDoc.
         * @param newState
         */
        void setAllFSMsToState(CharacterState newState);

        /**
         * TODO - complete JavaDoc.
         * @param enemyId
         * @return TODO - complete JavaDoc.
         */
        boolean isEnemyBeyondFrame(int enemyId);

        /**
         * TODO - complete JavaDoc.
         */
        void spawnRandomBossFromFirstList();

        /**
         * TODO - complete JavaDoc.
         */
        void setRadiusPlayerInMap();

        /**
         * TODO - complete JavaDoc.
         */
        void matchResult();

        /**
         * TODO - complete JavaDoc.
         * @param bossFight
         */
        void rewardCoinsForRound(boolean bossFight);

        /**
         * TODO - complete JavaDoc.
         */
        void setBossActive();

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        boolean isRoundSpawnComplete();

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        boolean retreat();

        /**
         * TODO - complete JavaDoc.
         */
        void notifyPauseStart();

        /**
         * TODO - complete JavaDoc.
         */
        void halveHeroMaxHealth();

        /**
         * TODO - complete JavaDoc.
         * @param type
         * @return TODO - complete JavaDoc.
         */
        boolean hasEntityTypeInMap(CardType type);

        /**
         * TODO - complete JavaDoc.
         * @param player
         * @param stateToCompare
         * @return TODO - complete JavaDoc.
         */
        boolean isPlayerInState(PlayableCharacterController player, CharacterState stateToCompare);

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        int generateUniqueCharacterId();

        /**
         * TODO - complete JavaDoc.
         */
        void handleBossMusic();

        /**
         * TODO - complete JavaDoc.
         * @param entityId
         * @return TODO - complete JavaDoc.
         */
        boolean isEntityEngaged(int entityId);

        /**
         * TODO - complete JavaDoc.
         * @param cardType
         * @return TODO - complete JavaDoc.
         */
        List<GenericCharacterController> getCharactersByType(CardType cardType);

        /**
         * TODO - complete JavaDoc.
         * @return TODO - complete JavaDoc.
         */
        int getCurrentCoins();

        /**
         * TODO - complete JavaDoc.
         * @param text
         */
        void updateEventText(String text);
}
