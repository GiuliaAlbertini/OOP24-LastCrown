package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.EnemyController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.impl.Pair;
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
    boolean isPlayerInCollision = false;

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
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId);
        //NON SEI INGAGGIATO

        System.out.println("personaggio" + character.getId().type());
        System.out.println("controlliamo questo ingaggio" + isEngagedWithDead);
        if (!isEngaged && wait && !isBossFight) {
            System.out.println("sono ferma qui in stop 1");
            //System.out.println("entro qui se ho combattuto e devo continuare a giocare o se non sono stato ingaggiato");
            wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
            //se sei ingaggiato con un morto
        } else if (match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId) || character.getId().type() == CardType.RANGED) {
            //sono un ranged
            System.out.println("sono ferma qui in stop 2"+ character.getId().type());

            if (character.getId().type() == CardType.RANGED) {
                if (match.isRangedFightPartnerDead(charId)){
                    character.setNextAnimation(Keyword.STOP);
                    character.showNextFrame();
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;

                }else if (!resolver.hasOpponentRangedPartner(charId) ) {
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
                    queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                }

                character.setNextAnimation(Keyword.STOP);
                character.showNextFrame();
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));

            } else {
                System.out.println("sono ferma qui in stop 3");

                character.setNextAnimation(Keyword.STOP);
                character.showNextFrame();
                wait = true;
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));

            }

        } else {
            System.out.println("sono ferma qui in stop 4");

            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        }
        return CharacterState.STOPPED;
    }
}
