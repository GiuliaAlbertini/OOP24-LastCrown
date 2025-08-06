package it.unibo.oop.lastcrown.controller.collision.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.EntityStateManager;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.model.card.CardType;

/**
 * Implementation of EntityStateManager.
 * Manages the state and lifecycle of all character entities within the match,
 * including their FSMs (Finite State Machines), controllers, and hitboxes.
 *
 * Provides utility methods for adding, updating, querying, and removing characters,
 * as well as bulk operations such as setting all FSMs to a specific state or
 * adjusting hitbox radii based on character type.
 */
public final class EntityStateManagerImpl implements EntityStateManager {
    private final Map<Integer, CharacterFSM> playerFSMs;
    private final Map<Integer, GenericCharacterController> charactersController;
    private final Map<GenericCharacterController, HitboxController> hitboxControllers;

     /**
     * Constructs a new EntityStateManagerImpl.
     *
     * Initializes the internal data structures used to track character controllers,
     * their associated hitboxes, and the FSMs that manage their behavior.
     */
    public EntityStateManagerImpl() {
        this.playerFSMs = new HashMap<>();
        this.charactersController = new HashMap<>();
        this.hitboxControllers = new HashMap<>();
    }

    @Override
    public void addCharacter(final int id, final GenericCharacterController controller,
            final HitboxController hitboxController, final CharacterFSM fsm) {
        charactersController.put(id, controller);
        hitboxControllers.put(controller, hitboxController);
        playerFSMs.put(id, fsm);
    }

    @Override
    public Optional<GenericCharacterController> getCharacterControllerById(final int id) {
        return Optional.ofNullable(this.charactersController.get(id));
    }

    @Override
    public Optional<HitboxController> getCharacterHitboxById(final int id) {
        final Optional<GenericCharacterController> charaControllerOpt = this.getCharacterControllerById(id);

        if (charaControllerOpt.isPresent()) {
            return Optional.ofNullable(this.hitboxControllers.get(charaControllerOpt.get()));
        }

        return Optional.empty();
    }

    @Override
    public void removeCharacterById(final int characterId) {
        final Optional<GenericCharacterController> controllerOpt = this.getCharacterControllerById(characterId);

        if (controllerOpt.isPresent()) {
            final GenericCharacterController controller = controllerOpt.get();
            this.hitboxControllers.remove(controller);
            this.playerFSMs.remove(characterId);
            this.charactersController.remove(characterId);
        }
    }

    @Override
    public void updateAll(final int deltaTime) {
        for (final CharacterFSM fsm : new ArrayList<>(playerFSMs.values())) {
            fsm.update(deltaTime);
        }
    }

    @Override
    public boolean hasEntityWithType(final CardType type) {
        for (final GenericCharacterController controller : charactersController.values()) {
            if (controller.getId().type() == type) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setRadiusForAllPlayers(final int meleeRadius, final int rangedRadius) {
        for (final Map.Entry<GenericCharacterController, HitboxController> entry : hitboxControllers.entrySet()) {
            final GenericCharacterController character = entry.getKey();
            final HitboxController hitboxController = entry.getValue();

            if (character.getId().type() == CardType.MELEE) {
                hitboxController.getRadius().ifPresent(radius -> radius.setRadius(meleeRadius));
            }
            if (character.getId().type() == CardType.RANGED) {
                hitboxController.getRadius().ifPresent(radius -> radius.setRadius(rangedRadius));
            }
        }
    }

    @Override
    public CharacterFSM getFSM(final int characterId) {
        return this.playerFSMs.get(characterId);
    }

    @Override
    public List<GenericCharacterController> getCharactersByType(final CardType cardType) {
        return this.charactersController.values().stream()
                .filter(controller -> controller.getId().type() == cardType)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HitboxController> getHitboxForController(final GenericCharacterController controller) {
        return Optional.ofNullable(this.hitboxControllers.get(controller));
    }

    @Override
    public void setAllFSMsToState(final CharacterState newState) {
        for (final CharacterFSM fsm : playerFSMs.values()) {
            fsm.setState(newState);
        }
    }

    @Override
    public boolean isEnemyBeyondFrame(final int enemyId, final int frameWidth) {
        for (final var entry : this.hitboxControllers.entrySet()) {
            final GenericCharacterController character = entry.getKey();
            final HitboxController hitbox = entry.getValue();

            if (character.getId().type() == CardType.ENEMY && character.getId().number() == enemyId) {
                return hitbox.getHitbox().getPosition().x() > frameWidth;
            }
        }
        return false;
    }

    @Override
    public Map<GenericCharacterController, HitboxController> getHitboxControllersMap() {
        return this.hitboxControllers;
	}
}
