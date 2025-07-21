package it.unibo.oop.lastcrown.view.characters.impl;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import it.unibo.oop.lastcrown.view.characters.api.CharacterAttackObserver;

/**
 * A class that defines the attack animations of a specific character.
 */
class AttackAnimation extends Animation {
    private final CharacterAttackObserver atckObs;
    private final List<List<BufferedImage>> frames;
    private int currentSequence;
    private int currentFrame;

    /**
     * @param frames the frames of the animations
     * @param atckObs the observer to advise when an attack animation is finished
     */
    protected AttackAnimation(final List<List<BufferedImage>> frames, final CharacterAttackObserver atckObs) {
        super(new ArrayList<>());
        this.frames = frames;
        this.atckObs = atckObs;
    }

    @Override
    protected final BufferedImage nextFrame() {
        if (currentFrame < frames.get(currentSequence).size() - 1) {
            return frames.get(currentSequence).get(++currentFrame);
        } else {
            atckObs.doAttack();
            currentFrame = 0;
            currentSequence = (currentSequence + 1) % frames.size(); // Passa alla prossima sequenza
            return frames.get(currentSequence).get(currentFrame);
        }
    }
}
