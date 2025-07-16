package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.List;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.impl.Pair;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * A StateHandler that handles the character's stopping state.
 * Sets the STOP animation frame and schedules a transition to the COMBAT state.
 */
public final class StoppingHandler implements StateHandler {
    private final EventFactory eventFactory;
    final CollisionResolver resolver;
    private final MatchController match;
    private boolean wait = false;
    boolean isPlayerInCollision = false;

    /**
     * @param eventFactory the factory used to create events for the character's
     *                     state transitions
     */
    public StoppingHandler(final EventFactory eventFactory, final MatchController matchController, CollisionResolver resolver) {
        this.eventFactory = eventFactory;
        this.match = matchController;
        this.resolver = resolver;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        final int charId = character.getId().number();
        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isEngaged = isPlayer ? match.isPlayerEngaged(charId) : match.isEnemyEngaged(charId);

        if (!isEngaged && wait) {
            wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        } else if (match.isEngagedWithDead(charId)) {
            character.setNextAnimation(Keyword.STOP);
            character.showNextFrame();
            wait = true;
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        } else {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        }
        return CharacterState.STOPPED;
    }
}
