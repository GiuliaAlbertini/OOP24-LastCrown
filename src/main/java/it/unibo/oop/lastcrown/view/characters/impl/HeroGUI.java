package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Set;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A standard implementation of HeroGUI interface.
 */
public class HeroGUI extends GenericCharacterGUIImpl {
    private static final Set<Keyword> SUPPORTED_ANIMATION = Set.of(Keyword.RUN_RIGHT, Keyword.RUN_LEFT, 
     Keyword.STOP, Keyword.STOP_LEFT, Keyword.ATTACK, Keyword.DEATH);
    private final List<BufferedImage> runLeftImages;
    private final List<BufferedImage> stopLeftImages;

    /**
     * @param atckObs the observer of the enemy attacks
     * @param charName the name of the enemy
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public HeroGUI(final CharacterAttackObserver atckObs,
     final String charName, final int width, final int height) {
        super(atckObs, CardType.HERO.get(), charName, width, height);
        this.runLeftImages = this.getSelectedFrames(Keyword.RUN_LEFT.get(), CardType.HERO.get(), charName);
        this.stopLeftImages = this.getSelectedFrames(Keyword.STOP_LEFT.get(), CardType.HERO.get(), charName);
    }

    @Override
    public final Set<Keyword> getSupportedAnimation() {
        return SUPPORTED_ANIMATION;
    }

    @Override
    public final Animation getCorrespondingAnimation(final Keyword keyword) {
     if (keyword.equals(Keyword.RUN_LEFT)) {
            return new Animation(this.runLeftImages);
        } else if (keyword.equals(Keyword.STOP_LEFT)) {
            return new Animation(this.stopLeftImages);
        } else {
            throw new UnsupportedOperationException("Requested an usupported animation");
        }
    }
}
