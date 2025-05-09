package it.unibo.oop.lastcrown.controller.characters.impl;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.api.PlayableCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.PlayableCharacterGUIImpl;

/**
 * A standard implementation of PlayableCharacterController interface.
 */
public class PlayableCharacterControllerImpl extends GenericCharacterControllerImpl implements PlayableCharacterController {
    private PlayableCharacterGUI view;
    private final String charName;
    private final String playableCharType;
    private final Double speedMultiplier;
    private final int actionRange;

    /**
     * @param obs the character death observer that communicates with the main controller
     * the death of this linked playable character
     * @param id the numerical id of this controller
     * @param playableChar the playable character linked to this controller
     */
    public PlayableCharacterControllerImpl(final CharacterDeathObserver obs, final int id,
    final PlayableCharacter playableChar) {
        super(obs, id, playableChar, playableChar.getType());
        this.view = null;
        this.charName = playableChar.getName();
        this.playableCharType = playableChar.getType();
        this.speedMultiplier = playableChar.getSpeedMultiplier();
        this.actionRange = playableChar.getActionRange();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final PlayableCharacterGUI newView = new PlayableCharacterGUIImpl(this,
         this.playableCharType, this.charName, this.speedMultiplier, width, height);
        this.view = newView;
        return newView;
    }

    @Override
    public final void jumpUp() {
        this.view.startJumpUpSequence();
    }

    @Override
    public final void jumpDown() {
        this.view.startJumpDownSequence();
    }

    @Override
    public final void jumpForward() {
        this.view.startJumpForwardSequence();
    }

    @Override
    public final int getActionRange() {
        return this.actionRange;
    }
}
