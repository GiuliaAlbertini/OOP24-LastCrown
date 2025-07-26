package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JComponent;

import it.unibo.oop.lastcrown.model.card.CardType;
import it.unibo.oop.lastcrown.view.ImageLoader;
import it.unibo.oop.lastcrown.view.characters.Keyword;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAnimationPanel;
import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;
import it.unibo.oop.lastcrown.view.characters.api.GenericCharacterGUI;
import it.unibo.oop.lastcrown.view.characters.CharacterPathLoader;

/**
 * A standard implementation of CharacterGUI interface.
 */
public abstract class GenericCharacterGUIImpl implements GenericCharacterGUI {
    private Set<Keyword> supportedAnimations;
    private final int charWidth;
    private final int charHeight;
    private final String charType;
    private final Animation run;
    private final Animation stop;
    private final Animation attack;
    private final Animation death;
    private final Map<Keyword, Animation> animations;
    private Animation currentAnimation;
    private CharacterAnimationPanel animationPanel;

     /**
     * @param atckObs the observer of the character attacks
     * @param charType the type of the linked character
     * @param charName the name of the linked character
     * @param width the horizontal size of the character animation panel
     * @param height the vertical size of the character animation panel
     */
    public GenericCharacterGUIImpl(final CharacterAttackObserver atckObs,
     final String charType, final String charName, final int width, final int height) {
        this.charType = charType;
        this.charWidth = width;
        this.charHeight = height;
        this.run = new Animation(this.getSelectedFrames(Keyword.RUN_RIGHT.get(), charType, charName));
        this.stop = new Animation(this.getSelectedFrames(Keyword.STOP.get(), charType, charName));
        this.death = new Animation(this.getSelectedFrames(Keyword.DEATH.get(), charType, charName));
        final List<List<String>> attackPaths = CharacterPathLoader.loadAttackPaths(charType, charName);
        final List<List<BufferedImage>> attacksImages = new ArrayList<>();
        for (final var paths : attackPaths) {
            attacksImages.addLast(ImageLoader.getAnimationFrames(paths, this.charWidth, this.charHeight));
        }
        this.attack = new AttackAnimation(attacksImages, atckObs);
        this.animations = new EnumMap<>(Keyword.class);
        this.animations.put(Keyword.RUN_RIGHT, run);
        this.animations.put(Keyword.STOP, stop);
        this.animations.put(Keyword.DEATH, death);
        this.animations.put(Keyword.ATTACK, attack);
    }

    @Override
    public final int getDeathAnimationSize() {
        return this.death.getAnimationSize();
    }

    /**
     * Set the supported animation keyowrd of this character.
     */
    public final void setSupportedAnimation() {
        this.supportedAnimations = this.getSupportedAnimation();
    }

    /**
     * it's designed to be overridden by the specific character GUIs.
     * @return the set of the supported animations keyword
     */
    public Set<Keyword> getSupportedAnimation() {
        return Set.of(Keyword.RUN_RIGHT, Keyword.STOP, Keyword.DEATH, Keyword.ATTACK);
    }

    /**
     * @param keyword the animation keyowrd
     * @return the animation corresponding to the given keyword
     */
    public abstract Animation getCorrespondingAnimation(Keyword keyword);

    /**
     * @param keyword the animation keyword
     * @param charType the type of the linked character
     * @param charName the name of the linked character
     * @return the frames of this character corresponding to the given keyword
     */
    public final List<BufferedImage> getSelectedFrames(final String keyword, final String charType, final String charName) {
        return ImageLoader.getAnimationFrames(CharacterPathLoader.loadPaths(charType, charName, keyword),
         this.charWidth, this.charHeight);
    }

    @Override
    public final void createAnimationPanel() {
        this.animationPanel = getAnimationPanel(this.charType);
    }
    /**
     * Create a new animation panel of this character. 
     * Set the color of the health bar according to this character type.
     * It's defined package protected because it's designed to be overridden by PlayableCharacterGUIImpl and HeroGUIImpl.
     * @param charType the type of the linked character
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
        return this.animationPanel.getComponent();
    }

    @Override
    public final void setHealthPercentage(final int percentage) {
        this.animationPanel.setHealthBarImage(percentage);
    }

    @Override
    public final void setNextAnimation(final Keyword keyword) {
        if (!this.supportedAnimations.contains(keyword)) {
            throw new UnsupportedOperationException("This character does not support "
            + "the given " + keyword.get() + "animation");
        }
        if (!this.animations.containsKey(keyword)) {
            this.currentAnimation = this.getCorrespondingAnimation(keyword);
            this.animations.put(keyword, this.currentAnimation);
        } else {
            this.currentAnimation = this.animations.get(keyword);
        }
    }

    @Override
    public final void setNextFrame() {
        this.animationPanel.setCharacterImage(this.currentAnimation.nextFrame());
    }

    @Override
    public final void setNextFrameAndMovement(final int x, final int y) {
        this.animationPanel.setCharacterImage(this.currentAnimation.nextFrame());
        this.animationPanel.setLocation(this.animationPanel.getX() + x, this.animationPanel.getY() + y);
    }
}
