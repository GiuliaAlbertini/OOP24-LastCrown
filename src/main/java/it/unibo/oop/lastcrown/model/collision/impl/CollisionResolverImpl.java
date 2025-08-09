package it.unibo.oop.lastcrown.model.collision.impl;

import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.model.collision.api.FollowEnemy;
import it.unibo.oop.lastcrown.model.collision.impl.handler.HandleFollowEnemy;
import it.unibo.oop.lastcrown.utility.Pair;
import it.unibo.oop.lastcrown.utility.api.Point2D;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implements CollisionResolver to manage various types of collision
 * engagements. This class handles melee movements (ENEMY type), and pair-based engagements
 * for RANGED, BOSS, and WALL event types. It uses a centralized data structure
 * to avoid code duplication for similar engagement logic.
 */
public final class CollisionResolverImpl implements CollisionResolver {
    private final Map<Integer, FollowEnemy> activeFollowMovements = new HashMap<>();
    private final Map<Integer, Integer> completedMeleeEngagements = new HashMap<>();

    /**
     * Centralized storage for all pair-based engagements (Ranged, Boss, Wall).
     * The key is the type of event, and the value is a Set of pairs of IDs
     * involved in that engagement. Using ConcurrentHashMap.newKeySet() for
     * thread-safety.
     */
    private final Map<EventType, Set<Pair<Integer, Integer>>> pairEngagements = new EnumMap<>(EventType.class);

    @Override
    public void notify(final CollisionEvent event) {
        switch (event.getType()) {
            case ENEMY -> handleMeleeEngagement(event);
            case BOSS, RANGED, WALL -> handlePairEngagement(event);
        }
    }

    /**
     * Handles ENEMY type events by initiating a "follow" movement.
     *
     * @param event The ENEMY collision event.
     */
    private void handleMeleeEngagement(final CollisionEvent event) {
        final int characterId = event.getCollidable1().getCardIdentifier().number();
        final FollowEnemy movement = new HandleFollowEnemy(event);
        movement.startFollowing();
        activeFollowMovements.put(characterId, movement);
    }

    /**
     * Handles all pair-based collision events (RANGED, BOSS, WALL) in a generic
     * way.
     * It extracts the IDs of the two collidables and adds them as a pair to the
     * corresponding engagement set.
     *
     * @param event The collision event.
     */
    private void handlePairEngagement(final CollisionEvent event) {
        final int id1 = event.getCollidable1().getCardIdentifier().number();
        final int id2 = event.getCollidable2().getCardIdentifier().number();
        this.getEngagementSet(event.getType()).add(new Pair<>(id1, id2));
    }

    /**
     * Checks if an entity with the given ID is involved in a specific type of
     * engagement.
     *
     * @param id   The ID of the entity to check.
     * @param type The type of engagement (e.g., RANGED, BOSS, WALL).
     * @return true if the entity is involved, false otherwise.
     */
    @Override
    public boolean hasOpponentPartner(final int id, final EventType type) {
        return this.getEngagementSet(type).stream()
                .anyMatch(pair -> pair.get1() == id || pair.get2() == id);
    }

    /**
     * Gets the ID of the opponent partner for a given entity in a specific
     * engagement type.
     *
     * @param id   The ID of the entity whose partner is sought.
     * @param type The type of engagement (e.g., RANGED, BOSS, WALL).
     * @return The partner's ID, or -1 if no partner is found.
     */
    @Override
    public Optional<Integer> getOpponentPartner(final int id, final EventType type) {
        return this.getEngagementSet(type).stream()
                .filter(pair -> pair.get1() == id || pair.get2() == id)
                .map(pair -> pair.get1() == id ? pair.get2() : pair.get1())
                .findFirst();
    }

