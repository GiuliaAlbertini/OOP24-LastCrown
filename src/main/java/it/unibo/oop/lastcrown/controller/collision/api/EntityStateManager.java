package it.unibo.oop.lastcrown.controller.collision.api;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.impl.CharacterFSM;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;

public interface EntityStateManager {

    void addCharacter(int id, GenericCharacterController controller, HitboxController hitboxController,
            CharacterFSM fsm);

    Optional<GenericCharacterController> getCharacterControllerById(int id);

    Optional<HitboxController> getCharacterHitboxById(int id);

    void removeCharacterById(int characterId);

    void updateAll(int deltaTime);

    boolean hasEntityWithType(CardType type);

    void setRadiusForAllPlayers(int meleeRadius, int rangedRadius);

    CharacterFSM getFSM(int characterId);

    List<GenericCharacterController> getCharactersByType(CardType cardType);

    Optional<HitboxController> getHitboxForController(GenericCharacterController controller);

    void setAllFSMsToState(CharacterState newState);

    boolean isEnemyBeyondFrame(int enemyId, int frameWidth);

    Map<GenericCharacterController, HitboxController> getHitboxControllersMap();

}