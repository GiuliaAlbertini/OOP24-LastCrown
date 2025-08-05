package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.collision.impl.MovementResult;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * StateHandler implementation that manages the FOLLOWING state,
 * where a character follows a target by updating its position and animation.
 */
public final class FollowingHandler implements StateHandler {
    private final MatchController matchController;
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;

    /**
     * Constructs a FollowingHandler with required controllers and resolver.
     *
     * @param matchController the match controller to update character positions
     * @param resolver        the collision resolver used to compute movement
     * @param eventFactory    factory to create character events
     */
    public FollowingHandler(final MatchController matchController,
            final CollisionResolver resolver,
            final EventFactory eventFactory) {
        this.matchController = matchController;
        this.eventFactory = eventFactory;
        this.resolver = resolver;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {

        if (character == null) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        }


        if (!(character instanceof PlayableCharacterController)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        }

        final int characterId = character.getId().number();

        if (matchController.isEngagedWithDead(characterId) || matchController.isBossFightPartnerDead(characterId)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        final Optional<MovementResult> movementOpt = resolver.updateMovementFor(characterId, deltaTime);

        if (movementOpt.isPresent()) {
            final MovementResult movement = movementOpt.get();
            final Movement mov = movement.delta();

            character.showNextFrameAndMove(mov);
            matchController.updateCharacterPosition(character, mov.x(), mov.y());

            if (!movement.active()) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            }
        } else {

            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        }

        queue.enqueue(this.eventFactory.createEvent(CharacterState.FOLLOWING));
        return CharacterState.FOLLOWING;
    }
}