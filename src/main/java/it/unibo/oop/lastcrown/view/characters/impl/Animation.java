package it.unibo.oop.lastcrown.view.characters.impl;

import java.util.List;
import java.awt.image.BufferedImage;

/**
 * A simple animation made by a list of frames.
 */
class Animation {
    private final List<BufferedImage> frames;
    private int currentFrame = -1;

    /**
     * @param frames the buffered images that create the animations.
     */
    protected Animation(final List<BufferedImage> frames) {
        this.frames = frames;
    }

    /**
     * Set the next frame to be shown.
     * It's designed to be overridden by the AttackAnimation.
     * @return the next frame to be shown
     */
    protected BufferedImage nextFrame() {
        this.currentFrame = (this.currentFrame + 1) % this.frames.size();
        return this.frames.get(this.currentFrame);
    }

    /**
     * @return the number of frames of this animation.
     */
    protected int getAnimationSize() {
        return this.frames.size();
    }
}
