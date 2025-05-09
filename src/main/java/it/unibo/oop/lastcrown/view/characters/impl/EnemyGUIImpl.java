package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.List;

import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.EnemyGUI;

/**
 * A standard implementation of EnemyGUI.
 */
public class EnemyGUIImpl extends GenericCharacterGUIImpl implements EnemyGUI {
    private final List<BufferedImage> runImages;
    private final List<BufferedImage> retreatImages;

    /**
     * @param atckObs the observer of the enemy attacks
     * @param movObs the observer of the enemy movements
     * @param charType the type of the enemy (normal enemy or boss)
     * @param charName the name of the enemy
     * @param speedMultiplier the speed multiplier of the enemy
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
     public EnemyGUIImpl(final CharacterAttackObserver atckObs, final CharacterMovementObserver movObs,
     final String charType, final String charName, final Double speedMultiplier, final int width, final int height) {
        super(atckObs, movObs, charType, charName, speedMultiplier, width, height);

        this.runImages = this.getSelectedFrames(Keyword.RUN_LEFT.get());
        this.retreatImages = this.getSelectedFrames(Keyword.RETREAT.get());
    }

    @Override
    public final void startRunLoop() {
        this.startAnimationSequence(this.runImages, Keyword.RUN_LEFT);
    }

    @Override
    public final void startRetreatLoop() {
        this.startAnimationSequence(this.retreatImages, Keyword.RUN_RIGHT);
    }
}
