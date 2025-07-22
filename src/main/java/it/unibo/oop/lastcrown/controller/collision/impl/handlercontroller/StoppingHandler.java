package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * A StateHandler that handles the character's stopping state.
 * Sets the STOP animation frame and schedules a transition to the COMBAT state
 * or IDLE based on engagement and combat status.
 */
public final class StoppingHandler implements StateHandler {
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private final EnemyRadiusScanner scanner;
    private final MatchController match;
    private boolean wait;

    /**
     * @param eventFactory the factory used to create events for the character's
     * state transitions
     * @param matchController the match controller for game-wide interactions
     * @param resolver the collision resolver for combat logic
     * @param scanner the enemy radius scanner for detecting targets
     */
    public StoppingHandler(final EventFactory eventFactory, final MatchController matchController,
                           final CollisionResolver resolver, final EnemyRadiusScanner scanner) {
        this.eventFactory = eventFactory;
        this.match = matchController;
        this.resolver = resolver;
        this.scanner = scanner;
        this.wait = false;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
                                 final int deltaTime) {
        final int charId = character.getId().number();
        final CardType characterType = character.getId().type();

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        if (characterType == CardType.RANGED) {
            handleRangedCharacter(character, queue, charId);
            return CharacterState.STOPPED;
        }

        final boolean isEngaged = match.isPlayerEngaged(charId) || match.isEnemyEngaged(charId);
        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId);

        if (!isEngaged && wait && !isBossFight) {
            this.wait = false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        } else if (isEngagedWithDead) {
            this.wait = true;
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        } else {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }
    }

    /**
     * Handles the specific logic for RANGED characters in the STOPPED state.
     * @param character The character controller.
     * @param queue The event queue.
     * @param charId The ID of the character.
     */
    private void handleRangedCharacter(final GenericCharacterController character, final EventQueue queue, final int charId) {
        if (match.isRangedFightPartnerDead(charId)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        } else if (!resolver.hasOpponentRangedPartner(charId)) {
            match.getCharacterControllerById(charId)
                 .filter(PlayableCharacterController.class::isInstance)
                 .map(PlayableCharacterController.class::cast)
                 .flatMap(scanner::scanForFollowEventForPlayer)
                 .ifPresent(event -> {
                     match.notifyCollisionObservers(event);
                     queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                 });
            if (!queue.hasPendingEvents()) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            }
        } else {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        }
    }
}
