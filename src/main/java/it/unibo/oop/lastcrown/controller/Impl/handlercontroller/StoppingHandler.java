package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
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
    private final MatchController match;
    private  boolean wait=false;

    /**
     * @param eventFactory the factory used to create events for the character's
     *                     state transitions
     */
    public StoppingHandler(final EventFactory eventFactory, MatchController matchController) {
        this.eventFactory = eventFactory;
        this.match = matchController;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        final int charId = character.getId().number();
        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isEngaged = isPlayer ? match.isPlayerEngaged(charId) : match.isEnemyEngaged(charId);


        System.out.println(wait);
        if (isPlayer && !isEngaged && wait) { //sono un player, non sono ingaggiato
            System.out.println("mi sono fermata e sono il player");
            wait=false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        } else if (match.isEngagedWithDead(charId)){ //se sei ingaggiato con uno morto
            character.setNextAnimation(Keyword.STOP);
            character.showNextFrame();
            wait=true;
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        }else{
            System.out.println(character.getId().type());
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        }
        return CharacterState.STOPPED;
    }

}

// devo modellare il fatto che se sei un player e hai un ingaggio ma ingaggiato
// alloa idle altrimenti che si player o meno si ferma combatte, ma deve sempre
// ritornare mentre si è lì lo stato stopped

/*
 * if (isPlayer && !isEngaged) {
 * queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
 * return CharacterState.IDLE;
 * } else {
 * character.setNextAnimation(Keyword.STOP);
 * character.showNextFrame();
 * queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
 * return CharacterState.COMBAT;
 * }
 * queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
 * return CharacterState.STOPPED;
 */