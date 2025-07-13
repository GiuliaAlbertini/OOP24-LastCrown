package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAnimationPanel;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.Movement;
import it.unibo.oop.lastcrown.view.characters.api.PlayableCharacterGUI;

/**
 * A standard implementation of PlayableCharacterGUI interface.
 */
public class PlayableCharacterGUIImpl extends GenericCharacterGUIImpl implements PlayableCharacterGUI {
    private final List<BufferedImage> runImages;
    private final List<BufferedImage> jumpUpImages;
    private final List<BufferedImage> jumpDownImages;
    private final List<BufferedImage> jumpForwardImages;
    private final CharacterMovementObserver movObs;
    private final CardIdentifier id;
    private final int panelWidth;
    private final int panelHeight;
    private CharacterAnimationPanel animationPanel;
    private int cont;

    /**
     * @param atckObs the observer of the character attacks
     * @param id the id of the linked character controller
     * @param movObs the observer of the character movements
     * @param charType the type of the character
     * @param charName the name of the character
     * @param speedMultiplier the speed multiplier of the character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public PlayableCharacterGUIImpl(final CharacterAttackObserver atckObs, final CardIdentifier id,
     final CharacterMovementObserver movObs, final String charType, final String charName,
      final Double speedMultiplier, final int width, final int height) {
        super(atckObs, id, movObs, charType, charName, speedMultiplier, width, height);
        this.cont = 0;
        this.animationPanel = null;
        this.movObs = movObs;
        this.id = id;
        this.panelWidth = width;
        this.panelHeight = height;
        this.runImages = this.getSelectedFrames(Keyword.RUN_RIGHT.get(), charType, charName);
        this.jumpUpImages = this.getSelectedFrames(Keyword.JUMPUP.get(), charType, charName);
        this.jumpDownImages = this.getSelectedFrames(Keyword.JUMPDOWN.get(), charType, charName);
        this.jumpForwardImages = new ArrayList<>();
        this.jumpForwardImages.addAll(this.jumpUpImages);
        for (final var frame : jumpDownImages) {
            this.jumpForwardImages.addLast(frame);
        }
    }

    @Override
    public final CharacterAnimationPanelImpl getAnimationPanel(final String charType) {
        final var newPanel = CharacterAnimationPanelImpl.create(panelWidth, panelHeight, charType, Color.GREEN);
        this.animationPanel = newPanel;
        return newPanel;
    }

    @Override
    public final void startJumpUpSequence() {
        this.startAnimationSequence(this.jumpUpImages, Keyword.JUMPUP);
    }

    @Override
    public final void startJumpDownSequence() {
        this.startAnimationSequence(this.jumpDownImages, Keyword.JUMPDOWN);
    }

    @Override
    public final void startJumpForwardSequence() {
        this.startAnimationSequence(this.jumpForwardImages, Keyword.JUMPFORWARD);
    }

    @Override
    public final void startManualRunningAnimation() {
        this.notifyDone();
        this.acquireLock();
    }

    @Override
    public final void doSelectedMovement(final Movement movement) {
        this.animationPanel.setCharacterImage(this.runImages.get(cont));
        this.animationPanel.setLocation(this.animationPanel.getX() + movement.x(),
        this.animationPanel.getY() + movement.y());
        this.movObs.notifyMovement(id, movement.x(), movement.y());
        cont = (cont + 1) % this.runImages.size();
    }

    @Override
    public final void stopManualRunningAnimation() {
        this.freeLock();
    }
}
