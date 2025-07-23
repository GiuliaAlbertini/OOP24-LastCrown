package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * StateHandler implementation that manages the "DEAD" state of a character.
 *
 * This handler triggers the character's death animation frame-by-frame and, once
 * the animation ends, performs cleanup operations such as removing the character
 * from the match and clearing relevant collision data.
 */
public final class DeadHandler implements StateHandler {

    private final MatchController match;
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private int currentFrameIndex;

    /**
     * @param matchController the match controller used to manage character engagement and removal
     * @param eventFactory the factory used to create character state events
     * @param resolver the collision resolver for clearing collision data
     */
    public DeadHandler(final MatchController matchController, final EventFactory eventFactory,
                       final CollisionResolver resolver) {
        this.match = matchController;
        this.eventFactory = eventFactory;
        this.resolver = resolver;
        this.currentFrameIndex = 0;
    }

    /**
     * Handles the logic associated with a character in the "DEAD" state.
     * If the death animation has not yet finished, the method shows the next
     * animation frame. Once the animation is complete, the character is removed
     * from the game, and all related collision data is cleared.
     *
     * @param character the character controller
     * @param queue the event queue used to enqueue new state transitions
     * @param deltaTime the time passed since the last frame (not used in this implementation)
     * @return CharacterState DEA indicating that the character remains in the DEAD state
     */
    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
                                 final int deltaTime) {
        if (character == null) {
            return CharacterState.DEAD;
        }

        final int totalFrames = character.getDeathAnimationSize();
        character.setNextAnimation(Keyword.DEATH);

        if (currentFrameIndex < totalFrames) {
            character.showNextFrame();
            currentFrameIndex++;
        }

        if (currentFrameIndex >= totalFrames) {
            if (character.getId().type() == CardType.BOSS) {
                resolver.clearAllOpponentPairs();
            }
            match.releaseEngagementFor(character.getId().number());
            match.removeCharacterCompletelyById(character.getId().number());
            resolver.clearEnemyCollision(character.getId().number());
            resolver.clearAllOpponentRangedPairs();
            resolver.clearBossFightPairById(character.getId().number());
            this.currentFrameIndex = 0;
            return CharacterState.DEAD;
        }

        queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
        return CharacterState.DEAD;
    }
}
