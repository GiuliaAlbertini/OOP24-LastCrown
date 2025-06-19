package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.view.characters.Keyword;
/**
 * A StateHandler that handles the character's stopping state.
 * Sets the STOP animation frame and schedules a transition to the COMBAT state.
 */
public final class StoppingHandler implements StateHandler {
    private final EventFactory eventFactory;

    /**
     * @param eventFactory the factory used to create events for the character's state transitions
     */
    public StoppingHandler(final EventFactory eventFactory) {
        this.eventFactory = eventFactory;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue, final int deltaTime) {
            character.setNextAnimation(Keyword.STOP);
            character.showNextFrame();
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
    }
}
