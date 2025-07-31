package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import it.unibo.oop.lastcrown.controller.characters.api.BossController;
import it.unibo.oop.lastcrown.controller.characters.api.CharacterHitObserver;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
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
            if (match.isPlayerEngaged(character.getId().number())) { // nemico
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else if (resolver.hasOpponentBossPartner(character.getId().number())) { // boss
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            } else if (resolver.hasOpponentRangedPartner(character.getId().number())) { // ranged
                opponentId = resolver.getOpponentRangedPartner(character.getId().number());
            }
        } else {
            if (match.isEnemyEngaged(character.getId().number())) {
                opponentId = match.getEngagedCounterpart(character.getId().number());
            } else if (resolver.hasOpponentWallPartner(character.getId().number())) {
                opponentId = resolver.getOpponentWallPartner(character.getId().number());
            } else {
                opponentId = resolver.getOpponentBossPartner(character.getId().number());
            }

        }

        Optional<CharacterHitObserver> opponentOpt;

        if (match.getWall().getCardIdentifier().number() == opponentId) {
            opponentOpt = Optional.of(match.getWall());
        } else {
            opponentOpt = match.getCharacterControllerById(opponentId).map(c -> (CharacterHitObserver) c);
        }

        if (opponentOpt.isEmpty()) {
            if (character.getId().type() != CardType.RANGED) {
                return CharacterState.IDLE;
            } else {
                return CharacterState.STOPPED;
            }
        }

        final CharacterHitObserver opponent = opponentOpt.get();
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

                // Se il nemico combatte il muro, anche il muro deve combattere
                if (opponent instanceof Wall wall) {
                    combatWall(wall, opponent);
                }


                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }

        return CharacterState.COMBAT;
    }

    private void setupCombat(GenericCharacterController character, CharacterHitObserver opponent) {
        if (character.getId().type() == CardType.BOSS && character instanceof BossController boss) {
            List<Integer> personaggi = resolver.getAllCharacterIdsInBossFight();
            boss.setOpponents(getCharactersFromIds(personaggi));
        } else {
            character.setOpponent(opponent);
        }

        character.setNextAnimation(Keyword.ATTACK);
        character.showNextFrame();

    }

    private void combatWall(Wall wall,  CharacterHitObserver enemy){
        List<Integer> enemies = resolver.getAllCharacterIdsInWallFight();
        wall.addOpponents(getCharactersFromIds(enemies));
        wall.doAttack();

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