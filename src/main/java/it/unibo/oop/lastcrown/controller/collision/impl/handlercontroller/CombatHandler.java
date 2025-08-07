package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;

import it.unibo.oop.lastcrown.controller.characters.api.Wall;
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
    private int opponentId;

    /**
     * Instantiates a combat handler.
     * @param eventFactory an event factory.
     * @param resolver the desired {@link CollisionResolver}
     * @param match the desired {@link MatchController}
     */
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

        if (character.getId().type() == CardType.RANGED && match.retreat()) {
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        final CardType characterType = character.getId().type();

        if (characterType == CardType.HERO || characterType == CardType.MELEE || characterType == CardType.RANGED) {
            if (match.isEntityEngaged(character.getId().number())) {
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else if (resolver.hasOpponentBossPartner(character.getId().number())) { // boss
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            } else if (resolver.hasOpponentRangedPartner(character.getId().number())) { // ranged
                opponentId = resolver.getOpponentRangedPartner(character.getId().number());
            }
        } else {
            if (match.isEntityEngaged(character.getId().number())) {
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else if (resolver.hasOpponentWallPartner(character.getId().number())) {
                opponentId = resolver.getOpponentWallPartner(character.getId().number());
            } else {
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            }
        }

        final Optional<CharacterHitObserver> opponentOpt;

        if (match.getWall().getCardIdentifier().number() == opponentId) {
            opponentOpt = Optional.of(match.getWall());
        } else {
            opponentOpt = match.getCharacterControllerById(opponentId).map(c -> c);
        }

        if (opponentOpt.isEmpty()) {
            if (character.getId().type() != CardType.RANGED) {
                return CharacterState.IDLE;
            } else {
                return CharacterState.STOPPED;
            }
        }

        final CharacterHitObserver opponent = opponentOpt.get();

        if (characterType == CardType.HERO || characterType == CardType.MELEE || characterType == CardType.RANGED) {
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
            if (opponent.isDead()) {
                queue.clear();
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            } else {
                setupCombat(character, opponent);
                if (opponent instanceof Wall wall) {
                    combatWall(wall);
                }
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }

        return CharacterState.COMBAT;
    }

    private void setupCombat(final GenericCharacterController character, final CharacterHitObserver opponent) {
        if (character.getId().type() == CardType.BOSS && character instanceof BossController boss) {
            final List<Integer> characters = resolver.getAllCharacterIdsInBossFight();
            boss.setOpponents(getCharactersFromIds(characters));
        } else {
            character.setOpponent(opponent);
        }

        character.setNextAnimation(Keyword.ATTACK);
        character.showNextFrame();
    }

    private void combatWall(final Wall wall) {
        if (wall.getCurrentHealth() <= 0) {
            match.setAllFSMsToState(CharacterState.STOPPED);
            match.halveHeroMaxHealth();
        } else {
            final List<Integer> enemies = resolver.getAllCharacterIdsInWallFight();
            wall.addOpponents(getCharactersFromIds(enemies));
            wall.doAttack();
        }
    }

    private List<CharacterHitObserver> getCharactersFromIds(final List<Integer> ids) {
        final List<CharacterHitObserver> characters = new ArrayList<>();
        for (final Integer id : ids) {
            final Optional<GenericCharacterController> controllerOpt = match.getCharacterControllerById(id);
            controllerOpt.ifPresent(characters::add);
        }
        return characters;
    }
}
