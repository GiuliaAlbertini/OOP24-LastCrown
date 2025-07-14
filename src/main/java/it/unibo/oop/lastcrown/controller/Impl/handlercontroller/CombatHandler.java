package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.Optional;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
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

    public CombatHandler(final EventFactory eventFactory,
            final CollisionResolver resolver,
            final MatchController match) {
        this.eventFactory = eventFactory;
        this.resolver = resolver;
        this.match = match;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue, final int deltaTime) {
        if (character.isDead()) {
            queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
            return CharacterState.DEAD;
        }

        final boolean isPlayer = character instanceof PlayableCharacterController;
        final Optional<Integer> opponentIdOpt = isPlayer
                ? resolver.getEnemyId(character.getId().number())
                : resolver.getCharacterId(character.getId().number());

        if (opponentIdOpt.isEmpty()) {
            return CharacterState.IDLE;
        }

        final int opponentId = opponentIdOpt.get();
        final Optional<GenericCharacterController> opponentOpt = match.getCharacterControllerById(opponentId);
        if (opponentOpt.isEmpty()) {
            System.out.println("valore di OPPONENT" + opponentOpt.isEmpty());
            return CharacterState.IDLE;
        }
        final GenericCharacterController opponent = opponentOpt.get();
        if (isPlayer) {
            // === COMBATTIMENTO GIOCATORE ===
            if (opponent.isDead()) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED)); // nemico può morire e animarsi
            } else {
                System.out.println("sto combattendo e sono il player");
                setupCombat(character, opponent);
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }

        } else {
            if (opponent.isDead()) {
                queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
            } else {
                //System.out.println("sto combattendo e sono il nemico");
                setupCombat(character, opponent);
                queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
                return CharacterState.COMBAT;
            }
        }
        return CharacterState.COMBAT;
    }

    private void setupCombat(GenericCharacterController character,
            GenericCharacterController opponent) {
        character.setOpponent(opponent);
        character.setNextAnimation(Keyword.ATTACK);
        character.showNextFrame();
    }
}