    /**
     * Retrieves a list of all IDs involved in a specific part of a pair for a given
     * engagement type.
     *
     * @param type        The type of engagement.
     * @param idExtractor A function to extract the desired ID from a pair (e.g.,
     *                    Pair::get1 or Pair::get2).
     * @return A list of unique IDs.
     */
    private List<Integer> getEngagedIds(final EventType type,
            final Function<Pair<Integer, Integer>, Integer> idExtractor) {
        return this.getEngagementSet(type).stream()
                .map(idExtractor)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllCharacterIdsInWallFight() {
        return getEngagedIds(EventType.WALL, Pair::get2);
    }

    @Override
    public List<Integer> getAllCharacterIdsInBossFight() {
        return getEngagedIds(EventType.BOSS, Pair::get1);
    }

    /**
     * Clears all engagements of a specific type.
     *
     * @param type The type of engagements to clear.
     */
    @Override
    public void clearEngagementsByType(final EventType type) {
        this.getEngagementSet(type).clear();
    }

    /**
     * Clears all pair-based engagements (RANGED, BOSS, WALL).
     */
    @Override
    public void clearAllPairEngagements() {
        pairEngagements.clear();
    }

    /**
     * Removes all engagement pairs of a specific type that involve a given ID.
     *
     * @param id   The ID to remove from engagements.
     * @param type The type of engagement to clear from.
     */
    @Override
    public void clearEngagementsById(final int id, final EventType type) {
        this.getEngagementSet(type).removeIf(pair -> pair.get1() == id || pair.get2() == id);
    }

    /**
     * Updates the movement for a character with the specified ID over the given
     * time delta. If the character is currently following an enemy, this method updates their
     * movement, calculates the movement delta, and determines whether the movement should
     * continue. If the movement is completed, it removes the follow movement and records the
     * engagement.
     *
     * @param characterId the unique identifier of the character whose movement is
     *                    to be updated
     * @param deltaMs     the time in milliseconds since the last update
     * @return an {@link Optional} containing the MovementResult if the
     *         character has an active movement, or Optional#empty() if
     *         there is no active movement for the character
     */
    @Override
    public Optional<MovementResult> updateMovementFor(final int characterId, final long deltaMs) {
        final FollowEnemy movement = activeFollowMovements.get(characterId);
        if (movement != null) {
            final boolean stillMoving = movement.update(deltaMs);
            final Point2D delta = movement.getDelta();
            final Movement movementDelta = new Movement((int) delta.x(), (int) delta.y());

            if (!stillMoving) {
                activeFollowMovements.remove(characterId);
                completedMeleeEngagements.put(movement.getEnemy().getCardIdentifier().number(), characterId);
            }
            return Optional.of(new MovementResult(
                    movement.getCharacter(),
                    movement.getCurrentPosition(),
                    movementDelta,
                    stillMoving));
        }
        return Optional.empty();
    }

    /**
     * Checks if the enemy with the specified ID has been involved in a completed
     * melee engagement.
     *
     * @param id the unique identifier of the enemy to check
     * @return true if the enemy has either initiated or been the target of a
     *         completed melee engagement; false otherwise
     */

    @Override
    public boolean wasEnemyCollided(final int id) {
        return completedMeleeEngagements.containsKey(id) || completedMeleeEngagements.containsValue(id);
    }

    /**
     * Removes all completed melee engagements involving the specified character.
     *
     * @param characterId the unique identifier of the character whose completed
     *                    melee engagements should be cleared
     */
    @Override
    public void clearEnemyCollision(final int characterId) {
        completedMeleeEngagements.values().removeIf(value -> value == characterId);
    }

    /**
     * Utility method to safely get the Set of pairs for a given event type.
     * If the set does not exist for that type, it is created and added to the map.
     *
     * @param type The event type.
     * @return The non-null, thread-safe Set of pairs for that type.
     */
    private Set<Pair<Integer, Integer>> getEngagementSet(final EventType type) {
        return pairEngagements.computeIfAbsent(type, k -> ConcurrentHashMap.newKeySet());
    }
}
