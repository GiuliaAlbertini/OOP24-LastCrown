package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A standard implementation of PlayableCharacterGUI interface.
 */
public class PlayableCharacterGUI extends GenericCharacterGUIImpl {
    private static final Set<Keyword> SUPPORTED_ANIMATION = Set.of(Keyword.RUN_RIGHT, 
     Keyword.STOP, Keyword.JUMPUP, Keyword.JUMPDOWN, Keyword.ATTACK, Keyword.DEATH);
    private final List<BufferedImage> jumpUpImages;
    private final List<BufferedImage> jumpDownImages;

    /**
     * @param atckObs the observer of the character attacks
     * @param charType the type of the playable character
     * @param charName the name of the character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public PlayableCharacterGUI(final CharacterAttackObserver atckObs,
     final String charType, final String charName, final int width, final int height) {
        super(atckObs, charType, charName, width, height);
        this.jumpUpImages = this.getSelectedFrames(Keyword.JUMPUP.get(), charType, charName);
        this.jumpDownImages = this.getSelectedFrames(Keyword.JUMPDOWN.get(), charType, charName);
    }

    @Override
    public final Set<Keyword> getSupportedAnimation() {
        return SUPPORTED_ANIMATION;
    }

    @Override
    public final Animation getCorrespondingAnimation(final Keyword keyword) {
        if (keyword.equals(Keyword.JUMPUP)) {
            return new Animation(this.jumpUpImages);
        } else if (keyword.equals(Keyword.JUMPDOWN)) {
            return new Animation(this.jumpDownImages);
        } else {
            throw new UnsupportedOperationException("Requested an usupported animation");
        }
    }
}
