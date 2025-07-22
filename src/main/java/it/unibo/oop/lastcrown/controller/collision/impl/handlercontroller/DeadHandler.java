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
 * StateHandler implementation that manages the "dead" state of a character.
 *
 * This handler sets the death animation for the character and processes
 * one animation frame per call, performing cleanup when the animation
 * completes.
 */
public final class DeadHandler implements StateHandler {
    private final MatchController match;
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private int currentFrameIndex;

    public DeadHandler(final MatchController matchController, final EventFactory eventFactory,
            final CollisionResolver resolver) {
        this.match = matchController;
        this.eventFactory = eventFactory;
        this.resolver = resolver;
        this.currentFrameIndex = 0;
    }

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