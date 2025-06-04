package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import it.unibo.oop.lastcrown.model.card.CardIdentifier;
import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.HeroGUI;

/**
 * A standard implementation of HeroGUI interface.
 */
public class HeroGUIImpl extends GenericCharacterGUIImpl implements HeroGUI {
    private static final double RESIZE_SCALE = 0.5;
    private int width;
    private int height;
    private CharacterAnimationPanelImpl animationPanel;
    private final int widthVariation;
    private final int heightVariation;
    private List<BufferedImage> runImages;
    private List<BufferedImage> runLeftImages;
    private List<BufferedImage> stopImages;
    private List<BufferedImage> stopLeftImages;

    /**
     * @param atckObs the observer of the character attacks
     * @param id the id of the linked character controller
     * @param movObs the observer of the character movements
     * @param charName the name of the enemy
     * @param speedMultiplier the speed multiplier of the enemy
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public HeroGUIImpl(final CharacterAttackObserver atckObs, final CardIdentifier id,
     final CharacterMovementObserver movObs, final String charName, 
     final Double speedMultiplier, final int width, final int height) {
        super(atckObs, id, movObs, CardType.HERO.get(), charName, speedMultiplier, width, height);
        this.width = width;
        this.height = height;
        this.animationPanel = null;
        this.widthVariation = (int) (this.width * RESIZE_SCALE);
        this.heightVariation = (int) (this.height * RESIZE_SCALE);
        this.runImages = this.getSelectedFrames(Keyword.RUN_RIGHT.get(), CardType.HERO.get(), charName);
        this.runLeftImages = this.getSelectedFrames(Keyword.RUN_LEFT.get(), CardType.HERO.get(), charName);
        this.stopImages = this.getSelectedFrames(Keyword.STOP.get(), CardType.HERO.get(), charName);
        this.stopLeftImages = this.getSelectedFrames(Keyword.STOP_LEFT.get(), CardType.HERO.get(), charName);
    }


    @Override
    public final CharacterAnimationPanelImpl getAnimationPanel(final String charType) {
        final var newPanel = CharacterAnimationPanelImpl.create(width, height, charType, Color.GREEN);
        this.animationPanel = newPanel;
        return newPanel;
    }
       /**
     * Set the size of the animation panel linked to this character GUI.
     * @param newWidth new panel width
     * @param newHeight new panel height
     */
     private void setAnimationPanelSize(final int newWidth, final int newHeight) {
        this.width = newWidth;
        this.height = newHeight;
        this.animationPanel.setSize(newWidth, newHeight);
        this.animationPanel.setHealthBarAlignment();
    }

    @Override
    public final void useInGameFrames() {
        this.width = this.width - this.widthVariation;
        this.height = this.height - this.heightVariation;
        this.resizeFrames();
    }

    @Override
    public final void useShopFrames() {
        this.width = this.width + widthVariation;
        this.height = this.height + heightVariation;
        this.resizeFrames();
    }

    private void resizeFrames() {
        this.setAnimationPanelSize(this.width, this.height);
        this.runImages = ImageLoader.resizeFrames(runImages, this.width, this.height);
        this.runLeftImages = ImageLoader.resizeFrames(runLeftImages, this.width, this.height);
        this.stopImages = ImageLoader.resizeFrames(stopImages, this.width, this.height);
        this.stopLeftImages = ImageLoader.resizeFrames(stopLeftImages, this.width, this.height);
    }

    @Override
    public final void startRunLoop() {
        this.startAnimationSequence(this.runImages, Keyword.RUN_RIGHT);
    }

    @Override
    public final void startRunLeftLoop() {
        this.startAnimationSequence(this.runLeftImages, Keyword.RUN_LEFT);
    }

    @Override
    public final void startStopLoop() {
        this.startAnimationSequence(this.stopImages, Keyword.STOP);
    }

    @Override
    public final void startStopLeftLoop() {
        this.startAnimationSequence(this.stopLeftImages, Keyword.STOP_LEFT);
    }
}
