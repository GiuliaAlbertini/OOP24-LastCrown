package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.PlayableCharacterGUI;

/**
 * A standard implementation of PlayableCharacterGUI interface.
 */
public class PlayableCharacterGUIImpl extends GenericCharacterGUIImpl implements PlayableCharacterGUI {
    private final List<BufferedImage> jumpUpImages;
    private final List<BufferedImage> jumpDownImages;
    private final List<BufferedImage> jumpForwardImages;

    /**
     * @param obs the observer of the character attacks
     * @param charType the type of the playable character
     * @param charName the name of the character
     * @param speedMultiplier the speed multiplier of the character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public PlayableCharacterGUIImpl(final CharacterAttackObserver obs, final String charType,
     final String charName, final Double speedMultiplier, final int width, final int height) {
        super(obs, charType, charName, speedMultiplier, width, height);

        this.jumpUpImages = this.getSelectedFrames(Keyword.JUMPUP.get());
        this.jumpDownImages = this.getSelectedFrames(Keyword.JUMPDOWN.get());
        this.jumpForwardImages = new ArrayList<>();
        this.jumpForwardImages.addAll(this.jumpUpImages);
        for (final var frame : jumpDownImages) {
            this.jumpForwardImages.addLast(frame);
        }
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
}
