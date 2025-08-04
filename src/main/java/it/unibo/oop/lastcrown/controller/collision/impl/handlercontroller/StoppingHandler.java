package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.HitboxController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

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
    private static final int ENEMY_SPEED = 2;
    private boolean retreat;


    /**
     * @param eventFactory    the factory used to create events for the character's
     *                        state transitions
     * @param matchController the match controller for game-wide interactions
     * @param resolver        the collision resolver for combat logic
     * @param scanner         the enemy radius scanner for detecting targets
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

        final boolean isPlayer = character instanceof PlayableCharacterController;
        final boolean isBosshandle = resolver.hasOpponentBossPartner(character.getId().number());
        final int charId = character.getId().number();
        final CardType characterType = character.getId().type();

        match.matchResult();

        if (characterType == CardType.HERO && !match.hasBossInMap() && match.isRoundSpawnComplete()
            && !match.hasAnyPlayerInMap() && !match.hasAnyEnemiesInMap()){
            match.setRadiusPlayerInMap();
            match.getRandomBossFromFirstList();
        }

        // == caso heroe ==
        if (characterType == CardType.HERO) {
            handleHeroCharacter(character, queue, charId);
            return CharacterState.STOPPED;
        }

            // == caso Ranged ==
        if (characterType == CardType.RANGED) {
            if (!retreat) {
                return handleRangedCharacter(character, queue, charId);
            } else {
                return handleRetreatRanged(character, queue, charId);
            }
        }

        //SE NON SEI HERO, inizio ritirata
        if (!match.hasBossInMap()) { //se il boss non è nella mappa allora
            if (match.getWall().getCurrentHealth() <= 0) {
                if (character.getId().type() == CardType.ENEMY) {
                    final Movement movementCharacter = new Movement(ENEMY_SPEED, 0);
                    character.setNextAnimation(Keyword.RETREAT);
                    character.showNextFrameAndMove(movementCharacter);
                    match.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());
                    retreat=true;
                    //aspetta che tutti i nemici siano andati via dalla mappa
                    if (match.isEnemyBeyondFrame(character.getId().number())) {
                        queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
                        return CharacterState.DEAD;
                    }
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                } else {
                    character.setNextAnimation(Keyword.STOP);
                    character.showNextFrame();
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
                //se sono spawnati tutti i nemici e non ci sono più nemici nella mappa
            }else if (match.isRoundSpawnComplete() && !match.hasAnyEnemiesInMap()) {
                    match.getRandomBossFromFirstList();
                    match.setRadiusPlayerInMap();
                    character.setNextAnimation(Keyword.STOP);
                    character.showNextFrame();
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
            }
        }else if (!isBosshandle){ //se il boss compare andate tutti in idle
            //match.setAllFSMsToState(CharacterState.IDLE);
            retreat=false;
            queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
            return CharacterState.IDLE;
        }

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        // == caso TrupZone ==
        if (isPlayer && isAtTroopZoneLimit(character) && !match.hasBossInMap()) {
            handleCharacterTrupzone((PlayableCharacterController) character, queue, charId);
            return CharacterState.STOPPED;
        }




        final boolean isEngaged = match.isPlayerEngaged(charId) || match.isEnemyEngaged(charId);
        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId);
        final boolean isWallFight = resolver.hasOpponentWallPartner(charId);
        // == caso enemy ==
        if (!isEngaged && wait && !isBossFight && !isWallFight) {
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
     *
     * @param character The character controller.
     * @param queue     The event queue.
     * @param charId    The ID of the character.
     */
    private CharacterState handleRangedCharacter(final GenericCharacterController character, final EventQueue queue,
            final int charId) {

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        match.getCharacterControllerById(charId)
                    .filter(PlayableCharacterController.class::isInstance)
                    .map(PlayableCharacterController.class::cast)
                    .flatMap(scanner::scanForFollowEventForPlayer)
                    .ifPresent(event -> {
                        match.notifyCollisionObservers(event);
                    });

        //se la ritirata è falsa
        if (match.isRangedFightPartnerDead(charId)) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        //se la ritirata è falsa
        } else if ((resolver.hasOpponentRangedPartner(charId)) || isBossFight) {
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }



    private CharacterState handleRetreatRanged(final GenericCharacterController character, final EventQueue queue, final int charId) {

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();

        final boolean isBossFight = resolver.hasOpponentBossPartner(charId);
        match.getCharacterControllerById(charId)
                    .filter(PlayableCharacterController.class::isInstance)
                    .map(PlayableCharacterController.class::cast)
                    .flatMap(scanner::scanForFollowEventForPlayer)
                    .ifPresent(event -> {
                        match.notifyCollisionObservers(event);
                    });

         if (isBossFight) {
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }




    private CharacterState handleHeroCharacter(final GenericCharacterController character, final EventQueue queue,
            final int charId) {
        final boolean isEngagedWithDead = match.isBossFightPartnerDead(charId);

        character.setNextAnimation(Keyword.STOP);
        character.showNextFrame();


        if (match.hasBossInMap() && !match.hasAnyPlayerInMap()) {
            // Boss in campo, nessun altro player: prova lo scanner
            scanner.scanForFollowEventForHero(character)
                    .ifPresent(match::notifyCollisionObservers);

        if (isEngagedWithDead) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        } else if (resolver.hasOpponentBossPartner(character.getId().number())) {
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;

    }





    private CharacterState handleCharacterTrupzone(final PlayableCharacterController player, final EventQueue queue,
            final int charId) {

        scanner.scanForFollowEventForPlayer(player)
                .ifPresent(match::notifyCollisionObservers);
        final boolean isEngagedWithDead = match.isEngagedWithDead(charId) || match.isBossFightPartnerDead(charId);

        if (isEngagedWithDead) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        } else if (match.isPlayerEngaged(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        } else if (resolver.hasOpponentBossPartner(player.getId().number())) {
            queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
            return CharacterState.COMBAT;
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
        return CharacterState.STOPPED;
    }

    private boolean isAtTroopZoneLimit(final GenericCharacterController player) {
        Optional<HitboxController> hitboxController = match.getCharacterHitboxById(player.getId().number());
        if (hitboxController.isPresent()) {
            int limit = match.getMatchView().getTrupsZoneLimit() - hitboxController.get().getHitbox().getWidth();
            int roundedLimit = limit + (5 - (limit % 5)) % 5;
            return hitboxController.get().getHitbox().getPosition().x() >= roundedLimit;
        }
        return false;
    }
}
