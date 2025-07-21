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
        System.out.println("sono in following" + character.getId().type());
        if (character instanceof PlayableCharacterController) {
            final int characterId = character.getId().number();


            boolean follow= matchController.isEngagedWithDead(characterId);
            final Optional<MovementResult> movementOpt = resolver.updateMovementFor(characterId, deltaTime);
            System.out.println("ti blocchi qui2?");
            //se il movimento è presente e il bro non è morto
            System.out.println(movementOpt.isPresent());
            if (movementOpt.isPresent()) {
                final MovementResult movement = movementOpt.get();
                final Movement mov = movement.delta();
                character.showNextFrameAndMove(mov);
                matchController.updateCharacterPosition(character, mov.x(), mov.y());
                if (!movementOpt.get().active()) {
                    System.out.println("sono un meleee e questo è l'ultimo mio avviso"+ character.getId().type());
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
            }/*else{
                queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
                return CharacterState.IDLE;
            }*/
        }

        queue.enqueue(this.eventFactory.createEvent(CharacterState.FOLLOWING));
        return CharacterState.FOLLOWING;
    }

}
