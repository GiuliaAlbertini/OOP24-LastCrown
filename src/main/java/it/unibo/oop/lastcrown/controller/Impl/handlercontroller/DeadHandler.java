package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * StateHandler implementation that manages the "dead" state of a character.
 * 
 * This handler sets the death animation for the character and iterates through all
 * animation frames before confirming the character is dead.
 */
public final class DeadHandler implements StateHandler {

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue, final int deltaTime) {
        final int frame = character.getDeathAnimationSize();
        character.setNextAnimation(Keyword.DEATH);
        for (int i = 0; i < frame; i++) {
            character.showNextFrame();
        }
        return CharacterState.DEAD;
    }
}
