package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.AnimationPanelProxy;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.AnimationHandler;
import it.unibo.oop.lastcrown.view.characters.CustomLock;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.CharacterMovementObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.api.ReadOnlyAnimationPanel;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A standard implementation of CharacterGUI interface.
 */
public class GenericCharacterGUIImpl implements GenericCharacterGUI {
    private volatile int charWidth;
    private volatile int charHeight;
    private final CharacterAttackObserver atckObs;
    private final String charType;
    private final List<BufferedImage> runImages;
    private final  List<BufferedImage> stopImages;
    private final  List<List<BufferedImage>> attacksImages;
    private final List<BufferedImage> deathImages;
    private CharacterAnimationPanelImpl animationPanel;
    private final AnimationHandler anHandler;
    private final CustomLock lock = new CustomLock();
    private volatile boolean done;

    /**
     * @param atckObs the observer of the character attacks
     * @param movObs the observer of the character movements
     * @param charType the type of the character
     * @param charName the name of the character
     * @param speedMultiplier the speed multiplier of the character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public GenericCharacterGUIImpl(final CharacterAttackObserver atckObs, final CharacterMovementObserver movObs,
     final String charType, final String charName, final Double speedMultiplier, final int width, final int height) {
        this.atckObs = atckObs;
        this.charType = charType;
        this.charWidth = width;
        this.charHeight = height;
        this.anHandler = new AnimationHandler(movObs, speedMultiplier);
        this.runImages = this.getSelectedFrames(Keyword.RUN_RIGHT.get(), charType, charName);
        this.stopImages = this.getSelectedFrames(Keyword.STOP.get(), charType, charName);
        this.deathImages = this.getSelectedFrames(Keyword.DEATH.get(), charType, charName);
        final List<List<String>> attackPaths = CharacterPathLoader.loadAttackPaths(charType, charName);
        this.attacksImages = new ArrayList<>();
        for (final var paths : attackPaths) {
            this.attacksImages.addLast(ImageLoader.getAnimationFrames(paths, this.charWidth, this.charHeight));
        }
    }

    /**
     * @param keyword the animation keyword
     * @param charType the type of the character
     * @param charName the name of the character
     * @return the frames of this character corresponding to the given keyword
     */
    public final List<BufferedImage> getSelectedFrames(final String keyword, final String charType, final String charName) {
        return ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(charType, charName, keyword),
         this.charWidth, this.charHeight);
    }

    @Override
    public final void createAnimationPanel() {
        this.animationPanel = this.getAnimationPanel(this.charType);
    }

    /**
     * Create a new animation panel of this character. 
     * Set the color of the health bar according to this character type.
     * It's package protected because it's designed to be overridden by PlayableCharacterGUIImpl and HeroGUIImpl.
     * @param charType the type of the character
     * @return new Animation Panel of this character.
     */
    protected CharacterAnimationPanelImpl getAnimationPanel(final String charType) {
        final Color color;
        if (CardType.ENEMY.get().equals(charType) || CardType.BOSS.get().equals(charType)) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
        return CharacterAnimationPanelImpl.create(charWidth, charHeight, charType, color);
    }

    @Override
    public final JComponent getGraphicalComponent() {
        final ReadOnlyAnimationPanel safePanel = AnimationPanelProxy.createSafePanel(this.animationPanel); 
        return safePanel.getComponent();
    }

    @Override
    public final void setSpeedMultiplier(final double newSpeedMul) {
        this.anHandler.setSpeedMultiplier(newSpeedMul);
    }

    @Override
    public final void setHealthPercentage(final int percentage) {
        this.animationPanel.setHealthBarImage(percentage);
    }

     /**
     * This method is designed to be overridable because some 
     * characters run to the right and the enemies run to the left.
     */
    @Override
    public void startRunLoop() {
        this.startAnimationSequence(runImages, Keyword.RUN_RIGHT);
    }

    /**
     * This method is designed to be overridable by the HeroGUI implementation.
     */
    @Override
    public void startStopLoop() {
        this.startAnimationSequence(stopImages, Keyword.STOP);
    }

    @Override
    public final void startDeathSequence() {
        this.startAnimationSequence(deathImages, Keyword.DEATH);
    }

    @Override
    public final void startAttackLoop() {
       this.startAnimationSequence(attacksImages.get(0), Keyword.ATTACK);
    }

    /**
     * Starts a generic animation sequence (can be a loop depending on the keyword passed). 
     * @param frames the images list of the animation
     * @param keyword the keyword of the specific animation
     */
    public final void startAnimationSequence(final List<BufferedImage> frames, final Keyword keyword) {
        this.notifyDone();
        new Thread(() -> {
            this.acquireLock();
            this.start();
            switch (keyword) {
                case Keyword.STOP, Keyword.STOP_LEFT, Keyword.RUN_RIGHT, Keyword.RUN_LEFT, Keyword.RETREAT:
                    while (!done) {
                        this.anHandler.startAnimationSequence(frames, keyword, this.animationPanel);
                    }
                    break;
                case Keyword.ATTACK:
                    int cont = 0;
                    while (!done) {
                        this.anHandler.startAnimationSequence(this.attacksImages.get(cont), keyword, animationPanel);
                        this.atckObs.doAttack();
                        cont = (cont + 1) % this.attacksImages.size();
                    }
                    break;
                case Keyword.DEATH, Keyword.JUMPUP, Keyword.JUMPDOWN, Keyword.JUMPFORWARD:
                    this.anHandler.startAnimationSequence(frames, keyword, this.animationPanel);
                    break;
            }
            if (Keyword.DEATH.equals(keyword)) {
                this.animationPanel.disposeClosing();
                this.animationPanel = null;
            }
            this.freeLock();
        }).start();
    }

    @Override
    public final void notifyDone() {
        this.done = true;
        this.anHandler.stop();
    }

    @Override
    public final void start() {
        this.done = false;
        this.anHandler.start();
    }

    @Override
    public final void freeLock() {
        this.lock.releaseLock();
    }

    @Override
    public final void acquireLock() {
        this.lock.acquireLock();
    }
}
