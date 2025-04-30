package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.Keyword;
import it.unibo.oop.lastcrown.view.characters.AnimationHandler;
import it.unibo.oop.lastcrown.view.characters.CustomLock;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A standard implementation of CharacterGUI interface.
 */
public class GenericCharacterGUIImpl implements GenericCharacterGUI {
    private volatile int charWidth;
    private volatile int charHeight;
    private final CharacterAttackObserver observer;
    private final String charType;
    private final String charName;
    private final List<BufferedImage> runImages;
    private final  List<BufferedImage> stopImages;
    private final  List<List<BufferedImage>> attacksImages;
    private final List<BufferedImage> deathImages;
    private CharacterAnimationPanelImpl animationPanel;
    private final AnimationHandler anHandler;
    private final CustomLock lock = new CustomLock();
    private volatile boolean done;

    /**
     * @param obs the observer of the character attacks
     * @param charType the type of the character
     * @param charName the name of the character
     * @param speedMultiplier the speed multiplier of the character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public GenericCharacterGUIImpl(final CharacterAttackObserver obs, final String charType,
    final String charName, final Double speedMultiplier, final int width, final int height) {
        this.observer = obs;
        this.charType = charType;
        this.charName = charName;
        this.charWidth = width;
        this.charHeight = height;
        this.animationPanel = this.createAnimationPanel();
        this.anHandler = new AnimationHandler(speedMultiplier);
        this.runImages = this.getSelectedFrames(Keyword.RUN_RIGHT.get());
        this.stopImages = this.getSelectedFrames(Keyword.STOP.get());
        this.deathImages = this.getSelectedFrames(Keyword.DEATH.get());
        final List<List<String>> attackPaths = CharacterPathLoader.loadAttackPaths(charType, charName);
        this.attacksImages = new ArrayList<>();
        for (final var paths : attackPaths) {
            this.attacksImages.addLast(ImageLoader.getAnimationFrames(paths, this.charWidth, this.charHeight));
        }
    }

    /**
     * @param keyword the animation keyword
     * @return the frames of this character corresponding to the given keyword
     */
    public final List<BufferedImage> getSelectedFrames(final String keyword) {
        return ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(this.charType, 
        this.charName, keyword), this.charWidth, this.charHeight);
    }

    /**
     * Create a new animation panel of this character. 
     * Set the color of the health bar according to this character type.
     * @return new Animation Panel of this character.
     */
    private CharacterAnimationPanelImpl createAnimationPanel() {
        final Color color;
        if ("enemy".equals(this.charType)) {
            color = Color.RED;
        } else {
            color = Color.GREEN;
        }
        return CharacterAnimationPanelImpl.create(charWidth, charHeight, color);
    }

    @Override
    public final void setAnimationPanelSize(final int newWidth, final int newHeight) {
        this.charWidth = newWidth;
        this.charHeight = newHeight;
        this.animationPanel.setSize(this.charWidth, this.charHeight);
    }

    @Override
    public final void setAnimationPanelPosition(final JPanel mainPanel, final int x, final int y) {
        this.animationPanel.setBounds(x - charWidth / 2, y - charHeight / 2, charWidth, charHeight);
        this.animationPanel.setHealthBarPosition();
        mainPanel.add(animationPanel);
        mainPanel.repaint();
        this.startStopLoop();
    }

    @Override
    public final void setSpeedMultiplier(final double newSpeedMul) {
        this.anHandler.setSpeedMultiplier(newSpeedMul);
    }

    @Override
    public final void setHealthPercentage(final int percentage) {
        this.animationPanel.setHealthBarImage(percentage);
    }

    @Override
    public final void startRunLoop() {
        this.startAnimationSequence(runImages, Keyword.RUN_RIGHT);
    }

    @Override
    public final  void startStopLoop() {
        this.startAnimationSequence(stopImages, Keyword.STOP);
    }

    @Override
    public final void startDeathSequence() {
        this.startAnimationSequence(deathImages, Keyword.DEATH);
    }

    @Override
    public final void startAttackLoop() {
        this.notifyDone();
        new Thread(() -> {
                int cont = 0;
                this.lock.acquireLock();
                this.start();
                while (!done) {
                    this.anHandler.startAnimationSequence(attacksImages.get(cont), Keyword.ATTACK, this.animationPanel);
                    this.observer.doAttack();
                    cont = (cont + 1) % this.attacksImages.size();
                }
                this.lock.releaseLock();
            }).start();
    }

    /**
     * Starts a generic animation sequence (can be a loop depending on the keyword passed). 
     * @param frames the images list of the animation
     * @param keyword the keyword of the specific animation
     */
    public final void startAnimationSequence(final List<BufferedImage> frames, final Keyword keyword) {
        this.notifyDone();
        new Thread(() -> {
            this.lock.acquireLock();
            this.start();
            switch (keyword) {
                case Keyword.STOP, Keyword.RUN_RIGHT, Keyword.RUN_LEFT, Keyword.RETREAT:
                    while (!done) {
                        this.anHandler.startAnimationSequence(frames, keyword, this.animationPanel);
                    }
                    break;
                case Keyword.DEATH, Keyword.JUMPUP, Keyword.JUMPDOWN, Keyword.JUMPFORWARD:
                    this.anHandler.startAnimationSequence(frames, keyword, this.animationPanel);
                    break;
                default:
                    break;
            }
            if (Keyword.DEATH.equals(keyword)) {
                this.animationPanel.disposeClosing();
                this.animationPanel = null;
            }
            this.lock.releaseLock();
        }).start();
    }

    /**
     * Notify the current working thread that
     * it has to finish doing the animation loop and to release the mutual exclusion.
     * If no thread currently posess the mutual exclusion it does anything.
     */
    public void notifyDone() {
        this.done = true;
        this.anHandler.stop();
    }

    /**
     * Set the boolean flag done to false. The current working thread
     * will start doing the animation loop and it won't stop
     * until another thread calls the notifyDone() method.
     */
    public void start() {
        this.done = false;
        this.anHandler.start();
    }
}
