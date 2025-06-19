package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

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
    private GenericCharacterController enemy;
    private GenericCharacterController charactercont;
    private CollisionResolver resolver;
    private MatchController match;

    /**
     * Constructs a CombatHandler with the required dependencies.
     * @param eventFactory the factory used to generate state transition events
     * @param resolver the collision resolver used to manage combat collisions
     * @param match the match controller providing access to character management
     */
    public CombatHandler(final EventFactory eventFactory, final CollisionResolver resolver, final MatchController match) {
        this.eventFactory = eventFactory;
        this.resolver = resolver;
        this.match = match;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue, final int deltaTime) {
        if (character != null) {
            final var player = character instanceof PlayableCharacterController;
            if (player) {
                if (match.getCharacterControllerById(
                        resolver.getEnemyId(character.getId().number())).isPresent()) {
                            enemy = match.getCharacterControllerById(
                                    resolver.getEnemyId(character.getId().number())).get();
                            character.setOpponent(enemy);
                            character.setNextAnimation(Keyword.ATTACK);
                            character.showNextFrame();
                            if (enemy.isDead()) {
                                resolver.clearEnemyCollision(
                                    enemy.getId().number(), character.getId().number());
                                match.removeCharacterCompletelyById(enemy.getId().number());
                                queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
                                return CharacterState.IDLE;
                            }
                }

            } else {
                //parte del nemico
                if (match.getCharacterControllerById(
                        resolver.getCharacterId(character.getId().number())).isPresent()) {
                            charactercont = match.getCharacterControllerById(
                                resolver.getCharacterId(character.getId().number())).get();
                            character.setOpponent(charactercont);
                            character.setNextAnimation(Keyword.ATTACK);
                            character.showNextFrame();
                            if (charactercont.isDead()) {
                              resolver.clearEnemyCollision(
                              character.getId().number(), charactercont.getId().number());
                              match.removeCharacterCompletelyById(charactercont.getId().number());
                              queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
                              return CharacterState.IDLE;
                            }
                }
            }

            if (character.isDead()) {
                queue.enqueue(eventFactory.createEvent(CharacterState.DEAD));
                return CharacterState.DEAD;
            }

        }
        queue.enqueue(eventFactory.createEvent(CharacterState.COMBAT));
        return CharacterState.COMBAT;

    }
}
