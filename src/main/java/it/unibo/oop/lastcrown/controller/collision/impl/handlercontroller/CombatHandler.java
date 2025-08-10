package it.unibo.oop.lastcrown.controller.collision.impl.handlercontroller;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
import it.unibo.oop.lastcrown.model.collision.api.EventType;
import it.unibo.oop.lastcrown.view.characters.Keyword;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Handles the COMBAT state for characters. This handler manages attack logic,
 * checks for opponent death, resolves collisions, and transitions the
 * character to the appropriate next state.
 */
@SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Handler needs live references to orchestrate game logic.")
public final class CombatHandler implements StateHandler {
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private final MatchController match;

    /**
     * Constructs a new CombatHandler.
     *
     * @param eventFactory an event factory.
     * @param resolver     the collision resolver.
     * @param match        the match controller.
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

        return findOpponentId(character)
                .flatMap(this::getOpponentObserverById)
                .map(opponent -> handleOpponentFound(character, opponent, queue))
                .orElseGet(() -> handleNoOpponent(character));
    }

    private CharacterState handleOpponentFound(final GenericCharacterController character,
            final CharacterHitObserver opponent,
            final EventQueue queue) {
        if (opponent.isDead()) {
            queue.clear();
            queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            return CharacterState.STOPPED;
        }

        setupCombat(character, opponent);
        if (opponent instanceof Wall wall) {
            combatWall(wall);
        }
        queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        return CharacterState.COMBAT;
    }

    private CharacterState handleNoOpponent(final GenericCharacterController character) {
        if (character.getId().type() == CardType.RANGED) {
            return CharacterState.STOPPED;
        }
        return CharacterState.IDLE;
    }

    /**
     * Tries to find an opponent ID by chaining checks using Optional.or().
     *
     * @param character The character searching for an opponent.
     * @return An Optional containing the opponent's ID if found.
     */
    private Optional<Integer> findOpponentId(final GenericCharacterController character) {
        final int characterId = character.getId().number();

        final Optional<Integer> mainEngagement = Optional.of(match.getEngagedCounterpart(characterId))
                .filter(id -> id != -1);

        if (isPlayerLike(character)) {
            return mainEngagement
                    .or(() -> resolver.getOpponentPartner(characterId, EventType.BOSS))
                    .or(() -> resolver.getOpponentPartner(characterId, EventType.RANGED));
        } else {
            return mainEngagement
                    .or(() -> resolver.getOpponentPartner(characterId, EventType.WALL))
                    .or(() -> resolver.getOpponentPartner(characterId, EventType.BOSS));
        }
    }

    private Optional<CharacterHitObserver> getOpponentObserverById(final int opponentId) {
        if (match.getWall().getCardIdentifier().number() == opponentId) {
            return Optional.of(match.getWall());
        }
        return match.getCharacterControllerById(opponentId).map(c -> c);
    }

    private boolean isPlayerLike(final GenericCharacterController character) {
        final CardType type = character.getId().type();
        return type == CardType.HERO || type == CardType.MELEE || type == CardType.RANGED;
    }

    private void setupCombat(final GenericCharacterController character, final CharacterHitObserver opponent) {
        if (character.getId().type() == CardType.BOSS && character instanceof BossController boss) {
            final List<Integer> charactersInFight = resolver.getAllCharacterIdsInBossFight();
            boss.setOpponents(getCharactersFromIds(charactersInFight));
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
            final List<Integer> enemiesOnWall = resolver.getAllCharacterIdsInWallFight();
            wall.addOpponents(getCharactersFromIds(enemiesOnWall));
            wall.doAttack();
        }
    }

    private List<CharacterHitObserver> getCharactersFromIds(final List<Integer> ids) {
        return ids.stream()
                .map(match::getCharacterControllerById)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }
}
