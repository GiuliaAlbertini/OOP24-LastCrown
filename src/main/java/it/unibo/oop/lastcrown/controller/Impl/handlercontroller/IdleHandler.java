package it.unibo.oop.lastcrown.controller.impl.handlercontroller;

import java.util.List;

import it.unibo.oop.lastcrown.controller.api.MatchController;
import it.unibo.oop.lastcrown.controller.characters.api.GenericCharacterController;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.impl.EnemyRadiusScanner;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.CharacterState;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventFactory;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.EventQueue;
import it.unibo.oop.lastcrown.controller.impl.eventcharacters.StateHandler;
import it.unibo.oop.lastcrown.model.api.CollisionEvent;
import it.unibo.oop.lastcrown.model.api.CollisionResolver;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.Movement;

/**
 * Handler responsible for managing the IDLE state of characters.
 * In the IDLE state, the character performs a default movement (e.g., walking),
 * and transitions to FOLLOWING if an enemy is detected within range (for
 * players),
 * or to STOPPED if a collision is detected (for enemies).
 */
public final class IdleHandler implements StateHandler {
    private final MatchController matchController;
    private final EnemyRadiusScanner scanner;
    private final EventFactory eventFactory;
    private final CollisionResolver resolver;
    private boolean controllo;
    private boolean combattimento;

    /**
     * Constructs an IdleHandler with the required dependencies.
     *
     * @param matchController the controller for match-level operations such as
     *                        character updates
     * @param scanner         the utility to detect nearby enemies within radius
     * @param eventFactory    the factory used to generate character state
     *                        transition events
     * @param resolver        the collision resolver for managing combat
     *                        interactions
     */
    public IdleHandler(final MatchController matchController,
            final EnemyRadiusScanner scanner,
            final EventFactory eventFactory,
            final CollisionResolver resolver) {
        this.matchController = matchController;
        this.scanner = scanner;
        this.eventFactory = eventFactory;
        this.resolver = resolver;
    }

    @Override
    public CharacterState handle(final GenericCharacterController character, final EventQueue queue,
            final int deltaTime) {

        if (character != null) {
            final var player = character instanceof PlayableCharacterController;
            // Imposta l'animazione in base al tipo di personaggio
            character.setNextAnimation(player ? Keyword.RUN_RIGHT : Keyword.RUN_LEFT);
            // Muovi il personaggio
            final Movement movementCharacter = new Movement(player ? 2 : -2, 0);
            character.showNextFrameAndMove(movementCharacter);

            matchController.updateCharacterPosition(character, movementCharacter.x(), movementCharacter.y());

            // Logica per personaggi giocanti
            if (player) {
                // Scanner gestisce già il controllo "in combattimento" in modo sincronizzato
                final List<CollisionEvent> followEvents = scanner.scanForFollowEvents();

                if (isCharacterInvolved(followEvents, character) && !followEvents.isEmpty()) {
                    final CollisionEvent event = followEvents.get(0);

                    // Notifica gli osservatori della collisione
                    matchController.notifyCollisionObservers(event);

                    // Passa allo stato FOLLOWING
                    queue.enqueue(eventFactory.createEvent(CharacterState.FOLLOWING));
                    return CharacterState.FOLLOWING;
                }
            }
            // Logica per nemici
            else {
                final boolean collision = resolver.wasEnemyCollided(character.getId().number());
                if (collision) {
                    // Passa allo stato STOPPED se c'è collisione
                    queue.enqueue(eventFactory.createEvent(CharacterState.STOPPED));
                    return CharacterState.STOPPED;
                }
            }
        }

        // Resta in IDLE se non ci sono transizioni di stato
        queue.enqueue(eventFactory.createEvent(CharacterState.IDLE));
        return CharacterState.IDLE;
    }

    private boolean isCharacterInvolved(final List<CollisionEvent> events, final GenericCharacterController character) {
        final int id = character.getId().number();
        return events.stream().anyMatch(ev -> ev.getCollidable1().getCardidentifier().number() == id ||
                ev.getCollidable2().getCardidentifier().number() == id);
    }

}
