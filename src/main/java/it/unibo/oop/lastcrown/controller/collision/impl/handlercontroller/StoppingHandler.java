package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * A StateHandler that handles the character's stopping state.
 * Sets the STOP animation frame and schedules a transition to the COMBAT state.
 */
public final class StoppingHandler implements StateHandler {
    private final EventFactory eventFactory;
    final CollisionResolver resolver;
    final EnemyRadiusScanner scanner;
    private final MatchController match;
    private boolean wait = false;


    /**
     * @param eventFactory the factory used to create events for the character's
     *                     state transitions
     */
    public StoppingHandler(final EventFactory eventFactory, final MatchController matchController,
            CollisionResolver resolver, final EnemyRadiusScanner scanner) {
        this.eventFactory = eventFactory;
        this.match = matchController;
        this.resolver = resolver;
        this.scanner= scanner;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {
        final int charId = character.getId().number();
        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isEngaged = isPlayer ? match.isPlayerEngaged(charId) : match.isEnemyEngaged(charId);
        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        //NON SEI INGAGGIATO
            System.out.println("sono dentro allo stopped"+ character.getId().type());
        if (!isEngaged && wait && !isBossFight) {
            //System.out.println("entro qui se ho combattuto e devo continuare a giocare o se non sono stato ingaggiato");
            wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
            //se sei ingaggiato con un morto
        } else if (match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId) || character.getId().type() == CardType.RANGED) {
            //System.out.println("qui devo entrare per forza");

            if (character.getId().type() == CardType.RANGED) {
                if (match.isRangedFightPartnerDead(charId)){
                    //System.out.println("qui devo entrare per forza2");

                    character.setNextAnimation(Keyword.STOP);
                    character.showNextFrame();
                    System.out.println("sto guardando da stopping");
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;

                    //se non sono nella lista partner significa che sono nella bossfight
                }else if (!resolver.hasOpponentRangedPartner(charId) && !resolver.hasOpponentBossPartner(charId) ) {
                    //System.out.println("qui devo entrare per forza3");

                    Optional<GenericCharacterController> controllerOpt = match.getCharacterControllerById(character.getId().number());
                    if (controllerOpt.isPresent() && controllerOpt.get() instanceof PlayableCharacterController playerCol) {
                        Optional<CollisionEvent> eventOpt = scanner.scanForFollowEventForPlayer(playerCol);
                        eventOpt.ifPresent(event -> {
                            match.notifyCollisionObservers(event);
                            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));

                        });
                    }else{
                            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    }
                }else{

                    //System.out.println("volevo sapere dove entravo4");
                    queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                }

                character.setNextAnimation(Keyword.STOP);
                character.showNextFrame();
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));

            } else {
                character.setNextAnimation(Keyword.STOP);
                character.showNextFrame();
                wait = true;
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));

            }

        } else {
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
        }
        return CharacterState.STOPPED;
    }
}
