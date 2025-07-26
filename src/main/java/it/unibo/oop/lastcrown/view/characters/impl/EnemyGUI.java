package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A standard implementation of EnemyGUI.
 */
public class EnemyGUI extends GenericCharacterGUIImpl {
    private static final Set<Keyword> SUPPORTED_ANIMATION = Set.of(Keyword.RUN_LEFT, Keyword.RETREAT, 
     Keyword.ATTACK, Keyword.DEATH, Keyword.STOP);
    private final List<BufferedImage> runImages;
    private final List<BufferedImage> retreatImages;

     /**
     * @param atckObs the observer of the enemy attacks
     * @param charType the type of the enemy (normal enemy or boss)
     * @param charName the name of the enemy
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public EnemyGUI(final CharacterAttackObserver atckObs,
     final String charType, final String charName, final int width, final int height) {
        super(atckObs, charType, charName, width, height);
        this.runImages = this.getSelectedFrames(Keyword.RUN_LEFT.get(), charType, charName);
        this.retreatImages = this.getSelectedFrames(Keyword.RETREAT.get(), charType, charName);
    }

     @Override
     public final Set<Keyword> getSupportedAnimation() {
        return SUPPORTED_ANIMATION;
     }

     @Override
     public final Animation getCorrespondingAnimation(final Keyword keyword) {
        if (keyword.equals(Keyword.RUN_LEFT)) {
            return new Animation(this.runImages);
        } else if (keyword.equals(Keyword.RETREAT)) {
            return new Animation(this.retreatImages);
        } else {
            throw new UnsupportedOperationException("Requested an usupported animation");
        }
     }
}
