package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.collision.api.MatchController;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.collision.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.model.collision.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;

/**
 * Handles the COMBAT state for characters during gameplay.
 * This handler manages attack logic, checks for opponent death,
 * resolves collisions, and transitions the character to the appropriate state.
 */
public final class CombatHandler implements StateHandler {
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private final MatchController match;
    int opponentId;

    public CombatHandler(final EventFactory eventFactory,
            final CollisionResolver resolver,
            final MatchController match) {
        this.eventFactory = eventFactory;
        this.resolver = resolver;
        this.match = match;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {

        if (character.isDead()) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        }
        //System.err.println("sono in combat1" + character.getId().type());
        boolean soldato =match.isPlayerEngaged(character.getId().number());
        boolean boss=resolver.hasOpponentBossPartner(character.getId().number());
        boolean ranged= resolver.hasOpponentRangedPartner(character.getId().number());

        final boolean isPlayer = character instanceof PlayableCharacterController;
        if (isPlayer) {/*
            System.out.println("collisione soldier " + soldato);
            System.out.println("collisione boss "+ boss);
            System.out.println("collisione ranged "+ranged);
            */
            System.err.println("sono in combat2"+ character.getId().type());

            if (match.isPlayerEngaged(character.getId().number())) {
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else if (resolver.hasOpponentBossPartner(character.getId().number())) {
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            } else if (resolver.hasOpponentRangedPartner(character.getId().number())) {
                opponentId = resolver.getOpponentRangedPartner(character.getId().number());

            }
        } else {
            if (match.isEnemyEngaged(character.getId().number())) {
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else {
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            }

        }

        // ti prendi il controller
        final Optional<GenericCharacterController> opponentOpt = match.getCharacterControllerById(opponentId);
        if (opponentOpt.isEmpty()) {
            if (character.getId().type() != CardType.RANGED) {
                    //System.err.println("sono in combat3"+ character.getId().type());

                queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
                return CharacterState.IDLE;
            } else {
                System.err.println("sono in combat4"+ character.getId().type());
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            }
        }

        final GenericCharacterController opponent = opponentOpt.get();
        if (isPlayer) {
            System.err.println("sono in combat5"+ character.getId().type());

            // === COMBATTIMENTO GIOCATORE ===
            if (opponent.isDead()) {
                queue.clear();
                System.out.println("non penso di entrare in stopped se non sono morta" + character.getId().type());
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            } else {
                System.err.println("sono in combat6"+ character.getId().type());

                setupCombat(character, opponent);
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }

        } else {
            // === COMBATTIMENTO NEMICO ===

            if (opponent.isDead()) {
                queue.clear();
                System.out.println("non penso di entrare in stopped se non sono morta" + character.getId().type());
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            } else {
                setupCombat(character, opponent);

                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }
        return CharacterState.COMBAT;
    }

    private void setupCombat(GenericCharacterController character, GenericCharacterController opponent) {
        if (character.getId().type() == CardType.BOSS && character instanceof BossController boss) {
            List<Integer> personaggi = resolver.getAllCharacterIdsInBossFight();
            boss.setOpponents(getCharactersFromIds(personaggi));
        } else {
            character.setOpponent(opponent);
        }

        character.setNextAnimation(Keyword.ATTACK);
        character.showNextFrame();
    }

    private List<CharacterHitObserver> getCharactersFromIds(final List<Integer> ids) {
        final List<CharacterHitObserver> characters = new ArrayList<>();
        for (final Integer id : ids) {
            final Optional<GenericCharacterController> controllerOpt = match.getCharacterControllerById(id);
            controllerOpt.ifPresent(characters::add); // valido perché è anche CharacterHitObserver
        }
        return characters;
    }

}
