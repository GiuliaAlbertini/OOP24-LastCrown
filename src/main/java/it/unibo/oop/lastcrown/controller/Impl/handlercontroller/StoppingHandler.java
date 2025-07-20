package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.List;

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
        //System.out.println("sono nello stopped" + character.getId().type());
        final int charId = character.getId().number();
        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isEngaged = isPlayer ? match.isPlayerEngaged(charId) : match.isEnemyEngaged(charId);
        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        //NON SEI INGAGGIATO
        if (!isEngaged && wait && !isBossFight) {
            //System.out.println("entro qui se ho combattuto e devo continuare a giocare o se non sono stato ingaggiato");
            wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
            //se sei ingaggiato con un morto
        } else if (match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId)
                || character.getId().type() == CardType.RANGED) {
            if (character.getId().type() == CardType.RANGED) {
                //System.out.println("in questo tempo sono qui");
                final List<CollisionEvent> events = scanner.scanForFollowEvents();
                //System.out.println(events);

                if (!events.isEmpty()) {
                    //System.out.println("sono in stopper?");
                    final CollisionEvent event = events.get(0);
                    match.notifyCollisionObservers(event);
                }
                //System.out.println(resolver.hasOpponentRangedPartner(charId));
                if (resolver.hasOpponentRangedPartner(charId)){
                    //System.out.println("combatto e sono un ranged");
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
            //System.out.println("ora combatto");
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        }
        return CharacterState.STOPPED;
    }
}
