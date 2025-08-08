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
        return Optional.ofNullable(charactersController.get(id));
    }

    @Override
    public Optional<HitboxController> getCharacterHitboxById(final int id) {
        return getCharacterControllerById(id)
                .map(hitboxControllers::get);
    }

    @Override
    public void removeCharacterById(final int characterId) {
        getCharacterControllerById(characterId).ifPresent(controller -> {
            hitboxControllers.remove(controller);
            playerFSMs.remove(characterId);
            charactersController.remove(characterId);
        });
    }

    @Override
    public void updateAll(final int deltaTime) {
        new ArrayList<>(playerFSMs.values()).forEach(fsm -> fsm.update(deltaTime));
    }

    @Override
    public boolean hasEntityWithType(final CardType type) {
        return charactersController.values().stream()
                .anyMatch(controller -> controller.getId().type() == type);
    }

    @Override
    public void setRadiusForAllPlayers(final int meleeRadius, final int rangedRadius) {
        hitboxControllers.entrySet().forEach(entry -> {
            final GenericCharacterController character = entry.getKey();
            final HitboxController hitboxController = entry.getValue();
            final CardType cardType = character.getId().type();

            if (cardType == CardType.MELEE) {
                setRadiusForController(hitboxController, meleeRadius);
            } else if (cardType == CardType.RANGED) {
                setRadiusForController(hitboxController, rangedRadius);
            }
        });
    }

    @Override
    public CharacterFSM getFSM(final int characterId) {
        return playerFSMs.get(characterId);
    }

    @Override
    public List<GenericCharacterController> getCharactersByType(final CardType cardType) {
        return charactersController.values().stream()
                .filter(controller -> controller.getId().type() == cardType)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<HitboxController> getHitboxForController(final GenericCharacterController controller) {
        return Optional.ofNullable(hitboxControllers.get(controller));
    }

    @Override
    public void setAllFSMsToState(final CharacterState newState) {
        playerFSMs.values().forEach(fsm -> fsm.setState(newState));
    }

    @Override
    public boolean isEnemyBeyondFrame(final int enemyId, final int frameWidth) {
        return hitboxControllers.entrySet().stream()
                .filter(entry -> isTargetEnemy(entry.getKey(), enemyId))
                .map(Map.Entry::getValue)
                .anyMatch(hitbox -> isHitboxBeyondFrame(hitbox, frameWidth));
    }

    @Override
    public Map<GenericCharacterController, HitboxController> getHitboxControllersMap() {
        return Map.copyOf(hitboxControllers);
    }

    /**
     * Helper method to set radius for a given hitbox controller.
     *
     * @param hitboxController the controller to modify
     * @param radius the new radius value
     */
    private void setRadiusForController(final HitboxController hitboxController, final int radius) {
        hitboxController.getRadius().ifPresent(radiusObj -> radiusObj.setRadius(radius));
    }

    /**
     * Helper method to check if a character is the target enemy.
     *
     * @param character the character to check
     * @param enemyId the target enemy ID
     * @return true if the character is the target enemy
     */
    private boolean isTargetEnemy(final GenericCharacterController character, final int enemyId) {
        return character.getId().type() == CardType.ENEMY
                && character.getId().number() == enemyId;
    }

    /**
     * Helper method to check if a hitbox is beyond the frame width.
     *
     * @param hitbox the hitbox to check
     * @param frameWidth the frame width limit
     * @return true if the hitbox position exceeds the frame width
     */
    private boolean isHitboxBeyondFrame(final HitboxController hitbox, final int frameWidth) {
        return hitbox.getHitbox().getPosition().x() > frameWidth;
    }
}
