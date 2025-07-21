package it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.PlayableCharacterGUI;

/**
 * A standard implementation of PlayableCharacterController interface.
 */
public class PlayableCharacterControllerImpl extends GenericCharacterControllerImpl implements PlayableCharacterController {
    private final String charName;
    private final String playableCharType;
    private final int actionRange;

    /**
     * @param deathObs the character death observer that communicates with the main controller
     * the death of this linked playable character
     * @param id the numerical id of this controller
     * @param playableChar the playable character linked to this controller
     */
    public PlayableCharacterControllerImpl(final CharacterDeathObserver deathObs, 
      final int id, final PlayableCharacter playableChar) {
        super(deathObs, id, playableChar, playableChar.getType());
        this.charName = playableChar.getName();
        this.playableCharType = playableChar.getType().get();
        this.actionRange = playableChar.getActionRange();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final var view = new PlayableCharacterGUI(this, this.playableCharType, this.charName, width, height);
        view.setSupportedAnimation();
        return view;
    }

    @Override
    public final int getActionRange() {
        return this.actionRange;
    }
}
