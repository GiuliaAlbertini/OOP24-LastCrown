package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.model.card.CardType;
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

        final boolean isPlayer = character instanceof PlayableCharacterController;
        if (isPlayer) {
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
                return CharacterState.IDLE;
            } else {
                return CharacterState.STOPPED;
            }
        }

        final GenericCharacterController opponent = opponentOpt.get();
        if (isPlayer) {
            // === COMBATTIMENTO GIOCATORE ===
            if (opponent.isDead()) {
                queue.clear();
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                return CharacterState.STOPPED;
            } else {
                setupCombat(character, opponent);
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }

        } else {
            // === COMBATTIMENTO NEMICO ===

            if (opponent.isDead()) {
                queue.clear();
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
