package it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;

/**
 * Create a PlayableCharacterController with the specified parameters.
 */
public final class PlCharControllerFactory {
    private PlCharControllerFactory() { }

    /**
     * @param deathObs the character death observer of this controller
     * @param contrId the numerical id of this controller
     * @param playableChar the playable character linked to this controller
     * @return a new Playable character Controller
     */
    public static PlayableCharacterController createPlCharController(final CharacterDeathObserver deathObs,
     final int contrId, final PlayableCharacter playableChar) {
        return new PlayableCharacterControllerImpl(deathObs, contrId, playableChar);
    }
}
