package it.unibo.oop.lastcrown.controller.characters.impl.playablecharacter;

import it.unibo.oop.lastcrown.controller.characters.api.CharacterDeathObserver;
import it.unibo.oop.lastcrown.controller.characters.api.PlayableCharacterController;
import it.unibo.oop.lastcrown.controller.characters.impl.GenericCharacterControllerImpl;
import it.unibo.oop.lastcrown.model.characters.api.PlayableCharacter;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.api.Movement;
import it.unibo.oop.lastcrown.view.characters.api.PlayableCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.impl.PlayableCharacterGUIImpl;

/**
 * A standard implementation of PlayableCharacterController interface.
 */
public class PlayableCharacterControllerImpl extends GenericCharacterControllerImpl implements PlayableCharacterController {
    private PlayableCharacterGUI view;
    private final CharacterMovementObserver movObs;
    private final String charName;
    private final String playableCharType;
    private final Double speedMultiplier;
    private final int actionRange;

    /**
     * @param deathObs the character death observer that communicates with the main controller
     * the death of this linked playable character
     * @param movObs the observer of the character movements
     * @param id the numerical id of this controller
     * @param playableChar the playable character linked to this controller
     */
    public PlayableCharacterControllerImpl(final CharacterDeathObserver deathObs, 
     final CharacterMovementObserver movObs, final int id, final PlayableCharacter playableChar) {
        super(deathObs, id, playableChar, playableChar.getType());
        this.view = null;
        this.movObs = movObs;
        this.charName = playableChar.getName();
        this.playableCharType = playableChar.getType().get();
        this.speedMultiplier = playableChar.getSpeedMultiplier();
        this.actionRange = playableChar.getActionRange();
    }

    @Override
    public final GenericCharacterGUI createView(final int width, final int height) {
        final PlayableCharacterGUI newView = new PlayableCharacterGUIImpl(this, this.getId(),
         this.movObs, this.playableCharType, this.charName, this.speedMultiplier, width, height);
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

     @Override
    public final void setManualRunningAnimation() {
        this.view.startManualRunningAnimation();
    }

    @Override
    public final void stopManualRunningAnimation() {
        this.view.stopManualRunningAnimation();
    }

    @Override
    public final void movePanel(final Movement movement) {
        this.view.doSelectedMovement(movement);
    }
}
