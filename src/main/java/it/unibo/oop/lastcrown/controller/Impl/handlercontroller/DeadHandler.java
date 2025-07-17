package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.HashMap;
import java.util.Map;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * StateHandler implementation that manages the "dead" state of a character.
 *
 * This handler sets the death animation for the character and iterates through all
 * animation frames before confirming the character is dead.
 */
public final class DeadHandler implements StateHandler {
    private final MatchController match;
    private final EventFactory eventFactory;
    CollisionResolver resolver;

    int i;

    public DeadHandler(final MatchController matchController, final EventFactory eventFactory, CollisionResolver resolver) {
        this.match = matchController;
        this.eventFactory=eventFactory;
        this.resolver = resolver;

    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue, final int deltaTime) {
        final int frame = character.getDeathAnimationSize();
        character.setNextAnimation(Keyword.DEATH);
        while (i < frame) {
            character.showNextFrame();
            i++;

            if (i==frame){
                if (character.getId().type()== CardType.BOSS){
                    resolver.clearAllOpponentPairs();
                }
                match.releaseEngagementFor(character.getId().number());
                match.removeCharacterCompletelyById(character.getId().number());
                resolver.clearEnemyCollision(character.getId().number());
                resolver.clearBossFightPairById(character.getId().number());
            }
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;

        }
        return CharacterState.DEAD;
    }

}
